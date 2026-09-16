# Proposta do produto — Replay

**Disciplina:** DIM0547 — Programação Back-end
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

O backlog está no quadro do GitHub Projects deste repositório: [Inserir o link do seu GitHub Projects aqui]

---

## 4. Plataforma-alvo

**Escolha: Web (Navegador).**

O produto é voltado para a exibição de um catálogo de mídias e gerenciamento de acervo pessoal. A escolha pela Web permite que qualquer usuário acesse a plataforma instantaneamente de um computador ou celular, sem a barreira de entrada de precisar baixar e instalar um aplicativo apenas para anunciar ou buscar um disco específico. 

**Alternativa descartada: Aplicativo Mobile Nativo (Android/iOS).**

Exigiria o desenvolvimento de duas bases de código distintas (ou uso de frameworks híbridos complexos) e passaria por processos de aprovação em lojas de aplicativos. Para um MVP de marketplace de nicho, o atrito de instalação não compensa o ganho de funcionalidades nativas (como GPS contínuo ou câmera nativa), já que o upload de fotos via navegador atende perfeitamente à necessidade.

---

## 5. Estratégia de backend

**Escolha: Kotlin com Ktor (Serviço Principal) e Go (Microsserviço).**

O domínio do sistema é relacional (usuários têm muitos anúncios, anúncios geram mensagens). O Kotlin com Ktor foi escolhido para a API principal por ser leve, moderno e lidar de forma assíncrona com o alto volume de requisições do catálogo de mídias, garantindo produtividade e segurança de tipos (null-safety). Em paralelo, um microsserviço em Go será responsável exclusivamente pelo Chat (WebSockets), isolando a carga de I/O concorrente do serviço principal.

**Alternativas descartadas.**

**Java com Quarkus:** Embora seja uma excelente opção madura, foi descartada porque a equipe preferiu a sintaxe menos verbosa e os recursos modernos da linguagem Kotlin (como coroutines) para acelerar o desenvolvimento do MVP.

**Monolito Único:** Manter o serviço de chat (mensageria em tempo real) acoplado ao CRUD principal de mídias no mesmo servidor poderia gerar gargalos de performance e consumo excessivo de memória, prejudicando a navegação no catálogo.

---

## 6. Equipe

| Nome | Matrícula | Papel |
| :--- | :--- | :--- |
| Vinícius Daniel Pires Thome | 20230037303 | Desenvolvimento Backend (Kotlin/Go) |
| Pedro Vitor de Oliveira Moura | 20230036093 | Desenvolvimento Backend (Kotlin/Go) |

*(Nota: Ajustem os papéis se houver divisão de front-end/back-end entre vocês)*

---

## 7. Coorte de apresentação e integração com outra disciplina

**Coorte:** [Inserir Coorte A, B, etc., ou formato da apresentação]

Não há integração com outra disciplina neste semestre.