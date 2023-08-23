package com.veterinaria.vet.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("Shared/Error");
        modelAndView.addObject("code", 403);
        System.out.println(e.getMessage());
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
