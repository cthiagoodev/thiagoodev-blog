# thiagoodev Blog

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.x-purple.svg?logo=kotlin)](https://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-green.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Jaspr](https://img.shields.io/badge/Jaspr-SSR-blue.svg?logo=dart)](https://github.com/schultek/jaspr)
[![Architecture](https://img.shields.io/badge/Architecture-Event%20Driven-orange.svg)]()
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg?logo=docker)]()

**thiagoodev Blog** é a minha plataforma oficial de artigos e conteúdo técnico. Mais do que um blog, este projeto serve como um *playground* de engenharia de software para demonstrar e experimentar conceitos avançados de **Sistemas Distribuídos**, **Server-Side Rendering (SSR)** e **Arquitetura Limpa**.

O foco do conteúdo abrange: Desenvolvimento de Software, Resenhas de Livros Técnicos, Arquitetura de Sistemas e Carreira em Tech.

---

## 🏗 Arquitetura & Design

Este repositório contém o **Blog Service** e o **Frontend SSR**. Ele foi projetado para operar dentro de um ecossistema de microsserviços, comunicando-se assincronamente com outros domínios (como o *Portfolio Service*).

### Principais Decisões Técnicas

1.  **Backend (Spring Boot + Kotlin):**
    * Adoção de **Clean Architecture** (Domain, Application, Infra, Presentation) para isolar as regras de negócio de frameworks.
    * Uso de **Kotlin 2.3** para aproveitar features modernas de concorrência e expressividade.

2.  **Frontend (Jaspr + Dart):**
    * Utilização do **Jaspr** para trazer a DX (Developer Experience) do Flutter para a Web Nativa.
    * **SSR (Server-Side Rendering):** O HTML é gerado no servidor para SEO perfeito e *First Contentful Paint* instantâneo.
    * **Hydration:** Pequenos bundles JS "hidratam" a página para interatividade no cliente.

3.  **Event-Driven Integration (Kafka):**
    * O Blog não consulta o serviço de Portfólio/Palestras via HTTP síncrono.
    * Ele assina tópicos no **Kafka** para receber eventos (ex: `TalkCreatedEvent`). Isso garante desacoplamento total: se o Portfólio cair, o Blog continua servindo conteúdo.

4.  **Qualidade & Testes (TDD):**
    * O desenvolvimento segue estritamente o **TDD (Test-Driven Development)**, garantindo que todo código de negócio nasça testado e desacoplado.
---

## 🛠 Tech Stack

### Backend Service
* **Linguagem:** Kotlin
* **Framework:** Spring Boot 4
* **Database:** PostgreSQL 18
* **Messaging:** Apache Kafka
* **Migrations:** Flyway
* **Testing:** JUnit 5

### Frontend Service
* **Framework:** Jaspr (Dart)
* **Client HTTP:** Dio

---# thiagoodev-blog
