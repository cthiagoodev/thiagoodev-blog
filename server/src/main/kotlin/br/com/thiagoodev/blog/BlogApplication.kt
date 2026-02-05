package br.com.thiagoodev.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableWebSecurity
class BlogApplication

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}
