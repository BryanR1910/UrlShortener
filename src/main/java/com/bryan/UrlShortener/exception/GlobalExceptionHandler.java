package com.bryan.UrlShortener.exception;

import com.bryan.UrlShortener.model.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException (MethodArgumentNotValidException exception,
                                                                          WebRequest webRequest){
        Map<String,String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((e) -> {
            String key = ((FieldError)e).getField();
            String value = e.getDefaultMessage();
            mapErrors.put(key, value);
        });
        ApiResponse apiResponse = new ApiResponse(mapErrors.toString(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerShortUrlNotFound(ShortUrlNotFoundException ex, WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(webRequest.getDescription(false), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(ShortCodeGenerationException.class)
    public ResponseEntity<ApiResponse> handlerShortCodeGeneration(ShortCodeGenerationException ex, WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(webRequest.getDescription(false), ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

}
