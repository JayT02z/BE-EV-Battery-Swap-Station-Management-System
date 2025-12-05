package com.boilerplate.code;

import com.boilerplate.auth.AuthUserApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = AuthUserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.flyway.enabled=false",
		"eureka.client.enabled=false",
		"spring.cloud.discovery.enabled=false",
		"app.kafka.enabled=false",
		"spring.mail.host=localhost",
		"spring.mail.port=25",
		"spring.main.allow-bean-definition-overriding=true"
})
class AuthUserApplicationTests {

	@Test
	void contextLoads() {
	}

}
