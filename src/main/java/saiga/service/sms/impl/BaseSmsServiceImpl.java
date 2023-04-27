package saiga.service.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import saiga.client.SmsApiClient;
import saiga.service.sms.BaseSmsService;
import saiga.sms.BearerAuthInterceptor;
import saiga.sms.SmsConfigProperties;
import saiga.sms.payload.SmsLoginResponse;

import java.util.Objects;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Service
public record BaseSmsServiceImpl(
        BearerAuthInterceptor bearerAuthInterceptor,
        SmsApiClient smsApiClient,
        SmsConfigProperties smsConfigProperties
) implements BaseSmsService {

    private static final Logger logger = LoggerFactory.getLogger(BaseSmsServiceImpl.class);
    @Override
    public void reloadSmsApi() {
        try {
            final ResponseEntity<SmsLoginResponse> login = smsApiClient.login(
                    smsConfigProperties.getLogin(), smsConfigProperties.getPassword());
            logger.info(String.format("Sms Api reloaded with status code %s", login.getBody()));
            final String token = Objects.requireNonNull(login.getBody()).getData().get("token");
            bearerAuthInterceptor.setToken(String.format("Bearer %s", token));
        } catch (Exception e) {
            logger.warn("Can't load sms host");
        }
    }
}
