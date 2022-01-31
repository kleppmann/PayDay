package az.ibar.payday.ms.auth.service.impl;

import az.ibar.payday.ms.auth.exception.AuthException;
import az.ibar.payday.ms.auth.model.LoginRequest;
import az.ibar.payday.ms.auth.service.AuthService;
import az.ibar.payday.ms.auth.util.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplMock implements AuthService {

    private final JwtHelper jwtHelper;
    @Value("${test.username}")
    String username;
    @Value("${test.username}")
    String password;

    public AuthServiceImplMock(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public String login(LoginRequest request) {
        if (request.getUsername().equals(username) && request.getPassword().equals(password))
            return jwtHelper.generateToken(request);
        throw new AuthException("incorrect username or password");
    }
}
