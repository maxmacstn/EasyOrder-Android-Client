package com.example.magiapp.easyorder;

/**
 * Created by MaxMac on 27-Oct-17.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;
//import de.codecrafters.tableviewexample.data.Car;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;


public class FoodItemTableDataAdapter extends LongPressAwareTableDataAdapter<FoodItem> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public FoodItemTableDataAdapter(final Context context, final List<FoodItem> data, final TableView<FoodItem> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final FoodItem foodItem = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderFoodTypeIcon(foodItem, parentView);
                break;
            case 1:
                renderedView = renderString(foodItem.getID());
                break;
            case 2:
                renderedView = renderString(foodItem.getName());
                break;
            case 3:
                renderedView = renderPrice(foodItem);
                break;
            case 4:
                renderedView = renderString(foodItem.getQuantity()+"");
                break;
        }

        return renderedView;
    }



    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        /*
        final Car car = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableCatName(car);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
        */
        return null;

    }


    /*

    private View renderEditableCatName(final Car car) {
        final EditText editText = new EditText(getContext());
        editText.setText(car.getName());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new CarNameUpdater(car));
        return editText;
    }

    */

    private View renderPrice(final FoodItem foodItem) {
        final String priceString = String.format("%.2f฿",foodItem.getPrice());

        final TextView textView = new TextView(getContext());
        textView.setText(priceString);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);


        return textView;
    }




    private View renderFoodTypeIcon(final FoodItem foodItem, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(foodItem.getLogo());
        return view;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    /*

    private static class CarNameUpdater implements TextWatcher {

        private Car carToUpdate;

        public CarNameUpdater(Car carToUpdate) {
            this.carToUpdate = carToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            carToUpdate.setName(s.toString());
        }
    }
    */

}