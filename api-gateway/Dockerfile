FROM azul/zulu-openjdk-alpine:25 AS builder
WORKDIR /var/tmp/api-gateway
COPY gradlew gradlew
COPY gradle/wrapper gradle/wrapper

RUN ./gradlew --version
COPY build.gradle .
COPY settings.gradle .
COPY api-gateway/build.gradle api-gateway/build.gradle
COPY discovery-server/build.gradle discovery-server/build.gradle
COPY inventory-service/build.gradle inventory-service/build.gradle
COPY order-service/build.gradle order-service/build.gradle
COPY product-service/build.gradle product-service/build.gradle

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :api-gateway:dependencies --no-daemon --parallel --build-cache

COPY api-gateway/src api-gateway/src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :api-gateway:bootJar --no-daemon --parallel --build-cache
RUN java -Djarmode=tools -jar api-gateway/build/libs/api-gateway.jar extract --destination extracted

FROM azul/zulu-openjdk-alpine:25-jre
WORKDIR /root
COPY --from=builder /var/tmp/api-gateway/extracted/api-gateway.jar ./
COPY --from=builder /var/tmp/api-gateway/extracted/lib ./lib

EXPOSE 8080
CMD ["java", "-jar", "./api-gateway.jar"]
