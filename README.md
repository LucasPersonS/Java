# Quiz API

Uma API RESTful para gerenciar quizzes, construída com Java e Jersey. Esta aplicação permite que os usuários criem, leiam, atualizem e excluam quizzes, além de gerenciar perguntas dentro de cada quiz.

## Índice

- [Recursos](#recursos)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Documentação da API](#documentação-da-api)
  - [Endpoints](#endpoints)
    - [Criar um Quiz](#criar-um-quiz)
    - [Obter Todos os Quizzes](#obter-todos-os-quizzes)
    - [Obter um Quiz por ID](#obter-um-quiz-por-id)
    - [Atualizar um Quiz](#atualizar-um-quiz)
    - [Excluir um Quiz](#excluir-um-quiz)
    - [Adicionar uma Pergunta a um Quiz](#adicionar-uma-pergunta-a-um-quiz)
    - [Remover uma Pergunta de um Quiz](#remover-uma-pergunta-de-um-quiz)
- [Executando Testes](#executando-testes)
- [Logging](#logging)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## Recursos

- **Gerenciamento de Quizzes**: Criação, recuperação, atualização e exclusão de quizzes.
- **Gerenciamento de Perguntas**: Adicionar e remover perguntas dentro dos quizzes.
- **Persistência**: Utiliza o Banco de Dados Oracle para armazenamento de dados.
- **Testes**: Inclui testes unitários para controladores e serviços.
- **Suporte a CORS**: Configurado para lidar com Cross-Origin Resource Sharing.

## Pré-requisitos

- **Java Development Kit (JDK) 17** ou superior
- **Maven 3.6.0** ou superior
- **Banco de Dados Oracle** (ou qualquer banco de dados SQL compatível)
- **Git** (opcional, para clonar o repositório)

## Instalação

1. **Clone o Repositório**

   ```bash
   git clone https://github.com/seu-usuario/quiz-api.git
   cd quiz-api
   ```

2. **Construa o Projeto**

   Use o Maven para compilar o projeto e baixar as dependências necessárias.

   ```bash
   mvn clean install
   ```

## Configuração do Banco de Dados

1. **Instale o Banco de Dados Oracle**

   Certifique-se de que o Oracle Database está instalado e em execução.

   Insira sua database name no arquivo `ConnectionFactory.java` localizado em `src/main/java/br/com/fiap/global/infra/dao/ConnectionFactory.java`.

2. **Crie o Esquema do Banco de Dados**

   Execute o script SQL fornecido para criar as tabelas e sequências necessárias.

   ```bash
   sqlplus seu_usuario/sua_senha@oracle.fiap.com.br:1521/ORCL @src/main/resources/cria_tabelas_quiz.sql
   ```

   **Nota**: Substitua `seu_usuario` e `sua_senha` pelas suas credenciais reais do banco de dados.

## Configuração

1. **Conexão com o Banco de Dados**

   Atualize os detalhes de conexão com o banco de dados em `ConnectionFactory.java` localizado em `src/main/java/br/com/fiap/global/infra/dao/ConnectionFactory.java`.

   ```java
   String urlDeConexao = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
   String login = "seu_usuario";
   String senha = "sua_senha";
   ```

   **Dica de Segurança**: Para maior segurança, considere externalizar essas configurações utilizando variáveis de ambiente ou uma ferramenta de gerenciamento de configuração.

2. **Configuração da Porta**

   A aplicação escuta na porta `8080` por padrão. Para alterar isso, modifique o `BASE_URI` em `Main.java` localizado em `src/main/java/br/com/fiap/global/Main.java`.

   ```java
   public static final String BASE_URI = "http://localhost:8080/";
   ```

## Executando a Aplicação

1. **Inicie o Servidor**

   Use o Maven para executar a aplicação.

   ```bash
   mvn exec:java
   ```

   Você deverá ver uma mensagem indicando que a Quiz API foi iniciada:

   ```
   Quiz API started with endpoints available at http://localhost:8080/
   Hit Ctrl-C to stop it...
   ```

2. **Acessando a API**

   A API está agora acessível em `http://localhost:8080/`.

## Documentação da API

### Endpoints

#### Criar um Quiz

- **URL**

  `/quizzes`

- **Método**

  `POST`

- **Cabeçalhos**

  `Content-Type: application/json`

- **Corpo**

  ```json
  {
    "titulo": "Sustentabilidade de Energia",
    "descricao": "Quiz sobre práticas sustentáveis na energia elétrica.",
    "perguntas": [
      {
        "texto": "Qual a fonte de energia mais sustentável?",
        "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
        "respostaCorreta": 0
      }
    ]
  }
  ```

- **Resposta de Sucesso**

  - **Código**: `201 CREATED`
  - **Conteúdo**

    ```json
    {
      "id": 1,
      "titulo": "Sustentabilidade de Energia",
      "descricao": "Quiz sobre práticas sustentáveis na energia elétrica.",
      "perguntas": [
        {
          "id": 1,
          "texto": "Qual a fonte de energia mais sustentável?",
          "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
          "respostaCorreta": 0
        }
      ]
    }
    ```

#### Obter Todos os Quizzes

- **URL**

  `/quizzes`

- **Método**

  `GET`

- **Resposta de Sucesso**

  - **Código**: `200 OK`
  - **Conteúdo**

    ```json
    [
      {
        "id": 1,
        "titulo": "Sustentabilidade de Energia",
        "descricao": "Quiz sobre práticas sustentáveis na energia elétrica.",
        "perguntas": [
          {
            "id": 1,
            "texto": "Qual a fonte de energia mais sustentável?",
            "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
            "respostaCorreta": 0
          }
        ]
      },
      ...
    ]
    ```

#### Obter um Quiz por ID

- **URL**

  `/quizzes/{id}`

- **Método**

  `GET`

- **Parâmetros da URL**

  - `id` (Long): ID do quiz.

- **Resposta de Sucesso**

  - **Código**: `200 OK`
  - **Conteúdo**

    ```json
    {
      "id": 1,
      "titulo": "Sustentabilidade de Energia",
      "descricao": "Quiz sobre práticas sustentáveis na energia elétrica.",
      "perguntas": [
        {
          "id": 1,
          "texto": "Qual a fonte de energia mais sustentável?",
          "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
          "respostaCorreta": 0
        }
      ]
    }
    ```

- **Resposta de Erro**

  - **Código**: `404 NOT FOUND`
  - **Conteúdo**

    ```json
    {
      "mensagem": "Quiz não encontrado para o ID: {id}"
    }
    ```

#### Atualizar um Quiz

- **URL**

  `/quizzes/{id}`

- **Método**

  `PUT`

- **Parâmetros da URL**

  - `id` (Long): ID do quiz.

- **Cabeçalhos**

  `Content-Type: application/json`

- **Corpo**

  ```json
  {
    "titulo": "Energia Sustentável Atualizado",
    "descricao": "Descrição atualizada do quiz.",
    "perguntas": [
      {
        "id": 1,
        "texto": "Qual a fonte de energia mais renovável?",
        "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
        "respostaCorreta": 3
      }
    ]
  }
  ```

- **Resposta de Sucesso**

  - **Código**: `200 OK`
  - **Conteúdo**

    ```json
    {
      "id": 1,
      "titulo": "Energia Sustentável Atualizado",
      "descricao": "Descrição atualizada do quiz.",
      "perguntas": [
        {
          "id": 1,
          "texto": "Qual a fonte de energia mais renovável?",
          "opcoes": ["Solar", "Nuclear", "Fóssil", "Eólica"],
          "respostaCorreta": 3
        }
      ]
    }
    ```

- **Resposta de Erro**

  - **Código**: `404 NOT FOUND`
  - **Conteúdo**

    ```json
    {
      "mensagem": "Quiz não encontrado para o ID: {id}"
    }
    ```

#### Excluir um Quiz

- **URL**

  `/quizzes/{id}`

- **Método**

  `DELETE`

- **Parâmetros da URL**

  - `id` (Long): ID do quiz.

- **Resposta de Sucesso**

  - **Código**: `204 NO CONTENT`

- **Resposta de Erro**

  - **Código**: `404 NOT FOUND`
  - **Conteúdo**

    ```json
    {
      "mensagem": "Quiz não encontrado para o ID: {id}"
    }
    ```

#### Adicionar uma Pergunta a um Quiz

- **URL**

  `/quizzes/{quizId}/perguntas`

- **Método**

  `POST`

- **Parâmetros da URL**

  - `quizId` (Long): ID do quiz.

- **Cabeçalhos**

  `Content-Type: application/json`

- **Corpo**

  ```json
  {
    "texto": "Qual a cor do céu?",
    "opcoes": ["Azul", "Verde", "Vermelho", "Amarelo"],
    "respostaCorreta": 0
  }
  ```

- **Resposta de Sucesso**

  - **Código**: `201 CREATED`
  - **Conteúdo**

    ```json
    {
      "id": 2,
      "texto": "Qual a cor do céu?",
      "opcoes": ["Azul", "Verde", "Vermelho", "Amarelo"],
      "respostaCorreta": 0
    }
    ```

#### Remover uma Pergunta de um Quiz

- **URL**

  `/quizzes/{quizId}/perguntas/{perguntaId}`

- **Método**

  `DELETE`

- **Parâmetros da URL**

  - `quizId` (Long): ID do quiz.
  - `perguntaId` (Long): ID da pergunta.

- **Resposta de Sucesso**

  - **Código**: `204 NO CONTENT`

- **Resposta de Erro**

  - **Código**: `404 NOT FOUND`
  - **Conteúdo**

    ```json
    {
      "mensagem": "Quiz não encontrado para o ID: {quizId}"
    }
    ```

## Executando Testes

O projeto inclui testes unitários para controladores e serviços utilizando JUnit 5.

Execute o seguinte comando para rodar os testes:

```bash
mvn test
```


## Logging

O logging está configurado usando o framework embutido `java.util.logging`. Os logs podem ser encontrados na saída do console. Para personalizar o comportamento do logging, modifique as configurações de logging nas respectivas classes Java ou introduza um arquivo de configuração de logging conforme necessário.
