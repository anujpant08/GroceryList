package com.minimaldev4playstore.grocerylist;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Spice implements  ItemCategoryInterface{
    private String name = "";
    private String weight = "";
    private String quantity = "";

    public Spice(String name) {
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
        return "Spices";
    }

    @Override
    public boolean equals(Object spiceObject) {
        if (this == spiceObject) return true;
        if (spiceObject == null || getClass() != spiceObject.getClass()) return false;
        Spice spice = (Spice) spiceObject;
        return this.name.equals(spice.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @NonNull
    @Override
    public String toString() {
        return "Spice{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}
