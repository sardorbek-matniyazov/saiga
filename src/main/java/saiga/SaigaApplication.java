package saiga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SaigaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaigaApplication.class, args);
    }
}
