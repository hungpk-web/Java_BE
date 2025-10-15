#!/bin/bash

# Script để chạy development environment

echo "🚀 Starting Development Environment..."

# Tạo thư mục exports nếu chưa có
mkdir -p exports

# Chạy Docker Compose
echo "📦 Starting Docker containers..."
docker-compose -f docker/docker-compose.yml up --build

echo "✅ Development environment started!"
echo "🌐 Application: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🗄️  Adminer: http://localhost:8081"
