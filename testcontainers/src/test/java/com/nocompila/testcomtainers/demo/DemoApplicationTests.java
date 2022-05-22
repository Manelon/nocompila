package com.nocompila.testcomtainers.demo;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class DemoApplicationTests {

	//Create kafka container
	@Container
	public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.1.1"));

	@Test
	void contextLoads() {
		
		Properties textProducerProperties = new Properties();
		textProducerProperties.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
		textProducerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		textProducerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		String textTopic = "text-topic";

		Producer<String, String> producer = new KafkaProducer<>(textProducerProperties);

		int messagesProduced = 6;


		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-1701", "USS Enterprise"));
		
		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-1701-D", "USS Enterprise D"));
		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-2000", "USS Excelsior"));
		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-2100", "USS Reliant"));
		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-2200", "USS Voyager"));
		producer.send(new ProducerRecord<String,String>(textTopic, "NCC-2300", "USS Defiant"));

		//producer.commitTransaction();

		producer.close();
		
		//consume from kafka

		Properties textConsumProperties = new Properties();
		textConsumProperties.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
		textConsumProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		textConsumProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		textConsumProperties.put("group.id", "test-group");
		textConsumProperties.put("auto.offset.reset", "earliest");
		textConsumProperties.put("enable.auto.commit", "true");
		textConsumProperties.put("auto.commit.interval.ms", "1000");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(textConsumProperties);
		consumer.subscribe(java.util.Arrays.asList(textTopic));
		boolean doConsume = true;
		int mesagesConsumed = 0;
		while(doConsume){
			ConsumerRecords<String, String> record = consumer.poll(Duration.ofMillis(1000));	
			mesagesConsumed += record.count();
			for (ConsumerRecord<String, String> consumerRecord : record) {
				System.out.println("Consumed message: " + consumerRecord.value());
			}
			if(record.count() == 0){
				consumer.close();
				doConsume = false;
			}
		}

		//Assert messages produced and consumed are equal
		assertEquals(messagesProduced, mesagesConsumed);
		

		






		
	}

	@DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.properties.bootstrap.servers",kafkaContainer::getBootstrapServers);


    }

}
