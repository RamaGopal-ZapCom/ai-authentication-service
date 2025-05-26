package com.zapcom.exceptions;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerAlreadyExist.class)
    public ResponseEntity<?> handleCustomerAlreadyExist(CustomerAlreadyExist e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error-message", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        final String[] message = new String[1];
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                message[0] = (fieldError.getDefaultMessage())
        );
        error.put("error-message", message[0]);
        System.out.println(fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(CustomerNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyActivated.class)
    public ResponseEntity<?> accountAlreadyActivated(AccountAlreadyActivated e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException(AuthenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerException(NullPointerException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
