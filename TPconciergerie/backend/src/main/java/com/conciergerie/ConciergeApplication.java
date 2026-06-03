package com.conciergerie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // 1. Correction ici

@SpringBootApplication // 2. Correction ici
public class ConciergeApplication {
    public static void main(String[] args) { // 3. Correction ici
        SpringApplication.run(ConciergeApplication.class, args);
    }
}