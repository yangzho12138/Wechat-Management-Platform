package management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("management.mapper")
public class Template8005Main {
    public static void main(String[] args) {
        SpringApplication.run(Template8005Main.class,args);
    }
}
