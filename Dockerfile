# Etapa 1: build con GraalVM
FROM ghcr.io/graalvm/graalvm-ce:latest AS builder

RUN gu install native-image

WORKDIR /app

COPY . .

RUN gradlew nativeCompile --no-daemon

# Etapa 2: imagen mínima con binario
FROM debian:bullseye-slim

WORKDIR /app

COPY --from=builder /app/build/native/nativeCompile/app .

EXPOSE 10080

CMD ["./app"]