package com.veterinaria.vet.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class CheckUserAspect {

    @Autowired
    private HttpSession httpSession;

    @Before("@annotation(com.veterinaria.vet.annotations.CheckUser)")
    public void checkUserRole(JoinPoint joinPoint) throws Exception {
        Object userRole = httpSession.getAttribute("user_role");
        if (userRole == null || !userRole.equals("[USER]")) {
            throw new RuntimeException("No tienes permisos para acceder a esta página");
        }
    }
}
