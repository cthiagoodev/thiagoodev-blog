package br.com.thiagoodev.blog.modules.user.presentation.controllers

import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.presentation.utils.toUser
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import br.com.thiagoodev.blog.modules.user.presentation.dtos.UserResponseDto
import br.com.thiagoodev.blog.modules.user.presentation.utils.fromUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    /**
     * Rota Autenticada: Recupera os dados do usuário atualmente logado.
     *
     * Integração com Spring Security:
     * A anotação [@AuthenticationPrincipal] é um "atalho" do Spring Web. Em vez de precisarmos
     * consultar o banco de dados pelo usuário a cada requisição, o Spring Web acessa o
     * `SecurityContextHolder` (que foi preenchido pelo nosso `AuthenticationFilter` momentos antes).
     * Ele extrai o objeto principal logado e o injeta diretamente como parâmetro nesta função.
     * Isso garante performance e segurança, pois a identidade já foi validada na entrada.
     */
    @GetMapping("/me")
    fun getCurrentlyLoggedUser(
        @AuthenticationPrincipal userDetails: UserDetailsAdapter,
    ): ResponseEntity<UserResponseDto> {
        val response = UserResponseDto.fromUser(userDetails.getUser())
        return ResponseEntity.ok(response)
    }

    /**
     * Rota Pública: Criação de um novo usuário.
     *
     * Ciclo de Vida da Senha:
     * Neste ponto, o [CreateUserDto] recebe a senha exatamente como o usuário digitou (Texto Plano / Plain Text).
     * O Controller NÃO deve encriptar a senha. Sua única função é receber o JSON e passá-lo para a
     * camada de serviço. É o `UserService` (não mostrado aqui, mas testado anteriormente) que terá a
     * responsabilidade de pegar essa senha em texto plano e transformá-la em um Hash BCrypt ANTES de
     * invocar o repositório para salvar no banco. O Spring Security está configurado para ignorar
     * a exigência de tokens nesta rota.
     */
    @PostMapping("/create")
    fun create(
        @Valid @RequestBody dto: CreateUserDto,
    ): ResponseEntity<UserResponseDto> {
        val user: User = userService.create(dto.toUser())
        val response = UserResponseDto.fromUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}