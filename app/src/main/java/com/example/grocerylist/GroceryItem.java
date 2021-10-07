package com.example.grocerylist;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class GroceryItem {
    private List<Fruit> fruitList;
    private List<Vegetable> vegetableList;
    private List<Spice> spiceList;
    private List<Bread> breadList;
    private List<Dairy> dairyList;
    private String title;

    public List<Fruit> getFruitList() {
        return fruitList;
    }

    public void setFruitList(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }

    public List<Vegetable> getVegetableList() {
        return vegetableList;
    }

    public void setVegetableList(List<Vegetable> vegetableList) {
        this.vegetableList = vegetableList;
    }

    public List<Spice> getSpiceList() {
        return spiceList;
    }

    public void setSpiceList(List<Spice> spiceList) {
        this.spiceList = spiceList;
    }

    public List<Bread> getBreadList() {
        return breadList;
    }

    public void setBreadList(List<Bread> breadList) {
        this.breadList = breadList;
    }

    public List<Dairy> getDairyList() {
        return dairyList;
    }

    public void setDairyList(List<Dairy> dairyList) {
        this.dairyList = dairyList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object groceryItemObject) {
        if (this == groceryItemObject) return true;
        if (groceryItemObject == null || getClass() != groceryItemObject.getClass()) return false;
        GroceryItem groceryItem = (GroceryItem) groceryItemObject;
        return this.title.equals(groceryItem.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title);
    }

    @NonNull
    @Override
    public String toString() {
        return "GroceryItem{" +
                "fruitList=" + fruitList +
                ", vegetableList=" + vegetableList +
                ", spiceList=" + spiceList +
                ", breadList=" + breadList +
                ", dairyList=" + dairyList +
                ", title='" + title + '\'' +
                '}';
    }
}
