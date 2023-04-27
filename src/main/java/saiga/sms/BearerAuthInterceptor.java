package saiga.sms;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@Data
@NoArgsConstructor
public class BearerAuthInterceptor implements RequestInterceptor {
    private String token = "token";

    public BearerAuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(
                "Authorization", String.format("Bearer %s", token)
        );
    }
}
