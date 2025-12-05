package com.boilerplate.code;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = StationInventoryApplicationTests.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
				"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
				"org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
		"spring.main.allow-bean-definition-overriding=true"
})
class StationInventoryApplicationTests {

	@Test
	void contextLoads() {
	}

}
