# TUTORIAL


## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-security/reference/index.html 
- https://github.com/spring-projects/spring-security/ 
- https://docs.spring.io/spring-security/reference/servlet/architecture.html (arquitetura) 
- https://docs.spring.io/spring-security/reference/servlet/getting-started.html (começando)
- https://oauth.net/2/ (Oauth2)
- https://auth0.com/docs (Oauth2 e outros) 
- https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html (Oauth2)
- https://www.keycloak.org/ (Keycloak - open source)
- https://www.keycloak.org/getting-started/getting-started-docker (Keycloak com Docker)
- https://www.keycloak.org/server/configuration (configurações do Keycloak) 
- https://www.keycloak.org/server/configuration-production (configurações de produção do Keycloak)
- https://www.keycloak.org/server/all-config (todas configurações do Keycloak)
- https://www.keycloak.org/docs-api/latest/rest-api/index.html (Keycloak REST API)
- 
- 
- 
- https://spring.io/projects/spring-authorization-server (Spring Authorization Server)
- https://docs.spring.io/spring-authorization-server/reference/overview.html (Spring Authorization Server)
- https://www.jwt.io/ 
- https://www.youtube.com/playlist?list=PLZV0a2jwt22s5NCKOwSmHVagoDW8nflaC (playlist do Dan Vega)
- 
- 
- https://www.okta.com/ (produto comercial)
- https://aws.amazon.com/pt/cognito/ (produto comercial)
- 

### Introdução: 

