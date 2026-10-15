# api-ktor — serviço principal (Kotlin/Ktor)

CRUD de **Usuários** e **Anúncios** (1:N) do Replay. PostgreSQL + Flyway + Exposed.

## Subir

```bash
docker compose up --build        # na raiz: Postgres + API
```

Só o banco (para rodar a API pelo Gradle):

```bash
docker compose up db -d
cd api-ktor && ./gradlew run
```

- Swagger UI: http://localhost:8080/swagger
- OpenAPI (JSON): http://localhost:8080/api.json

Variáveis de ambiente (padrões de desenvolvimento): `DB_URL`, `DB_USER`, `DB_PASSWORD`, `PORT`.

## Camadas (`src/main/kotlin/replay`)

| Pacote | Papel |
|---|---|
| `dominio` | Modelos, erros e interfaces de repositório. Sem framework. |
| `casosdeuso` | Regras e validação. |
| `adaptadores/banco` | Implementação dos repositórios (Exposed) e Flyway. |
| `adaptadores/http` | Rotas Ktor, DTOs e erros em `application/problem+json`. |

## Rotas

| Método | Rota | Observação |
|---|---|---|
| GET/POST | `/usuarios` | `?pagina&tamanho&cidade` |
| GET/PUT/DELETE | `/usuarios/{id}` | |
| GET/POST | `/usuarios/{id}/anuncios` | relacionamento 1:N |
| GET/POST | `/anuncios` | `?pagina&tamanho&formato&status&titulo&usuarioId` |
| GET/PUT/DELETE | `/anuncios/{id}` | |
