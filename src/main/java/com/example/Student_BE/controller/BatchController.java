package com.example.Student_BE.controller;

import com.example.Student_BE.utils.ApiResponse;
import com.example.Student_BE.service.BatchService;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller để quản lý Batch Jobs
 */
@RestController
@RequestMapping("/api/batch")
@CrossOrigin(origins = "*")
public class BatchController {

    @Autowired
    private BatchService batchService;

    /**
     * API để trigger export Student CSV
     */
    @PostMapping("/export/students")
    public ResponseEntity<ApiResponse<String>> exportStudentsToCsv() {
        try {
            // Khởi động batch job
            JobExecution jobExecution = batchService.startExportStudentCsvJob();

            return ResponseEntity.ok(ApiResponse.success(
                    "Batch job đã được khởi động thành công",
                    batchService.getJobStatus(jobExecution)
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    400,
                    "Lỗi khi khởi động batch job: " + e.getMessage()
            ));
        }
    }

    /**
     * API để kiểm tra trạng thái batch job
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getBatchStatus() {
        return ResponseEntity.ok(ApiResponse.success(
                "Batch service đang hoạt động",
                "Batch processing service is running"
        ));
    }

    /**
     * API để lấy thông tin về batch processing
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<String>> getBatchInfo() {
        return ResponseEntity.ok(ApiResponse.success(
                "Thông tin Batch Processing",
                """
                    Batch Processing Features:
                    - Export Student data to CSV
                    - Chunk-based processing (10 records per chunk)
                    - Automatic directory creation
                    - Timestamped file names
                    - Transaction management
                    - Error handling and logging
                    """
        ));
    }
}
