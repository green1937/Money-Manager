package com.example.moneymanager;

import io.realm.RealmObject;

public class Period extends RealmObject {
    String dateStart;
    String dateEnd;
    int budget;
    int priceFood;
    int priceTransport;
    int priceEntertainment;
    int priceWork;
    int priceStudy;
    int priceHealth;
    int priceOther;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(int priceFood) {
        this.priceFood = priceFood;
    }

    public int getPriceTransport() {
        return priceTransport;
    }

    public void setPriceTransport(int priceTransport) {
        this.priceTransport = priceTransport;
    }

    public int getPriceEntertainment() {
        return priceEntertainment;
    }

    public void setPriceEntertainment(int priceEntertainment) {
        this.priceEntertainment = priceEntertainment;
    }

    public int getPriceWork() {
        return priceWork;
    }

    public void setPriceWork(int priceWork) {
        this.priceWork = priceWork;
    }

    public int getPriceStudy() {
        return priceStudy;
    }

    public void setPriceStudy(int priceStudy) {
        this.priceStudy = priceStudy;
    }

    public int getPriceHealth() {
        return priceHealth;
    }

    public void setPriceHealth(int priceHealth) {
        this.priceHealth = priceHealth;
    }

    public int getPriceOther() {
        return priceOther;
    }

    public void setPriceOther(int priceOther) {
        this.priceOther = priceOther;
    }

}
