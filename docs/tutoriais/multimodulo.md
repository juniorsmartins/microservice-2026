PASSO-A-PASSO 

A API-USERS será o exemplo prático desse tutorial.

1. Criar módulos na IDEA (new -> module);

```
Criei três módulos: 

enterprise-business-rules, 
application-business-rules e 
interface-adapters.
```

2. Declara os módulos no settings.gradle, que está na raíz, com include();

```
rootProject.name = 'api-users'

include('enterprise-business-rules')
include('application-business-rules')
include('interface-adapters')
```

3. Optei por apagar o gradle.build da raiz. Cada módulo terá um build.gradle (são três);

build.gradle do enterprise-business-rules:
```
plugins {
    id 'java'
}

group = 'backend.finance'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    testImplementation platform('org.junit:junit-bom:5.11.3') 
    testImplementation 'org.junit.jupiter:junit-jupiter' 
    testImplementation 'org.mockito:mockito-core:5.20.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.20.0'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}
```

build.gradle do application-business-rules:
```
plugins {
    id 'java'
}

group = 'backend.finance'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation project(':enterprise-business-rules')

    testImplementation platform('org.junit:junit-bom:5.11.3') 
    testImplementation 'org.junit.jupiter:junit-jupiter' 
    testImplementation 'org.mockito:mockito-core:5.20.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.20.0'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}
```

build.gradle do interface-adapters:
```
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.7'
    id 'io.spring.dependency-management' version '1.1.7'
    id "com.github.davidmc24.gradle.plugin.avro" version "1.9.1"
}

group = 'backend.finance'
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
    implementation project(':application-business-rules')
    
    implementation 'org.springframework.boot:spring-boot-starter-web'

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.liquibase:liquibase-core'
    runtimeOnly 'org.postgresql:postgresql'
    runtimeOnly 'com.h2database:h2'

    implementation 'org.springframework.kafka:spring-kafka'
    implementation group: 'io.confluent', name: 'kafka-avro-serializer', version: '8.0.0' 
    implementation group: 'io.confluent', name: 'kafka-schema-registry-client', version: '8.0.0' 
    implementation group: 'org.apache.avro', name: 'avro', version: '1.12.0' 
    testImplementation 'org.springframework.kafka:spring-kafka-test'
    testImplementation group: 'io.confluent', name: 'kafka-schema-registry', version: '8.0.0' 

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation group: 'io.rest-assured', name: 'rest-assured', version: '5.5.6'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

avro {
    createSetters = true 
    fieldVisibility = "PRIVATE" 
    outputCharacterEncoding = "UTF-8"
}

test {
    useJUnitPlatform()
}

bootJar {
    archiveFileName = 'api-users.jar' 
}

jar {
    enabled = false
}
```

4. Coloca classe main e application.yml (também o de teste) no módulo mais externo, que possui dependências de Spring;


5. Adiciona detalhes sobre build (compilação) no gradle.build do módulo mais externo;

```
bootJar {
    archiveFileName = 'api-users.jar' // padronização do nome. Garante sempre ser api-users.jar, independente da versão.
}

jar {
    enabled = false
}
```
 
6. Alterações no Dockerfile. Mudanças no RUN e no COPY --from=builder, que usa o nome definido no build.gradle (api-users.jar);

```
ARG IMAGE_BUILD="amazoncorretto:25-alpine3.22-jdk"
ARG IMAGE_RUN="amazoncorretto:25-alpine-full"

FROM ${IMAGE_BUILD} AS builder

COPY . .

# Gera apenas o bootJar do módulo interface-adapters
RUN ./gradlew :interface-adapters:bootJar --no-daemon --stacktrace


FROM ${IMAGE_RUN} AS runner

ARG APP_NAME="microsserviços"
ARG APP_VERSION="v0.0.1"
ARG APP_DESCRIPTION="Microsserviços em Java (25Lts) e Spring Boot (3.5.6)."
ARG DEVELOPER="juniormartins"
ARG MAINTAINER="juniormartins"
ARG CONTATO="dev.juniorsmartins@gmail.com"

LABEL aplication=${APP_NAME} \
    version=${APP_VERSION} \
    description=${APP_DESCRIPTION} \
    authors=${DEVELOPER} \
    maintainer=${MAINTAINER} \
    contato=${CONTATO}

RUN apk add --no-cache curl

# Copia APENAS o JAR gerado do módulo interface-adapters
COPY --from=builder interface-adapters/build/libs/api-users.jar app.jar

ENTRYPOINT ["java", "--enable-preview","-jar","app.jar"]
```

7. Comandos gradle para ver se deu certo.

```
Gerar o jar
./gradlew :interface-adapters:bootJar 

Liste o jar
ls interface-adapters/build/libs/ 

Rode o jar
java -jar interface-adapters/build/libs/api-users.jar

Subir o docker-compose
docker compose up --build
```

