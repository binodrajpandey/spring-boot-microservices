FROM azul/zulu-openjdk-alpine:25 AS builder
WORKDIR /var/tmp/discovery-server
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
    ./gradlew :discovery-server:dependencies --no-daemon --parallel --build-cache

COPY discovery-server/src discovery-server/src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :discovery-server:bootJar --no-daemon --parallel --build-cache
RUN java -Djarmode=tools -jar discovery-server/build/libs/discovery-server.jar extract --destination extracted

FROM azul/zulu-openjdk-alpine:25-jre
WORKDIR /root
COPY --from=builder /var/tmp/discovery-server/extracted/discovery-server.jar ./
COPY --from=builder /var/tmp/discovery-server/extracted/lib ./lib

EXPOSE 8080
CMD ["java", "-jar", "./discovery-server.jar"]
