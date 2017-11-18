package com.example.magiapp.easyorder.data;

/**
 * Created by MaxMac on 27-Oct-17.
 */


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magiapp.easyorder.R;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;
//import de.codecrafters.tableviewexample.data.Car;

import java.text.NumberFormat;
import java.util.List;

import static java.lang.String.format;

/**
 * Adaptor for FoodItem object for using with TableView
 */
public class FoodItemTableDataAdapter extends TableDataAdapter<FoodItem> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private static final String[] TABLE_HEADERS = {"Type","ID", "Name", "Price","Qty."};


    public FoodItemTableDataAdapter(final Context context, final List<FoodItem> data, final TableView<FoodItem> tableView) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final FoodItem foodItem = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderFoodTypeIcon(foodItem, parentView);
                break;
            case 1:
                renderedView = renderString(String.format("%02d", foodItem.getID()));
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

    public String[] getHeaderData(){
        return TABLE_HEADERS;

    }


}