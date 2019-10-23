package com.example.rentacar.user.model;

public class CarOrder {
    private String finishDay, startDay, price, idClient;

    public CarOrder() {
    }

    public CarOrder(String finishDay, String startDay, String price, String idClient) {
        this.finishDay = finishDay;
        this.startDay = startDay;
        this.price = price;
        this.idClient = idClient;
    }

    public String getFinishDay() {
        return finishDay;
    }

    public void setFinishDay(String finishDay) {
        this.finishDay = finishDay;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}
