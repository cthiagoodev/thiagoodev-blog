package br.com.thiagoodev.blog.auth.presentation.controllers

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.auth.application.services.AuthService
import br.com.thiagoodev.blog.modules.auth.domain.entities.Token
import br.com.thiagoodev.blog.modules.auth.presentation.controllers.AuthController
import br.com.thiagoodev.blog.modules.auth.presentation.dtos.CredentialsDto
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import kotlin.test.Test

/**
 * Suite de Testes de Integração da Camada Web (Slice Test) para o [AuthController].
 *
 * Arquitetura de Isolamento:
 * O @WebMvcTest carrega APENAS a infraestrutura web do Spring (Filtros, Conversores JSON,
 * Validadores do Jakarta e o Controller específico). O banco de dados e os serviços pesados
 * ficam de fora, garantindo que o teste foque apenas na porta de entrada HTTP.
 */
@WebMvcTest(AuthController::class)
/**
 * Desligamento de Filtros (addFilters = false):
 * Como a rota de login (/auth/) é a rota onde o usuário adquire o token, não faz sentido
 * passarmos pelo nosso `AuthenticationFilter` (que exige um token). Desligar os filtros
 * globais aqui evita o problema do "ovo e a galinha" nos testes desta rota específica.
 */
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    /**
     * O Postman embutido do Spring. Ele simula requisições HTTP e roteia diretamente
     * para o nosso Controller sem precisar abrir portas de rede (como localhost:8080).
     */
    @Autowired
    private lateinit var mockMvc: MockMvc

    /**
     * Responsável por serializar nossos DTOs Kotlin em JSON e vice-versa.
     */
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    /**
     * Substitui o serviço real de autenticação por um Dublê (Mock) dentro do contexto do Spring.
     * Como não temos banco de dados aqui, nós vamos ditar o que este serviço deve responder.
     */
    @MockitoBean
    private lateinit var authService: AuthService

    /**
     * Mocks de Infraestrutura:
     * Mesmo desligando os filtros com `addFilters = false`, o Spring Security pode tentar
     * inicializar alguns beans de segurança em background. Fornecemos esses mocks para
     * satisfazer as dependências do contexto Web e evitar falhas de injeção na subida do teste.
     */
    @MockitoBean
    private lateinit var jwtService: JwtService

    @MockitoBean
    private lateinit var userRepository: UserRepository

    private val faker = Faker()
    private lateinit var credentialsDto: CredentialsDto
    private lateinit var token: Token

    /**
     * Padrão Arrange (Preparação):
     * Garante dados limpos e aleatórios antes de cada cenário de teste, evitando "Magic Strings"
     * e contaminação de estado entre os testes.
     */
    @BeforeEach
    fun setup() {
        credentialsDto = CredentialsDto(
            email = faker.internet().emailAddress(),
            password = faker.credentials().password()
        )

        token = Token(
            accessToken = faker.internet().uuid(),
            expiresIn = faker.number().numberBetween(3600L, 7200L)
        )
    }

    /**
     * CENÁRIO DE ESTUDO 1: Sucesso na Autenticação (Caminho Feliz).
     *
     * Validações Arquiteturais:
     * 1. O Controller recebe o JSON corretamente e o mapeia para [CredentialsDto].
     * 2. O Controller aciona o [AuthService] repassando os dados do DTO.
     * 3. O Controller mapeia a entidade [Token] retornada para um [TokenResponseDto].
     * 4. A resposta HTTP é 200 (OK) com a estrutura JSON esperada.
     */
    @Test
    fun `should return 200 OK and the token data when credentials are valid`() {
        /** Ensina o mock: "Se alguém te passar esse email e senha exatos, devolva este token." */
        `when`(authService.authenticate(credentialsDto.email, credentialsDto.password))
            .thenReturn(token)

        val payload = objectMapper.writeValueAsString(credentialsDto)

        /** DSL do Kotlin para o MockMvc: Uma sintaxe declarativa, limpa e imune a erros de digitação. */
        mockMvc.post("/auth/") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value(token.accessToken) }
            jsonPath("$.expires_in") { value(token.expiresIn) }
        }
    }

    /**
     * CENÁRIO DE ESTUDO 2: Proteção da Camada de Serviço (Fail-Fast).
     *
     * Validações Arquiteturais:
     * Testa o comportamento da anotação `@Valid` no Controller.
     * Se o JSON chegar com dados inválidos (ex: email em branco), o Spring Validation deve
     * interceptar a requisição e devolver um erro 400 (Bad Request) IMEDIATAMENTE.
     */
    @Test
    fun `should return 400 Bad Request when credentials payload is invalid`() {
        /** Cria intencionalmente um DTO que viola a restrição @NotBlank do campo email. */
        val invalidDto = CredentialsDto(
            email = "   ",
            password = faker.credentials().password()
        )

        val payload = objectMapper.writeValueAsString(invalidDto)

        mockMvc.post("/auth/") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isBadRequest() }
        }

        /**
         * A Prova Definitiva (Behavior Verification):
         * Garante ao nível de framework que o [AuthService] JAMAIS foi acionado.
         * Isso prova que a requisição foi abortada pelo validador do Jakarta antes mesmo
         * de entrar no escopo interno da função do Controller, economizando processamento.
         */
        verify(authService, never()).authenticate(any(), any())
    }
}