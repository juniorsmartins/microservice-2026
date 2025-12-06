# TUTORIAL

## Data e Hora (ISO-8601)

* Fontes:
- https://www.iso.org/iso-8601-date-and-time-format.html 
- https://docs.spring.io/spring-boot/reference/features/external-config.html
- https://www.baeldung.com/java-format-localdate-iso-8601-t-z
- https://www.baeldung.com/java-jackson-offsetdatetime 
- https://en.wikipedia.org/wiki/ISO_8601 
- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.conversion-service 
- https://www.youtube.com/watch?v=aAUopejsqIc 

A ISO-8601 é uma norma internacional para representar datas e horas em um formato claro, 
inequívoco e universalmente aceito. Esta norma ISO ajuda a remover dúvidas que podem 
resultar das várias convenções, culturas e fusos horários que afetam uma operação global. 
Ela dá uma maneira de apresentar datas e horários que é claramente definido e compreensível 
para pessoas e máquinas.

A ISO 8601 aborda essa incerteza estabelecendo uma maneira internacionalmente acordada de 
representar as datas: YYYY-MM-DD

Portanto, a ordem dos elementos utilizados para expressar data e hora na ISO 8601 é a 
seguinte: ano, mês, dia, hora, minutos, segundos e milissegundos. Por exemplo, 
27 de setembro de 2022 às 18h está representado como 2022-09-27 18:00:00.000.

Padrão é: aceita qualquer fuso horário como entrada, armazenar em UTC e retornar em UTC.
```
Formato: AAAA-MM-DDThh:mm:ss.sssZ 

Segue abaixo a descrição dos componentes:

    AAAA : Representa o ano com quatro dígitos (ex.: 2023)
    MM : Representa o mês com dois dígitos (ex.: 03 para março)
    DD : Representa o dia do mês com dois dígitos (ex.: 15)
    'T': Um caractere ' T ' literal que separa a data da hora.
    hh : Representa a hora do dia no formato de 24 horas (ex.: 14 para 14h).
    mm : Representa os minutos (ex.: 30)
    ss : Representa os segundos (ex.: 45)
    sss : Representa milissegundos (opcional e pode variar em duração)
    'Z' : Um caractere 'Z' literal que indica que a hora está em Tempo Universal 
    Coordenado (UTC).
```

Passo-a-passo: 
1. Adicionar serverTimeZone (serverTimezone=UTC) na url do datasource;
```
url: jdbc:mariadb://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:db-notifications}?serverTimezone=UTC
```
2. Adicionar TimeZone (TimeZone.setDefault(TimeZone.getTimeZone("UTC"))) na classe Main.
```
static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(ApiNotificationsApplication.class, args);
}
```


## Locale 



