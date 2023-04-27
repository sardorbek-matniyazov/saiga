package saiga.sms.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageSentResponse {
    private String id;
    private String message;
    private String status;
}
