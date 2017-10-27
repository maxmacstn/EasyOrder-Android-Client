package com.example.magiapp.easyorder;

/**
 * Created by MaxMac on 27-Oct-17.
 */

public class FoodItem {
    private double price;
    private String name;
    private int quantity = 0;
    private boolean isAvailable = true;
    private String id;
    private int type;
    public static final int MAIN_DISH = 1;
    public static final int SOUP = 2;
    public static final int BEVERAGE = 3;
    public static final int DESSERT = 4;

    public FoodItem(String id, String name, double price, int type) {
        this.price = price;
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getType() {
        return type;
    }

    public int getLogo() {
        switch (type){
            case 1:
                return R.drawable.rice;
            case 2:
                return R.drawable.soup;
            case 3:
                return R.drawable.beverage;
            case 4:
                return R.drawable.dessert;
        }
        return R.mipmap.ic_launcher;
    }

    public String getID(){return id;}
}
