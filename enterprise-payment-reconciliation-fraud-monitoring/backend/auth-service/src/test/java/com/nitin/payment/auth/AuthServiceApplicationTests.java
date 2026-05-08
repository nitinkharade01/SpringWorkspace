package com.nitin.payment.auth;

import org.junit.jupiter.api.Test;
class AuthServiceApplicationTests {
    @Test
    void applicationClassExists() {
        AuthServiceApplication application = new AuthServiceApplication();
        org.assertj.core.api.Assertions.assertThat(application).isNotNull();
    }
}
