package com.example.helloandroid;

/**
 * A Model class that holds the name of a country, its currency type and image
 * of the country's flag
 */
public class Model {
    private String country, currency;
    private int image;

    public Model(String country, String currency, int image){
        this.country = country;
        this.currency = currency;
        this.image = image;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
