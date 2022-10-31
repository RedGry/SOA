package se.ifmo.ru.firstservice.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.ifmo.ru.firstservice.exception.NotFoundException;
import se.ifmo.ru.firstservice.util.ResponseUtils;
import se.ifmo.ru.firstservice.web.model.Error;

import javax.persistence.NoResultException;

@ControllerAdvice
public class MainControllerAdvice {
    private ResponseUtils responseUtils;

    public MainControllerAdvice(ResponseUtils responseUtils) {
        this.responseUtils = responseUtils;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Error> validationException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Error> noResultException(NoResultException e){
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> handleNotFoundException(NotFoundException e) {
        return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }

//    @ExceptionHandler(Throwable.class)
//    public ResponseEntity<Error> handleThrowable(Throwable e) {
//        return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//    }
}
