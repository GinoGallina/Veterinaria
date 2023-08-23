package com.veterinaria.vet.DTO;

public class ErrorMessage {
    private String message;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ErrorMessage(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
