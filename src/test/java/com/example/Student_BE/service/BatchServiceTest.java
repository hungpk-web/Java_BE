package com.example.Student_BE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import java.security.Timestamp;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BatchServiceTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job exportStudentCsvJob;

    @InjectMocks
    private BatchService batchService;

    @BeforeEach
    void initData() {

    }

    @Test
    void startExportStudentCsvJob_success() throws Exception {
        // GIVEN
        JobExecution mockJobExecution = mock(JobExecution.class);
        when(mockJobExecution.getId()).thenReturn(1L);
        when(mockJobExecution.getStatus()).thenReturn(org.springframework.batch.core.BatchStatus.COMPLETED);
        when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class))).thenReturn(mockJobExecution);

        // WHEN
        JobExecution result = batchService.startExportStudentCsvJob();

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(org.springframework.batch.core.BatchStatus.COMPLETED, result.getStatus());

        verify(jobLauncher).run(eq(exportStudentCsvJob), any(JobParameters.class));
    }

    @Test
    void startExportStudentCsvJob_jobLauncherThrowsException_throwsException() throws Exception {
        // GIVEN
        when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class)))
                .thenThrow(new RuntimeException("Job launcher error"));

        // WHEN & THEN
        Exception exception = assertThrows(Exception.class, () -> {
            batchService.startExportStudentCsvJob();
        });

        assertEquals("Job launcher error", exception.getMessage());
        verify(jobLauncher).run(eq(exportStudentCsvJob), any(JobParameters.class));
    }

    @Test
    void startExportStudentCsvJob_jobParametersContainRequiredKeys() throws Exception {
        // GIVEN
        JobExecution mockJobExecution = mock(JobExecution.class);
        when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class))).thenReturn(mockJobExecution);

        // WHEN
        batchService.startExportStudentCsvJob();

        // THEN
        verify(jobLauncher).run(eq(exportStudentCsvJob), argThat(jobParameters -> {
            return jobParameters.getParameters().containsKey("timestamp") &&
                    jobParameters.getParameters().containsKey("jobName") &&
                    jobParameters.getParameters().containsKey("startTime");
        }));
    }

    @Test
    void getJobStatus_success() {
        // GIVEN
        JobExecution jobExecution = mock(JobExecution.class);
        when(jobExecution.getId()).thenReturn(1L);
        when(jobExecution.getStatus()).thenReturn(org.springframework.batch.core.BatchStatus.COMPLETED);
        when(jobExecution.getStartTime()).thenReturn(LocalDateTime.now());
        when(jobExecution.getEndTime()).thenReturn(LocalDateTime.now());

        // WHEN
        String result = batchService.getJobStatus(jobExecution);

        // THEN
        assertNotNull(result);
        assertTrue(result.contains("Job ID: 1"));
        assertTrue(result.contains("Status: COMPLETED"));
        assertTrue(result.contains("Start Time:"));
        assertTrue(result.contains("End Time:"));
    }

    @Test
    void getJobStatus_nullJobExecution_returnsNotFound() {
        // GIVEN
        JobExecution nullJobExecution = null;

        // WHEN
        String result = batchService.getJobStatus(nullJobExecution);

        // THEN
        assertEquals("Job not found", result);
    }

    @Test
    void getJobStatus_runningJob_returnsCorrectStatus() {
        // GIVEN
        JobExecution runningJobExecution = mock(JobExecution.class);
        when(runningJobExecution.getId()).thenReturn(2L);
        when(runningJobExecution.getStatus()).thenReturn(org.springframework.batch.core.BatchStatus.STARTED);
        when(runningJobExecution.getStartTime()).thenReturn(LocalDateTime.now());
        when(runningJobExecution.getEndTime()).thenReturn(null);

        // WHEN
        String result = batchService.getJobStatus(runningJobExecution);

        // THEN
        assertNotNull(result);
        assertTrue(result.contains("Job ID: 2"));
        assertTrue(result.contains("Status: STARTED"));
        assertTrue(result.contains("End Time: null"));
    }

    @Test
    void getJobStatus_failedJob_returnsCorrectStatus() {
        // GIVEN
        JobExecution failedJobExecution = mock(JobExecution.class);
        when(failedJobExecution.getId()).thenReturn(3L);
        when(failedJobExecution.getStatus()).thenReturn(org.springframework.batch.core.BatchStatus.FAILED);
        when(failedJobExecution.getStartTime()).thenReturn(LocalDateTime.now());
        when(failedJobExecution.getEndTime()).thenReturn(LocalDateTime.now());

        // WHEN
        String result = batchService.getJobStatus(failedJobExecution);

        // THEN
        assertNotNull(result);
        assertTrue(result.contains("Job ID: 3"));
        assertTrue(result.contains("Status: FAILED"));
    }

    @Test
    void startExportStudentCsvJob_multipleCalls_createsUniqueJobParameters() throws Exception {
        // GIVEN
        JobExecution mockJobExecution = mock(JobExecution.class);
        when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class))).thenReturn(mockJobExecution);

        // WHEN
        batchService.startExportStudentCsvJob();
        batchService.startExportStudentCsvJob();

        // THEN
        verify(jobLauncher, times(2)).run(eq(exportStudentCsvJob), any(JobParameters.class));
    }


}