package br.com.thiagoodev.blog.auth.application.services

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.auth.application.services.AuthService
import br.com.thiagoodev.blog.modules.auth.domain.exceptions.NotAuthenticatedException
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Testes Unitários Puros para o Serviço de Autenticação ([AuthService]).
 *
 * Objetivo de Estudo:
 * Diferente dos testes de Controller ou Repository, aqui nós NÃO levantamos o contexto do Spring Boot.
 * O objetivo é testar EXCLUSIVAMENTE a lógica da função `authenticate`, isolando-a de bancos de dados
 * reais e da infraestrutura HTTP.
 * * Por que isolar? Para garantir que o nosso código saiba reagir a todos os cenários possíveis
 * (sucesso, usuário não encontrado, senha errada, falha no JWT) em milissegundos, sem depender de
 * sistemas externos estarem online.
 */
@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    /**
     * @Mock cria "Dublês" (Proxies) das nossas dependências.
     * Como não temos o Spring para injetar as classes reais, o Mockito cria versões falsas dessas
     * interfaces/classes. Nós temos total controle sobre o que essas versões falsas vão responder
     * quando forem chamadas pelo nosso [AuthService].
     */
    @Mock
    private lateinit var jwtService: JwtService

    /**
     * O [AuthenticationManager] é o motor do Spring Security. Mockar ele é crucial para testarmos
     * o fluxo de login sem precisarmos engatilhar toda a validação real de BCrypt e sessões.
     */
    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var userRepository: UserRepository

    /**
     * O [Authentication] é a interface que o Spring retorna quando um login dá certo.
     * Nós a mockamos para podermos forçar o retorno do método `isAuthenticated()` para true ou false
     * durante os nossos testes.
     */
    @Mock
    private lateinit var authentication: Authentication

    /**
     * @InjectMocks é o maestro. Ele pega a nossa classe REAL [AuthService] e injeta os @Mocks
     * (declarados acima) dentro do construtor dela.
     */
    @InjectMocks
    private lateinit var authService: AuthService

    private val faker = Faker()

    /**
     * CENÁRIO DE ESTUDO 1: O Caminho Feliz (Happy Path).
     * * Valida o fluxo perfeito de login:
     * 1. O usuário existe no banco.
     * 2. A senha está correta (o AuthenticationManager aprova).
     * 3. O JWT é gerado com sucesso.
     * 4. O objeto Token final é retornado com os dados corretos.
     */
    @Test
    fun `should authenticate user and return token successfully`() {
        val name = faker.name().fullName()
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()
        val generatedToken = faker.internet().uuid()
        val expiration = faker.number().numberBetween(3600L, 7200L)

        val user = User(
            name = name,
            email = email,
            password = password,
            permissions = mutableSetOf(),
        )

        /** Fase de Preparação (Arrange): Ensinando os Mocks a responderem com sucesso. */
        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        `when`(jwtService.buildToken(email)).thenReturn(generatedToken)
        `when`(jwtService.getExpiration(generatedToken)).thenReturn(expiration)

        /** Fase de Ação (Act): Aciona o nosso serviço real. */
        val result = authService.authenticate(email, password)

        /** Fase de Verificação (Assert de Estado): Garante que a saída (Output) está correta. */
        assertEquals(generatedToken, result.accessToken)
        assertEquals(expiration, result.expiresIn)

        /** * Fase de Verificação (Assert de Comportamento):
         * Garante que o serviço NÃO "pulou" etapas. Ele TEM que ter consultado o banco,
         * chamado o validador de senhas e gerado o token, cada um exatamente 1 vez.
         */
        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
    }

    /**
     * CENÁRIO DE ESTUDO 2: Fail-Fast (Falha Rápida) por E-mail Inexistente.
     * * Se o e-mail não existe no banco, não faz sentido gastar CPU chamando o AuthenticationManager
     * para validar uma senha. O sistema deve estourar a exceção [UserNotFoundException] e abortar a operação imediatamente.
     */
    @Test
    fun `should throw UserNotFoundException when email does not exist`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        /** O repositório falso avisa: "Não achei ninguém com esse e-mail". */
        `when`(userRepository.findUniqueByEmail(email)).thenReturn(null)

        /** Verifica se a exceção correta foi lançada pelo nosso serviço. */
        assertFailsWith<UserNotFoundException> {
            authService.authenticate(email, password)
        }

        /** O comportamento de Fail-Fast sendo testado na prática: garante que os métodos abaixo NUNCA foram chamados. */
        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, never()).buildToken(anyString())
    }

    /**
     * CENÁRIO DE ESTUDO 3: Validação Explícita do Status de Autenticação.
     * * Embora o Spring geralmente lance exceções para falhas, esta é uma camada de proteção da nossa
     * regra de negócio. Se, por acaso, o objeto retornado disser que `isAuthenticated == false`,
     * a nossa exceção customizada deve proteger a emissão do JWT.
     */
    @Test
    fun `should throw NotAuthenticatedException when authentication is not successful`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        /** Forçando o mock da autenticação a simular um estado "Não Autenticado" (false). */
        `when`(authentication.isAuthenticated).thenReturn(false)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)

        assertFailsWith<NotAuthenticatedException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        /** Garante que, mediante a falha lógica acima, nenhum JWT foi emitido para um usuário não autenticado. */
        verify(jwtService, never()).buildToken(anyString())
    }

    /**
     * CENÁRIO DE ESTUDO 4: Repasse de Exceções Nativas do Spring Security.
     * * Quando o usuário digita a senha errada, o `AuthenticationManager` dispara nativamente a
     * [BadCredentialsException]. O nosso serviço não deve mascarar esse erro; ele deve deixá-lo
     * "subir" para que o nosso Controller Advice o capture mais tarde.
     */
    @Test
    fun `should throw exception when authentication manager throws BadCredentialsException`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        /** Simula o motor do Spring "estourando" ao ver que a senha não bate com o Hash do banco. */
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(BadCredentialsException::class.java)

        /** Verifica se o nosso serviço permitiu a exceção subir (bubble up). */
        assertFailsWith<BadCredentialsException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, never()).buildToken(anyString())
    }

    /**
     * CENÁRIO DE ESTUDO 5: Falha Inesperada na Emissão do JWT.
     * * Se a lib do JWT falhar (ex: chave secreta inválida), o processo deve quebrar
     * antes de tentar recuperar a expiração de um token que não existe.
     */
    @Test
    fun `should throw exception when jwtService fails to build token`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        /** Simulando uma quebra interna no serviço de tokens. */
        `when`(jwtService.buildToken(email)).thenThrow(RuntimeException::class.java)

        assertFailsWith<RuntimeException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
        /** Como o `buildToken` falhou, o `getExpiration` nunca deve ser invocado. */
        verify(jwtService, never()).getExpiration(anyString())
    }

    /**
     * CENÁRIO DE ESTUDO 6: Falha Inesperada na Recuperação da Expiração.
     * * O JWT foi gerado, mas ocorreu um erro de parsing logo na sequência ao
     * tentarmos descobrir a data exata de expiração.
     */
    @Test
    fun `should throw exception when jwtService fails to get expiration`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()
        val generatedToken = faker.internet().uuid()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        `when`(jwtService.buildToken(email)).thenReturn(generatedToken)
        /** Simulando o erro no último passo do serviço. */
        `when`(jwtService.getExpiration(generatedToken)).thenThrow(RuntimeException::class.java)

        assertFailsWith<RuntimeException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
        verify(jwtService, times(1)).getExpiration(generatedToken)
    }
}