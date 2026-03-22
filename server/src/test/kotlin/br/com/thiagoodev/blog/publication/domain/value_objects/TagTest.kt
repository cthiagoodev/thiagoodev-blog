package br.com.thiagoodev.blog.publication.domain.value_objects

import br.com.thiagoodev.blog.modules.publication.domain.value_objects.Tag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Suite de Testes Unitários Puros para o Value Object [Tag].
 *
 * Objetivo de Estudo:
 * Validar o comportamento do método de fábrica estático `from()`. Como ele
 * lida com entradas de usuários que podem vir sujas (com espaços extras ou
 * fora do padrão de capitalização) via JSON, testar esses cenários anômalos
 * garante a robustez da camada de domínio.
 */
class TagTest {

    /**
     * CENÁRIO DE ESTUDO 1: Busca Exata (Caminho Feliz).
     *
     * Valida se a busca encontra corretamente a Tag quando a string enviada
     * é idêntica ao valor mapeado.
     */
    @Test
    fun `should return correct enum when string matches value exactly`() {
        val tag = Tag.from("Spring Boot")

        assertEquals(Tag.SPRING_BOOT, tag)
    }

    /**
     * CENÁRIO DE ESTUDO 2: Tolerância a Caixa (Case Insensitivity).
     *
     * Valida se a diretiva `ignoreCase = true` está funcionando, permitindo
     * que um usuário pesquise por "KOTLIN", "kotlin" ou "KoTlIn" e encontre
     * a mesma tag corretamente.
     */
    @Test
    fun `should return correct enum ignoring case sensitivity`() {
        val tagUppercase = Tag.from("CÓDIGO LIMPO")
        val tagLowercase = Tag.from("código limpo")

        assertEquals(Tag.CLEAN_CODE, tagUppercase)
        assertEquals(Tag.CLEAN_CODE, tagLowercase)
    }

    /**
     * CENÁRIO DE ESTUDO 3: Tolerância a Espaços Acidentais (Trimming).
     *
     * Valida se a aplicação da função `trim()` impede que erros de digitação
     * comuns no Frontend (como colocar um espaço no fim da palavra) invalidem
     * a busca.
     */
    @Test
    fun `should return correct enum even if string has trailing or leading spaces`() {
        val tagWithSpaces = Tag.from("   Arquitetura   ")

        assertEquals(Tag.ARCHITECTURE, tagWithSpaces)
    }

    /**
     * CENÁRIO DE ESTUDO 4: Prevenção de Exceções (Safe Null Return).
     *
     * Valida a vantagem arquitetural de usar este método ao invés do `valueOf()`.
     * Se uma tag não existir, o sistema deve retornar nulo suavemente para que
     * a camada de serviço (ou validador do DTO) tome a decisão de rejeitar,
     * sem estourar uma exceção de servidor genérica.
     */
    @Test
    fun `should return null when value does not match any tag`() {
        val unknownTag = Tag.from("Ruby on Rails")

        assertNull(unknownTag)
    }
}