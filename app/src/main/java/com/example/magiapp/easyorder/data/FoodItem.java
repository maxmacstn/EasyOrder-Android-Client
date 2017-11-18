package com.example.magiapp.easyorder.data;

import android.util.Log;

import com.example.magiapp.easyorder.R;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Food Item object for storing single food item.
 * Created by MaxMac on 27-Oct-17.
 */


public class FoodItem implements Serializable {
    private double price;
    private String name;
    private int quantity = 0;
    private boolean isAvailable = true;
    private int id;
    private FoodType foodType;

    public FoodItem(int id, String name, double price, FoodType type) {
        this.price = price;
        this.name = name;
        this.foodType = type;
        this.id = id;
    }


    private int getTypeByString(String type) {
        switch (type) {
            case "MAIN_DISH":
                return 1;
            case "SOUP":
                return 2;
            case "BEVERAGE":
                return 3;
            case "DESSERT":
                return 4;
            default:
                return 0;

        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void setFoodType(FoodType type) {
        foodType = type;
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
        return foodType.ordinal() + 1;
    }

    public FoodType getEnumType() {
        return foodType;
    }

    /**
     * Get logo location for using as symbol in TableView
     *
     * @return resource data of type symbol.
     */
    public int getLogo() {
        switch (foodType) {
            case MAIN_DISH:
                return R.drawable.rice;
            case SOUP:
                return R.drawable.soup;
            case BEVERAGE:
                return R.drawable.beverage;
            case DESSERT:
                return R.drawable.dessert;
        }
        return R.mipmap.ic_launcher;
    }


    public int getID() {
        return id;
    }


    @Override
    public String toString() {
        return String.format("\nType:%s id:%02d price:%-15s qty:%.2f฿   qty= %d", foodType.toString(), id, name, price, quantity);
    }
}
