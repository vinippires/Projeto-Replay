# Replay

O **Replay** é uma plataforma focada no nicho de colecionadores e entusiastas, conectando pessoas localmente para facilitar a troca, compra e venda de mídias físicas, como CDs e DVDs.

## Equipe

- **Vinícius Daniel Pires Thome** - Matrícula: 20230037303
- **Pedro Vitor de Oliveira Moura** - Matrícula: 20230036093

**Coorte:** b, apresentações online

## Entregas

- **Sprint 0** — Vídeo: https://drive.google.com/file/d/11irgwcPDJ9cGOTLo1x2IQ1zPy8-ml4Hu/view?usp=sharing · Proposta: [docs/proposta.md](docs/proposta.md)
- **Sprint 1** — Vídeo: _(colar o link aqui)_
- **Backlog:** https://github.com/users/vinippires/projects/1/views/1

## Estrutura do repositório

- `api-ktor/` — serviço principal em **Kotlin/Ktor** (CRUD de usuários e anúncios, PostgreSQL, Flyway). Ver [api-ktor/README.md](api-ktor/README.md).
- `services/` — microsserviço de chat em tempo real (**Go**, WebSockets), previsto para as próximas sprints.
- `docs/` — documentação do projeto (`proposta.md`).
- `docker-compose.yml` — PostgreSQL e API.

## Como rodar

Pré-requisitos: Docker e Docker Compose (Java 21 e Go podem ser gerenciados pelo [Mise](https://mise.jdx.dev)).

```bash
docker compose up --build     # ou: mise run up
```

- **Swagger UI:** http://localhost:8080/swagger
- **OpenAPI (JSON):** http://localhost:8080/api.json

## Tasks do Mise

```bash
mise run build   # compila os projetos
mise run up      # sobe banco + API com docker compose
```

## Sprint 1 — o que foi entregue

- CRUD completo de `Usuario` e `Anuncio` (relação 1:N), com paginação e filtros feitos no SQL
- Migrações versionadas com Flyway (`api-ktor/src/main/resources/db/migration`)
- Arquitetura em camadas (domínio sem dependência de framework)
- Validação de entradas e erros em *problem details* (RFC 9457)
- OpenAPI gerado do código, com Swagger UI

Os testes (unitários, integração e de arquitetura) ficam para a Sprint 2.
