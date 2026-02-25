package br.com.thiagoodev.blog.modules.auth.presentation.utils

import br.com.thiagoodev.blog.modules.auth.domain.entities.Token
import br.com.thiagoodev.blog.modules.auth.presentation.dtos.TokenResponseDto

fun TokenResponseDto.Companion.fromToken(token: Token): TokenResponseDto {
    return TokenResponseDto(
        token = token.accessToken,
        expiresIn = token.expiresIn,
    )
}