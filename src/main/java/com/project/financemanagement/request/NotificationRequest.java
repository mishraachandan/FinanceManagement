package com.project.financemanagement.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;


public class NotificationRequest {

    @Size(min = 3, max = 8, message = "Size of the request should be between 3 to 8 characters.")
    private String request;

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "request='" + request + '\'' +
                '}';
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
