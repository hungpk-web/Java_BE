package com.example.Student_BE.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class cho xử lý file operations
 */
public class FileUtils {

    public static void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }



    /**
     * Tạo file path với timestamp
     *
     */
    public static String createFilePathWithTimestamp(String directory, String prefix, String extension) {
        String timestamp = DateTimeUtils.generateTimestamp();
        return directory + File.separator + prefix + "_" + timestamp + extension;
    }


    /**
     * Escape CSV value (handle commas, quotes)
     */
    public static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }

        // Nếu có comma hoặc quote, wrap trong quotes và escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}
