package com.example.grocerylist;

public interface ItemCategoryInterface {
    String name = "";
    Double weight = 0.0;
    Double quantity = 0.0;
    String family = "";
    public void setName(String name);
    public String getName();
    public void setWeight(Double weight);
    public Double getWeight();
    public void setQuantity(Double quantity);
    public Double getQuantity();
    public String getFamily();
}
