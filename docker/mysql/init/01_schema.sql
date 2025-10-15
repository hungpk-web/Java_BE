-- Schema khởi tạo cho database student_db
-- Lưu ý: các bảng, cột được định nghĩa theo đặc tả bạn cung cấp

-- Bảng user
CREATE TABLE IF NOT EXISTS user (
  user_id     INT AUTO_INCREMENT PRIMARY KEY,
  user_name   VARCHAR(20) NOT NULL,
  password    VARCHAR(15) NOT NULL
) ENGINE=InnoDB;

-- Bảng student
CREATE TABLE IF NOT EXISTS student (
  student_id    INT AUTO_INCREMENT PRIMARY KEY,
  student_name  VARCHAR(20) NOT NULL,
  student_code  VARCHAR(10) NOT NULL
) ENGINE=InnoDB;

-- Bảng student_info
CREATE TABLE IF NOT EXISTS student_info (
  info_id        INT AUTO_INCREMENT PRIMARY KEY,
  student_id     INT NOT NULL,
  address        VARCHAR(255) NOT NULL,
  average_score  DOUBLE NOT NULL,
  date_of_birth  DATETIME NOT NULL,
  CONSTRAINT fk_student_info_student
    FOREIGN KEY (student_id) REFERENCES student(student_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;


