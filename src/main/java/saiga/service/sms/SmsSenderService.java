package saiga.service.sms;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
public interface SmsSenderService {
    void sendUserLoginCode(String phoneNumber, String code);
}
