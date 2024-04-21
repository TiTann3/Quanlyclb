package com.example.doanandroid.model;

public class DanhSachCLB {
    public int idclb;
    public String tenclb;

    public byte[] anh;

    public DanhSachCLB(int idclb, String tenclb, byte[] anh) {
        this.idclb = idclb;
        this.tenclb = tenclb;
        this.anh = anh;
    }

    public DanhSachCLB(int idclb, String tenclb) {
        this.idclb = idclb;
        this.tenclb = tenclb;
    }

    @Override
    public String toString() {
        return tenclb;
    }

    public int getIdclb() {
        return idclb;
    }

    public void setIdclb(int idclb) {
        this.idclb = idclb;
    }

    public String getTenclb() {
        return tenclb;
    }

    public void setTenclb(String tenclb) {
        this.tenclb = tenclb;
    }
}
