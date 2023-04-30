# File storage api

API para upload e download de arquivos em uma base de dados.

## Status do Projeto

![](https://img.shields.io/badge/Status-Finalizado-green?style=for-the-badge&logo=appveyor)

## Funcionalidades

- Download de ardquivos;
- Upload de ardquivos até 200MB;
- Detalhes de todas os arquivos;
- E de um arquivo específico.

## Documentação da API

```yaml
openapi: 3.0.1
info:
  title: File Storage api
  description: Api para fazer upload/dowload de arquivos
  contact:
    name: gasfgrv
    url: https://github.com/gasfgrv
    email: gustavo_almeida11@hotmail.com
  version: V1
servers:
  - url: http://localhost:8080
    description: Generated server url
paths:
  /v1/files/upload:
    post:
      tags:
        - files-controler
      operationId: uploadFile
      requestBody:
        content:
          multipart/form-data:
            schema:
              required:
                - file
              type: object
              properties:
                file:
                  type: string
                  format: binary
      responses:
        "404":
          description: Not Found
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "417":
          description: Expectation Failed
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "413":
          description: Payload Too Large
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ResponseMessage'
  /v1/files/{id}/download:
    get:
      tags:
        - files-controler
      operationId: downloadFile
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        "404":
          description: Not Found
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "417":
          description: Expectation Failed
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "413":
          description: Payload Too Large
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "200":
          description: OK
          content:
            application/octet-stream:
              schema:
                type: array
                items:
                  type: string
                  format: byte
  /v1/files/{id}/details:
    get:
      tags:
        - files-controler
      operationId: getFileDetais
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        "404":
          description: Not Found
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "417":
          description: Expectation Failed
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "413":
          description: Payload Too Large
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/FileDBDetails'
  /v1/files/info:
    get:
      tags:
        - files-controler
      operationId: filesInfo
      responses:
        "404":
          description: Not Found
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "417":
          description: Expectation Failed
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "413":
          description: Payload Too Large
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/Problem'
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/ResponseFile'
components:
  schemas:
    Problem:
      type: object
      properties:
        type:
          type: string
          format: uri
        title:
          type: string
        status:
          type: integer
          format: int32
        detail:
          type: string
        instance:
          type: string
          format: uri
    Links:
      type: object
      additionalProperties:
        $ref: '#/components/schemas/Link'
    ResponseMessage:
      type: object
      properties:
        message:
          type: string
        _links:
          $ref: '#/components/schemas/Links'
    FileDBDetails:
      type: object
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string
        type:
          type: string
        _links:
          $ref: '#/components/schemas/Links'
    ResponseFile:
      type: object
      properties:
        name:
          type: string
        url:
          type: string
        type:
          type: string
        size:
          type: integer
          format: int64
        _links:
          $ref: '#/components/schemas/Links'
    Link:
      type: object
      properties:
        href:
          type: string
        hreflang:
          type: string
        title:
          type: string
        type:
          type: string
        deprecation:
          type: string
        profile:
          type: string
        name:
          type: string
        templated:
          type: boolean
```

## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

- `DB_URL`: url de conexão com o banco de dados
- `DB_USER`: usuário do banco de dados
- `DB_PASS`: senha do banco de dados

## Rodando localmente

Clone o projeto

```bash
  git clone git@github.com:gasfgrv/file-storage-api.git
```

Entre no diretório do projeto

```bash
  cd file-storage-api
```

Instale as dependências

```bash
  mvn clean package -DskipTests
```

Subir banco de dados

```bash
  docker-compose up -d
```

Inicie a aplicação, pode ser pela IDE (Intellij ou Eclipse), ou rodando o seguinte comando:

```bash
    java \
    -Dspring.datasource.url=${DB_URL} \
    -Dspring.datasource.username=${DB_USER} \
    -Dspring.datasource.password=${DB_PASS} \
    -jar target/file-storage-api-0.0.1-SNAPSHOT.jar
```

Use a collection para ajudar com os endpoints:

[![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=&uri=)

## Rodando os testes

Para rodar os testes, rode os seguinte comandos

- Subir a base de dados, necessário para o teste de contexto da aplicação:

```bash
  docker compose up -d
```

- Executar os testes

```bash
  mvn tests
```

## Stack utilizada

- Java 17
- Spring Boot 3
- Maven

### Dependências:

- spring data jpa
- spring hateoas
- spring web
- flyway
- lombok
- postgresql
- springdoc-openapi-starter-webmvc-ui
- spring validation
- testcontainers

## Autores

- [@gasfgrv](https://www.github.com/gasfgrv)

