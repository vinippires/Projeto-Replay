# Proposta do produto — Replay

**Disciplina:** DIM0547 — DESENVOLVIMENTO DE SISTEMAS WEB II
**Semestre:** 2026.2
**Repositório:** [https://github.com/vinippires/Projeto-Replay/tree/main]

---

## 1. Visão do produto

Para colecionadores, cinéfilos e amantes de música que possuem CDs e DVDs parados em casa ou buscam itens específicos para suas coleções.  O Replay é uma plataforma web de troca e compra de mídias físicas que permite cadastrar o acervo pessoal, buscar obras e negociar diretamente com outros usuários. 
Diferente de marketplaces genéricos ou de sebos físicos tradicionais, nosso produto é focado exclusivamente no nicho de entretenimento físico, conectando pessoas localmente com um chat em tempo real para fechar negócio.

---

## 2. Definição do MVP

| No MVP | Fora do MVP |
| :--- | :--- |
| Cadastro e autenticação de conta de usuário | Integração com gateways de pagamento |
| Criação de anúncios de CDs e DVDs (com fotos e estado de conservação) | Cálculo de frete e logística de entrega |
| Busca no catálogo por título, artista/diretor ou formato | Player de reprodução de mídia ou prévias de áudio |
| Chat interno (mensageria em tempo real) para negociação | Sistema complexo de recomendações por IA |
| Atualização de status do anúncio (disponível/negociado) | Painel administrativo avançado para moderação |

---

## 3. Backlog inicial

O backlog está no quadro do GitHub Projects deste repositório: [https://github.com/users/vinippires/projects/1/views/1]

---

## 4. Entidades principais do domínio
Com base no MVP definido, o domínio do sistema gira em torno das seguintes entidades principais:

* **Usuário:** Responsável pelo cadastro na plataforma, gerenciamento do seu acervo pessoal e interação com outros colecionadores.
* **Anúncio:** Representa os CDs e DVDs cadastrados, contendo os dados do produto (título, estado de conservação, fotos).
* **Mensagem:** Responsável por registrar a comunicação em tempo real entre o usuário interessado e o dono do anúncio para a negociação.

---

## 5. Decisão: Kotlin/Ktor ou Java/Quarkus
**Decisão:** Kotlin/Ktor

Optamos por Kotlin com Ktor porque a equipe deseja explorar recursos modernos da linguagem e garantir maior robustez no código através da segurança de tipos (null-safety). O domínio do sistema exige lidar de forma assíncrona com as requisições do catálogo de mídias, e as corrotinas do Kotlin facilitam essa concorrência de maneira eficiente. Além disso, o Ktor ser mais leve e flexível nos proporciona a produtividade necessária para acelerar o desenvolvimento do nosso MVP.

---

## 6. Divisão de responsabilidades entre o serviço principal e Go
O sistema será dividido entre um serviço principal (Kotlin/Ktor) e um microsserviço dedicado (Go):
* **Serviço Principal (Kotlin/Ktor):** Ficará responsável pelo domínio relacional (CRUD), como gerenciamento de usuários, autenticação e gerenciamento do catálogo de anúncios de mídias.
* **Microsserviço (Go):** Será responsável exclusivamente pelo Chat (WebSockets) e mensageria em tempo real.

---

## 7. Equipe

| Nome | Matrícula | Papel |
| :--- | :--- | :--- |
| Vinícius Daniel Pires Thome | 20230037303 | Desenvolvimento Fullstack (Kotlin/Go/HTML/JS) |
| Pedro Vitor de Oliveira Moura | 20230036093 | Desenvolvimento Fullstack (Kotlin/Go/HTML/JS) |

---

## 8. Coorte de apresentação e integração com outra disciplina

**Coorte:b**, apresentações online.

há integração com outra disciplina neste semestre: (PROCESSOS DE SOFTWARE e DESENVOLVIMENTO DE SISTEMAS WEB II)
