package de.shifen.gloin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xurenlu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GLoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(GLoinApplication.class, args);
    }

}
