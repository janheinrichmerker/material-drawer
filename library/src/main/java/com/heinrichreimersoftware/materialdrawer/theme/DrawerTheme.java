/*
 * Copyright 2015 Heinrich Reimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heinrichreimersoftware.materialdrawer.theme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.materialdrawer.R;

public class DrawerTheme {

    private Context context;


    /**
     * Used for the drawer / drawer item / drawer profile list background
     */
    private int backgroundColor;

    /**
     * Used for the drawer / drawer item / drawer profile list text
     */
    private int textColorPrimary;

    /**
     * Used for the drawer / drawer item / drawer profile list secondary text
     */
    private int textColorSecondary;

    /**
     * Used for the drawer header text
     */
    private int textColorPrimaryInverse;

    /**
     * Used for the drawer header secondary text
     */
    private int textColorSecondaryInverse;

    /**
     * Used for the selected drawer item / drawer profile text and icon tinting
     */
    private int highlightColor;


    public DrawerTheme(@NonNull DrawerTheme theme) {
        context = theme.getContext();

        backgroundColor = theme.getBackgroundColor();
        textColorPrimary = theme.getTextColorPrimary();
        textColorSecondary = theme.getTextColorSecondary();
        textColorPrimaryInverse = theme.getTextColorPrimaryInverse();
        textColorSecondaryInverse = theme.getTextColorSecondaryInverse();
        highlightColor = theme.getHighlightColor();
    }

    public DrawerTheme(Context context) {
        this.context = context;

        int[] attrs = {android.R.attr.windowBackground, android.R.attr.textColorPrimary, android.R.attr.textColorSecondary, android.R.attr.textColorPrimaryInverse, android.R.attr.textColorSecondaryInverse, R.attr.colorAccent};
        TypedArray array = context.obtainStyledAttributes(attrs);

        backgroundColor = array.getColor(0, 0);
        textColorPrimary = array.getColor(1, 0);
        textColorSecondary = array.getColor(2, 0);
        textColorPrimaryInverse = array.getColor(3, 0);
        textColorSecondaryInverse = array.getColor(4, 0);
        highlightColor = array.getColor(5, 0);

        array.recycle();
    }

    public boolean isLightTheme() {
        return isLightColor(backgroundColor);
    }

    private boolean isLightColor(int color) {
        return (1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255) < 0.5;
    }

    public Context getContext() {
        return context;
    }

    public DrawerTheme setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DrawerTheme setBackgroundColorRes(@ColorRes int backgroundColorRes) {
        this.backgroundColor = context.getResources().getColor(backgroundColorRes);
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public DrawerTheme setTextColorPrimary(int textColorPrimary) {
        this.textColorPrimary = textColorPrimary;
        return this;
    }

    public DrawerTheme setTextColorPrimaryRes(@ColorRes int textColorPrimaryRes) {
        this.textColorPrimary = context.getResources().getColor(textColorPrimaryRes);
        return this;
    }

    public int getTextColorPrimary() {
        return textColorPrimary;
    }

    public DrawerTheme setTextColorSecondary(int textColorSecondary) {
        this.textColorSecondary = textColorSecondary;
        return this;
    }

    public DrawerTheme setTextColorSecondaryRes(@ColorRes int textColorSecondaryRes) {
        this.textColorSecondary = context.getResources().getColor(textColorSecondaryRes);
        return this;
    }

    public int getTextColorSecondary() {
        return textColorSecondary;
    }

    public DrawerTheme setTextColorPrimaryInverse(int textColorPrimaryInverse) {
        this.textColorPrimaryInverse = textColorPrimaryInverse;
        return this;
    }

    public DrawerTheme setTextColorPrimaryInverseRes(@ColorRes int textColorPrimaryInverseRes) {
        this.textColorPrimaryInverse = context.getResources().getColor(textColorPrimaryInverseRes);
        return this;
    }

    public int getTextColorPrimaryInverse() {
        return textColorPrimaryInverse;
    }

    public DrawerTheme setTextColorSecondaryInverse(int textColorSecondaryInverse) {
        this.textColorSecondaryInverse = textColorSecondaryInverse;
        return this;
    }

    public DrawerTheme setTextColorSecondaryInverseRes(@ColorRes int textColorSecondaryInverseRes) {
        this.textColorSecondaryInverse = context.getResources().getColor(textColorSecondaryInverseRes);
        return this;
    }

    public int getTextColorSecondaryInverse() {
        return textColorSecondaryInverse;
    }

    public DrawerTheme setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public DrawerTheme setHighlightColorRes(@ColorRes int highlightColorRes) {
        this.highlightColor = context.getResources().getColor(highlightColorRes);
        return this;
    }

    public int getHighlightColor() {
        return highlightColor;
    }
}