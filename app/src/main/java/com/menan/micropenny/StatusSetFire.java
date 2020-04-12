package com.menan.micropenny;

public class StatusSetFire {

    String date;
    int impression,click,status;
    double balance,refBalance;


    public StatusSetFire() {
    }

    public double getRefBalance() {
        return refBalance;
    }

    public void setRefBalance(double refBalance) {
        this.refBalance = refBalance;
    }

    public String getDate() {
        return date;
    }


    public int getImpression() {
        return impression;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImpression(int impression) {
        this.impression = impression;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public double getBalance() {
        return balance;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClick() {
        return click;
    }


    public int getStatus() {
        return status;
    }


    public StatusSetFire(String date, int impression, int click, int status,double balance,double refBalance) {
        this.date = date;
        this.impression = impression;
        this.click = click;
        this.status = status;
        this.balance=balance;
        this.refBalance=refBalance;
    }
}
