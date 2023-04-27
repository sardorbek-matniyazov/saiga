package saiga.service.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import saiga.client.SmsApiClient;
import saiga.service.sms.BaseSmsService;
import saiga.service.sms.SmsSenderService;
import saiga.sms.SmsConfigProperties;
import saiga.sms.SmsMessageProperties;
import saiga.sms.payload.SmsMessageSentResponse;
import saiga.utils.statics.MessageResourceHelperFunction;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Service
public record SmsSenderServiceImpl(
        SmsApiClient smsApiClient,
        SmsMessageProperties smsMessageProperties,
        SmsConfigProperties smsConfigProperties,
        BaseSmsService baseSmsServiceImpl,
        MessageResourceHelperFunction messageResourceHelperFunction
) implements SmsSenderService {
    private static final Logger logger = LoggerFactory.getLogger(SmsSenderServiceImpl.class);
    @Override
    public void sendUserLoginCode(String phoneNumber, String code) {
        final String message = String.format(
                messageResourceHelperFunction.apply("sms.confirmation_code_message"),
                code
        );

        sendMessage(phoneNumber, message, 1);
    }

    private synchronized void sendMessage(String phoneNumber, String message, int count) {
        if (count > 3) {
            logger.error("Sms sending failed 4xx error, reLogin process failed");
            return;
        }
        try {
            final ResponseEntity<SmsMessageSentResponse> objectResponseEntity = smsApiClient.sendSms(
                    phoneNumber.substring(1),
                    message,
                    "4546",
                    smsMessageProperties.getCallbackUrl()
            );
            logger.info(objectResponseEntity.getHeaders().toString());
        } catch (Exception e) {
            baseSmsServiceImpl.reloadSmsApi();
            sendMessage(phoneNumber, message, count + 1);
        }
    }
}
