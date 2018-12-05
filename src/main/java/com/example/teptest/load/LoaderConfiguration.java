package com.example.teptest.load;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Random;

@Configuration
@ConfigurationProperties(prefix = "tep-test.loader")
@Profile("load")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoaderConfiguration {

    String alphabet;
    float gapLikelihood;
    int gapMaxSize;
    int number;
    int batchSize;
    long seed;

    @Bean
    public Random random() {
        return new Random(seed);
    }

}
