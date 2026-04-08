FROM azul/zulu-openjdk-alpine:25 AS builder
WORKDIR /var/tmp/order-service
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
    ./gradlew :order-service:dependencies --no-daemon --parallel --build-cache

COPY order-service/src order-service/src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :order-service:bootJar --no-daemon --parallel --build-cache
RUN java -Djarmode=tools -jar order-service/build/libs/order-service.jar extract --destination extracted

FROM azul/zulu-openjdk-alpine:25-jre
WORKDIR /root
COPY --from=builder /var/tmp/order-service/extracted/order-service.jar ./
COPY --from=builder /var/tmp/order-service/extracted/lib ./lib

EXPOSE 8080
CMD ["java", "-jar", "./order-service.jar"]
