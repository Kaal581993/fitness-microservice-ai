package com.fitness_microservices.activityservice;

import com.fitness_microservices.activityservice.model.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

@SpringBootTest(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
class ActivityserviceApplicationTests {

	@TestConfiguration
	static class MockConfig {
		@Bean
		@Primary
		@SuppressWarnings("unchecked")
		public KafkaTemplate<String, Activity> kafkaTemplate() {
			return mock(KafkaTemplate.class);
		}

		@Bean
		@Primary
		public WebClient userServiceWebClient() {
			return mock(WebClient.class);
		}

		@Bean
		@Primary
		public WebClient.Builder webClientBuilder() {
			return WebClient.builder();
		}
	}

	@Test
	void contextLoads() {
	}

}
