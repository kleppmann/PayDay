package az.payday.apigateway.security.jwt;

import com.google.common.net.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig implements Serializable {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
    private Integer passwordStrength;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
