package az.payday.apigateway.security.exception;

import az.payday.apigateway.security.model.RestErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({CustomAuthenticationException.class})
    public ResponseEntity<RestErrorDto> handleAuthException(
            CustomAuthenticationException authenticationException,
            HttpServletRequest httpServletRequest) {

        RestErrorDto errorDto = RestErrorDto.builder()
                .errorMessage(authenticationException.getMessage())
                .httpCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.of(Optional.of(errorDto));
    }
}
