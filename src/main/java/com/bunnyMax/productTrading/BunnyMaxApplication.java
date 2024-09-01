package com.bunnyMax.productTrading;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@ServletComponentScan
@OpenAPIDefinition(info = @Info(title = "Bunny Max Product Trading", version = "1.0", description = "API documentation for Bunny Max Product Trading"))
public class BunnyMaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(BunnyMaxApplication.class, args);
    }

}
