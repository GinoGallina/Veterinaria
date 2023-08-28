package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.veterinaria.vet.Models.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        ModelAndView modelAndView = new ModelAndView("Shared/Error");
        modelAndView.addObject("code", 403);
        System.out.println(e.getMessage());
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ModelAndView modelAndView = new ModelAndView("Shared/Error");
        modelAndView.addObject("code", 404);
        System.out.println(e.getMessage());
        modelAndView.addObject("message", "P\u00E1gina no encontrada");
        return modelAndView;
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException e) {
        ModelAndView modelAndView = new ModelAndView("Shared/Error");
        modelAndView.addObject("code", 404);
        System.out.println(e.getMessage());
        modelAndView.addObject("message", "P\u00E1gina no encontrada");
        return modelAndView;
    }

    
    @ExceptionHandler(LoginException.class) // Cambia MyCustomException por el tipo de excepción que deseas manejar                                            // de manera diferente
    public ModelAndView handleLoginException(LoginException  e) {
        ModelAndView modelAndView = new ModelAndView("Auth/Login");
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("Usuario y/o contraseñna incorrectas");
        modelAndView.addObject("errors",errorMessages );
        return modelAndView;
    }
    /*  @ExceptionHandler(MethodArgumentNotValidException.class) // Cambia MyCustomException por el tipo de excepción que deseas manejar                                            // de manera diferente
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException  e) {
        BindingResult bindingResult = e.getBindingResult();
                // Obtener los mensajes de error personalizados
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        ModelAndView modelAndView = new ModelAndView("Auth/Login");
        modelAndView.addObject("errors",errorMessages );
        return modelAndView;
    }*/
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }
        Response json = new Response();
        json.setMessages(errorMessages);
        json.setTitle("ERROR");
        return new ResponseEntity<Object>(json, HttpStatus.BAD_REQUEST);
    }


}
