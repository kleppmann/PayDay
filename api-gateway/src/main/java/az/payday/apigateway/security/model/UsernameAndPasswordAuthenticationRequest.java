package az.payday.apigateway.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsernameAndPasswordAuthenticationRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
