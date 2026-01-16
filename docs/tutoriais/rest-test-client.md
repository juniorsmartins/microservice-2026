# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.danvega.dev/blog/spring-framework-7-rest-test-client 
- https://www.youtube.com/watch?v=dPM8n0uNhes&t=452s 
- https://github.com/danvega/rest-test-client
- https://www.baeldung.com/spring-resttestclient-guide 
- https://docs.spring.io/spring-framework/reference/testing/resttestclient.html 
- 

### Introdução: 
```
O ecossistema de testes do Spring evoluiu de simulações baseadas em mocks para a integração completa com servidores 
embutidos. A mais recente adição, o RestTestClient no Spring Framework 7.0, preenche essa lacuna, oferecendo uma 
interface concisa, no estilo de construtor, para interações HTTP sem a complexidade dos clientes tradicionais. Isso 
o torna uma alternativa leve ao MockMvc ou ao WebTestClient - ideal para testes de integração que exigem 
velocidade, legibilidade e flexibilidade.

RestTestClienté um cliente HTTP projetado para testar aplicações de servidor. Ele encapsula o cliente do Spring 
RestClient e o utiliza para realizar requisições, mas expõe uma fachada de teste para verificar as respostas. 
RestTestClient pode ser usado para realizar testes HTTP de ponta a ponta. Também pode ser usado para testar 
aplicações Spring MVC sem um servidor em execução, através do MockMvc.

O RestTestClient oferece cinco maneiras diferentes de testar suas APIs REST:

1) Testes unitários com bindToController;
2) Testes Spring MVC com bindToMockMvc;
3) Testes de integração com bindToApplicationContext;
4) Testes de ponta a ponta com bindToServer;
5) Pontos de extremidade funcionais com bindToRouterFunction.



```

## 2. Configuração

### Passo-a-passo

### Implementação: 

```

```




