# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY . .

# Build (with retry for transient network/DNS failures when pulling dependencies)
ENV MAVEN_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"
RUN set -eux; \
		for i in 1 2 3 4 5; do \
			mvn -B -U clean install -DskipTests && break; \
			echo "Maven build failed (attempt ${i}/5), retrying..."; \
			sleep $((i * 5)); \
			if [ "$i" -eq 5 ]; then \
				echo "Maven build failed after 5 attempts"; \
				exit 1; \
			fi; \
		done

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=builder /build/Host/target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]