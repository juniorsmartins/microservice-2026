# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.youtube.com/watch?v=daPwd4DnEfA 
- https://spring.io/blog/2025/12/11/spring-ai-2-0-0-M1-available-now 
- https://github.com/spring-projects/spring-ai 
- https://github.com/spring-ai-community/awesome-spring-ai 
- https://platform.openai.com/docs/quickstart?language=java 
- https://platform.openai.com/settings/organization/billing/overview 
- 
-
- https://docs.spring.io/spring-ai/reference/getting-started.html
- https://spring.io/blog/2024/11/19/why-spring-ai
- 
-
- https://www.baeldung.com/spring-ai 
- https://www.youtube.com/watch?v=FzLABAppJfM 
- https://www.youtube.com/watch?v=NscHAlj-yQ0 
- 
- https://usama.codes/blog/spring-ai-2-spring-boot-4-guide
- https://www.youtube.com/playlist?list=PLZV0a2jwt22uoDm3LNDFvN6i2cAVU_HTH 
- 
- 
- https://www.youtube.com/watch?v=oP7tn_YfoOk&list=PLuNxlOYbv61hmSWcdM0rtoWT0qEjZMIhU&index=1 (playlist)
- 


### Introdução: 

```
O Spring AI fornece camada de abstração comum para trabalhar com diferentes provedores de IA usando os 
padrões de programação Spring já conhecidos. Isso elimina a necessidade de usar explicitamente SDKs 
específicos do provedor e nos permite alternar entre diferentes modelos sem alterar o código do 
aplicativo.

Pré-requisitos do Spring AI 2.0.0-M1: 
- Java 21;
- Spring Boot 4.0; 
- Spring Framework 7.0.




```


## 2. Configuração


### Passo-a-passo

Pré-requisitos:
1. Criar APi Key do OpenAI
   a. https://platform.openai.com/settings/organization/api-keys
2.

API Cliente:
1. Adicionar dependência;
    a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
2. Adicionar configuração no application.yml;
    a. Configuração de IA;
    b. Configuração de Logging;
    c. Configuração de Redis (memória para Chat);
3. Criação no docker-compose.yml;
    a. Serviço de database do Redis;
    b. Serviço de UI para visualização do Redis (opcional);






### Implementação: 







