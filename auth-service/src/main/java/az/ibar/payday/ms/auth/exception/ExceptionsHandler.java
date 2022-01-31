package az.ibar.payday.ms.auth.exception;

import az.ibar.payday.ms.auth.model.RestErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthException.class})
    public RestErrorResponse handleAuthException(
            AuthException authException) {

        return RestErrorResponse.builder().errorMessage(authException.getMessage()).httpCode(
                HttpStatus.UNAUTHORIZED.value()).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public RestErrorResponse handleUnknownException(
            Exception notFoundException) {

        return RestErrorResponse.builder().errorMessage(notFoundException.getMessage()).httpCode(
                HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
}
