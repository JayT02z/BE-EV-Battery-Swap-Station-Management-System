package com.boilerplate.code;

import com.gateway.gateway.GatewayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = GatewayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"eureka.client.enabled=false",
		"spring.cloud.gateway.discovery.locator.enabled=false",
		"spring.cloud.discovery.enabled=false",
		"spring.cloud.gateway.enabled=true"
})
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
