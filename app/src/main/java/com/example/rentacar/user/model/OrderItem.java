package com.example.rentacar.user.model;

public class OrderItem {

    String carTitle, date, firstDay, idCar, lastDay, price, time;

    public OrderItem(){

    }

    public OrderItem(String carTitle, String date, String firstDay, String idCar, String lastDay, String price, String time) {
        this.carTitle = carTitle;
        this.date = date;
        this.firstDay = firstDay;
        this.idCar = idCar;
        this.lastDay = lastDay;
        this.price = price;
        this.time = time;
    }

    public String getCarTitle() {
        return carTitle;
    }

    public void setCarTitle(String carTitle) {
        this.carTitle = carTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getIdCar() {
        return idCar;
    }

    public void setIdCar(String idCar) {
        this.idCar = idCar;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
