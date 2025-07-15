package com.finance_tracker;

import com.finance_tracker.config.EnvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class FinanceTrackerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FinanceTrackerApplication.class);
        app.addInitializers(new EnvInitializer());
        app.run(args);
    }
}
