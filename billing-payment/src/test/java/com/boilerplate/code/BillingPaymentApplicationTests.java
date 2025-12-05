package com.boilerplate.code;

import com.bill.billing.BillingPaymentApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BillingPaymentApplication.class)
@ActiveProfiles("test")
class BillingPaymentApplicationTests {

	@Test
	void contextLoads() {
	}

}
