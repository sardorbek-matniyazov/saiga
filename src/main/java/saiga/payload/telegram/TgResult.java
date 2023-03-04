package saiga.payload.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record TgResult(
    boolean ok,
    Result result
) {}