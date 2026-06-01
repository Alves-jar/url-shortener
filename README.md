# 🔗 URL Shortener

API REST para encurtamento de URLs construída com **Spring Boot 4** e **SQL Server 2022**, containerizado com Docker.

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring Data JPA | — |
| SQL Server | 2022 |
| Docker / Docker Compose | — |
| Maven | — |

---

## Estrutura do Projeto

```
urlShortener/
├── src/
│   └── main/
│       ├── java/com/noxus/urlShortener/
│       │   ├── controller/     # UrlController — endpoints REST
│       │   ├── service/        # UrlService — regras de negócio
│       │   ├── repository/     # UrlRepository — acesso ao banco
│       │   ├── model/          # Url — entidade JPA
│       │   └── dto/            # UrlRequest — record de entrada
│       └── resources/
│           └── application.yaml
├── init/
│   └── init.sql                # Script de criação do banco e tabela
├── docker-compose.yml
└── pom.xml
```

---

## Configuração e Execução

### Pré-requisitos

- Java 25+
- Maven
- Docker Desktop

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Aguarde o container ficar com status `healthy`:

```bash
docker compose ps
```

### 2. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## Banco de Dados

A conexão é configurada em `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=url_shortener;encrypt=true;trustServerCertificate=true
    username: sa
    password: AkzZ@1234
```

O script `init/init.sql` cria automaticamente o banco `url_shortener` e a tabela `urls` na primeira execução do container.

### Estrutura da tabela `urls`

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | BIGINT IDENTITY | Chave primária |
| `shorten_code` | VARCHAR(20) | Código curto único |
| `original_url` | NVARCHAR(2000) | URL original |
| `clicks` | BIGINT | Contador de acessos |
| `created_at` | DATETIME2 | Data de criação |
| `expire_at` | DATETIME2 | Data de expiração (nullable) |

---

## Endpoints

### Encurtar uma URL

```http
POST /save
Content-Type: application/json

{
  "url": "https://www.exemplo.com.br/pagina/muito/longa"
}
```

**Resposta** `201 Created`:

```json
{
  "id": 1,
  "originalUrl": "https://www.exemplo.com.br/pagina/muito/longa",
  "shortenCode": "a1b2c3d4",
  "clicks": 0,
  "createdAt": "2026-05-28T19:00:00",
  "expireAt": "2026-06-04T19:00:00"
}
```

---

### Buscar uma URL pelo endereço original

```http
GET /get?url=https://www.exemplo.com.br/pagina/muito/longa
```

**Resposta** `200 OK`: retorna o objeto da URL cadastrada.

---

### Redirecionar pelo código curto

```http
GET /{shortCode}
```

**Respostas possíveis:**

| Status | Situação |
|---|---|
| `302 Found` | Redireciona para a URL original e incrementa o contador de cliques |
| `404 Not Found` | Código curto não encontrado |
| `410 Gone` | URL expirada (removida automaticamente) |

---

## Como funciona o encurtamento

1. A URL recebida é validada como URI válida.
2. Verifica se a URL já foi cadastrada — se sim, retorna o registro existente.
3. Gera um hash MD5 da URL original e usa os primeiros 8 caracteres como código curto.
4. O link é salvo com expiração de **7 dias** a partir da criação.

---

## Observações

- Links expirados são **removidos do banco** automaticamente no primeiro acesso após a expiração.
- O contador de cliques é incrementado a cada redirecionamento bem-sucedido.
- Não é possível definir uma data de expiração customizada via API — atualmente é sempre 7 dias.