package ru.geekbrains.cookbook.controller.rest.response;

public class ErrorResponse {
    private String message;
    private long timestamp;

    public ErrorResponse() {
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
