package saiga.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import saiga.payload.request.ConfirmationCodeRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.util.function.Predicate;

import static java.time.LocalDateTime.now;
import static saiga.utils.statics.Constants._CONFIRMATION_CODE_LIMIT;
import static saiga.utils.statics.Constants._NUMBER_LENGTH;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirmation_codes")
@Table(name = "confirmation_codes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phone_number"})
})
public class ConfirmationCode extends BaseCreatable implements Predicate<ConfirmationCodeRequest> {
    @Id
    @Column(name = "phone_number", unique = true, length = _NUMBER_LENGTH, nullable = false)
    private String phoneNumber;

    @Column(name = "code", length = _NUMBER_LENGTH)
    private String code;

    @Column(name = "tries", nullable = false)
    private Integer tries = 0;

    public ConfirmationCode(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        resetTries();
    }

    @Override
    public boolean test(ConfirmationCodeRequest confirmationCodeRequest) {
        return confirmationCodeRequest.phoneNumber().equals(this.phoneNumber)
                && confirmationCodeRequest.code().equals(this.code);
    }

    public ConfirmationCode resetCode() {
        this.code = null;
        return this;
    }

    public ConfirmationCode setCode(String code) {
        this.code = code;
        resetTries();
        return this;
    }

    public ConfirmationCode incrementTries() {
        this.tries++;
        return this;
    }

    public boolean isExpiredByTime() {
        return this.getCreatedDate().toLocalDateTime().plusMinutes(_CONFIRMATION_CODE_LIMIT).isBefore(now());
    }

    public ConfirmationCode resetTries() {
        this.tries = 0;
        return this;
    }

    public boolean isTriesExceeded() {
        return this.tries >= 3;
    }
}
