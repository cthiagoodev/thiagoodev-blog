package br.com.thiagoodev.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
class BlogApplication

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}
