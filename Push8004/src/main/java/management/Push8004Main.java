package management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("management.mapper")
@EnableAsync
@EnableScheduling // 定时任务
@EnableRetry // 重试
public class Push8004Main {
    public static void main(String[] args) {
        SpringApplication.run(Push8004Main.class,args);
    }
}
