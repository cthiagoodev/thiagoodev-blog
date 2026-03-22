package br.com.thiagoodev.blog.auth.presentation.utils

import br.com.thiagoodev.blog.modules.auth.domain.entities.Token
import br.com.thiagoodev.blog.modules.auth.presentation.dtos.TokenResponseDto
import br.com.thiagoodev.blog.modules.auth.presentation.utils.fromToken
import net.datafaker.Faker
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Suite de Testes Unitários Puros para o Mapeador de Token.
 *
 * Objetivo de Estudo:
 * Testar mapeadores (Mappers) pode parecer redundante, mas é uma das redes de segurança
 * mais baratas e eficientes da nossa pirâmide de testes. Como a execução não envolve o
 * Spring Boot, este teste roda na casa dos microssegundos.
 *
 * Por que testar algo tão simples?
 * Se amanhã outro desenvolvedor alterar acidentalmente o contrato do DTO (por exemplo,
 * esperando que a variável interna se chame 'accessToken' em vez de 'token') ou modificar
 * a ordem dos parâmetros na função construtora, este teste irá quebrar instantaneamente,
 * prevenindo que um erro de formatação de JSON chegue até o cliente final.
 */
class TokenResponseDtoMapperTest {

    private val faker = Faker()

    /**
     * CENÁRIO DE ESTUDO ÚNICO: Garantia de Contrato (Contract Guarantee).
     *
     * Validação Arquitetural:
     * Assegura que os dados gerados pela camada de domínio (a entidade [Token]) sejam
     * perfeitamente copiados para o objeto de transferência de dados ([TokenResponseDto]),
     * respeitando as nomenclaturas exigidas pela resposta HTTP da nossa API.
     */
    @Test
    fun `should correctly map Token entity to TokenResponseDto`() {
        /** Arrange (Preparação): Criamos uma entidade de domínio pura com dados aleatórios. */
        val tokenEntity = Token(
            accessToken = faker.internet().uuid(),
            expiresIn = faker.number().numberBetween(3600L, 7200L)
        )

        /** Act (Ação): Invocamos a nossa Extension Function atrelada ao Companion Object. */
        val dto = TokenResponseDto.fromToken(tokenEntity)

        /**
         * Assert (Verificação): Confirmamos a correspondência exata dos valores.
         * Note a diferença de nomenclatura: `tokenEntity.accessToken` vira `dto.token`.
         */
        assertEquals(tokenEntity.accessToken, dto.token)
        assertEquals(tokenEntity.expiresIn, dto.expiresIn)
    }
}