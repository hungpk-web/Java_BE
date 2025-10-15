package com.example.Student_BE.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writer để ghi dữ liệu Student vào file CSV
 */
@Component
public class StudentCsvWriter implements ItemWriter<String[]> {

    private FileWriter fileWriter;
    private boolean headerWritten = false;

    @Value("${batch.export.path:./exports/}")
    private String exportPath;

    @Override
    public void write(Chunk<? extends String[]> chunk) throws Exception {
        // Tạo file nếu chưa có
        if (fileWriter == null) {
            createFile();
        }

        // Ghi header nếu chưa ghi
        if (!headerWritten) {
            writeHeader();
            headerWritten = true;
        }

        // Ghi dữ liệu từ chunk
        for (String[] row : chunk) {
            writeRow(row);
        }

        // Flush để đảm bảo dữ liệu được ghi
        fileWriter.flush();
    }

    /**
     * Tạo file CSV với tên file có timestamp
     */
    private void createFile() throws IOException {
        // Tạo thư mục nếu chưa có
        java.io.File exportDir = new java.io.File(exportPath);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        // Tạo tên file với timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = exportPath + "students_export_" + timestamp + ".csv";

        fileWriter = new FileWriter(fileName);
    }

    /**
     * Ghi header của CSV
     */
    private void writeHeader() throws IOException {
        String header = "Student ID,Student Name,Student Code,Address,Average Score,Date of Birth\n";
        fileWriter.write(header);
    }

    /**
     * Ghi một dòng dữ liệu vào CSV
     */
    private void writeRow(String[] row) throws IOException {
        StringBuilder csvRow = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) {
                csvRow.append(",");
            }
            // Escape comma và quote trong dữ liệu
            String value = row[i] != null ? row[i].replace("\"", "\"\"") : "";
            csvRow.append("\"").append(value).append("\"");
        }
        csvRow.append("\n");
        fileWriter.write(csvRow.toString());
    }

    /**
     * Đóng file writer
     */
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
