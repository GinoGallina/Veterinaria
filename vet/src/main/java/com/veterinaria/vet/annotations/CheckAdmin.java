package com.veterinaria.vet.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) // Mantén la anotación disponible en tiempo de ejecución
public @interface CheckAdmin {
}
