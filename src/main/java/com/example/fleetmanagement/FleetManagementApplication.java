package com.example.fleetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FleetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetManagementApplication.class, args);
    }

}
