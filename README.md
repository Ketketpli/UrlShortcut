# UrlShortcut 🔗

Encurtador de links serverless construído com Java e AWS.

## 📋 Sobre o Projeto

API serverless que permite encurtar URLs longas e redirecionar usuários para a URL original através de um código curto único.

## 🏗️ Arquitetura

```
Usuário → API Gateway → Lambda (Java) → DynamoDB
```

| Serviço | Função |
|---|---|
| **AWS Lambda** | Executa o código Java sem servidor |
| **API Gateway** | Expõe os endpoints HTTP |
| **DynamoDB** | Armazena as URLs e seus códigos curtos |

## 🚀 Endpoints

### Criar URL encurtada
```
POST /createUrl
```
**Body:**
```json
{
    "originalUrl": "https://www.google.com.br",
    "expirationTime": "1800000000"
}
```
**Resposta:**
```json
{
    "shortUrl": "3868dc75"
}
```

### Redirecionar URL
```
GET /redirectUrl
```
**Body:**
```json
{
    "shortUrl": "3868dc75"
}
```
**Resposta:** `302 Found` → redireciona para a URL original

**Erro:** `404` → quando o código curto não existe

## 🛠️ Tecnologias

- **Java 21**
- **Maven**
- **AWS Lambda**
- **AWS API Gateway**
- **AWS DynamoDB**
- **Jackson** (parse de JSON)
- **AWS SDK v2** (DynamoDB client)

## 📁 Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── com/urlKatz/
            └── LambdaService/
                ├── CreateUrlLambda.java       # Cria e salva a URL encurtada
                ├── RedirectLambda.java        # Redireciona para a URL original
                ├── LambdaDTO/
                │   └── UrlData.java           # DTO com os dados da URL
                └── LambdaException/
                    └── UrlNotFoundException.java  # Exceção personalizada
```

## ⚙️ Como funciona

1. O usuário envia uma URL longa via `POST /createUrl`
2. O sistema gera um código curto de 8 caracteres usando UUID
3. A URL original, o código curto e o tempo de expiração são salvos no DynamoDB
4. O usuário pode acessar `GET /redirectUrl` com o código curto
5. O sistema busca a URL original no DynamoDB e redireciona com status `302`
6. Caso o código não exista, retorna `404` com mensagem de erro

## 🚀 Deploy

### Pré-requisitos
- Java 21
- Maven
- AWS CLI configurado
- Conta AWS com acesso ao Lambda, DynamoDB e API Gateway

### Gerando o JAR
No IntelliJ IDEA, execute **Maven → Lifecycle → package** ou:
```bash
mvn clean package
```
O JAR será gerado em `target/URL-shortcut-1.0-SNAPSHOT.jar`.

### Configurando o Lambda
1. Crie uma função Lambda com runtime **Java 21**
2. Faça o upload do JAR gerado
3. Configure o Handler:
   - CreateUrl: `com.urlKatz.LambdaService.CreateUrlLambda::handleRequest`
   - Redirect: `com.urlKatz.LambdaService.RedirectLambda::handleRequest`
4. Adicione a permissão `AmazonDynamoDBFullAccess` na role do Lambda

### Configurando o DynamoDB
Crie uma tabela com:
- **Nome:** `shortAWS`
- **Partition key:** `shortUrl` (String)

### Configurando o API Gateway
Crie uma **HTTP API** com duas rotas:
- `POST /createUrl` → integração com `createLambdaUrl`
- `GET /redirectUrl` → integração com `redirectUrlLambda`

## 👤 Autor

Paulo Henrique Rodrigues Varela  
[github.com/Ketketpli](https://github.com/Ketketpli)