SPRING SECURITY
```
Spring Security é um framework poderoso e altamente customizável para adicionar autenticação (quem você é) e 
autorização (o que você pode fazer) em aplicações Java/Spring.

PROTEÇÕES DE SEGURANÇA

O Spring Security oferece diversas proteções automáticas:

- CSRF (Cross-Site Request Forgery): proteção contra ataques onde sites maliciosos enganam o navegador a executar 
  ações não autorizadas. Funciona através de tokens únicos por sessão que devem ser incluídos em requisições que 
  modificam dados (POST, PUT, DELETE).

- Security Headers: headers HTTP para prevenir ataques como XSS, clickjacking, MIME sniffing, etc.

- Session Management: controle de sessões concorrentes, fixação de sessão, timeout.

- Password Encoding: armazenamento seguro de senhas usando algoritmos como BCrypt, evitando senhas em texto plano.
```
AUTENTICAÇÃO
```
O Spring Security oferece suporte abrangente para autenticação. A autenticação é a forma como verificamos a identidade 
de quem está tentando acessar um recurso específico. Uma maneira comum de autenticar usuários é exigindo que eles 
insiram um nome de usuário e uma senha. Uma vez realizada a autenticação, conhecemos a identidade e podemos realizar a 
autorização.

A. Autenticação básica (stateful): login e senha - gera valor de sessão para ser armazenado em cookie do navegador. O 
   servidor mantém o estado da sessão.
```
AUTORIZAÇÃO
```
O Spring Security oferece suporte abrangente para autorização. Autorização é o processo de determinar quem tem 
permissão para acessar um recurso específico. O Spring Security proporciona defesa em profundidade, permitindo 
autorização baseada em requisição (URLs) e autorização baseada em método (@PreAuthorize, @Secured).

Exemplos:
- Baseada em requisição: controlar acesso a /admin/** apenas para usuários com role ADMIN;
- Baseada em método: anotar métodos específicos para exigir permissões granulares.

B. Oauth2 (stateless): utiliza um servidor de autorização separado que emite tokens de acesso. Toda a lógica de 
   autenticação e autorização é centralizada num único componente (Authorization Server). O token é enviado em cada 
   requisição, eliminando a necessidade de sessões no servidor.
   
IMPORTANTE: a estrutura Oauth2 foi criada originalmente para dar suporte à autorização. Posteriormente, acabaram 
usando também para autenticação. 

Oauth2: 
- Resource Owner (proprietário do recurso): o usuário dono dos dados;
- Client (aplicação cliente): quem deseja acessar recursos do proprietário;
- Authorization Server: servidor que autentica e emite tokens;
- Resource Server: servidor que hospeda os recursos protegidos;
- Scopes: permissões granulares que o cliente deseja, como acesso a dados ou determinadas ações. O servidor de 
  autenticação quem emite um token de acesso para o cliente com base em escopos. Esses escopos decidem quais ações o 
  aplicativo cliente pode executar.
```
OPEN ID CONNECT (OIDC)
```
OpenID Connect é um protocolo de autenticação construído sobre o OAuth2. Enquanto o OAuth2 foi originalmente projetado 
apenas para autorização (conceder acesso a recursos), o OpenID Connect adiciona uma camada de autenticação, permitindo 
verificar a identidade do usuário.

Diferenças principais:

OAuth2:
- Focado em AUTORIZAÇÃO (o que você pode acessar)
- Access Token: contém permissões/escopos, mas não identifica quem é o usuário
- Pergunta: "Este token pode acessar o recurso X?"

OpenID Connect:
- Adiciona AUTENTICAÇÃO (quem você é) ao OAuth2
- ID Token (JWT): contém informações sobre o usuário (nome, email, foto, etc.)
- Access Token: continua sendo usado para autorização de recursos
- Pergunta: "Quem é este usuário?" + "O que ele pode acessar?"

Como funciona:
1. Usuário faz login no Authorization Server
2. Servidor retorna um ID Token (identidade do usuário) + Access Token (autorização)
3. Aplicação valida o ID Token para saber quem é o usuário
4. Aplicação usa o Access Token para acessar recursos protegidos

Exemplo prático:
Quando você faz login em um site usando "Entrar com Google":
- Google autentica você (OpenID Connect)
- Retorna um ID Token dizendo "Este é João Silva, email: joao@gmail.com"
- Retorna um Access Token permitindo acessar foto de perfil, calendário, etc.

Estrutura do ID Token (JWT):
{
  "sub": "usuario123",           // ID único do usuário
  "name": "João Silva",          // Nome completo
  "email": "joao@gmail.com",     // Email
  "picture": "https://...",      // Foto de perfil
  "iss": "https://accounts.google.com",  // Emissor
  "aud": "sua-aplicacao-id",     // Audiência (sua aplicação)
  "exp": 1234567890              // Expiração
}

Em resumo: OAuth2 diz "o que você pode fazer", OpenID Connect adiciona "quem você é".
```
CENTRALIZED IDENTITY AND ACCESS MANAGEMENT (IAM)
```
IAM centralizado é um sistema único que gerencia quem pode acessar o quê em todas as aplicações e recursos da 
organização. 

Sem IAM centralizado: Cada aplicação tem seu próprio cadastro de usuários; Usuário tem login/senha diferente para cada 
sistema.

Como funciona: Um servidor central (IAM) armazena: 
- Identidades: todos os usuários da organização; 
- Permissões: o que cada usuário pode fazer; 
- Políticas de acesso: regras de quem acessa o quê. As aplicações delegam a autenticação e autorização para esse 
  servidor central.

Exemplos de soluções IAM: 
- Keycloak (open source);
- Okta (comercial);
- Auth0 (comercial);
- AWS IAM;
- Azure Active Directory / Microsoft ID.

Benefícios principais: 
- Single Sign-On (SSO): login uma vez, acessa tudo; 
- Gestão centralizada: adiciona/remove usuários em um só lugar; 
- Segurança: políticas de senha, MFA e auditoria centralizadas; 
- Controle de acesso: gerencia permissões de forma unificada.

Exemplo prático: Funcionário novo entra na empresa: Cadastrado no IAM uma vez; Automaticamente ganha acesso a email, 
ERP, CRM, intranet; Muda de departamento? Permissões atualizadas no IAM refletem em todos os sistemas; Sai da empresa? 
Acesso revogado em todos os lugares simultaneamente.
```
KEYCLOAK
```
O Keycloak é um produto de gerenciamento de identidade e acesso de código aberto (open source). Ele fornece uma solução 
completa para autenticação, autorização e gerenciamento de usuários. O Keycloak suporta diversos protocolos de 
autenticação, como OpenID Connect, OAuth2 e SAML, e oferece recursos avançados, como Single Sign-On (SSO), autenticação 
multifator (MFA), integração com redes sociais, e muito mais. Ele é amplamente utilizado para proteger aplicações e 
serviços, permitindo que os desenvolvedores se concentrem na lógica de negócios, enquanto o Keycloak cuida da segurança.

- Realm: Realm é a unidade de organização principal no Keycloak. Ele representa um espaço de gerenciamento de usuários, 
  clientes, roles e políticas. Cada realm é isolado dos outros, permitindo que você tenha múltiplos ambientes ou 
  aplicações dentro do mesmo servidor Keycloak. Por exemplo, você pode ter um realm para desenvolvimento, outro para 
  produção, ou um realm para cada aplicação.
- Realm master: O realm master é o realm padrão criado automaticamente quando você instala o Keycloak. Ele é usado 
  principalmente para gerenciar a própria instância do Keycloak, incluindo a criação de outros realms, gerenciamento de 
  usuários e configuração global. O realm master é o ponto de entrada para a administração do Keycloak, mas não deve ser 
  usado para gerenciar usuários ou clientes de aplicações, pois é destinado apenas para administração do servidor 
  Keycloak.


```
LOGGING
```
O Spring Security fornece um registro abrangente de todos os eventos relacionados à segurança nos níveis DEBUG e TRACE. 
Isso pode ser útil ao depurar sua aplicação, pois, por questões de segurança, o Spring Security não adiciona detalhes 
sobre o motivo da rejeição da requisição ao corpo da resposta. Se você se deparar com erro 401 (não autenticado) ou 
403 (não autorizado), é muito provável que haja uma mensagem de log explicando o que aconteceu.

Configuração no application.properties ou application.yml:
logging.level.org.springframework.security=TRACE
```

