package com.example.magiapp.easyorder.data;

import java.util.Comparator;

/**
 * Comparator for foodItem Object
 * Created by MaxMac on 12-Nov-17.
 */
public class FoodItemComparator {
    public static Comparator<FoodItem> typeComparator = new Comparator<FoodItem>() {
        @Override
        public int compare(FoodItem o1, FoodItem o2) {
            return o1.getType() - o2.getType();
        }
    };

    public static Comparator<FoodItem> nameComparator = new Comparator<FoodItem>() {
        @Override
        public int compare(FoodItem o1, FoodItem o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static Comparator<FoodItem> idComparator = new Comparator<FoodItem>() {
        @Override
        public int compare(FoodItem o1, FoodItem o2) {
            return o1.getID() - (o2.getID());
        }
    };

}
