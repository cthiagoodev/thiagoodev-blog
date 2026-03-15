package br.com.thiagoodev.blog.user.infrastructure.repositories

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Suite de Testes de Integração da Camada de Persistência (Slice Test) para o [UserRepository].
 *
 * Arquitetura de Isolamento (Spring Boot):
 * A anotação @DataJpaTest cria um contêiner focado exclusivamente em dados.
 * Diferente de um teste E2E (Ponta a Ponta) que sobe toda a aplicação, esta anotação ignora
 * completamente Controllers, Services e Filtros Web. Ela levanta na memória apenas:
 * 1. O motor do Hibernate/JPA.
 * 2. As classes marcadas com @Entity.
 * 3. As interfaces do Spring Data JPA (como o UserRepository).
 *
 * Comportamento Transacional:
 * Cada método @Test nesta classe roda dentro de uma transação de banco de dados isolada.
 * Ao final da execução de cada método, o Spring automaticamente dispara um ROLLBACK.
 * Isso significa que não precisamos criar rotinas para limpar tabelas; o banco volta
 * ao seu estado original (vazio) como mágica após cada teste.
 */
@DataJpaTest
/**
 * @TestPropertySource: Intercepta e sobrescreve as propriedades de ambiente (application.yml)
 * exclusivamente durante a execução desta classe de teste.
 * * - "spring.flyway.enabled=false": Desativa o gerenciador de migrações. Rodar dezenas de
 * scripts de criação de tabelas a cada teste degrada a performance absurdamente.
 * - "spring.jpa.hibernate.ddl-auto=create-drop": Delega a responsabilidade de criação das
 * tabelas para o próprio Hibernate. Ele lê as entidades e monta o esquema no banco H2
 * em milissegundos, apagando tudo ao final.
 */
@TestPropertySource(properties = [
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
])
class UserRepositoryTest {

    /**
     * O Repositório real sob teste.
     * Aqui utilizamos a implementação concreta gerada em tempo de execução pelo Spring Data JPA,
     * garantindo que estamos testando as queries SQL reais que irão para produção.
     */
    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * [TestEntityManager]: A "Agulha" de Injeção de Dados do Spring.
     * * Por que não usar o próprio `userRepository.save()` para preparar o banco?
     * Para manter a integridade científica do teste. Se utilizarmos o repositório para
     * salvar o dado e depois utilizarmos o mesmo repositório para buscar o dado, não
     * teremos certeza se a busca falhou ou se foi o salvamento que falhou.
     * O EntityManager ignora o repositório e injeta o dado diretamente no banco,
     * separando a "preparação" da "ação testada".
     */
    @Autowired
    private lateinit var entityManager: TestEntityManager

    private val faker = Faker()
    private lateinit var user: User

    /**
     * Fase de Preparação (Arrange): Executada antes de iniciar cada método @Test.
     * * Instancia uma nova entidade User completamente desvinculada (Transient State).
     * Nota: Atribuímos `createdAt` manualmente aqui pois testes @DataJpaTest frequentemente
     * rodam sem os interceptadores de auditoria do Spring (@EntityListeners), o que causaria
     * uma ConstraintViolationException (Not Null) no banco se a data estivesse ausente.
     */
    @BeforeEach
    fun setup() {
        user = User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
            permissions = mutableSetOf(Permission.READ, Permission.ADMIN),
            createdAt = LocalDateTime.now(),
        )
    }

    /**
     * CENÁRIO: Recuperação de Entidade Complexa (Relacionamentos Fetch/Eager).
     * * Garante que o método de busca personalizada [UserRepository.findUniqueByEmail]
     * traduz corretamente a requisição para SQL e consegue hidratar a entidade User
     * junto com suas coleções filhas (Permissions).
     */
    @Test
    fun `Should return user with fetched permissions when email exists`() {
        /** * persistAndFlush(): Transita a entidade para o estado "Persistent" e força
         * o Hibernate a disparar o comando SQL `INSERT` contra o banco H2 imediatamente.
         */
        entityManager.persistAndFlush(user)


        /**
         * Isolamento de Contexto (O Segredo dos Testes JPA):
         * O Hibernate possui um Cache de Primeiro Nível na memória. Se não limparmos esse cache,
         * a linha abaixo (`findUniqueByEmail`) não irá gerar um `SELECT` no banco, mas sim
         * devolver o objeto que já está guardado na memória. O `clear()` esvazia esse cache,
         * forçando a execução de uma query real e validando a integração de fato.
         */
        entityManager.clear()

        val foundUser = userRepository.findUniqueByEmail(user.email)

        assertNotNull(foundUser)
        assertEquals(user.email, foundUser.email)

        /** * Valida se as tabelas relacionadas (user_permissions) foram trazidas corretamente
         * pelo mapeamento do JPA.
         */
        assertEquals(2, foundUser.permissions.size)
        assertTrue(foundUser.permissions.contains(Permission.READ))
        assertTrue(foundUser.permissions.contains(Permission.ADMIN))
    }

    /**
     * CENÁRIO: Prevenção de Exceções de Dados Não Encontrados.
     * * Valida o comportamento amigável do repositório ao não encontrar registros.
     * Como o retorno é anulável (User?), o Spring Data JPA deve retornar `null` de forma
     * silenciosa em vez de lançar `EmptyResultDataAccessException`.
     */
    @Test
    fun `Should return null when finding by non-existent email`() {
        val foundUser = userRepository.findUniqueByEmail(faker.internet().emailAddress())

        assertNull(foundUser)
    }

    /**
     * CENÁRIO: Otimização de Busca Booleana (Exists - Caminho Feliz).
     * * O método `existsByEmail` é projetado para ser leve. O Spring Data converte isso
     * em uma query otimizada (ex: SELECT 1 FROM users WHERE email = ? LIMIT 1) em vez
     * de carregar a entidade inteira para a memória.
     */
    @Test
    fun `Should return true when checking existence of registered email`() {
        entityManager.persistAndFlush(user)
        /** Não é necessário utilizar o entityManager.clear() aqui, pois consultas booleanas
         * nativas frequentemente bypassam a leitura completa do cache de qualquer forma. */

        val exists = userRepository.existsByEmail(user.email)

        assertTrue(exists)
    }

    /**
     * CENÁRIO: Otimização de Busca Booleana (Exists - Caminho Negativo).
     * * Garante que a query de verificação retorna `false` de forma determinística
     * ao escanear a tabela com um e-mail não registrado.
     */
    @Test
    fun `Should return false when checking existence of unregistered email`() {
        val exists = userRepository.existsByEmail(faker.internet().emailAddress())

        assertFalse(exists)
    }
}