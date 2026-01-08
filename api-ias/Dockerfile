# Variáveis de tempo de construção (com valor default - opcional)
ARG IMAGE_BUILD="amazoncorretto:25-alpine3.22-jdk"
ARG IMAGE_RUN="amazoncorretto:25-alpine-full"

# ================================
# 1 - ESTÁGIO DE BUILD (compilação)
# ================================
FROM ${IMAGE_BUILD} AS builder

# Copiar todos os arquivos da raíz do projeto (origem) para dentro da imagem (destino)
# Copia o projeto (incluindo settings.gradle e submódulos)
COPY . .

# Roda testes e depois gera o bootJar do módulo infraestrutura
RUN /gradlew :business:test
RUN /gradlew :application:test
RUN /gradlew :infrastructure:test
RUN /gradlew :infrastructure:bootJar --no-daemon --stacktrace


# ================================
# 2 - ESTÁGIO DE EXECUÇÃO
# ================================
FROM ${IMAGE_RUN} AS runner

# Metadados
# Variáveis de tempo de construção (com valor default - opcional)
ARG APP_NAME="microsserviços"
ARG APP_VERSION="v0.0.2"
ARG APP_DESCRIPTION="Microsserviços em Java (25Lts) e Spring Boot (4.0.1)."
ARG DEVELOPER="juniormartins"
ARG MAINTAINER="juniormartins"
ARG CONTATO="dev.juniorsmartins@gmail.com"

LABEL aplication=${APP_NAME} \
    version=${APP_VERSION} \
    description=${APP_DESCRIPTION} \
    authors=${DEVELOPER} \
    maintainer=${MAINTAINER} \
    contato=${CONTATO}

# Instala o curl
RUN apk add --no-cache curl

# Copia APENAS o JAR gerado do módulo interface-adapters
COPY --from=builder infrastructure/build/libs/api-ias.jar app.jar
# Copiava o JAR quando ele estava no build da raiz.
# COPY --from=builder /build/libs/*.jar app.jar

# Comandos
ENTRYPOINT ["java","-jar","app.jar"]

