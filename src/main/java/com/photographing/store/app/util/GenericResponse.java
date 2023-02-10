package com.photographing.store.app.util;

// This class sends the Generic response to the client
public class GenericResponse {
    private String status;
    private String message;
    private Object data;

    public GenericResponse(){}

    public GenericResponse(String status, String message, Object data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public GenericResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
