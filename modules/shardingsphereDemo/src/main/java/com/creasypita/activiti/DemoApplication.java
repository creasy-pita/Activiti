package com.creasypita.activiti;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Created by lujq on 11/12/2023.
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class DemoApplication {
}
