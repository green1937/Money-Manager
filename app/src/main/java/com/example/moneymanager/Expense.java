package com.example.moneymanager;

import io.realm.RealmObject;


public class Expense extends RealmObject {
    int price;          // Сумма расхода
    String currency;    // Валюта расхода
    String category;    // Категория расхода
    String description;  // Описание расхода
    String dateExp;      // Дата, к которой привязан расход
    String Photo_uri;     // Фото чека


    public int getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDateExp() {
        return dateExp;
    }

    public String getPhoto_uri() {
        return Photo_uri;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateExp(String dateExp) {
        this.dateExp = dateExp;
    }

    public void setPhoto_uri(String photo_uri) {
        Photo_uri = photo_uri;
    }
}