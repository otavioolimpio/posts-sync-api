# Posts Sync API

API REST desenvolvida em **Java 21** e **Spring Boot 3.3.4** para gerenciamento e sincronização de postagens, integrada ao **PostgreSQL 16** via **Docker**.

---

## Tecnologias Utilizadas

* **Java 21** (LTS)
* **Spring Boot 3.3.4**
* **Spring Data JPA** & **Hibernate 6**
* **PostgreSQL 16** (via Docker Compose)
* **Lombok**
* **Maven**

---

## Arquitetura e Decisões Técnicas

* **Isolamento de Portas (`5433:5432`):** A porta externa do container foi configurada para `5433` no `docker-compose.yml` para evitar conflitos de porta (*Port Conflict*) com instâncias locais do PostgreSQL rodando no ambiente de desenvolvimento na porta padrão (`5432`).
* **Padronização de Ambiente (Java 21 LTS + Spring Boot 3.3.4):** Adotadas versões estáveis e com suporte a longo prazo para garantir compatibilidade com a especificação Jakarta EE 10 e Hibernate 6.
* **Empacotamento Executável (Fat JAR):** A aplicação utiliza o servidor web **Apache Tomcat** embutido do Spring Boot, permitindo a execução autônoma do artefato `.jar`.
* **Estratégia de Branching no Git (*Feature Branching*):** Isolamento de novas funcionalidades em *branches* dedicadas para manter a *branch* principal (`main`) sempre estável e pronta para deploy.

---

## Como Executar o Projeto

### Pré-requisitos
* **Java 21** instalado
* **Docker Desktop** em execução
* **Git**

### 1. Clonar o repositório
```bash
git clone https://github.com/otavioolimpio/posts-sync-api.git
cd posts-sync-api
```

### 2. Subir o container do banco de dados PostgreSQL
```Bash
docker-compose up -d
```

### 3. Compilar e executar a aplicação
```Bash
docker-compose up -d
```