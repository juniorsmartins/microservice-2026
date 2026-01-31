# TUTORIAL


## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-security/reference/index.html 
- https://github.com/spring-projects/spring-security/ 
- https://docs.spring.io/spring-security/reference/servlet/architecture.html (arquitetura) 
- https://docs.spring.io/spring-security/reference/servlet/getting-started.html (começando)
- 
- 
- 
- https://www.youtube.com/playlist?list=PLZV0a2jwt22s5NCKOwSmHVagoDW8nflaC (playlist do Dan Vega)

### Introdução: 

```
Spring Security é um framework poderoso e altamente customizável para adicionar autenticação (quem você é) e 
autorização (o que você pode fazer) em aplicações Java/Spring.

AUTENTICAÇÃO

O Spring Security oferece suporte abrangente para autenticação . A autenticação é a forma como verificamos a identidade 
de quem está tentando acessar um recurso específico. Uma maneira comum de autenticar usuários é exigindo que eles 
insiram um nome de usuário e uma senha. Uma vez realizada a autenticação, conhecemos a identidade e podemos realizar a 
autorização.

a. Autenticação básica: login e senha - gera valor de sessão para ser armazenado em cookie do navegador. 
b. Oauth2: servidor de autenticação e token de acesso - toda a lógica de autenticação e autorização centralizada num 
único componente.

- Oauth2: 

AUTORIZAÇÃO

O Spring Security oferece suporte abrangente para autorização . Autorização é o processo de determinar quem tem 
permissão para acessar um recurso específico. O Spring Security proporciona defesa em profundidade , permitindo 
autorização baseada em requisição e autorização baseada em método.

CENTRALIZED IDENTITY AND ACCESS MANAGEMENT (IAM)

IAM centralizado é um sistema único que gerencia quem pode acessar o quê em todas as aplicações e recursos da 
organização. 

Sem IAM centralizado: Cada aplicação tem seu próprio cadastro de usuários; Usuário tem login/senha 
diferente para cada sistema.

Como funciona: Um servidor central (IAM) armazena: Identidades: todos os usuários da organização; Permissões: o que cada 
usuário pode fazer; Políticas de acesso: regras de quem acessa o quê. As aplicações delegam a autenticação e autorização 
para esse servidor central.

Exemplos de soluções IAM: Keycloak (open source), Okta (comercial), Auth0 (comercial), AWS IAM e Azure Active 
Directory / Microsoft Entra ID.

Benefícios principais: Single Sign-On (SSO): login uma vez, acessa tudo; Gestão centralizada: adiciona/remove usuários 
em um só lugar; Segurança: políticas de senha, MFA, auditoria centralizadas; Controle de acesso: gerencia permissões de 
forma unificada.

Exemplo prático: Funcionário novo entra na empresa: Cadastrado no IAM uma vez; Automaticamente ganha acesso a email, 
ERP, CRM, intranet; Muda de departamento? Permissões atualizadas no IAM refletem em todos os sistemas; Sai da empresa? 
Acesso revogado em todos os lugares simultaneamente.

LOGGING

O Spring Security fornece um registro abrangente de todos os eventos relacionados à segurança nos níveis DEBUG e TRACE. 
Isso pode ser muito útil ao depurar sua aplicação, pois, por questões de segurança, o Spring Security não adiciona 
detalhes sobre o motivo da rejeição de uma requisição ao corpo da resposta. Se você se deparar com um erro 401 ou 403, 
é muito provável que encontre uma mensagem de log que o ajudará a entender o que está acontecendo.

logging.level.org.springframework.security=TRACE

```

## 2. Configuração

### Passo-a-passo

1. Adicionar dependências;
    a. implementation 'org.springframework.boot:spring-boot-starter-security' 
    b. testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
2. 


### Implementação: 


