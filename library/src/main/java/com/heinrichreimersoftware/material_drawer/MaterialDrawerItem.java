package com.heinrichreimersoftware.material_drawer;

import android.graphics.drawable.Drawable;

public class MaterialDrawerItem {
    public static final int SINGLE_LINE = 1;
    public static final int TWO_LINE = 2;
    public static final int THREE_LINE = 3;

    private Drawable mImage;
    private String mTextPrimary;
    private String mTextSecondary;
    private int mTextMode = -1;

    public void setImage(Drawable image) {
        mImage = image;
    }

    public Drawable getImage() {
        return mImage;
    }

    public boolean hasImage() {
        return mImage != null;
    }

    public void removeImage() {
        mImage = null;
    }


    public void setTextPrimary(String textPrimary) {
        mTextPrimary = textPrimary;
    }

    public String getTextPrimary() {
        return mTextPrimary;
    }

    public boolean hasTextPrimary() {
        return mTextPrimary != null && !mTextPrimary.equals("");
    }

    public void removeTextPrimary() {
        mTextPrimary = null;
    }


    public void setTextSecondary(String textSecondary) {
        mTextSecondary = textSecondary;
    }

    public String getTextSecondary() {
        return mTextSecondary;
    }

    public boolean hasTextSecondary() {
        return mTextSecondary != null && !mTextSecondary.equals("");
    }

    public void removeTextSecondary() {
        mTextSecondary = null;
    }


    public void setTextMode(int textMode) {
        mTextMode = textMode;
    }

    public int getTextMode() {
        return mTextMode;
    }

    public boolean hasTextMode() {
        return mTextMode > 0;
    }

    public void resetTextMode() {
        mTextMode = SINGLE_LINE;
    }
}
