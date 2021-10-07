package com.example.grocerylist;

public interface ItemCategoryInterface {
    String name = "";
    String weight = "";
    String quantity = "";
    String family = "";
    public void setName(String name);
    public String getName();
    public void setWeight(String weight);
    public String getWeight();
    public void setQuantity(String quantity);
    public String getQuantity();
    public String getFamily();
}
