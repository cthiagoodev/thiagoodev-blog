package br.com.thiagoodev.blog.user.presentation.controllers

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import br.com.thiagoodev.blog.modules.user.presentation.controllers.UserController
import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import tools.jackson.databind.ObjectMapper
import kotlin.test.Test

/**
 * Classe de testes de integração da camada Web (Slice Test) para o [UserController].
 *
 * Arquitetura Spring Boot:
 * A anotação @WebMvcTest instrui o framework a criar um "mini-contêiner" de Inversão de Controle (IoC).
 * Em vez de carregar a aplicação inteira (Banco de dados, Repositórios, Serviços de Negócio), o Spring
 * carrega EXCLUSIVAMENTE os componentes da camada de apresentação:
 * - Controllers (@RestController)
 * - Filtros Web e Interceptadores
 * - Conversores de JSON (Jackson)
 * - Validadores (@Valid)
 * - Tratadores de Exceção globais (@ControllerAdvice)
 *
 * A anotação @AutoConfigureMockMvc(addFilters = false) é uma estratégia de isolamento. Como o Spring Security
 * está presente no projeto, ele intercepta todas as requisições por padrão. Ao definirmos `addFilters = false`,
 * desligamos a barreira de segurança globalmente neste teste. Isso nos permite focar em testar a lógica pura de
 * rotas públicas (como a de registro) sem a complexidade de gerar tokens reais para cada teste.
 */
