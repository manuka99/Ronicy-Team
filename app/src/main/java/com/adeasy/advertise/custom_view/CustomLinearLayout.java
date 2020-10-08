package com.adeasy.advertise.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
@CoordinatorLayout.DefaultBehavior(MoveUpwardBehavior.class)
public class CustomLinearLayout extends LinearLayout {

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}