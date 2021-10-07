package com.example.grocerylist;

import androidx.annotation.NonNull;

public class Bread implements  ItemCategoryInterface{
    private String name = "";
    private String weight = "";
    private String quantity = "";

    public Bread(String name) {
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
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String getWeight() {
        return null;
    }

    @Override
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getQuantity() {
        return null;
    }


    @Override
    public String getFamily() {
        return "Breads";
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