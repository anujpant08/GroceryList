package com.example.grocerylist;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Vegetable implements  ItemCategoryInterface{
    private String name;
    private String weight = "";
    private String quantity = "";

    public Vegetable(String name) {
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
        return this.weight;
    }

    @Override
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getQuantity() {
        return this.quantity;
    }


    @Override
    public String getFamily() {
        return "Vegetables";
    }

    @Override
    public boolean equals(Object vegetableObject) {
        if (this == vegetableObject) return true;
        if (vegetableObject == null || getClass() != vegetableObject.getClass()) return false;
        Vegetable vegetable = (Vegetable) vegetableObject;
        return this.name.equals(vegetable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }


    @NonNull
    @Override
    public String toString() {
        return "Vegetable{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}
