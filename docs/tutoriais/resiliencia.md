# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-framework/reference/index.html
- https://docs.spring.io/spring-framework/reference/core/resilience.html
- https://docs.spring.io/spring-framework/docs/7.0.2/javadoc-api/org/springframework/resilience/annotation/Retryable.html
- https://www.youtube.com/watch?v=CT1wGTwOfg0 
- 
- 
- https://spring.io/projects/spring-cloud-circuitbreaker 
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/ 
- https://github.com/spring-projects/spring-retry 
- https://github.com/resilience4j/resilience4j 
- https://resilience4j.readme.io/docs/getting-started-3 
- 
- https://www.baeldung.com/resilience4j 
- 
- 
- 

### Introdução: 
```
Spring Framework - a partir da versão 7.0, o núcleo do Spring Framework inclui 
recursos comuns de resiliência, em particular @Retryable e @ConcurrencyLimit para 
invocações de métodos, bem como suporte a novas tentativas programáticas.

Ou seja, há duas formas de usar:
- Configuração por anotações;
- Configuração programática.

Por anotação: 

- @Retryable: anotação que especifica as características de repetição para um 
método individual (com a anotação declarada no nível do método) ou para todos os 
métodos invocados por proxy em uma determinada hierarquia de classes (com a 
anotação declarada no nível do tipo).

Isso pode ser adaptado especificamente para cada método, se necessário — por 
exemplo, restringindo as exceções a serem repetidas por meio dos atributos includes 
eexcludes. Os tipos de exceção fornecidos serão comparados com uma exceção lançada 
por uma invocação com falha, bem como com causas aninhadas.

Para casos de uso avançados, você pode especificar um personalizado 
MethodRetryPredicate por meio do predicate atributo em @Retryable, e o predicado 
será usado para determinar se deve tentar novamente uma invocação de método com 
falha com base em um Method e um dado Throwable – por exemplo, verificando a 
mensagem do Throwable.

IMPORTANTE 1: Idempotência É a regra de ouro. O método anotado com @Retryable 
precisa ser idempotente (executá-lo várias vezes deve ter o mesmo efeito que 
executá-lo uma única vez). Nunca use retry em operações de "estorno" ou 
"pagamento" sem um mecanismo de chave de idempotência.

IMPORTANTE 2: excluir exceções programadas e que não devem gerar retentativas. 
Por exemplo, no findById não deve repetir se o ID não existir, pois não há razão 
para repetir uma consulta que retornou "não encontrado". 

- @ConcurrencyLimit: uma anotação que especifica um limite de concorrência para 
um método individual (com a anotação declarada no nível do método) ou para todos 
os métodos invocados por proxy em uma determinada hierarquia de classes (com a 
anotação declarada no nível do tipo).

Isso visa proteger o recurso de destino contra o acesso simultâneo de muitas 
threads, de forma semelhante ao efeito de um limite de tamanho de pool para um 
pool de threads ou um pool de conexões que bloqueia o acesso se seu limite for 
atingido.

Habilitando Métodos Resilientes

Assim como muitos dos recursos principais do Spring baseados em anotações, 
@Retryableas @ConcurrencyLimit anotações de resiliência são projetadas como 
metadados que você pode optar por respeitar ou ignorar. A maneira mais 
conveniente de habilitar o processamento das anotações de resiliência é 
declará-las @EnableResilientMethods na @Configurationclasse correspondente.

```

Usei o seguinte para testar a resiliência dos métodos:
```
Random random = new Random();
if (random.nextDouble() < 0.5) {
    log.info("\n\n Find - Simulando falha temporária ao buscar clientes... \n");
    throw new RuntimeException("Erro temporário ao buscar clientes. Tentando novamente...");
}
```

## 2. Configuração

### Passo-a-passo

Configuração por anotações:
1. Adicionar anotação @EnableResilientMethods na classe Main;
2. Adicionar anotação @Retryable;
3. Adicionar anotação @ConcurrencyLimit;


### Implementação: 

1. Adicionar anotação @EnableResilientMethods na classe Main;
```
@SpringBootApplication(
    scanBasePackages = {
            "backend.finance.adapters",            
            "backend.finance.application",      
            "backend.finance.enterprise"        
    }
)
@RefreshScope
@EnableResilientMethods
public class ApiUserApplication {

    static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ApiUserApplication.class, args);
    }
}
```

2. Adicionar anotação @Retryable;
```
@Retryable(
        maxRetries = 3,
        jitter = 10,
        delay = 1000,
        multiplier = 2
)

Exemplo para findById:

@Retryable(
    excludes = {CustomerNotFoundCustomException.class},
    maxRetries = 5, 
    jitter = 10, 
    delay = 1000, 
    multiplier = 2 
)

Exemplo para update:

@Retryable(
    excludes = {CustomerNotFoundCustomException.class, EmailConflictRulesCustomException.class,
        UsernameConflictRulesCustomException.class, RoleNotFoundCustomException.class},
    maxRetries = 2,
    jitter = 10,
    delay = 1000,
    multiplier = 2
)
```

```

```

