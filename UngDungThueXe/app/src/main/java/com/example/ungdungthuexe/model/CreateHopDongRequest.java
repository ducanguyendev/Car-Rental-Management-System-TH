package com.example.ungdungthuexe.model;

import java.util.List;

public class CreateHopDongRequest {
    private int maKH;
    private int maNhanVien;
    private Integer maDatXe;
    private String ngayNhanXe;
    private String diaDiemNhan;
    private String ngayTraXe;
    private String diaDiemTra;
    private double tongTien;
    private double tienDatCoc;
    private List<ChiTietHopDongs> chiTietHopDongs;

    public CreateHopDongRequest(int maKH, int maNhanVien, Integer maDatXe, String ngayNhanXe,
                                String diaDiemNhan, String ngayTraXe, String diaDiemTra,
                                double tongTien, double tienDatCoc, List<ChiTietHopDongs> chiTietHopDongs) {
        this.maKH = maKH;
        this.maNhanVien = maNhanVien;
        this.maDatXe = maDatXe;
        this.ngayNhanXe = ngayNhanXe;
        this.diaDiemNhan = diaDiemNhan;
        this.ngayTraXe = ngayTraXe;
        this.diaDiemTra = diaDiemTra;
        this.tongTien = tongTien;
        this.tienDatCoc = tienDatCoc;
        this.chiTietHopDongs = chiTietHopDongs;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Integer getMaDatXe() {
        return maDatXe;
    }

    public void setMaDatXe(Integer maDatXe) {
        this.maDatXe = maDatXe;
    }

    public String getNgayNhanXe() {
        return ngayNhanXe;
    }

    public void setNgayNhanXe(String ngayNhanXe) {
        this.ngayNhanXe = ngayNhanXe;
    }

    public String getDiaDiemNhan() {
        return diaDiemNhan;
    }

    public void setDiaDiemNhan(String diaDiemNhan) {
        this.diaDiemNhan = diaDiemNhan;
    }

    public String getNgayTraXe() {
        return ngayTraXe;
    }

    public void setNgayTraXe(String ngayTraXe) {
        this.ngayTraXe = ngayTraXe;
    }

    public String getDiaDiemTra() {
        return diaDiemTra;
    }

    public void setDiaDiemTra(String diaDiemTra) {
        this.diaDiemTra = diaDiemTra;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienDatCoc() {
        return tienDatCoc;
    }

    public void setTienDatCoc(double tienDatCoc) {
        this.tienDatCoc = tienDatCoc;
    }

    public List<ChiTietHopDongs> getChiTietHopDongs() {
        return chiTietHopDongs;
    }

    public void setChiTietHopDongs(List<ChiTietHopDongs> chiTietHopDongs) {
        this.chiTietHopDongs = chiTietHopDongs;
    }
}

