package com.example.Student_BE.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import com.example.Student_BE.utils.DateTimeUtils;
import com.example.Student_BE.utils.FileUtils;

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
        // Tạo thư mục nếu chưa có bằng FileUtils
        FileUtils.createDirectoryIfNotExists(exportPath);

        // Tạo đường dẫn file với timestamp bằng FileUtils
        String fileName = FileUtils.createFilePathWithTimestamp(exportPath, "students_export", ".csv");

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
            // Escape giá trị CSV bằng FileUtils
            String value = FileUtils.escapeCsvValue(row[i]);
            csvRow.append(value);
        }
        csvRow.append("\n");
        fileWriter.write(csvRow.toString());
    }
}
