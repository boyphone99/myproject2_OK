package com.example.myproject2.Home;

/**
 * Created by Belal on 10/18/2017.
 */

public class Product {
    private int product_id;
    private String product_name;
    private double price_low;
    private double price_high;
    private String unit;
    private String product_img;

    public Product(int product_id, String product_name, double price_low, double price_high, String unit, String product_img) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price_low = price_low;
        this.price_high = price_high;
        this.unit = unit;
        this.product_img = product_img;
    }

    public int getId() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public double getPrice_low() {
        return price_low;
    }

    public double getPrice_high() {
        return price_high;
    }

    public String getUnit() {
        return unit;
    }

    public String getProduct_img() {
        return product_img;
    }
}
