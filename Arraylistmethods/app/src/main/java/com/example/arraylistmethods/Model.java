package com.example.arraylistmethods;

public class Model {
    @Override
    public String toString() {
        return "Model{" +
                "category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", description='" + description + '\'' +
                ", country='" + country + '\'' +
                ", currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    private String category;
    private String subcategory;
    private String description;
    private String country;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String currency;

    public Model() {
    }

    private String amount;

    public Model(String category, String subcategory, String description, String country, String currency, String amount) {
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.country = country;
        this.currency = currency;
        this.amount = amount;
    }
}
