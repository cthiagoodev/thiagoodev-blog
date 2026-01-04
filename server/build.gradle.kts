plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.thiagoodev"
version = "0.0.1-SNAPSHOT"
description = "Uma plataforma de conteúdo High-Performance projetada com arquitetura de Microsserviços Orientada a Eventos. O sistema utiliza Kotlin (Spring Boot) no backend para garantir robustez e escalabilidade, integrado via Kafka para sincronização assíncrona de dados. O frontend entrega renderização no lado do servidor (SSR) utilizando Jaspr (Dart), unindo a experiência de desenvolvimento do Flutter com a otimização de SEO da web nativa."

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
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
