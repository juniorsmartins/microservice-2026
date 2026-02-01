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
- 
- 
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
O Spring Security oferece suporte abrangente para autenticação . A autenticação é a forma como verificamos a identidade 
de quem está tentando acessar um recurso específico. Uma maneira comum de autenticar usuários é exigindo que eles 
insiram um nome de usuário e uma senha. Uma vez realizada a autenticação, conhecemos a identidade e podemos realizar a 
autorização.

A. Autenticação básica (stateful): login e senha - gera valor de sessão para ser armazenado em cookie do navegador. O 
   servidor mantém o estado da sessão.
```
AUTORIZAÇÃO
```
O Spring Security oferece suporte abrangente para autorização . Autorização é o processo de determinar quem tem 
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
- Segurança: políticas de senha, MFA, auditoria centralizadas; 
- Controle de acesso: gerencia permissões de forma unificada.

Exemplo prático: Funcionário novo entra na empresa: Cadastrado no IAM uma vez; Automaticamente ganha acesso a email, 
ERP, CRM, intranet; Muda de departamento? Permissões atualizadas no IAM refletem em todos os sistemas; Sai da empresa? 
Acesso revogado em todos os lugares simultaneamente.
```
KEYCLOAK
```
O Keycloak é um produto de gerenciamento de identidade e acesso de código aberto (open source). 



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

1. Adicionar dependências;
    a. implementation 'org.springframework.boot:spring-boot-starter-security' 
    b. testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
2. 


### Implementação: 


