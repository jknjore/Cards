package com.logicea.cards.error_handling;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.logicea.cards.responses.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Catch All errors from system and return a customized response
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Response response = new Response();
        String error = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String field = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField();
        response.setMessage(String.format("%s %s",field, error));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        Response response = new Response();
         final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
         response.setMessage(error);
         return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        Response response = new Response();
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        response.setMessage(errors.get(0));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, final WebRequest request) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadCredentialsException.class, ExpiredJwtException.class, SignatureException.class, MalformedJwtException.class, })
    public ResponseEntity<Object> handleBadCredentialsException(final BadCredentialsException ex, final WebRequest request) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedError(final AccessDeniedException ex, HttpServletRequest request) {
            Response response = new Response();
            response.setMessage(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        Response response = new Response();

        if(ex.getCause() instanceof CustomException)
        {
            response.setMessage(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(ex.getCause() instanceof InvalidFormatException)
        {
            response.setMessage("Please ensure that all your inputs are in the correct format.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

           response.setMessage("An error occurred processing your request.");
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
