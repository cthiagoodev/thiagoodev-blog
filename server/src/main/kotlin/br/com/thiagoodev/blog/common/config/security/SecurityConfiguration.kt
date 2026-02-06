package br.com.thiagoodev.blog.common.config.security

import br.com.thiagoodev.blog.common.filters.AuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration(private val authenticationFilter: AuthenticationFilter) {
    companion object {
        val PUBLIC_ENDPOINTS: Array<String> = arrayOf(
            "/api/publications/**",
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, *PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, AuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}