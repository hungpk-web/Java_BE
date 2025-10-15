FROM openjdk:17-jdk-slim

# Cài đặt Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Tạo thư mục làm việc
WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copy pom.xml trước để cache dependencies
COPY pom.xml .

# Download dependencies (sẽ được cache nếu pom.xml không thay đổi)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Command để chạy với hot reload
CMD ["mvn", "spring-boot:run"]
