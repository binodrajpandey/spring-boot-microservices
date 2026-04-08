FROM azul/zulu-openjdk-alpine:25 AS builder
WORKDIR /var/tmp/product-service
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
    ./gradlew :product-service:dependencies --no-daemon --parallel --build-cache

COPY product-service/src product-service/src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :product-service:bootJar --no-daemon --parallel --build-cache
RUN java -Djarmode=tools -jar product-service/build/libs/product-service.jar extract --destination extracted

FROM azul/zulu-openjdk-alpine:25-jre
WORKDIR /root
COPY --from=builder /var/tmp/product-service/extracted/product-service.jar ./
COPY --from=builder /var/tmp/product-service/extracted/lib ./lib

EXPOSE 8080
CMD ["java", "-jar", "./product-service.jar"]
