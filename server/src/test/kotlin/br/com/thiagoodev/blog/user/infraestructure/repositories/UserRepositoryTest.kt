package br.com.thiagoodev.blog.user.infrastructure.repositories

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Classe de testes de integração da camada de Persistência (Slice Test) para o [UserRepository].
 *
 * Arquitetura Spring Boot:
 * A anotação @DataJpaTest é o equivalente do @WebMvcTest, mas para o Banco de Dados.
 * Ela corta a camada Web e a camada de Serviços. O Spring carrega APENAS:
 * - Entidades (@Entity)
 * - Repositórios do Spring Data JPA
 * - Configurações do Hibernate
 * * Magia Oculta:
 * Por padrão, o @DataJpaTest substitui o seu banco de produção por um banco de dados em memória
 * (como o H2), caso você o tenha nas dependências de teste. Além disso, cada método @Test
 * é executado dentro de uma TRANSAÇÃO que sofre ROLLBACK automático no final. Ou seja, você
 * insere dados, testa, e o Spring apaga tudo sozinho antes do próximo teste começar.
 */
@DataJpaTest
/**
 * @TestPropertySource permite sobrescrever as configurações do `application.yml` apenas para esta classe.
 * * "spring.flyway.enabled=false": Se você usa Flyway (ferramenta de migração de banco), rodar todos os scripts
 * do zero a cada teste pode deixar a suíte lenta. Desligamos o Flyway aqui.
 * * "spring.jpa.hibernate.ddl-auto=create-drop": Como desligamos o Flyway, pedimos para o próprio Hibernate
 * olhar para as nossas anotações @Entity e criar as tabelas no banco de testes automaticamente antes de
 * rodar a classe, e apagá-las no final.
 */
@TestPropertySource(properties = [
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
])
class UserRepositoryTest {

    /**
     * O Repositório real que queremos testar. Note que não usamos @MockitoBean aqui,
     * pois queremos a implementação VERDADEIRA que o Spring Data JPA gera para nós
     * se comunicando com o banco de testes.
     */
    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * [TestEntityManager] é uma ferramenta exclusiva para testes fornecida pelo Spring.
     * Ela é uma versão "anabolizada" do EntityManager padrão do JPA.
     * * Por que usamos ele em vez do `userRepository.save()` para preparar os dados?
     * Resposta: Para garantir a independência do teste. Se quisermos testar o método `findUniqueByEmail`,
     * e usarmos o `repository.save()` para inserir o dado, não saberemos se o teste passou porque o
     * `find` funciona bem ou apenas porque o `save` jogou o dado no cache da memória.
     * Usar o EntityManager contorna o repositório para injetar dados direto na veia do banco.
     */
    @Autowired
    private lateinit var entityManager: TestEntityManager

    private val faker = Faker()
    private lateinit var user: User

    /**
     * Prepara uma entidade User nova e desvinculada do banco antes de cada teste.
     */
    @BeforeEach
    fun setup() {
        user = User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
            permissions = mutableSetOf(Permission.READ, Permission.ADMIN)
        )
    }

    /**
     * CENÁRIO: Consulta por E-mail com Entidades Relacionadas.
     * * Testa se o repositório consegue encontrar o usuário pelo e-mail e se o Hibernate
     * monta a entidade corretamente, incluindo listas/sets (como a lista de permissões).
     */
    @Test
    fun `Should return user with fetched permissions when email exists`() {
        // Arrange (Prepara)
        /** * persistAndFlush(): Salva o usuário e FORÇA o Hibernate a enviar o comando `INSERT`
         * para o banco de dados imediatamente. Sem o flush, o Hibernate poderia segurar
         * o insert na memória para enviar depois.
         */
        entityManager.persistAndFlush(user)

        /**
         * O SEGREDO DOS TESTES JPA: entityManager.clear()
         * O Hibernate possui o "Cache de Primeiro Nível" (L1 Cache). Se você insere um dado, ele fica
         * na memória local. Se você faz um `find()` logo depois, o Hibernate devolve o objeto da memória
         * e NEM VAI AO BANCO DE DADOS (não gera um SELECT real).
         * O comando `clear()` apaga a memória do Hibernate. Isso garante que a linha abaixo
         * seja obrigada a disparar um comando SELECT SQL verdadeiro contra o banco.
         */
        entityManager.clear()

        // Act (Ação)
        val foundUser = userRepository.findUniqueByEmail(user.email)

        // Assert (Verificação)
        assertNotNull(foundUser) // Garante que não veio nulo
        assertEquals(user.email, foundUser.email) // Garante que trouxe o e-mail certo

        // Verifica se a coleção de permissões foi carregada (Fetched) corretamente do banco
        assertEquals(2, foundUser.permissions.size)
        assertTrue(foundUser.permissions.contains(Permission.READ))
        assertTrue(foundUser.permissions.contains(Permission.ADMIN))
    }

    /**
     * CENÁRIO: Tratamento de Dados Inexistentes (Caminho Negativo).
     * * Garante que a query retorna null em vez de lançar uma exceção
     * (EmptyResultDataAccessException) quando o registro não é encontrado.
     */
    @Test
    fun `Should return null when finding by non-existent email`() {
        // Passamos um e-mail gerado aleatoriamente que NUNCA foi salvo no banco de testes
        val foundUser = userRepository.findUniqueByEmail(faker.internet().emailAddress())

        assertNull(foundUser)
    }

    /**
     * CENÁRIO: Validação de Existência - Verdadeiro (Exists Query).
     * * O Spring Data JPA traduz a palavra "exists" para uma query super otimizada
     * (ex: SELECT 1 FROM users WHERE email = ? LIMIT 1).
     * Este teste verifica se a query retorna `true` quando o dado está lá.
     */
    @Test
    fun `Should return true when checking existence of registered email`() {
        // Salva o usuário (Não precisamos do clear() aqui porque não estamos extraindo
        // a entidade completa do banco de volta para a memória, apenas checando um booleano).
        entityManager.persistAndFlush(user)

        val exists = userRepository.existsByEmail(user.email)

        assertTrue(exists)
    }

    /**
     * CENÁRIO: Validação de Existência - Falso (Exists Query).
     * * Garante que a otimização do "exists" retorna `false` corretamente
     * se o e-mail não bater com nenhuma linha da tabela.
     */
    @Test
    fun `Should return false when checking existence of unregistered email`() {
        val exists = userRepository.existsByEmail(faker.internet().emailAddress())

        assertFalse(exists)
    }
}