package com.Sadraafzar.Messenger.Model;

public class messagecompaneyModel {
    public String titel;
    public String subtitel;

    public messagecompaneyModel() {
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getSubtitel() {
        return subtitel;
    }

    public void setSubtitel(String subtitel) {
        this.subtitel = subtitel;
    }

    public messagecompaneyModel(String titel, String subtitel) {
        this.titel = titel;
        this.subtitel = subtitel;
    }
}
