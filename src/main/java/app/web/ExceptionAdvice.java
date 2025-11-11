package app.web;

import app.exceptions.MealPlanNotFoundException;
import app.exceptions.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import app.web.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){

        ErrorResponse dto= new ErrorResponse(LocalDateTime.now(), e.getMessage());

          return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(dto);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MealPlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMealPlanNotFound(MealPlanNotFoundException e) {
        ErrorResponse dto= new ErrorResponse(LocalDateTime.now(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(dto);
    }



    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleDeleteOwnRecipes(UnauthorizedAccessException e){

        ErrorResponse dto= new ErrorResponse(LocalDateTime.now(), e.getMessage());
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(dto);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {
        ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), "Validation failed");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }
}
