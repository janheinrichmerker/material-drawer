package com.heinrichreimersoftware.materialdrawer.animation;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class AnimatableColorMatrixColorFilter {
    private ColorMatrixColorFilter mFilter;
    private ColorMatrix mMatrix;

    public AnimatableColorMatrixColorFilter(ColorMatrix matrix) {
        setColorMatrix(matrix);
    }

    public ColorMatrixColorFilter getColorFilter() {
        return mFilter;
    }

    public void setColorMatrix(ColorMatrix matrix) {
        mMatrix = matrix;
        mFilter = new ColorMatrixColorFilter(matrix);
    }

    public ColorMatrix getColorMatrix() {
        return mMatrix;
    }
}