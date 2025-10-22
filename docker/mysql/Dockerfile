FROM mysql:8

# Các biến môi trường để MySQL tạo sẵn database và user ở lần chạy đầu
ENV MYSQL_ROOT_PASSWORD=Student@123.
ENV MYSQL_DATABASE=student_db
ENV MYSQL_USER=student
ENV MYSQL_PASSWORD=Student@123
ENV TZ=Asia/Ho_Chi_Minh

# Copy các script init vào thư mục đặc biệt; MySQL sẽ tự chạy theo thứ tự tên file
COPY init/ /docker-entrypoint-initdb.d/

# Expose cổng mặc định (thông tin, việc publish cổng làm ở docker run/compose)
EXPOSE 3306


