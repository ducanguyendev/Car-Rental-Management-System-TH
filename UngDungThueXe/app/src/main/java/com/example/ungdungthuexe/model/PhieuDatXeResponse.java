package com.example.ungdungthuexe.model;

public class PhieuDatXeResponse {
    private boolean success;
    private String message;
    private PhieuDatXeData data;

    public PhieuDatXeResponse(boolean success, String message, PhieuDatXeData data) {
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

    public PhieuDatXeData getData() {
        return data;
    }

    public void setData(PhieuDatXeData data) {
        this.data = data;
    }

    // Nested class for data
    public static class PhieuDatXeData {
        private int maDatXe;
        private String ngayDat;

        public PhieuDatXeData(int maDatXe, String ngayDat) {
            this.maDatXe = maDatXe;
            this.ngayDat = ngayDat;
        }

        public int getMaDatXe() {
            return maDatXe;
        }

        public void setMaDatXe(int maDatXe) {
            this.maDatXe = maDatXe;
        }

        public String getNgayDat() {
            return ngayDat;
        }

        public void setNgayDat(String ngayDat) {
            this.ngayDat = ngayDat;
        }
    }
}

