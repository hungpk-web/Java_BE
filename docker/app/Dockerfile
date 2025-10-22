FROM openjdk:17-jdk-slim

# Cài đặt Maven, MySQL client và netcat để kiểm tra kết nối
RUN apt-get update && \
    apt-get install -y maven default-mysql-client netcat-traditional && \
    rm -rf /var/lib/apt/lists/*

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

# Copy wait-for-mysql script
COPY docker/wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

# Build application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Sử dụng entrypoint để đợi MySQL sẵn sàng
ENTRYPOINT ["/wait-for-mysql.sh", "mysql", "student", "Student@123", "student_db"]

# Command để chạy với hot reload
CMD ["mvn", "spring-boot:run"]
