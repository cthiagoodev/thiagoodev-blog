package br.com.thiagoodev.blog.common.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value("\${jwt.secret.key}")
    private lateinit var secretKey: String

    private fun getSigningKey(): SecretKey {
        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(subject: String): String {
        return Jwts.builder()
            .subject(subject)
            .signWith(getSigningKey())
            .compact()
    }
}