package com.example.Student_BE.controller;

import com.example.Student_BE.dto.ApiResponse;
import com.example.Student_BE.service.CsvExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý CSV export
 */
@RestController
@RequestMapping("/api/csv")
@Tag(name = "CSV Export", description = "API export dữ liệu ra CSV")
@CrossOrigin(origins = "*")
public class CsvController {

    @Autowired
    private CsvExportService csvExportService;

    /**
     * API export danh sách students ra CSV
     */
    @PostMapping("/export-students")
    @Operation(summary = "Export Students CSV", description = "Export danh sách sinh viên ra file CSV")
    public ResponseEntity<ApiResponse<String>> exportStudentsCsv() {
        try {
            String filePath = csvExportService.exportStudentsToCsv();
            return ResponseEntity.ok(ApiResponse.success("Export CSV thành công", filePath));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Lỗi khi export CSV: " + e.getMessage()));
        }
    }
}
