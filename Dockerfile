FROM ubuntu:24.04 AS builder

RUN apt-get update && apt-get install -y curl unzip
# Download Azul Zulu JDK 25
RUN curl -O https://cdn.azul.com/zulu/bin/zulu25.26.15-ca-jdk25.0.0-ea.2-linux_x64.tar.gz && \
    tar -xf zulu25.26.15-ca-jdk25.0.0-ea.2-linux_x64.tar.gz && \
    mv zulu25.26.15-ca-jdk25.0.0-ea.2-linux_x64 /opt/jdk

ENV JAVA_HOME=/opt/jdk
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app
COPY . .
RUN ./gradlew :vertigram-jooq-app:installDist --no-daemon

FROM ubuntu:24.04
COPY --from=builder /opt/jdk /opt/jdk
ENV JAVA_HOME=/opt/jdk
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app
COPY --from=builder /app/vertigram-jooq-app/build/install/vertigram-jooq-app /app/vertigram-jooq-app
COPY app-config.json /app/app-config.json

CMD ["/app/vertigram-jooq-app/bin/vertigram-jooq-app", "/app/app-config.json"]
