#!/bin/bash
# Script để đợi MySQL sẵn sàng trước khi khởi động ứng dụng

set -e

host="$1"
shift
user="$1"
shift
password="$1"
shift
database="$1"
shift
cmd="$@"

echo "⏳ Đang chờ MySQL sẵn sàng tại $host:3306..."

# Đợi MySQL port mở
until nc -z "$host" 3306; do
  echo "⏳ MySQL chưa sẵn sàng - đang chờ..."
  sleep 2
done

echo "✅ MySQL port đã mở!"

# Đợi MySQL thực sự chấp nhận kết nối và database sẵn sàng
max_tries=30
count=0
until mysql -h"$host" -u"$user" -p"$password" -D"$database" -e "SELECT 1" >/dev/null 2>&1; do
  count=$((count + 1))
  if [ $count -ge $max_tries ]; then
    echo "❌ Timeout: MySQL không sẵn sàng sau $max_tries lần thử"
    exit 1
  fi
  echo "⏳ Đang chờ MySQL database sẵn sàng... (lần thử $count/$max_tries)"
  sleep 2
done

echo "✅ MySQL đã sẵn sàng! Đang khởi động ứng dụng..."

# Chạy lệnh được truyền vào
exec $cmd

