package com.example.doanandroid.model;

public class ThongSoCauThu {

    public String tenct;
    public int soao;
    public String vitri;
    public int sotran;
    public int banthang;
    public int thevang;
    public int thedo;

    public ThongSoCauThu(String tenct, int soao, String vitri, int sotran, int banthang, int thevang, int thedo) {
        this.tenct = tenct;
        this.soao = soao;
        this.vitri = vitri;
        this.sotran = sotran;
        this.banthang = banthang;
        this.thevang = thevang;
        this.thedo = thedo;
    }

    public ThongSoCauThu(String tenct, int soao) {
        this.tenct = tenct;
        this.soao = soao;
    }

    public String getTenct() {
        return tenct;
    }

    public void setTenct(String tenct) {
        this.tenct = tenct;
    }

    public int getSoao() {
        return soao;
    }

    public void setSoao(int soao) {
        this.soao = soao;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public int getSotran() {
        return sotran;
    }

    public void setSotran(int sotran) {
        this.sotran = sotran;
    }

    public int getBanthang() {
        return banthang;
    }

    public void setBanthang(int banthang) {
        this.banthang = banthang;
    }

    public int getThevang() {
        return thevang;
    }

    public void setThevang(int thevang) {
        this.thevang = thevang;
    }

    public int getThedo() {
        return thedo;
    }

    public void setThedo(int thedo) {
        this.thedo = thedo;
    }
}
