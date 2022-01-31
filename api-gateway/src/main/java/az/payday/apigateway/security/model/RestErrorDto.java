package az.payday.apigateway.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestErrorDto {
    private Integer httpCode;
    private String errorCode;
    private String errorMessage;
}
