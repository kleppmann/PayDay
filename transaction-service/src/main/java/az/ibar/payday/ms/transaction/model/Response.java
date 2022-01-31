package az.ibar.payday.ms.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    @NotNull
    private String requestId;
    private String errorCode;
    private String errorMessage;
}
