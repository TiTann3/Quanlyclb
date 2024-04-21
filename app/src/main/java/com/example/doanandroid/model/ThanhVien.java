package com.example.doanandroid.model;

public class ThanhVien {
    public int matv;
    public String hoten;
    public String ngaysinh;
    public String chucvu;
    public String quoctich;
    public byte[] anh;

    public ThanhVien(int matv, String hoten, String ngaysinh, String chucvu, String quoctich, byte[] anh) {
        this.matv = matv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.chucvu = chucvu;
        this.quoctich = quoctich;
        this.anh = anh;
    }

    public ThanhVien(int matv, String hoten) {
        this.matv = matv;
        this.hoten = hoten;
    }
    public String toString() {
        return hoten;
    }

    public int getMatv() {
        return matv;
    }

    public void setMatv(int matv) {
        this.matv = matv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }
}
