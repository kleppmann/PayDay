package az.ibar.payday.ms.auth.service;

import az.ibar.payday.ms.auth.model.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
}
