package com.appndroid.crick20.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;

public class MyViewFlipper extends ViewFlipper {

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try{
            super.onDetachedFromWindow();
        }catch(Exception e) {
			// Log.d("MyViewFlipper","Stopped a viewflipper crash");
            stopFlipping();
        }
    }
}
