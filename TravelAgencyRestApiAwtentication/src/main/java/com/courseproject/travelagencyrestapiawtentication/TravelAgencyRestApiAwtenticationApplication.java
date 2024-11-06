package com.courseproject.travelagencyrestapiawtentication;

import com.courseproject.travelagencyrestapiawtentication.secinit.InitializeUserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TravelAgencyRestApiAwtenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelAgencyRestApiAwtenticationApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            InitializeUserRoles initRoles = ctx.getBean(InitializeUserRoles.class);
            initRoles.commandForAuthorizationAdmin(); // Извикване на метода за команден ред
        };
    }
}
