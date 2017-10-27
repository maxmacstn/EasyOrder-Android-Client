package com.example.magiapp.easyorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaxMac on 27-Oct-17.
 */

public final class MenuMaker {
    public static List<FoodItem> createFoodMenuList() {

        List<FoodItem> foodMenu = new ArrayList<>();

        foodMenu.add(new FoodItem("001", "Fried Rice", 25, FoodItem.MAIN_DISH));
        foodMenu.add(new FoodItem("002", "Omlette", 35.5, FoodItem.MAIN_DISH));
        foodMenu.add(new FoodItem("003", "Green Tea", 25, FoodItem.BEVERAGE));
        foodMenu.add(new FoodItem("004", "Pudding", 25, FoodItem.DESSERT));
        foodMenu.add(new FoodItem("005", "Miso Soup", 25, FoodItem.SOUP));

        return foodMenu;
    }

}
