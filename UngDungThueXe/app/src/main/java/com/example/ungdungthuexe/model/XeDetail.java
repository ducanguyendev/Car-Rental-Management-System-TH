package com.example.ungdungthuexe.model;

public class XeDetail {
    private int maXe;
    private String bienSoXe;
    private String tenXe;
    private String trangThai;
    private int maLoaiXe;
    private String imageURL;
    private LoaiXeInfo loaiXe;

    public XeDetail(int maXe, String bienSoXe, String tenXe, String trangThai, 
                    int maLoaiXe, String imageURL, LoaiXeInfo loaiXe) {
        this.maXe = maXe;
        this.bienSoXe = bienSoXe;
        this.tenXe = tenXe;
        this.trangThai = trangThai;
        this.maLoaiXe = maLoaiXe;
        this.imageURL = imageURL;
        this.loaiXe = loaiXe;
    }

    public int getMaXe() {
        return maXe;
    }

    public void setMaXe(int maXe) {
        this.maXe = maXe;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getMaLoaiXe() {
        return maLoaiXe;
    }

    public void setMaLoaiXe(int maLoaiXe) {
        this.maLoaiXe = maLoaiXe;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LoaiXeInfo getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(LoaiXeInfo loaiXe) {
        this.loaiXe = loaiXe;
    }

    // Nested class for LoaiXe information
    public static class LoaiXeInfo {
        private int maLoaiXe;
        private String tenLoaiXe;
        private String moTa;

        public LoaiXeInfo(int maLoaiXe, String tenLoaiXe, String moTa) {
            this.maLoaiXe = maLoaiXe;
            this.tenLoaiXe = tenLoaiXe;
            this.moTa = moTa;
        }

        public int getMaLoaiXe() {
            return maLoaiXe;
        }

        public void setMaLoaiXe(int maLoaiXe) {
            this.maLoaiXe = maLoaiXe;
        }

        public String getTenLoaiXe() {
            return tenLoaiXe;
        }

        public void setTenLoaiXe(String tenLoaiXe) {
            this.tenLoaiXe = tenLoaiXe;
        }

        public String getMoTa() {
            return moTa;
        }

        public void setMoTa(String moTa) {
            this.moTa = moTa;
        }
    }
}

