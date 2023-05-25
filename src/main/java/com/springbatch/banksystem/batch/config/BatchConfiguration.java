package com.springbatch.banksystem.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.banksystem.dao.TransactionRepository;
import com.springbatch.banksystem.dto.TransactionDto;
import com.springbatch.banksystem.entity.Account;
import com.springbatch.banksystem.entity.Customer;
import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.job.TransactionFilterProfessor;
import com.springbatch.banksystem.job.TransactionListener;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	@Autowired
	private TransactionRepository transactionRepo;

	// Autowire StepBuilderFactory
	@Autowired
	private StepBuilderFactory sbf;

	// Autowire JobBuilderFactory
	@Autowired
	private JobBuilderFactory jbf;

	// Reader class Object
	@Bean
	public FlatFileItemReader<TransactionDto> reader() {

		FlatFileItemReader<TransactionDto> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("/dataSource.txt"));
		reader.setLinesToSkip(1);
		// reader.setResource(new FileSystemResource("D:/dataSource.txt"));
		// reader.setResource(new
		// UrlResource("https://domain.com/files/dataSource.txt"));

		reader.setLineMapper(new DefaultLineMapper<TransactionDto>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setDelimiter("|");
						setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
					}
				});

				setFieldSetMapper(new BeanWrapperFieldSetMapper<TransactionDto>() {
					{
						setTargetType(TransactionDto.class);
					}
				});
			}
		});

		reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());

		return reader;
	}

	// Writer class Object
	@Bean
	public ItemWriter<Transaction> writer() {
		return transactions -> {
			log.info("Saving Transactions: " + transactions);
			transactionRepo.saveAll(transactions);
		};
	}

	// Processor class Object
	@Bean
	public TransactionFilterProfessor processor() {
		TransactionFilterProfessor processor = new TransactionFilterProfessor();
		return processor;
	}

	// Listener class Object
	@Bean
	public JobExecutionListener listener() {
		return new TransactionListener();
	}

	// Step Object
	@Bean
	public Step stepA() {
		return sbf.get("stepA")
				.<TransactionDto, Transaction>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer()).build();
	}

	// Job Object
	@Bean
	public Job jobA() {
		return jbf.get("jobA")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(stepA())
				.build();
	}
}
