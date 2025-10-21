package com.example.Student_BE.controller;

import com.example.Student_BE.service.BatchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BatchService batchService;

    private JobExecution jobExecution;
    private String jobStatus;

    @BeforeEach
    void initData() {
        // Setup JobExecution mock
        jobExecution = Mockito.mock(JobExecution.class);
        JobInstance jobInstance = Mockito.mock(JobInstance.class);

        Mockito.when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        Mockito.when(jobInstance.getJobName()).thenReturn("exportStudentCsvJob");
        Mockito.when(jobExecution.getId()).thenReturn(1L);
        Mockito.when(jobExecution.getStatus()).thenReturn(org.springframework.batch.core.BatchStatus.COMPLETED);

        // Setup job status
        jobStatus = "Job ID: 1, Status: COMPLETED, Job Name: exportStudentCsvJob";
    }

    @Test
    void exportStudentsToCsv_success() throws Exception {
        // GIVEN
        Mockito.when(batchService.startExportStudentCsvJob())
                .thenReturn(jobExecution);

        Mockito.when(batchService.getJobStatus(ArgumentMatchers.any(JobExecution.class)))
                .thenReturn(jobStatus);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/batch/export/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Batch job đã được khởi động thành công"))
                .andExpect(MockMvcResultMatchers.jsonPath("data")
                        .value("Job ID: 1, Status: COMPLETED, Job Name: exportStudentCsvJob"));
    }

    @Test
    void exportStudentsToCsv_serviceError_fail() throws Exception {
        // GIVEN
        Mockito.when(batchService.startExportStudentCsvJob())
                .thenThrow(new RuntimeException("Lỗi khi khởi động batch job"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/batch/export/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Lỗi khi khởi động batch job: Lỗi khi khởi động batch job"));
    }

    @Test
    void exportStudentsToCsv_databaseConnectionError_fail() throws Exception {
        // GIVEN
        Mockito.when(batchService.startExportStudentCsvJob())
                .thenThrow(new RuntimeException("Không thể kết nối database"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/batch/export/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Lỗi khi khởi động batch job: Không thể kết nối database"));
    }




}

