package az.ibar.payday.ms.auth.controller;

import az.ibar.payday.ms.auth.jwt.JwtConfig;
import az.ibar.payday.ms.auth.model.LoginRequest;
import az.ibar.payday.ms.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;

    public AuthController(@Qualifier("authServiceImplMock") AuthService authService,
                          JwtConfig jwtConfig) {
        this.authService = authService;
        this.jwtConfig = jwtConfig;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<Void> register(@RequestBody @Valid LoginRequest loginUser) {
        String token = authService.login(loginUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(jwtConfig.getTokenPrefix().concat("Token"),
                jwtConfig.getTokenPrefix().concat(token));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}
