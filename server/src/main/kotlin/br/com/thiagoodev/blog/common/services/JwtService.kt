package br.com.thiagoodev.blog.common.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

/**
 * Serviço de Criptografia e Emissão de Tokens (JSON Web Token).
 * Responsável por gerar os "crachás de acesso" da aplicação e validá-los matematicamente.
 * * Uma vez gerado o token, a aplicação não precisa de uma tabela de banco de dados extra
 * para controlar as sessões; a simples assinatura válida com a [secretKey] prova que
 * o token foi gerado pelo nosso servidor e não foi adulterado.
 */
@Service
class JwtService {
    @Value("\${jwt.secret.key}")
    private lateinit var secretKey: String
    private val issuer: String = "Blog"
    private val expiration: Date = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)

    private fun getSigningKey(): SecretKey {
        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun buildToken(subject: String): String {
        return Jwts.builder()
            .subject(subject)
            .signWith(getSigningKey())
            .issuer(issuer)
            .expiration(expiration)
            .compact()
    }

    fun getSubject(token: String): String {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .requireIssuer(issuer)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun getExpiration(token: String): Long {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .requireIssuer(issuer)
            .build()
            .parseSignedClaims(token)
            .payload
            .expiration.time
    }
}