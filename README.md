# Replay

O **Replay** é uma plataforma focada no nicho de colecionadores e entusiastas, conectando pessoas localmente para facilitar a troca, compra e venda de mídias físicas, como CDs e DVDs.

## Equipe
* **Vinícius Daniel Pires Thome** - Matrícula: 20230037303
* **Pedro Vitor de Oliveira Moura** - Matrícula: 20230036093

**Coorte:** [b, apresentações online]

## Apresentação (Sprint 0)
* **Vídeo de Apresentação:** [https://drive.google.com/file/d/11irgwcPDJ9cGOTLo1x2IQ1zPy8-ml4Hu/view?usp=sharing]
* **Documento de Proposta:** (https://github.com/vinippires/Projeto-Replay/blob/main/docs/proposta.md)
* **Backlog:** https://github.com/users/vinippires/projects/1/views/1

## Estrutura do Repositório

Conforme a arquitetura de referência, o repositório está dividido em:

* `api/` - Contém o microsserviço de mensageria (Go).
* `services/` - Contém o serviço principal da aplicação (Java/Quarkus).
* `protos/` - Contém as definições de contratos/buffers (se aplicável).
* `docs/` - Contém a documentação do projeto, incluindo a `proposta.md`.

## Como Rodar Localmente
Para rodar este projeto, você precisará ter instalado na sua máquina:
* [Mise] (para gerenciamento de ferramentas e tasks)
* [Docker e Docker Compose] (para os serviços dependentes)
* Java 21+ e Go 1.22+ (podem ser gerenciados pelo Mise)

## Comandos Principais (Mise Tasks)

O projeto utiliza o arquivo `mise.toml` na raiz para padronizar a execução de tarefas. 

1. **Para compilar os projetos (Serviço principal e microsserviço):**
   ```bash
   mise run build
