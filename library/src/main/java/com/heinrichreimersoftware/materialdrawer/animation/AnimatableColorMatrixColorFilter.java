package com.heinrichreimersoftware.materialdrawer.animation;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class AnimatableColorMatrixColorFilter {
    private ColorMatrixColorFilter mFilter;

    public AnimatableColorMatrixColorFilter(ColorMatrix matrix) {
        setColorMatrix(matrix);
    }

    public ColorMatrixColorFilter getColorFilter() {
        return mFilter;
    }

    public void setColorMatrix(ColorMatrix matrix) {
        mFilter = new ColorMatrixColorFilter(matrix);
    }
}