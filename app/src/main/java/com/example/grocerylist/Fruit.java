package com.example.grocerylist;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Fruit implements  ItemCategoryInterface{
    private String name;
    private String weight = "";
    private String quantity = "";

    public Fruit(String name) {
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
        return "Fruits";
    }

    @Override
    public boolean equals(Object fruitObject) {
        if (this == fruitObject) return true;
        if (fruitObject == null || getClass() != fruitObject.getClass()) return false;
        Fruit fruit = (Fruit) fruitObject;
        return this.name.equals(fruit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @NonNull
    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}
