package br.com.thiagoodev.blog.publication.infrastructure.repositories

import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import br.com.thiagoodev.blog.modules.publication.infrastructure.repositories.PublicationRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Suite de Testes de Integração da Camada de Persistência (Slice Test).
 *
 * Utiliza o banco de dados H2 em memória. Focamos em garantir que o Hibernate/Spring Data
 * está traduzindo nossos nomes de métodos (Derived Queries) e anotações `@Query` em SQL
 * válido e retornando os resultados esperados.
 */
@DataJpaTest
@TestPropertySource(properties = [
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
])
class PublicationRepositoryTest {

    @Autowired
    private lateinit var publicationRepository: PublicationRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private val faker = Faker()
    private lateinit var activePublication: Publication
    private lateinit var deletedPublication: Publication

    /**
     * Fase de Preparação (Arrange):
     * Cria um estado previsível no banco antes de cada teste.
     * Inserimos uma publicação normal e uma deletada para testar os filtros de "Soft Delete".
     */
    @BeforeEach
    fun setup() {
        val now = LocalDateTime.now()

        activePublication = Publication(
            title = faker.book().title(),
            slug = faker.internet().slug(),
            description = faker.lorem().sentence(),
            text = faker.lorem().paragraph(),
            viewsCount = 10L,
            image = null,
            createdAt = now.minusDays(2),
            updatedAt = now
        )

        deletedPublication = Publication(
            title = faker.book().title(),
            slug = faker.internet().slug() + "-deleted",
            description = faker.lorem().sentence(),
            text = faker.lorem().paragraph(),
            viewsCount = 5L,
            image = null,
            createdAt = now.minusDays(5),
            updatedAt = now,
            deletedAt = now // <-- Esta é a chave do Soft Delete
        )

        entityManager.persistAndFlush(activePublication)
        entityManager.persistAndFlush(deletedPublication)
        entityManager.clear() // Limpa o cache L1 para forçar consultas SQL reais
    }

    /**
     * CENÁRIO DE ESTUDO 1: Consulta Paginada + Filtro de Soft Delete.
     */
    @Test
    fun `should return only active publications with pagination`() {
        val pageable = PageRequest.of(0, 10)

        val page = publicationRepository.findAllByDeletedAtIsNull(pageable)

        assertEquals(1, page.totalElements)
        assertEquals(activePublication.slug, page.content.first().slug)
    }

    /**
     * CENÁRIO DE ESTUDO 2: Consulta Única + Filtro de Soft Delete.
     */
    @Test
    fun `should find active publication by uuid`() {
        val found = publicationRepository.findByUuidAndDeletedAtIsNull(activePublication.uuid!!)

        assertNotNull(found)
        assertEquals(activePublication.title, found.title)
    }

    /**
     * CENÁRIO DE ESTUDO 3: Ocultação de Deletados.
     * Garante que um UUID válido, porém de uma entidade deletada, retorne nulo.
     */
    @Test
    fun `should return null when finding deleted publication by uuid`() {
        val found = publicationRepository.findByUuidAndDeletedAtIsNull(deletedPublication.uuid!!)

        assertNull(found)
    }

    /**
     * CENÁRIO DE ESTUDO 4: Busca por Intervalo de Datas (Between).
     */
    @Test
    fun `should find active publications within creation date range`() {
        val start = LocalDateTime.now().minusDays(3)
        val end = LocalDateTime.now()

        val results = publicationRepository.findAllByCreatedAtBetweenAndDeletedAtIsNull(start, end)

        assertEquals(1, results.size)
        assertEquals(activePublication.uuid, results.first().uuid)
    }

    /**
     * CENÁRIO DE ESTUDO 5: Atualização Direta no Banco (JPQL Modifying).
     *
     * Validações Críticas:
     * Como a Query `@Modifying` é executada DIRETAMENTE no banco de dados, ela
     * bypassa a memória (L1 Cache) do Hibernate. Para o nosso teste enxergar
     * essa alteração, nós temos que executar o `clear()` e buscar a entidade de novo.
     */
    @Test
    fun `should atomically increment views count using JPQL`() {
        val initialViews = activePublication.viewsCount

        publicationRepository.incrementViews(activePublication.uuid!!)

        // Esvazia o cache para forçar o Hibernate a ir no banco ver o novo valor
        entityManager.clear()

        val updatedPublication = publicationRepository.findById(activePublication.uuid!!).get()
        assertEquals(initialViews + 1, updatedPublication.viewsCount)
    }

    /**
     * CENÁRIO DE ESTUDO 6: Native SQL (Query Crua).
     */
    @Test
    fun `should find most viewed active publication using Native Query`() {
        // Insere uma publicação com mais views para competir
        val viralPublication = Publication(
            title = "Viral Post",
            slug = "viral-post",
            description = "Test",
            text = "Test",
            viewsCount = 1000L, // Maior que os 10L da activePublication
            image = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        entityManager.persistAndFlush(viralPublication)
        entityManager.clear()

        val mostViewed = publicationRepository.findMostViewed()

        assertNotNull(mostViewed)
        assertEquals("Viral Post", mostViewed.title)
        assertEquals(1000L, mostViewed.viewsCount)
    }
}