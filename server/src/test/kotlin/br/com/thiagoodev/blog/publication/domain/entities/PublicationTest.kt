package br.com.thiagoodev.blog.modules.publication.domain.entities

import net.datafaker.Faker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Suite de Testes Unitários Puros para a Entidade de Domínio [Publication].
 *
 * Arquitetura de Domínio Rico (Rich Domain Model):
 * Diferente de um modelo anêmico (que só possui getters e setters), esta entidade possui
 * comportamento próprio, como a propriedade calculada `isDeleted` e o método de mutação `delete()`.
 * Testar essas regras aqui garante que a lógica fundamental da publicação funcione em qualquer
 * lugar, independente de qual banco de dados ou framework estejamos usando por baixo dos panos.
 */
class PublicationTest {

    private val faker = Faker()

    /**
     * CENÁRIO DE ESTUDO 1: Integridade da Instanciação.
     *
     * Valida se os valores padrão definidos no construtor da classe (Default Arguments do Kotlin)
     * estão sendo aplicados corretamente ao criar uma nova publicação.
     */
    @Test
    fun `should create a new publication with correct default values`() {
        val title = faker.book().title()
        val slug = faker.internet().slug()
        val description = faker.lorem().sentence()

        val publication = Publication(
            title = title,
            slug = slug,
            description = description,
            text = null,
            image = null
        )

        assertEquals(title, publication.title)
        assertEquals(slug, publication.slug)
        assertEquals(description, publication.description)
        assertEquals(0L, publication.viewsCount)
        assertTrue(publication.tags.isEmpty())
        assertTrue(publication.talks.isEmpty())
        assertFalse(publication.isDeleted)
    }

    /**
     * CENÁRIO DE ESTUDO 2: Mutação de Estado (Soft Delete).
     *
     * Valida o comportamento de exclusão lógica. Em sistemas modernos, raramente apagamos
     * um dado fisicamente do banco (Hard Delete). O método `delete()` deve preencher a data
     * de exclusão e refletir isso na propriedade transiente `isDeleted`.
     */
    @Test
    fun `should perform soft delete correctly by setting deletedAt timestamp`() {
        val publication = Publication(
            title = faker.book().title(),
            slug = faker.internet().slug(),
            description = faker.lorem().sentence(),
            text = null,
            image = null
        )

        assertFalse(publication.isDeleted)
        assertEquals(null, publication.deletedAt)

        publication.delete()

        assertTrue(publication.isDeleted)
        assertNotNull(publication.deletedAt)
    }

    /**
     * CENÁRIO DE ESTUDO 3: Idempotência do Método de Exclusão.
     *
     * Idempotência significa que invocar o mesmo método várias vezes deve ter o mesmo efeito
     * de invocá-lo apenas uma vez. Este teste garante a cláusula de guarda `if(isDeleted) return`.
     * Se a publicação já estiver deletada, chamar `delete()` novamente NÃO deve sobrescrever
     * a data/hora original da exclusão.
     */
    @Test
    fun `should not overwrite deletedAt if publication is already deleted`() {
        val publication = Publication(
            title = faker.book().title(),
            slug = faker.internet().slug(),
            description = faker.lorem().sentence(),
            text = null,
            image = null
        )

        publication.delete()
        val firstDeletionTime = publication.deletedAt

        /** Simula um processamento ou delay antes da segunda chamada */
        Thread.sleep(10)

        publication.delete()
        val secondDeletionTime = publication.deletedAt

        assertEquals(firstDeletionTime, secondDeletionTime)
    }
}