package az.ibar.payday.ms.auth.exception;

public class AuthException extends RuntimeException {
    public AuthException(String errorMessage) {
        super(errorMessage);
    }
}
