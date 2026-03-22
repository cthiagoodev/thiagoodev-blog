package br.com.thiagoodev.blog.modules.publication.infrastructure.repositories

import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.UUID

/**
 * Contrato de Persistência (Repository) para a entidade [Publication].
 *
 * Arquitetura de Acesso a Dados (Spring Data JPA):
 * Esta interface herda de [JpaRepository], ganhando dezenas de métodos prontos
 * (save, findById, delete). O Spring em tempo de execução gera uma classe concreta
 * implementando esta interface através de Reflection e Proxies.
 */
interface PublicationRepository : JpaRepository<Publication, UUID> {

    /**
     * Padrão 1: Derived Query Methods (Consultas Derivadas do Nome).
     * O Spring lê a assinatura do método ("findAll", "By", "DeletedAt", "IsNull") e
     * monta o SQL sozinho.
     *
     * Paginação Embutida:
     * Ao receber um [Pageable] e retornar um [Page], o Spring Data otimiza a busca
     * executando um "LIMIT" e "OFFSET" automático no banco, economizando RAM ao não
     * carregar milhares de publicações de uma vez.
     */
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<Publication>

    /**
     * Garante a recuperação de uma publicação por UUID apenas se ela não sofreu Soft Delete.
     */
    fun findByUuidAndDeletedAtIsNull(uuid: UUID): Publication?

    /**
     * Busca um conjunto de publicações criadas dentro de um intervalo de tempo,
     * ignorando as que foram deletadas logicamente.
     */
    fun findAllByCreatedAtBetweenAndDeletedAtIsNull(start: LocalDateTime, end: LocalDateTime): List<Publication>

    /**
     * Padrão 2: JPQL (Java Persistence Query Language) + Modifying.
     *
     * Por que não trazer a entidade para a memória, fazer `publication.viewsCount++` e salvar?
     * Porque em um Blog de alto tráfego, isso gera Condição de Corrida (Race Condition).
     * Dois usuários lendo ao mesmo tempo leriam "10", somariam "+1" e salvariam "11"
     * (perdendo um view).
     *
     * A anotação [@Modifying] instrui o Spring de que isso não é um SELECT.
     * O UPDATE direto via JPQL força o próprio banco de dados (que garante atomicidade/ACID)
     * a enfileirar as atualizações e somar com segurança no HD.
     */
    @Modifying
    @Query("UPDATE Publication p SET p.viewsCount = p.viewsCount + 1 WHERE p.uuid = :uuid")
    fun incrementViews(uuid: UUID)

    /**
     * Padrão 3: Native SQL (Consultas Nativas).
     *
     * Para casos onde o JPQL não suporta uma sintaxe específica ou queremos extrair a máxima
     * performance usando recursos exclusivos do dialeto do banco de dados (ex: LIMIT 1 no Postgres/MySQL).
     * A flag `nativeQuery = true` avisa ao Hibernate: "Não tente traduzir isso, apenas jogue
     * essa string crua no motor de execução de SQL do banco".
     */
    @Query(
        value = "SELECT * FROM publications WHERE deleted_at IS NULL ORDER BY views_count DESC LIMIT 1",
        nativeQuery = true
    )
    fun findMostViewed(): Publication?
}