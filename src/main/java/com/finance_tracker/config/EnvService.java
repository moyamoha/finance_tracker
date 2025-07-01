package com.finance_tracker.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvService {

    private final Dotenv dotenv;

    public EnvService() {
        this.dotenv = Dotenv.load();
        dotenv.entries();
    }

    public String get(String key) {
        return dotenv.get(key);
    }

    public List<DotenvEntry> getEntries() {
        return new ArrayList<>(dotenv.entries());
    }

}
