package com.finance_tracker;

import com.finance_tracker.config.EnvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinanceTrackerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FinanceTrackerApplication.class);
        app.addInitializers(new EnvInitializer());
        app.run(args);
    }
}
