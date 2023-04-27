package saiga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class SaigaApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(SaigaApplication.class, args);
    }
}
