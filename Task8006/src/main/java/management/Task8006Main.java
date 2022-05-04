package management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("management.mapper")
public class Task8006Main {
    public static void main(String[] args) {
        SpringApplication.run(Task8006Main.class,args);
    }
}
