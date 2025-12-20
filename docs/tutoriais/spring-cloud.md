# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/ 
- https://spring.io/projects/spring-cloud-config 
- https://docs.spring.io/spring-boot/api/rest/actuator/index.html#page-title 
- https://docs.spring.io/spring-boot/api/rest/actuator/liquibase.html 
- https://docs.spring.io/spring-cloud-config/reference/server/environment-repository/git-backend.html 
- https://spring.io/guides/gs/centralized-configuration 
- https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/learn/lecture/50949711#overview 
- https://www.youtube.com/watch?v=E2HkL766VHs 
- https://www.youtube.com/watch?v=yNnLICy2zk4 


### Introdução: 

Spring Boot Actuator
```

```
Spring Cloud Config
```
Spring Cloud Config é uma solução do ecossistema Spring que resolve um dos principais 
desafios no desenvolvimento de aplicações baseadas em microsserviços: a centralização e 
gestão eficiente de configurações. Em ambientes de microsserviços, cada serviço pode ter 
suas próprias configurações, o que pode levar a inconsistências e dificuldades na 
manutenção. O Spring Cloud Config permite que as aplicações obtenham suas configurações de 
um repositório centralizado, facilitando a gestão e atualização dessas configurações sem a
necessidade de redeploy das aplicações. 

Composto por dois componentes principais: o servidor de configuração (Config Server) e o 
cliente de configuração (Config Client). O Config Server atua como um ponto central onde as 
configurações são armazenadas, geralmente em repositórios Git, SVN ou sistemas de arquivos. 
Já o Config Client é integrado às aplicações Spring Boot, permitindo que elas busquem suas 
configurações do Config Server durante a inicialização ou em tempo de execução.
```

## 2. Configuração

### Passo-a-passo

Cliente
1. Adicionar dependência no build.gradle:
  - a. Spring Boot Actuator;
  - b. Spring Cloud Config Client;
2. Configurar application.yml: 
3. Adicionar anotação @RefreshScope na Main e em todas as classes que deseja atualizar em tempo de execução;
4. Adicionar "refresh" na configuração de endpoints do Actuator no application.yml;
5. Fazer POST no endpoint /actuator/refresh para atualizar as configurações em tempo de execução;

Servidor
1. Criar projeto Spring com apenas duas dependências (ficará no monorepo das demais aplicações):
  - a. Spring Boot Actuator;
  - b. Spring Cloud Config Server;
2. Habilitar o Config Server na classe principal Main com a anotação @EnableConfigServer;
3. Criar novo repositório Git exclusivo para armazenar os arquivos de configuração;
   a.Criar diretório, como nome da api, onde serão colocados os respectivos arquivos de configuração;
   b. Criar os arquivos de configuração que serão consumidos pela aplicação cliente;
   c. Nome dos arquivos: {application-name}-{profile}.yml (configuração específica para cada aplicação e perfil,
   como dev, prod e etc.);
4. Configurar application.yml padrão do Config Server:
  - a. Apontar repositório Git para buscar arquivos de configuração;
  - b. Configurar endpoints do Actuator no application.yml padrão;
5. Criar diretório "config" no resources do ConfigServer para arquivos de configuração;

  


### Implementação: 

Cliente
1. build.gradle:
  a. Spring Boot Actuator
```
implementation 'org.springframework.boot:spring-boot-starter-actuator' 
testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
```
  b. Spring Cloud Config Client
```

```


