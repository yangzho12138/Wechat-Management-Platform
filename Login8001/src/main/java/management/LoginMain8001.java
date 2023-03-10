package management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LoginMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(LoginMain8001.class,args);
    }
}
