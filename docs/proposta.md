# Proposta do produto — Replay

**Disciplina:** DIM0547 — DESENVOLVIMENTO DE SISTEMAS WEB II
**Semestre:** 2026.2
**Repositório:** [https://github.com/vinippires/Projeto-Replay/tree/main]

---

## 1. Visão do produto

Para colecionadores, cinéfilos e amantes de música
Que possuem CDs e DVDs parados em casa ou buscam itens específicos para suas coleções
O Replay é uma plataforma web de troca e compra de mídias físicas
Que permite cadastrar o acervo pessoal, buscar obras e negociar diretamente com outros usuários
Diferente de marketplaces genéricos ou de sebos físicos tradicionais
Nosso produto é focado exclusivamente no nicho de entretenimento físico, conectando pessoas localmente com um chat em tempo real para fechar negócio.

---

## 2. Definição do MVP

| No MVP | Fora do MVP |
| :--- | :--- |
| Cadastro e autenticação de conta de usuário | Integração com gateways de pagamento |
| Criação de anúncios de CDs e DVDs (com fotos e estado de conservação) | Cálculo de frete e logística de entrega |
| Busca no catálogo por título, artista/diretor ou formato | Player de reprodução de mídia ou prévias de áudio |
| Chat interno (mensageria em tempo real) para negociação | Sistema complexo de recomendações por IA |
| Atualização de status do anúncio (disponível/negociado) | Painel administrativo avançado para moderação |

**Hipótese de valor:** acreditamos que entusiastas e colecionadores vão cadastrar seus CDs e DVDs não utilizados na plataforma, em vez de deixá-los pegando pó na estante, porque desejam renovar seus acervos e adquirir novos itens sem gastar o valor de um produto lacrado em loja.

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
A escolha se justifica pela preferência da equipe por uma sintaxe menos verbosa, a garantia de segurança de tipos (null-safety) e o uso de recursos modernos da linguagem, como coroutines. O Ktor, sendo leve, nos dá a flexibilidade e a produtividade necessárias para acelerar o desenvolvimento do MVP e lidar de forma eficiente e assíncrona com as requisições do catálogo de mídias.

---

## 6. Divisão de responsabilidades entre o serviço principal e Go
O sistema será dividido entre um serviço principal (Kotlin/Ktor) e um microsserviço dedicado (Go):
* **Serviço Principal (Kotlin/Ktor):** Ficará responsável pelo domínio relacional (CRUD), como gerenciamento de usuários, autenticação e gerenciamento do catálogo de anúncios de mídias.
* * **Microsserviço (Go):** Será responsável exclusivamente pelo Chat (WebSockets) e mensageria em tempo real.

---

## 7. Equipe

| Nome | Matrícula | Papel |
| :--- | :--- | :--- |
| Vinícius Daniel Pires Thome | 20230037303 | Desenvolvimento Backend (Kotlin/Go) |
| Pedro Vitor de Oliveira Moura | 20230036093 | Desenvolvimento Backend (Kotlin/Go) |

---

## 8. Coorte de apresentação

**Coorte:** [Inserir Coorte A, B, etc., ou formato da apresentação]

Não há integração com outra disciplina neste semestre.
