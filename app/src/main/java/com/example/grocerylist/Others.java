package com.example.grocerylist;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Others implements  ItemCategoryInterface{
    private String name;
    private String weight = "";
    private String quantity = "";

    public Others(String name) {
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
        return "Others";
    }

    @Override
    public boolean equals(Object othersObject) {
        if (this == othersObject) return true;
        if (othersObject == null || getClass() != othersObject.getClass()) return false;
        Others other = (Others) othersObject;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }


    @NonNull
    @Override
    public String toString() {
        return "Others{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}
