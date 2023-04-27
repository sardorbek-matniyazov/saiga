package saiga.sms.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class SmsLoginResponse {
    private String message;
    private Map<String, String> data;
}
