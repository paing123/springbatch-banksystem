package com.springbatch.banksystem.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.banksystem.dao.TransactionRepository;
import com.springbatch.banksystem.dto.TransactionDto;
import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.job.TransactionFilterProfessor;
import com.springbatch.banksystem.job.TransactionListener;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.text.ParseException;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactor;

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

	@Bean
	public ItemWriter<Transaction> writer() {
		return transactions -> {
			log.info("Saving Transactions: " + transactions);
			transactionRepo.saveAll(transactions);
		};
	}

	@Bean
	public TransactionFilterProfessor processor() {
		TransactionFilterProfessor processor = new TransactionFilterProfessor();
		return processor;
	}

	@Bean
	public JobExecutionListener listener() {
		return new TransactionListener();
	}

	@Bean
	public Step stepA() throws UnexpectedInputException, MalformedURLException, ParseException {
		return stepBuilderFactory.get("stepA")
				.<TransactionDto, Transaction>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				.skip(Exception.class)
				.skipLimit(500)
				.build();
	}

	@Bean
	public Job jobA() throws UnexpectedInputException, MalformedURLException, ParseException{
		return jobBuilderFactor.get("jobA")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(stepA())
				.build();
	}
}
