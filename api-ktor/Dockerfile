FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/install/replay-api /app
EXPOSE 8080
CMD ["/app/bin/replay-api"]
