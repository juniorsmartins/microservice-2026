# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-framework/reference/integration/email.html 
- https://docs.spring.io/spring-boot/reference/io/email.html 
- https://www.baeldung.com/spring-email 
- https://www.baeldung.com/java-email-sendgrid 
- https://javaee.github.io/javamail/ 
- https://mailtrap.io/blog/spring-send-email/ 
- https://www.youtube.com/watch?v=_MwdIaMy_Ao 


### Introdução

```
O Spring Framework fornece uma abstração para o envio de e-mails através da JavaMailSender 
interface e o Spring Boot oferece configuração automática e um módulo inicializador para essa 
abstração.
```

## 2. Configuração

### Passo-a-passo 

1. Adição no build.gradle;
   2. dependências;
2. Adição no application.yml;
3. Configuração programática.

### Implementação

build.gradle
```
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.0.0'
    id 'io.spring.dependency-management' version '1.1.7'
    id "com.github.davidmc24.gradle.plugin.avro" version "1.9.1"
    id "org.flywaydb.flyway" version "11.18.0"
}

group = 'backend.communication'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
    maven {
        url "https://packages.confluent.io/maven/"
    }
}

dependencies {
    implementation project(':aplicacao')

    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-mail'

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-flyway'
    implementation 'org.flywaydb:flyway-mysql'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    runtimeOnly 'com.h2database:h2'

    implementation 'org.springframework.boot:spring-boot-starter-kafka'
    implementation group: 'io.confluent', name: 'kafka-avro-serializer', version: '8.0.0'
    implementation group: 'io.confluent', name: 'kafka-schema-registry-client', version: '8.0.0'
    implementation group: 'org.apache.avro', name: 'avro', version: '1.12.0'
    testImplementation group: 'io.confluent', name: 'kafka-schema-registry', version: '8.0.0'

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-flyway-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-kafka-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-mail-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

avro {
    createSetters = true
    fieldVisibility = "PRIVATE"
    outputCharacterEncoding = "UTF-8"
    enableDecimalLogicalType = true
}

tasks.named('test') {
    useJUnitPlatform()
}

bootJar {
    archiveFileName = 'api-notifications.jar'
}

jar {
    enabled = false
}
```

Configuração do servidor de email no application.yml (TODO - falta configurar nos testes):
```
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: dev.juniorsmartins@gmail.com
    password: nrqd slph cgpq tlit
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

Configuração programática:
```
@Configuration
@Getter
public class EmailPropertiesConfig {

    @Value("${spring.mail.username}")
    public String mailUsername;
}

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender implements EmailOutputPort {

    private final EmailPropertiesConfig emailPropertiesConfig;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String destinatario, String assunto, String mensagem) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailPropertiesConfig.mailUsername);
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(mensagem);

        try {

            javaMailSender.send(message);
            log.info("\n\n sendEmail - Email enviado com sucesso: {} \n", message);

        } catch (MailException e) {
            log.error("\n\n sendEmail - Erro ao enviar email: {} \n", e.getMessage());
        }
    }
}
```