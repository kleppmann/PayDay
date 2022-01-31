package az.ibar.payday.ms.auth.util;

import az.ibar.payday.ms.auth.jwt.JwtConfig;
import az.ibar.payday.ms.auth.model.LoginRequest;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtHelper {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtHelper(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String generateToken(LoginRequest loginRequest) {
        Map<String, String> authorities = new HashMap<>();
        authorities.put("authority", "ROLE_ADMIN");

        return Jwts.builder()
                .setSubject(loginRequest.getUsername())
                .claim("authorities", List.of(authorities))
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }
}
