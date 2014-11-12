package com.heinrichreimersoftware.material_drawer;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

public class MaterialDrawerLayout extends DrawerLayout{
    public MaterialDrawerLayout(Context context) {
        this(context, null);
    }

    public MaterialDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        for(int i = 1; i < getChildCount(); i++){
            removeViewAt(i);
        }
        inflate(getContext(), R.layout.drawer, this);
    }
}
