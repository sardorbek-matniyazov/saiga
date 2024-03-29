package saiga.utils.statics;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 07 Mar 2023
 **/
@Component
public record MessageResourceHelperFunction (
        MessageSource messageSource,
        GlobalMethodsToHelp globalMethodsToHelp
) implements Function<String, String> {
    @Override
    public String apply(String code) {
        try {
            return messageSource.getMessage(
                    code,
                    null,
                    switch (globalMethodsToHelp.getCurrentUser().getLang()) {
                        case KAA -> Locale.forLanguageTag("KA");
                        case UZ -> Locale.forLanguageTag("UZ");
                        case RUS -> Locale.forLanguageTag("RU");
                        default -> Locale.ENGLISH;
                    }
            );
        } catch (Exception e) {
            return messageSource.getMessage(
                    code,
                    null,
                    Locale.forLanguageTag("KA")
            );
        }
    }
}
