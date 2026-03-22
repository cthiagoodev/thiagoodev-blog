package br.com.thiagoodev.blog.common.error.controllers

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Interceptador Global de Exceções (Controller Advice).
 *
 * Arquitetura AOP (Aspect-Oriented Programming):
 * Esta classe utiliza a anotação [@RestControllerAdvice]. Isso significa que ela funciona como um
 * "guarda-chuva" invisível sobre TODOS os Controllers da aplicação. Se qualquer exceção estourar
 * em um Controller (ou em um Service chamado por ele) e não for tratada com try/catch, ela
 * "voará" até bater neste escudo. Aqui, nós pegamos a exceção nua, logamos (se necessário) e a
 * transformamos em um JSON padronizado e amigável para o cliente (Frontend/Mobile).
 *
 * A Ordem de Precedência:
 * A anotação [@Order(Ordered.LOWEST_PRECEDENCE)] é uma jogada arquitetural de mestre.
 * Ela diz ao Spring: "Se existir algum outro @RestControllerAdvice mais específico no sistema
 * (como o seu `UserErrorController`), deixe ele tentar tratar o erro primeiro. Se ele não souber
 * como tratar, aí sim o erro cai para mim como último recurso." Isso evita conflitos de tratamento.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalErrorController {

    /**
     * O "Cata-Tudo" (Catch-All Fallback).
     * Se uma exceção genérica (NullPointerException, erro de rede, etc.) estourar e não tiver
     * nenhum tratamento específico previsto, ela cai aqui.
     * Retorna o temido 500 Internal Server Error, garantindo que a API não devolva o "Stack Trace"
     * (o código cru com as linhas de erro do Java) para o cliente final, o que seria uma falha de
     * segurança grave de exposição de dados.
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Internal server error",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ).toResponseEntity()
    }

    /**
     * Tratamento de Exceções de Domínio (Business Rules).
     * Quando o nosso `UserService` ou `AuthService` não encontra um usuário no banco de dados,
     * eles lançam essa exceção puramente lógica. O Spring a captura e traduz para um HTTP 404 (Not Found).
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "User not found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()
    }

    /**
     * Tratamento de Violação de Integridade do Banco de Dados.
     * Esta exceção é lançada diretamente pelo Hibernate/JPA quando uma regra do banco de dados é
     * quebrada (por exemplo, tentar salvar um e-mail duplicado em uma coluna que tem restrição UNIQUE,
     * ou tentar salvar nulo em uma coluna NOT NULL).
     * Retorna HTTP 400 (Bad Request) informando que os dados enviados corrompem o estado do banco.
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        ex: DataIntegrityViolationException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Data invalid",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    /**
     * O Motor de Tradução do Jakarta Validation.
     *
     * Como o Spring aciona isso:
     * Quando um Controller recebe um DTO anotado com `@Valid`, o motor do Spring roda as validações
     * (como `@NotBlank` ou `@Email`). Se alguma validação falhar, o Spring aborta a chamada do Controller
     * IMEDIATAMENTE e lança a [MethodArgumentNotValidException].
     *
     * O que fazemos aqui:
     * Nós abrimos essa exceção, extraímos a lista de todos os campos que deram erro (`ex.bindingResult.fieldErrors`)
     * e usamos o poder do Kotlin (`map`) para transformar essa lista complexa do Java em um array de JSONs
     * super limpo, contendo apenas o nome do campo ("field") e o motivo do erro ("error").
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<List<Map<String, String>>> {
        val fieldErrors: List<FieldError> = ex.bindingResult.fieldErrors
        val errorBody: List<Map<String, String>> =
            fieldErrors.map { field ->
                mapOf(
                    "field" to field.field,
                    "error" to (field.defaultMessage ?: "Invalid field")
                )
            }

        return ResponseEntity.badRequest().body(errorBody)
    }
}