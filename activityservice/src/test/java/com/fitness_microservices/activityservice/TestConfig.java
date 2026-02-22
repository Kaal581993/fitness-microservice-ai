package com.fitness_microservices.activityservice;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestConfig {

    /**
     * Mock KafkaTemplate for testing without actual Kafka broker.
     * This prevents "Connection refused" errors during tests.
     */
    @Bean
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
            org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
            org.springframework.kafka.support.serializer.JsonSerializer.class);
        
        ProducerFactory<String, Object> producerFactory = 
            new DefaultKafkaProducerFactory<>(configProps);
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Mock WebClient for testing without actual User Service.
     * This prevents Eureka/LoadBalancer errors during tests.
     */
    @Bean
    @Primary
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")  // Mock URL
                .build();
    }

    /**
     * Mock WebClient.Builder without LoadBalancer for tests.
     * This prevents Eureka client errors during tests.
     */
    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
