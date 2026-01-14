plugins {
	kotlin("jvm") version "2.3.0"
	kotlin("plugin.spring") version "2.3.0"
	kotlin("plugin.jpa") version "2.3.0"
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.thiagoodev"
version = "0.0.1-SNAPSHOT"
description = "Uma plataforma de conteúdo High-Performance projetada com arquitetura de Microsserviços Orientada a Eventos. O sistema utiliza Kotlin (Spring Boot) no backend para garantir robustez e escalabilidade, integrado via Kafka para sincronização assíncrona de dados. O frontend entrega renderização no lado do servidor (SSR) utilizando Jaspr (Dart), unindo a experiência de desenvolvimento do Flutter com a otimização de SEO da web nativa."

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:11.20.0")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
