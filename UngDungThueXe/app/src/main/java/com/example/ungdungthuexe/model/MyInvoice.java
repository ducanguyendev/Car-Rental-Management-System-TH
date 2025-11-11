package com.example.ungdungthuexe.model;

public class MyInvoice {
    private int maHoaDon;
    private String ngayLap;
    private double soTienThanhToan;
    private String loaiThanhToan;
    private int maHopDong;
    private HopDongInfo hopDong;
    private NhanVienInfo nhanVien;

    public MyInvoice(int maHoaDon, String ngayLap, double soTienThanhToan, String loaiThanhToan,
                     int maHopDong, HopDongInfo hopDong, NhanVienInfo nhanVien) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.soTienThanhToan = soTienThanhToan;
        this.loaiThanhToan = loaiThanhToan;
        this.maHopDong = maHopDong;
        this.hopDong = hopDong;
        this.nhanVien = nhanVien;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(double soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public HopDongInfo getHopDong() {
        return hopDong;
    }

    public void setHopDong(HopDongInfo hopDong) {
        this.hopDong = hopDong;
    }

    public NhanVienInfo getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVienInfo nhanVien) {
        this.nhanVien = nhanVien;
    }

    // Nested class for HopDong information
    public static class HopDongInfo {
        private int maHopDong;
        private String ngayLap;
        private double tongTien;

        public HopDongInfo(int maHopDong, String ngayLap, double tongTien) {
            this.maHopDong = maHopDong;
            this.ngayLap = ngayLap;
            this.tongTien = tongTien;
        }

        public int getMaHopDong() {
            return maHopDong;
        }

        public void setMaHopDong(int maHopDong) {
            this.maHopDong = maHopDong;
        }

        public String getNgayLap() {
            return ngayLap;
        }

        public void setNgayLap(String ngayLap) {
            this.ngayLap = ngayLap;
        }

        public double getTongTien() {
            return tongTien;
        }

        public void setTongTien(double tongTien) {
            this.tongTien = tongTien;
        }
    }

    // Nested class for NhanVien information
    public static class NhanVienInfo {
        private int maNhanVien;
        private String hoTen;

        public NhanVienInfo(int maNhanVien, String hoTen) {
            this.maNhanVien = maNhanVien;
            this.hoTen = hoTen;
        }

        public int getMaNhanVien() {
            return maNhanVien;
        }

        public void setMaNhanVien(int maNhanVien) {
            this.maNhanVien = maNhanVien;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }
    }
}

