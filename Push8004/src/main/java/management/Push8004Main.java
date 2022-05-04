package management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("management.mapper")
public class Push8004Main {
    public static void main(String[] args) {
        SpringApplication.run(Push8004Main.class,args);
    }
}
