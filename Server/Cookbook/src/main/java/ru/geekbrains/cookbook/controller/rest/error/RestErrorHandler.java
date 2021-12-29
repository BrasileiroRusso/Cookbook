package ru.geekbrains.cookbook.controller.rest.error;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import ru.geekbrains.cookbook.service.exception.UserAlreadyExistsException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@ControllerAdvice(basePackages = {"ru.geekbrains.cookbook.controller.rest"})
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messages;

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
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return handleExceptionInternal(e, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleBindException(ex, headers, status, request);
    }

    @ExceptionHandler({UserAlreadyExistsException.class })
    public ResponseEntity<Object> handleUserAlreadyExists(RuntimeException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("User already exists");
        errorDetail.setDetail("User already exists");
        errorDetail.setMessage(e.getClass().getName());
        return handleExceptionInternal(e, errorDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternal(RuntimeException e, WebRequest request){ ;
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Internal Exception");
        errorDetail.setDetail("System internal exception");
        errorDetail.setMessage(e.getClass().getName());
        return new ResponseEntity<>(errorDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
