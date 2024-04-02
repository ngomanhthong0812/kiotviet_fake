package com.example.kiotviet_fake.models;

public class Table {
    public String tenBan;
    public String thoiGian;
    public String gia;

    public Table(String tenBan, String thoiGian, String gia) {
        this.tenBan = tenBan;
        this.thoiGian = thoiGian;
        this.gia = gia;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
