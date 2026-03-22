package br.com.thiagoodev.blog.publication.domain.utils

import br.com.thiagoodev.blog.modules.publication.domain.utils.SlugFactory
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Suite de Testes Unitários Puros para o Utilitário de Domínio [SlugFactory].
 *
 * Metodologia:
 * Testes de utilitários de manipulação de string são baseados em entradas mapeadas (Inputs)
 * e saídas esperadas (Outputs). Aqui, validamos se o contrato de transformação atual
 * (letras minúsculas e espaços substituídos por hífens) está sendo respeitado.
 */
class SlugFactoryTest {

    /**
     * CENÁRIO DE ESTUDO 1: Transformação Completa (Caminho Feliz).
     *
     * Valida se a fábrica consegue lidar com uma string padrão, convertendo
     * todas as letras maiúsculas e substituindo os espaços vazios pelo caractere separador.
     */
    @Test
    fun `should convert string to lowercase and replace spaces with hyphens`() {
        val title = "My Awesome Spring Boot Post"
        val expectedSlug = "my-awesome-spring-boot-post"

        val factory = SlugFactory(title)
        val generatedSlug = factory.generate()

        assertEquals(expectedSlug, generatedSlug)
    }

    /**
     * CENÁRIO DE ESTUDO 2: Idempotência de Strings Já Formatadas.
     *
     * Valida se a fábrica é segura para ser executada em strings que, por algum motivo,
     * já venham formatadas ou não possuam espaços e letras maiúsculas. O processamento
     * não deve quebrar ou alterar o que já está correto.
     */
    @Test
    fun `should return same string if it is already lowercase and has no spaces`() {
        val title = "kotlin-architecture"
        val expectedSlug = "kotlin-architecture"

        val factory = SlugFactory(title)
        val generatedSlug = factory.generate()

        assertEquals(expectedSlug, generatedSlug)
    }

    /**
     * CENÁRIO DE ESTUDO 3: Tratamento de Espaços Múltiplos e Consecutivos.
     *
     * Nota Arquitetural:
     * Este teste serve como documentação viva do comportamento ATUAL da classe.
     * Como a implementação atual usa um simples `replace(" ", "-")`, espaços
     * consecutivos gerarão hífens consecutivos. Se no futuro a regra de negócio exigir
     * que múltiplos espaços virem apenas um hífen, este teste falhará e guiará o
     * desenvolvedor a usar Regex na refatoração da `SlugFactory`.
     */
    @Test
    fun `should replace consecutive spaces with consecutive hyphens based on current implementation`() {
        val title = "Clean  Code"
        val expectedSlug = "clean--code"

        val factory = SlugFactory(title)
        val generatedSlug = factory.generate()

        assertEquals(expectedSlug, generatedSlug)
    }
}