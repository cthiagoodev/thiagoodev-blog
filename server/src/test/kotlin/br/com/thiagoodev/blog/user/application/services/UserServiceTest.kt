package br.com.thiagoodev.blog.user.application.services

import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCrypt
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Classe de Testes Unitários Puros para o [UserService].
 *
 * Arquitetura de Testes:
 * Diferente do @WebMvcTest e do @DataJpaTest, aqui NÃO carregamos o contexto do Spring Boot.
 * Isso significa que a injeção de dependência nativa do Spring (@Autowired) não funciona.
 * A vantagem? O teste executa em frações de segundo, pois foca exclusivamente na lógica de negócio (CPU bound).
 *
 * @ExtendWith(MockitoExtension::class): Instrui o JUnit 5 a habilitar o motor do Mockito para esta classe.
 * É esta extensão que vai ler as anotações @Mock e @InjectMocks e orquestrar a criação dos dublês
 * antes de executar os métodos de teste.
 */
@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    /**
     * @Mock: Cria um objeto "Dublê" (Proxy) da interface UserRepository.
     * Diferente do @MockitoBean (que injeta o mock no contexto do Spring), o @Mock é puramente do Mockito.
     * Ele intercepta qualquer chamada feita a este repositório e permite que programemos seu comportamento.
     */
    @Mock
    private lateinit var userRepository: UserRepository

    /**
     * @InjectMocks: O orquestrador da Injeção de Dependência Manual.
     * O Mockito instanciará a classe REAL [UserService] e procurará no construtor dela
     * por dependências que coincidam com os campos anotados com @Mock nesta classe de teste.
     * Resultado: Teremos um UserService de verdade, mas que se comunica com um UserRepository falso.
     */
    @InjectMocks
    private lateinit var userService: UserService

    private val faker = Faker()
    private lateinit var user: User

    /**
     * Padrão de Isolamento: Prepara o estado inicial (Arrange) antes de cada método @Test.
     * A geração de dados dinâmicos com Faker evita que os testes fiquem viciados em "Magic Strings"
     * (strings fixas como "teste@teste.com"), garantindo que a lógica funcione para qualquer entrada.
     */
    @BeforeEach
    fun setup() {
        user = User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password()
        )
    }

    /**
     * CENÁRIO: Fluxo de Sucesso (Regra de Negócio e Criptografia).
     *
     * Valida três responsabilidades exclusivas da camada de Serviço:
     * 1. A orquestração das chamadas ao banco (verificar existência -> salvar).
     * 2. A mutação de estado seguro (hashing da senha do usuário em texto plano).
     * 3. O retorno da entidade processada.
     */
    @Test
    fun `Should return a new user with hashed password when the data is valid`() {
        /** Salva o estado original da senha para comparação posterior. */
        val plainPassword = user.password

        /** Arrange: Programa o repositório falso para simular que o e-mail está disponível e que o salvamento ocorreu com sucesso. */
        `when`(userRepository.existsByEmail(user.email)).thenReturn(false)
        `when`(userRepository.save(user)).thenReturn(user)

        /** Act: Aciona a lógica de negócio real. */
        val createdUser = userService.create(user)

        /**
         * Assertivas de Regra de Negócio:
         * Garante que a senha em texto plano NUNCA seja retornada ou persistida.
         * Utiliza o utilitário do BCrypt para validar se o hash gerado pelo Serviço
         * corresponde matematicamente à senha original.
         */
        assertNotEquals(plainPassword, createdUser.password)
        assertTrue(BCrypt.checkpw(plainPassword, createdUser.password))

        /**
         * Verificação Comportamental (Behavior Verification):
         * Tão importante quanto validar o retorno é garantir a sequência exata de chamadas às dependências.
         * Confirma que o Serviço consultou a existência e tentou salvar no banco exatamente uma vez.
         */
        verify(userRepository, times(1)).existsByEmail(user.email)
        verify(userRepository, times(1)).save(user)
    }

    /**
     * CENÁRIO: Fluxo de Exceção (Fail-Fast e Proteção de Dados).
     *
     * Valida o comportamento de guarda (Guard Clause) do serviço ao detectar um conflito de dados.
     */
    @Test
    fun `Should throw UserAlreadyExistsException when email is already registered`() {
        /** Arrange: Programa o repositório para simular que o banco já possui este e-mail. */
        `when`(userRepository.existsByEmail(user.email)).thenReturn(true)

        /**
         * Assertiva de Exceção (Kotlin Test):
         * A função [assertFailsWith] captura a execução do bloco lambda.
         * O teste só passa se a exceção exata (UserAlreadyExistsException) for arremessada
         * de dentro do método [userService.create].
         */
        assertFailsWith<UserAlreadyExistsException> {
            userService.create(user)
        }

        /**
         * Verificação Comportamental:
         * Confirma que o serviço fez a consulta inicial.
         */
        verify(userRepository, times(1)).existsByEmail(user.email)

        /**
         * Validação Crítica de Segurança:
         * Garante que a execução abortou imediatamente após a exceção, impedindo que
         * o repositório invocasse o método `save()`. (Nota: times(0) é o equivalente funcional a never()).
         */
        verify(userRepository, times(0)).save(user)
    }
}