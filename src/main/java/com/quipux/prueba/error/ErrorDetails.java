package com.quipux.prueba.error;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {

    private String message;
    private HttpStatus status;
    private Date timestamp;

    public ErrorDetails(String s, HttpStatus internalServerError, Date date) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
