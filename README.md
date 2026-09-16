# Replay 💿📀

O **Replay** é uma plataforma focada no nicho de colecionadores e entusiastas, conectando pessoas localmente para facilitar a troca, compra e venda de mídias físicas, como CDs e DVDs.

Este é o monorepo do projeto, desenvolvido para a disciplina DIM0547. Ele contém o serviço principal (Java/Quarkus) e o microsserviço de chat (Go).

## 👥 Equipe
* **Vinícius Daniel Pires Thome** - Matrícula: 20230037303
* **Pedro Vitor de Oliveira Moura** - Matrícula: 20230036093

**Coorte:** [Inserir a sua Coorte, ex: Coorte 1]

## 🎥 Apresentação (Sprint 0)
* **Vídeo de Apresentação:** [Link do vídeo no YouTube/Drive aqui]
* **Documento de Proposta:** [Link ou caminho para docs/proposta.md]
* **Quadro de Tarefas (Backlog):** [Link para o GitHub Projects]

## 📂 Estrutura do Repositório

Conforme a arquitetura de referência, o repositório está dividido em:

* `api/` - Contém o microsserviço de mensageria (Go).
* `services/` - Contém o serviço principal da aplicação (Java/Quarkus).
* `protos/` - Contém as definições de contratos/buffers (se aplicável).
* `docs/` - Contém a documentação do projeto, incluindo a `proposta.md`.

## 🚀 Como Rodar Localmente

### Pré-requisitos
Para rodar este projeto, você precisará ter instalado na sua máquina:
* [Mise](https://mise.jdx.dev/) (para gerenciamento de ferramentas e tasks)
* [Docker e Docker Compose](https://www.docker.com/) (para os serviços dependentes)
* Java 21+ e Go 1.22+ (podem ser gerenciados pelo Mise)

### Comandos Principais (Mise Tasks)

O projeto utiliza o arquivo `mise.toml` na raiz para padronizar a execução de tarefas. 

1. **Para compilar os projetos (Serviço principal e microsserviço):**
   ```bash
   mise run build
