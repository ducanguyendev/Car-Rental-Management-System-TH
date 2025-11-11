package com.example.ungdungthuexe.model;

public class CreateCustomerResponse {
    private boolean success;
    private String message;
    private CustomerData data;

    public CreateCustomerResponse(boolean success, String message, CustomerData data) {
        this.success = success;
        this.message = message;
        this.data = data;
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

    public CustomerData getData() {
        return data;
    }

    public void setData(CustomerData data) {
        this.data = data;
    }

    // Nested class for customer data
    public static class CustomerData {
        private int maKH;
        private String hoTen;

        public CustomerData(int maKH, String hoTen) {
            this.maKH = maKH;
            this.hoTen = hoTen;
        }

        public int getMaKH() {
            return maKH;
        }

        public void setMaKH(int maKH) {
            this.maKH = maKH;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }
    }
}
