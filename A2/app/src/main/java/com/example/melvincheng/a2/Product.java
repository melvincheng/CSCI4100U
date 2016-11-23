package com.example.melvincheng.a2;

public class Product {
    private long productId;
    private String name;
    private String description;
    private float price;

    public Product(long productId, String name, String description, float price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String toString() {
        return productId + " " + name + " " + description + " " + price;
    }
}
