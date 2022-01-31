package az.ibar.payday.ms.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestErrorResponse {
    private Integer httpCode;
    private String errorCode;
    private String errorMessage;
    private List<String> validations;
}
