# TUTORIAL

## Data e Hora (ISO-8601)

A ISO 8601 é uma norma internacional para representar datas e horas em um formato claro, 
inequívoco e universalmente aceito.

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
Aceita qualquer fuso horário como entrada, armazene em UTC e retorne em UTC.

### Fontes:
- https://docs.spring.io/spring-boot/reference/features/external-config.html 
- https://www.baeldung.com/java-format-localdate-iso-8601-t-z 
- https://www.baeldung.com/java-jackson-offsetdatetime 


## Locale 



