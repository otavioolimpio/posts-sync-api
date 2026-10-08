# Posts Sync API

API REST desenvolvida em Java 21 e Spring Boot 3.3.4 para gestão e sincronização de postagens a partir de uma API pública (JSONPlaceholder), integrada ao PostgreSQL 16 via Docker.

---

## Tecnologias Utilizadas

* Java 21 (LTS)
* Spring Boot 3.3.4
* Spring Data JPA & Hibernate 6
* PostgreSQL 16 (via Docker Compose)
* RestClient (Spring Framework 6 / Spring Boot 3)
* JUnit 5 & Mockito (Testes automatizados)
* Maven

---

## Arquitetura e Decisões Técnicas

* Isolamento de Portas (5433:5432): A porta externa do container foi configurada para 5433 no docker-compose.yml para evitar conflitos de porta (Port Conflict) com instâncias locais do PostgreSQL rodando no ambiente de desenvolvimento na porta padrão (5432).
* Compilação Estrita e POJOs Nativos: Optou-se por utilizar métodos getters/setters e construtores Java explícitos no modelo e DTOs, garantindo compatibilidade imediata no pipeline de compilação Maven com Java 21 sem dependência de processadores de anotação.
* Cliente HTTP Moderno (RestClient): Utilização da API fluente RestClient (introduzida no Spring 6) para consumo da API externa, substituindo o componente legado RestTemplate.
* Idempotência na Sincronização: O serviço de sincronização (PostService) verifica a existência de cada post antes de salvar. Se o post já existir no PostgreSQL, realiza a atualização do registro (updatedAt); caso contrário, realiza a inserção (createdAt).
* Tratamento Global de Exceções (@RestControllerAdvice): Captura erros de integração com a API externa (retornando status HTTP 502 Bad Gateway) e exceções inesperadas (status HTTP 500 Internal Server Error).
* Estratégia de Branching no Git (Feature Branching): Desenvolvimento isolado em branches de funcionalidade com merge concluído na branch principal (main).

---

## Endpoints da API

| Método | Endpoint | Descrição | Status HTTP |
| :--- | :--- | :--- | :--- |
| POST | /sync | Consome a API pública e sincroniza os posts no banco de dados | 201 Created |
| GET | /posts | Lista todas as postagens armazenadas no banco de dados | 200 OK |
| GET | /posts/{id} | Procura e retorna uma postagem específica pelo ID | 200 OK / 404 Not Found |
| GET | /health | Verifica o estado e a saúde da aplicação | 200 OK |

---

## Como Executar o Projeto

### Pré-requisitos
* Java 21
* Docker Desktop
* Git

### 1. Clonar o repositório
```bash
git clone https://github.com/otavioolimpio/posts-sync-api.git
cd posts-sync-api
```

### 2. Subir o container do banco de dados PostgreSQL
```Bash
docker-compose up -d
```

### 3. Executar os testes automatizados
```Bash
.\mvnw.cmd test
```

### 4. Compilar e executar a aplicação
```Bash
.\mvnw.cmd clean package -DskipTests
java -jar target/posts-sync-api-0.0.1-SNAPSHOT.jar
```