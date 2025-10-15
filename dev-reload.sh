#!/bin/bash

# Script để reload application sau khi thay đổi code

echo "🔄 Reloading application..."

# Restart container
docker-compose -f docker/docker-compose.yml restart student-app

echo "✅ Application reloaded!"