@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    /**
     * O [MockMvc] é o principal motor de testes web do Spring. Ele funciona como um "Postman interno".
     * Em vez de abrir uma porta de rede real (ex: localhost:8080), ele envia requisições HTTP simuladas
     * diretamente para o DispatcherServlet do Spring (o maestro que roteia as requisições para os Controllers).
     * Isso torna os testes extremamente rápidos e imunes a problemas de rede.
     */
    @Autowired
    private lateinit var mockMvc: MockMvc

    /**
     * O [WebApplicationContext] é o registro central (o cérebro) do contêiner do Spring Web.
     * Nós injetamos ele aqui para podermos recriar instâncias do [MockMvc] sob demanda
     * durante testes específicos (por exemplo, quando precisarmos ligar os filtros de segurança novamente).
     */
    @Autowired
    private lateinit var context: WebApplicationContext

    /**
     * O [ObjectMapper] é a biblioteca padrão do Spring (Jackson) responsável pela serialização
     * e desserialização. Ele traduz nossos objetos Kotlin (ex: DTOs) em Strings no formato JSON
     * para enviarmos no corpo (body) das requisições simuladas.
     */
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    /**
     * @MockitoBean é o substituto moderno (Spring Boot 3.4+) para o antigo @MockBean.
     * Como o @WebMvcTest cortou a camada de negócio, o Spring não tem uma instância real do [UserService]
     * para injetar no [UserController]. Esta anotação cria um "Dublê" (Mock) usando o Mockito e o insere
     * diretamente no contêiner do Spring.
     */
    @MockitoBean
    private lateinit var userService: UserService

    /**
     * Mocks de Infraestrutura de Segurança:
     * Embora o [UserController] não dependa de [JwtService] e [UserRepository], os Filtros do Spring Security
     * (que são carregados junto com a camada Web) dependem deles para validar tokens e buscar dados do usuário.
     * Se não mockarmos essas dependências, o contêiner do Spring falhará ao tentar instanciar o filtro de
     * autenticação durante a inicialização do teste.
     */
    @MockitoBean
    private lateinit var jwtService: JwtService

    @MockitoBean
    private lateinit var userRepository: UserRepository

    private val faker = Faker()
    private lateinit var user: User
    private lateinit var createUserDto: CreateUserDto

    /**
     * Padrão de Isolamento de Testes.
     * O método anotado com @BeforeEach é executado antes da execução de CADA método @Test.
     * O objetivo é recriar o estado dos dados (Payloads e Entidades) do zero, garantindo que
     * a mutação de dados ou comportamento em um teste não cause efeitos colaterais (flaky tests)
     * nos testes subsequentes.
     */
    @BeforeEach
    fun setUp() {
        val randomName = faker.name().fullName()
        val randomEmail = faker.internet().emailAddress()
        val randomPassword = faker.credentials().password()
        val randomCreatedDate = faker.timeAndDate().birthday().atStartOfDay()

        createUserDto = CreateUserDto(
            name = randomName,
            email = randomEmail,
            password = randomPassword
        )

        user = User(
            name = randomName,
            email = randomEmail,
            password = randomPassword,
            createdAt = randomCreatedDate,
        )
    }

    /**
     * CENÁRIO: Fluxo de Sucesso (Happy Path).
     *
     * Valida o comportamento base do endpoint de criação. Garante que:
     * 1. A requisição POST atinge a rota correta.
     * 2. O Controller aceita o JSON e o desserializa para o [CreateUserDto].
     * 3. O Controller chama o serviço adequadamente.
     * 4. A resposta possui o status HTTP 201 (Created) e o corpo da resposta corresponde aos dados do usuário.
     */
    @Test
    fun `Should return 201 Created and the user data when payload is valid`() {
        /** Ensina o mock: Quando a função create() for chamada com QUALQUER parâmetro, retorne o usuário pré-fabricado. */
        `when`(userService.create(any())).thenReturn(user)

        val payload = objectMapper.writeValueAsString(createUserDto)

        /** DSL Nativa do Kotlin para o MockMvc. Encapsula as configurações da requisição HTTP de forma declarativa. */
        mockMvc.post("/users/create") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
            /** O jsonPath avalia a resposta HTTP percorrendo a árvore do JSON. O símbolo "$" representa a raiz do JSON. */
            jsonPath("$.name") { value(user.name) }
            jsonPath("$.email") { value(user.email) }
        }
    }

    /**
     * CENÁRIO: Validação de Entrada Interceptada (Fail-Fast).
     *
     * Valida o comportamento da anotação @Valid no Controller.
     * Se um DTO com restrições violadas for enviado, o Spring Validation interceptará a requisição antes
     * mesmo dela entrar no escopo da função do Controller, retornando um status 400.
     */
    @Test
    fun `Should return 400 Bad Request when email is invalid`() {
        /** Monta um DTO propositalmente inválido para acionar a restrição @Email. */
        val invalidDto = CreateUserDto(
            name = faker.name().fullName(),
            email = "invalid-email",
            password = faker.credentials().password()
        )

        val payload = objectMapper.writeValueAsString(invalidDto)

        mockMvc.post("/users/create") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isBadRequest() }
        }

        /**
         * Verificação crucial de otimização arquitetural:
         * Garante que o Mockito registre que o [UserService] NUNCA foi acionado. Isso prova que o framework web
         * lidou com a rejeição dos dados ruins, protegendo a camada de negócios e o banco de dados.
         */
        verify(userService, never()).create(any())
    }

    /**
     * CENÁRIO: Tratamento de Exceções de Regra de Negócio.
     *
     * Simula uma falha na camada de negócio (ex: Violação de unicidade no banco de dados).
     * Avalia se a arquitetura do Spring (possivelmente via @ControllerAdvice ou mapeamento padrão)
     * é capaz de converter a [UserAlreadyExistsException] de uma exceção interna do Java/Kotlin
     * para uma resposta HTTP semântica de erro (Status 409 Conflict).
     */
    @Test
    fun `Should return error status when user email already exists`() {
        /** Modifica o comportamento do mock para lançar uma exceção em vez de retornar dados. */
        `when`(userService.create(any()))
            .thenThrow(UserAlreadyExistsException::class.java)

        val payload = objectMapper.writeValueAsString(createUserDto)

        mockMvc.post("/users/create") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isConflict() }
        }
    }

    /**
     * CENÁRIO: Resolução de Dependências do Spring Security (@AuthenticationPrincipal).
     *
     * A rota /users/me depende de extrair os dados do usuário atual a partir do token validado na requisição.
     * Como desativamos os filtros globalmente no topo da classe, precisamos recriar o MockMvc localmente
     * com a cadeia de filtros de segurança ativa.
     */
    @Test
    fun `Should return 200 OK and logged user data when authenticated`() {
        /** * Constrói uma nova instância isolada do MockMvc.
         * A função `apply<DefaultMockMvcBuilder>(springSecurity())` empacota a requisição com
         * toda a arquitetura de segurança real configurada no projeto.
         */
        val secureMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        /** Prepara um Dublê de Autenticação. O Spring precisa de uma classe que implemente UserDetails. */
        val userDetailsMock = mock(UserDetailsAdapter::class.java)
        `when`(userDetailsMock.getUser()).thenReturn(user)

        secureMockMvc.get("/users/me") {
            /** * A função `user()` é um RequestPostProcessor fornecido pelo pacote de testes do Spring Security.
             * Em vez de precisarmos assinar um token JWT real no teste, este comando injeta o objeto mockado
             * diretamente no SecurityContextHolder da requisição simulada, satisfazendo a anotação @AuthenticationPrincipal.
             */
            with(user(userDetailsMock))
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value(user.name) }
            jsonPath("$.email") { value(user.email) }
        }
    }

    /**
     * CENÁRIO: Validação da Cadeia de Autorização (Filtros).
     *
     * Garante que endpoints restritos rejeitem acessos anônimos.
     * Se um usuário tentar acessar `/users/me` sem um token ou credenciais na requisição,
     * o filtro de segurança (SecurityFilterChain) deve abortar a execução e retornar 401.
     */
    @Test
    fun `Should return 401 when accessing logged user route without authentication`() {
        /** Novamente, utilizamos o MockMvc blindado com a infraestrutura de segurança ativa. */
        val secureMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        /** * Realizamos um GET puro, intencionalmente omitindo o `with(user(...))`.
         * A expectativa é que o Status seja 401 (Unauthorized), significando falha de autenticação.
         */
        secureMockMvc.get("/users/me")
            .andExpect {
                status { isUnauthorized() }
            }
    }
}