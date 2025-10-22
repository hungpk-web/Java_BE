package com.example.Student_BE.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.example.Student_BE.entity.Student;

@Configuration
public class StudentCsvJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcCursorItemReader<Student> jdbcCursorItemReader;

    @Autowired
    private StudentCsvProcessor studentCsvProcessor;

    @Autowired
    private StudentCsvWriter studentCsvWriter;

    @Bean
    public Job exportStudentCsvJob() {
        return new JobBuilder("exportStudentCsvJob", jobRepository)
                .start(createExportDirectoryStep())
                .next(exportStudentCsvStep())
                .build();
    }

    /**
     * Step 1: Tạo thư mục export nếu chưa có
     */
    @Bean
    public Step createExportDirectoryStep() {
        return new StepBuilder("createExportDirectoryStep", jobRepository)
                .tasklet(createExportDirectoryTasklet(), transactionManager)
                .build();
    }

    /**
     * Step 2: Export dữ liệu Student ra CSV
     */
    @Bean
    public Step exportStudentCsvStep() {
        return new StepBuilder("exportStudentCsvStep", jobRepository)
                .<Student, String[]>chunk(10, transactionManager)
                .reader(jdbcCursorItemReader)
                .processor(studentCsvProcessor)
                .writer(studentCsvWriter)
                .build();
    }

    /**
     * Tasklet để tạo thư mục export nếu chưa có
     */
    @Bean
    public Tasklet createExportDirectoryTasklet() {
        return (contribution, chunkContext) -> {
            java.io.File exportDir = new java.io.File("./exports/");
            if (!exportDir.exists()) {
                boolean created = exportDir.mkdirs();
                System.out.println("Created export directory: " + created);
            }
            return RepeatStatus.FINISHED;
        };
    }
}
