package com.example.doanandroid.model;

import java.util.Date;

public class ChiTietTranDau {

    public int idtrandau;
    public int goalhome;
    public int goalaway;
    public String ngaythidau;
    public String tenclb_Home;
    public String tenclb_Away;

    public ChiTietTranDau(int idtrandau,  String tenclb_Home, String tenclb_Away, int goalhome, int goalaway, String ngaythidau) {
        this.idtrandau = idtrandau;
        this.goalhome = goalhome;
        this.goalaway = goalaway;
        this.ngaythidau = ngaythidau;
        this.tenclb_Home = tenclb_Home;
        this.tenclb_Away = tenclb_Away;
    }

    public int getIdtrandau() {
        return idtrandau;
    }

    public void setIdtrandau(int idtrandau) {
        this.idtrandau = idtrandau;
    }

    public int getGoalhome() {
        return goalhome;
    }

    public void setGoalhome(int goalhome) {
        this.goalhome = goalhome;
    }

    public int getGoalaway() {
        return goalaway;
    }

    public void setGoalaway(int goalaway) {
        this.goalaway = goalaway;
    }

    public String getNgaythidau() {
        return ngaythidau;
    }

    public void setNgaythidau(String ngaythidau) {
        this.ngaythidau = ngaythidau;
    }

    public String getTenclb_Home() {
        return tenclb_Home;
    }

    public void setTenclb_Home(String tenclb_Home) {
        this.tenclb_Home = tenclb_Home;
    }

    public String getTenclb_Away() {
        return tenclb_Away;
    }

    public void setTenclb_Away(String tenclb_Away) {
        this.tenclb_Away = tenclb_Away;
    }
}
