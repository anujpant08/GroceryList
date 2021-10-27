package com.minimaldev4playstore.grocerylist;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class GroceryItem {
    private List<Fruit> fruitList;
    private List<Vegetable> vegetableList;
    private List<Spice> spiceList;
    private List<Others> othersList;
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

    public List<Others> getOthersList() {
        return othersList;
    }

    public void setOthersList(List<Others> othersList) {
        this.othersList = othersList;
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
                ", othersList=" + othersList +
                ", title='" + title + '\'' +
                '}';
    }
}
