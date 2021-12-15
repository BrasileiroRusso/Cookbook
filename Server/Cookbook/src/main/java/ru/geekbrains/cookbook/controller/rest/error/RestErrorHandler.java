package ru.geekbrains.cookbook.controller.rest.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@ControllerAdvice(basePackages = {"ru.geekbrains.cookbook.controller.rest"})
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .timeStamp(new Date().getTime())
                .title("Resource Not Found")
                .detail(e.getMessage())
                .message(e.getClass().getName())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .timeStamp(new Date().getTime())
                .title("Message Not Readable")
                .detail(ex.getMessage())
                .message(ex.getClass().getName())
                .build();
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Validation failed");
        errorDetail.setMessage(e.getClass().getName());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return handleExceptionInternal(e, errorDetail, headers, status, request);
    }
}
