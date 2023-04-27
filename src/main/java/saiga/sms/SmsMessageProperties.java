package saiga.sms;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.message")
public class SmsMessageProperties {
    @Value("${sms.message.boss_phone}")
    private String basePhone;
    @Value("${sms.message.callback_url}")
    private String callbackUrl;
}
