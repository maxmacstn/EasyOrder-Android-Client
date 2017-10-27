package com.example.magiapp.easyorder;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * Created by MaxMac on 26-Oct-17.
 */

public class NumberDialog extends DialogFragment {
    private static final String
            ARG_numDials = "numDials";
    private static final String
            ARG_initValue = "initValue";
    private int numDials;
    private int currentValue;
    private NumberPicker[] numPickers;
    private OnNumberDialogDoneListener mListener;

    public static NumberDialog newInstance(
            int numDials, int initValue) {
        NumberDialog numdialog = new NumberDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_numDials, numDials);
        args.putInt(ARG_initValue, initValue);
        numdialog.setArguments(args);
        return numdialog;
    }

    public NumberDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numDials = getArguments().getInt(ARG_numDials);
            currentValue =
                    getArguments().getInt(ARG_initValue);
            numPickers = new NumberPicker[numDials];}
        if (savedInstanceState!=null){
            currentValue=savedInstanceState.
                    getInt("CurrentValue");}
    }

    private int getDigit(int d, int i) {
        String temp = Integer.toString(d);
        if (temp.length() <= i) return 0;
        int r = Character.getNumericValue(
                temp.charAt(temp.length() - i - 1));
        return r;
    }

    private int getValue(){
        int value=0;
        int mult=1;
        for(int i=0;i<numDials;i++){
            value+=numPickers[i].getValue()*mult;
            mult*=10;
        }
        return value;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        LinearLayout linLayoutH =
                new LinearLayout(getActivity());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,            LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutH.setLayoutParams(params);
        for (int i = numDials - 1; i >= 0; i--) {
            numPickers[i] = new NumberPicker(getActivity());
            numPickers[i].setMaxValue(9);
            numPickers[i].setMinValue(0);
            numPickers[i].setValue(
                    getDigit(currentValue, i));
            linLayoutH.addView(numPickers[i]);
        }
        LinearLayout linLayoutV =
                new LinearLayout(getActivity());
        linLayoutV.setOrientation(LinearLayout.VERTICAL);
        linLayoutV.addView(linLayoutH);
        Button okButton = new Button(getActivity());
        okButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        currentValue=getValue();
                        if (mListener != null) {
                            mListener.onDone(currentValue);
                        };
                        dismiss();
                    }
                });
        params.gravity = Gravity.CENTER_HORIZONTAL;
        okButton.setLayoutParams(params);
        okButton.setText("Done");
        linLayoutV.addView(okButton);
        return linLayoutV;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentValue",getValue());
    }

    public interface OnNumberDialogDoneListener {
        public void onDone(int value);
    }
}
