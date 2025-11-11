package com.example.ungdungthuexe.model;

public class RegisterResponse {
    private boolean success;
    private String message;
    private Customer customer;

    public RegisterResponse(boolean success, String message, Customer customer) {
        this.success = success;
        this.message = message;
        this.customer = customer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

