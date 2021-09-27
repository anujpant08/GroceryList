package com.example.grocerylist;

import androidx.annotation.NonNull;

public class Dairy implements  ItemCategoryInterface{
    private String name = "";
    private Double weight = 0.0;
    private Double quantity = 0.0;

    public Dairy(String name) {
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public Double getWeight() {
        return this.weight;
    }

    @Override
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public Double getQuantity() {
        return this.quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return "Bread{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}