package com.veterinaria.vet.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.data.annotation.AccessType;

@Retention(RetentionPolicy.RUNTIME) // Mantén la anotación disponible en tiempo de ejecución
@AccessType(AccessType.Type.PROPERTY) // Solo se puede acceder a la anotación a través metodos
public @interface CheckAdminVet {
}
