package ru.geekbrains.cookbook.controller.rest.error;

import lombok.RequiredArgsConstructor;
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
import ru.geekbrains.cookbook.service.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

@ControllerAdvice(basePackages = {"ru.geekbrains.cookbook.controller.rest"})
@RequiredArgsConstructor
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    private MessageSource messages;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .timeStamp(new Date().getTime())
                .title("Message not readable")
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
        errorDetail.setTitle("Validation failed");
        errorDetail.setDetail("Input data validation failed");
        errorDetail.setMessage(e.getClass().getName());
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, List<ValidationError>> errors = fieldErrors.stream()
                .collect(groupingBy(FieldError::getField, mapping(ValidationError::createFromFieldError, toList())));
        errorDetail.setErrors(errors);

        return handleExceptionInternal(e, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = e.getBindingResult();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Bind error");
        errorDetail.setDetail("Invalid" + result.getObjectName());
        errorDetail.setMessage(e.getClass().getName());
        List<ValidationError> errorList = result.getAllErrors().stream()
                .map(objectError -> new ValidationError(objectError.getCode(), objectError.getDefaultMessage()))
                .collect(toList());
        Map<String, List<ValidationError>> errors = new HashMap<>();
        errors.put(result.getObjectName(), errorList);
        return handleExceptionInternal(e, errorDetail, headers, HttpStatus.BAD_REQUEST, request);
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

    @ExceptionHandler({MainUnitNotDefined.class})
    public ResponseEntity<Object> handleMainUnitNotDefined(RuntimeException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Main unit not defined");
        errorDetail.setDetail("Main unit for unit type is not defined");
        errorDetail.setMessage(e.getClass().getName());
        return handleExceptionInternal(e, errorDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ResourceCannotDeleteException.class})
    public ResponseEntity<Object> handleResourceCannotDelete(RuntimeException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Resource cannot be deleted");
        errorDetail.setDetail("Resource cannot be deleted");
        errorDetail.setMessage(e.getClass().getName());
        return handleExceptionInternal(e, errorDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .timeStamp(new Date().getTime())
                .title("Resource not found")
                .detail(e.getMessage())
                .message(e.getClass().getName())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }

    @ExceptionHandler({UnitIncorrectTypeException.class})
    public ResponseEntity<Object> handleUnitIncorrectType(RuntimeException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setTitle("Incorrect unit type");
        errorDetail.setDetail("Main unit for the unit type must have the same unit type");
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
