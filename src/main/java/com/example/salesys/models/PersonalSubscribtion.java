package com.example.salesys.models;

public class PersonalSubscribtion {
    private int id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String provider;
    private int price;
    private double dataAmount;

    public PersonalSubscribtion(int id, String first_name, String last_name, String phone_number, String provider, int pris, double amount) {
        this.id=id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.provider = provider;
        this.price = pris;
        this.dataAmount = amount;
    }

    public PersonalSubscribtion(String phone_number, String provider, int pris, double amount) {
        this.first_name=null;
        this.last_name=null;
        this.phone_number = phone_number;
        this.provider = provider;
        this.price = pris;
        this.dataAmount = amount;
    }
    public PersonalSubscribtion(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDataAmount() {
        return dataAmount;
    }

    public void setDataAmount(double dataAmount) {
        this.dataAmount = dataAmount;
    }
}
