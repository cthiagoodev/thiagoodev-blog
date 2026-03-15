package br.com.thiagoodev.blog.common.config.security

import br.com.thiagoodev.blog.common.filters.AuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * O Coração da Infraestrutura de Segurança do Sistema.
 * Centraliza as regras de acesso, políticas de sessão e provedores de criptografia.
 */
@Configuration
class SecurityConfiguration(private val authenticationFilter: AuthenticationFilter) {

    /**
     * O SecurityFilterChain atua como a "catraca" do servidor web.
     * Todas as requisições HTTP batem nesta configuração antes de chegar a qualquer Controller.
     * * Configurações Críticas:
     * - `STATELESS`: Desliga o controle de sessão via Cookies/Memória do Spring. Em APIs RESTful com JWT,
     * o servidor não deve "lembrar" quem está logado entre uma requisição e outra. O token deve ser enviado sempre.
     * - `addFilterBefore`: Insere o nosso [AuthenticationFilter] (que checa o JWT) antes do filtro padrão do Spring,
     * garantindo que o framework reconheça nosso usuário autenticado antes de checar se a rota é protegida.
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/publications/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/**", "/users/create").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    /**
     * Expõe o [AuthenticationManager] nativo do Spring como um Bean injetável.
     * Ele é usado dentro do `AuthService` para executar de fato o processo de conferência de credenciais.
     */
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    /**
     * Define o algoritmo oficial de criptografia da aplicação.
     * Ao registrar o [BCryptPasswordEncoder] como um Bean, o `AuthenticationManager` do Spring utilizará
     * automaticamente esta classe para fazer o hash das senhas em texto plano submetidas durante o login,
     * para compará-las com os hashes guardados no banco.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}