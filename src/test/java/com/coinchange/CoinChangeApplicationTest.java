package com.coinchange;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoinChangeApplicationTest {
  @Test
  void contextLoads() {
    // This test will pass if the application context loads successfully
  }

  @Test
  void testRunMethod() {
        // Call the run method directly
        CoinChangeApplication.main(new String[] {});
    }
}
