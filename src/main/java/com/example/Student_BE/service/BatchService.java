package com.example.Student_BE.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Student_BE.utils.DateTimeUtils;

/**
 * Service để quản lý Batch Jobs
 */
@Service
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job exportStudentCsvJob;

    /**
     * Khởi động job export Student CSV
     */
    public JobExecution startExportStudentCsvJob() throws Exception {
        // Tạo JobParameters với timestamp để đảm bảo job chạy được nhiều lần
        String timestamp = DateTimeUtils.generateTimestamp();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", DateTimeUtils.getCurrentTimestamp())
                .addString("jobName", "exportStudentCsvJob")
                .addString("startTime", timestamp)
                .toJobParameters();

        // Launch job
        return jobLauncher.run(exportStudentCsvJob, jobParameters);
    }

    /**
     * Kiểm tra trạng thái job
     */
    public String getJobStatus(JobExecution jobExecution) {
        if (jobExecution == null) {
            return "Job not found";
        }

        return String.format("Job ID: %d, Status: %s, Start Time: %s, End Time: %s",
                jobExecution.getId(),
                jobExecution.getStatus(),
                jobExecution.getStartTime(),
                jobExecution.getEndTime());
    }
}