## 2. Configuração

### Passo-a-passo

Criar Auth Server (servidor de autenticação) com KeyCloak;
1. Criar container de Keycloak no docker compose;
2. Criar container de banco de dados, dedicado para o Keycloak, no docker-compose;
3. Entrar no Keycloak e configurar (http://localhost:7080/);

   a. Ir em Clients e clicar em Create Client;
   b. Criar Client (client ID: microservices-2026-credentials; name: microservices-2026; description: microservices-2026; clicar botão next; ativar client authentication; em authentication flow, marcar apenas "service accounts roles"; clicar botão next; clicar botão save)
   c. Pegar o secret para fazer requisições via Postman.   
4. Criar Roles (em Realm Roles, )

Adaptar Gateway Server para também ser Resource Server (servidor de recursos);
1. Adicionar dependências:
2. Criar classe KeycloakRoleConverter (implementando Converter<Jwt, Collection<GrantedAuthority>>);
3. Criar classe SecurityConfig (com anotações @Configuration e @EnableWebFluxSecurity);

4. Configurar o conversor na classe SecurityConfig (fazer ela usar o KeycloakRoleConverter);
5. Configurar application.yml;
6. Testar requisição no Postman (em authorization, adicionar Oauth2; token name = clientcredentials_accesstoken; grant type = Client Credentials; access token url = http://localhost:8080/realms/master/protocol/openid-connect/token ; client id = microservices-2026-cc; client secret = pegar a credencial no Keycloak; Scope = openid email profile; client authorization = send client credentials in body; clicar no botão Get New Access Token)


### Implementação: 


3. Entrar no Keycloak e configurar (http://localhost:7080/);
```
a. Acessar o Keycloak com as credenciais padrão;
b. Criar um novo Realm (realm: dev, prod, qa);
c. Criar um novo Client (não esquecer de verificar em qual realm está criando o cliente):
   1. Ir em Clients e clicar em Create Client (há opção de importar);
   2. Preencher os campos: 
      - Client Type: OpenID Connect;
      - Client ID: microservices-2026-cc; (o que é? é o identificador do cliente, usado para autenticação e autorização. CC = Client Credentials)
      - Name: Microservices 2026 Dev; (o que é? é o nome amigável do cliente, usado para identificação na interface do Keycloak. Pode ser qualquer nome que ajude a identificar o propósito do cliente)
      - Description: Microservices 2026 - ambiente de desenvolvimento;
      - Always Display In UI: manter desativado;
      - Clicar em Next;
      - Client Authentication: ativar; (o que é? é o processo de verificar a identidade do cliente usando credenciais, como client ID e client secret. É necessário para garantir que apenas clientes autorizados possam obter tokens de acesso).
      - Authorization: manter desativado;
      - Authentication Flow: marcar apenas "Service Accounts Roles"; (o que é? é um fluxo de autenticação onde o cliente atua como um usuário de serviço, permitindo que ele obtenha tokens de acesso com base em suas próprias credenciais, sem a necessidade de um usuário final. Isso é útil para cenários onde o cliente precisa acessar recursos protegidos em nome de si mesmo, como em integrações entre serviços ou automações).
      - PKCE Method: manter desativado; (o que é? PKCE (Proof Key for Code Exchange) é um mecanismo de segurança usado principalmente em aplicativos móveis e SPAs para proteger o fluxo de autorização contra ataques de interceptação. Ele envolve a geração de um código de verificação que é enviado junto com a solicitação de autorização e verificado posteriormente durante a troca do token. No caso de Client Credentials, PKCE não é necessário, pois não há interação com um usuário final).
      - Require DPoP bound tokens: manter desativado; (o que é? DPoP (Demonstration of Proof of Possession) é um mecanismo de segurança que vincula tokens de acesso a uma chave pública, garantindo que apenas o cliente que possui a chave privada correspondente possa usar o token. Isso ajuda a prevenir o uso indevido de tokens de acesso, mesmo que eles sejam interceptados por terceiros. No caso de Client Credentials, DPoP não é necessário, pois o cliente já está autenticado usando suas próprias credenciais).
      - Clicar em Next;
      - Root URL: manter em branco; (o que é? é a URL base do cliente, usada para redirecionamentos e callbacks durante o processo de autenticação. No caso de Client Credentials, como não há interação com um usuário final, essa URL não é necessária).
      - Home URL: manter em branco; (o que é? é a URL para a qual os usuários são redirecionados após o login bem-sucedido. No caso de Client Credentials, como não há interação com um usuário final, essa URL não é necessária).
      - Clicar em Save;
      - Após salvar, acessar a aba "Credentials" para obter o client secret, que será usado para autenticação e obtenção de tokens de acesso.
d. Criar Roles (em Realm Roles, criar role "admin" e role "user");
```



Adaptar Gateway Server para também ser Resource Server (servidor de recursos);
1. Adicionar dependências:
```
	implementation 'org.springframework.boot:spring-boot-starter-security' // Dependências principais de segurança
	testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server' // Para configurar o Gateway como Resource Server
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test'
	implementation 'org.springframework.security:spring-security-oauth2-jose:7.0.3' // Para suporte a JWT.
```
2. Criar classe KeycloakRoleConverter (implementando Converter<Jwt, Collection<GrantedAuthority>>);
```

```
3. Criar classe SecurityConfig (com anotações @Configuration e @EnableWebFluxSecurity);
```

```
4. Configurar o conversor na classe SecurityConfig (fazer ela usar o KeycloakRoleConverter);
5. Configurar application.yml;
```
a. Entrar na interface do Keycloak e acessar Realm Settings. No final da página, há a seção "Endpoints" com os endpoints de autenticação. Abra em outra aba "OpenID Endpoint Configuration". Então copie o endereço de URL da chave "jwks_uri". Servirá para descarregar certificado público do Keycloak, necessário para validar os tokens de acesso recebidos pelo Gateway Server. O URL deve ser algo como: "http://localhost:7080/realms/dev/protocol/openid-connect/certs". Esse URL será configurado no application.yml do Gateway Server para que ele possa validar os tokens JWT emitidos pelo Keycloak.

spring:
  application:
    name: gatewayserver

  # --------------------------------------------------
  # Config de Security (resource server)
  # --------------------------------------------------
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: "http://localhost:7080/realms/dev/protocol/openid-connect/certs"
  # --------------------------------------------------
```
6. Testar requisição no Postman
```
a. Crie uma requisição POST no Postman (nome: ClientCredentials_AccessToken); 
b. A requisição usará o endpoint de token do Keycloak para obter um token de acesso usando as credenciais do cliente criado.
   1. Entrar na interface do Keycloak e acessar Realm Settings. No final da página, há a seção "Endpoints" com os endpoints de autenticação. Abra em outra aba "OpenID Endpoint Configuration". Então copie o endereço de URL da chave "token_endpoint". Essa URL será usada para obter tokens de acesso via Postman. (ex: "http://localhost:7080/realms/dev/protocol/openid-connect/token")
c. Vá no "Body" da requisição e selecione a opção "x-www-form-urlencoded". Adicione os seguintes parâmetros:
   - grant_type: client_credentials (o que é? é o tipo de fluxo de autenticação que indica que o cliente está usando suas próprias credenciais para obter um token de acesso, sem a necessidade de um usuário final. Esse fluxo é adequado para cenários onde o cliente precisa acessar recursos protegidos em nome de si mesmo, como em integrações entre serviços ou automações).
      * client_credentials: é um tipo de grant (concessão) no OAuth2 onde o cliente se autentica usando suas próprias credenciais (client ID e client secret) para obter um token de acesso. É usado principalmente para comunicação entre serviços, onde não há um usuário final envolvido.
   - client_id: microservices-2026-cc (client ID do cliente criado no Keycloak)
   - client_secret: (client secret obtido na aba "Credentials" do cliente no Keycloak)
   - scope: openid email profile (escopos desejados, separados por espaço). (o que é? são permissões granulares que o cliente deseja obter no token de acesso. No caso de Client Credentials, os escopos podem ser usados para limitar as ações que o cliente pode realizar ou os recursos que pode acessar. Os escopos "openid", "email" e "profile" são comumente usados para obter informações básicas sobre o usuário, mas em um cenário de Client Credentials, eles podem ser personalizados para refletir as permissões específicas necessárias para a aplicação cliente).
d. Clique no botão "Send" para enviar a requisição. Se tudo estiver configurado corretamente, o Postman irá obter um token de acesso do Keycloak, que pode ser usado para autenticar requisições para o Gateway Server ou outros recursos protegidos.
```




