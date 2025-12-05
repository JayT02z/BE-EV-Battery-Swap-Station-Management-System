package com.boilerplate.code;

import com.bill.billing.BillingPaymentApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = BillingPaymentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
				"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
				"org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
		"spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
class BillingPaymentApplicationTests {

	@Test
	void contextLoads() {
	}

}
