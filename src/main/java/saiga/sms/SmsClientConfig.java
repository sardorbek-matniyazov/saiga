package saiga.sms;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saiga.client.SmsApiClient;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Configuration
public class SmsClientConfig {
    @Bean
    public SmsApiClient smsApiClient() {
        return Feign.builder()
//                .errorDecoder(new CustomErrorDecoder())
                .requestInterceptor(bearerAuthInterceptor())
                .contract(new CustomSpringMvcContract())
                .target(SmsApiClient.class, "https://notify.eskiz.uz/api/");
    }

    @Bean
    public BearerAuthInterceptor bearerAuthInterceptor() {
        return new BearerAuthInterceptor("token");
    }
}
