package com.example.ungdungthuexe.model;

public class ManageInvoiceResponse {
    private int maHoaDon;
    private String ngayLap;
    private double soTienThanhToan;
    private String loaiThanhToan;
    private int maHopDong;
    private HopDongInfo hopDong;

    public ManageInvoiceResponse(int maHoaDon, String ngayLap, double soTienThanhToan,
                                 String loaiThanhToan, int maHopDong, HopDongInfo hopDong) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.soTienThanhToan = soTienThanhToan;
        this.loaiThanhToan = loaiThanhToan;
        this.maHopDong = maHopDong;
        this.hopDong = hopDong;
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

    // Nested class for HopDong information
    public static class HopDongInfo {
        private KhachHangInfo khachHang;

        public HopDongInfo(KhachHangInfo khachHang) {
            this.khachHang = khachHang;
        }

        public KhachHangInfo getKhachHang() {
            return khachHang;
        }

        public void setKhachHang(KhachHangInfo khachHang) {
            this.khachHang = khachHang;
        }

        // Nested class for KhachHang information
        public static class KhachHangInfo {
            private String hoTen;

            public KhachHangInfo(String hoTen) {
                this.hoTen = hoTen;
            }

            public String getHoTen() {
                return hoTen;
            }

            public void setHoTen(String hoTen) {
                this.hoTen = hoTen;
            }
        }
    }
}

