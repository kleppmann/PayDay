package az.ibar.payday.ms.stock.exception;

import az.ibar.payday.ms.stock.model.RestErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public RestErrorResponse handleNotFoundException(
            NotFoundException notFoundException) {

        return RestErrorResponse.builder().errorMessage(notFoundException.getMessage()).httpCode(
                HttpStatus.NOT_FOUND.value()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public RestErrorResponse handleBadRequestException(
            BadRequestException badRequestException) {

        return RestErrorResponse.builder().errorMessage(badRequestException.getMessage()).httpCode(
                HttpStatus.BAD_REQUEST.value()).build();
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String bodyOfResponse = "Validation exception";
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        RestErrorResponse response = RestErrorResponse.builder().errorMessage(bodyOfResponse)
                .validations(validationList)
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(response, status);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public RestErrorResponse handleUnknownException(
            Exception notFoundException) {

        return RestErrorResponse.builder().errorMessage(notFoundException.getMessage()).httpCode(
                HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
}
