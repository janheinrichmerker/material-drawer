/*
 * Copyright 2014 Heinrich Reimer
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

package com.heinrichreimersoftware.materialdrawer.structure;

import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

/**
 * Object to be used with {@link com.heinrichreimersoftware.materialdrawer.DrawerAdapter} and {@link com.heinrichreimersoftware.materialdrawer.DrawerView} to display a drawer item.
 * Can hold an image, a primary text, a secondary text and a listener.
 */
public class DrawerItem {
    public static final int ICON = 1;
    public static final int AVATAR = 2;

    private boolean mIsDivider = false;

    private int mItemId;

    private Drawable mImage;
    private int mImageMode = -1;

    private String mTextPrimary;
    private String mTextSecondary;
    private int mTextMode = -1;

    private OnItemClickListener mOnClickListener;

    private ArrayAdapter<DrawerItem> mAdapter;

    public DrawerItem setIsDivider(boolean isDivider) {
        mIsDivider = isDivider;
        notifyDataChanged();
        return this;
    }

    public boolean isDivider() {
        return mIsDivider;
    }

    /**
     * Sets an image with a given image mode to the drawer item
     *
     * @param image     Image to set
     * @param imageMode Image mode to set
     */
    public DrawerItem setImage(Drawable image, int imageMode) {
        mImage = image;
        setImageMode(imageMode);
        notifyDataChanged();
        return this;
    }

    /**
     * Sets an image to the drawer item
     *
     * @param image Image to set
     */
    public DrawerItem setImage(Drawable image) {
        setImage(image, ICON);
        notifyDataChanged();
        return this;
    }

    public DrawerItem setItemId(int itemId) {
        mItemId = itemId;
        return this;
    }

    public int getItemId() {
        return mItemId;
    }

    /**
     * Gets the image of the drawer item
     *
     * @return Image of the drawer item
     */
    public Drawable getImage() {
        return mImage;
    }

    /**
     * Gets whether the drawer item has an image set to it
     *
     * @return True if the drawer item has an image set to it, false otherwise.
     */
    public boolean hasImage() {
        return mImage != null;
    }

    /**
     * Removes the image from the drawer item
     */
    public DrawerItem removeImage() {
        mImage = null;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets an image mode to the drawer item
     *
     * @param imageMode Image mode to set
     */
    public DrawerItem setImageMode(int imageMode) {
        if (imageMode != ICON && imageMode != AVATAR) {
            throw new IllegalArgumentException("Image mode must be either ICON or AVATAR.");
        }
        mImageMode = imageMode;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the image mode of the drawer item
     *
     * @return Image mode of the drawer item
     */
    public int getImageMode() {
        return mImageMode;
    }

    /**
     * Gets whether the drawer item has an image mode set to it
     *
     * @return True if the drawer item has an image mode set to it, false otherwise.
     */
    public boolean hasImageMode() {
        return mImageMode > 0;
    }

    /**
     * Resets the image mode from the drawer item
     */
    public DrawerItem resetImageMode() {
        mImageMode = ICON;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets a primary text to the drawer item
     *
     * @param textPrimary Primary text to set
     */
    public DrawerItem setTextPrimary(String textPrimary) {
        mTextPrimary = textPrimary;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the primary text of the drawer item
     *
     * @return Primary text of the drawer item
     */
    public String getTextPrimary() {
        return mTextPrimary;
    }

    /**
     * Gets whether the drawer item has a primary text set to it
     *
     * @return True if the drawer item has a primary text set to it, false otherwise.
     */
    public boolean hasTextPrimary() {
        return mTextPrimary != null && !mTextPrimary.equals("");
    }

    /**
     * Removes the primary text from the drawer item
     */
    public DrawerItem removeTextPrimary() {
        mTextPrimary = null;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets a secondary text to the drawer item
     *
     * @param textSecondary Secondary text to set
     */
    public DrawerItem setTextSecondary(String textSecondary) {
        mTextSecondary = textSecondary;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the secondary text of the drawer item
     *
     * @return Secondary text of the drawer item
     */
    public String getTextSecondary() {
        return mTextSecondary;
    }

    /**
     * Gets whether the drawer item has a secondary text set to it
     *
     * @return True if the drawer item has a secondary text set to it, false otherwise.
     */
    public boolean hasTextSecondary() {
        return mTextSecondary != null && !mTextSecondary.equals("");
    }

    /**
     * Removes the secondary text from the drawer item
     */
    public DrawerItem removeTextSecondary() {
        mTextSecondary = null;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets a click listener to the drawer item
     *
     * @param listener Listener to set
     */
    public DrawerItem setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the click listener of the drawer item
     *
     * @return Click listener of the drawer item
     */
    public OnItemClickListener getOnItemClickListener() {
        return mOnClickListener;
    }

    /**
     * Gets whether the drawer item has a click listener set to it
     *
     * @return True if the drawer item has a click listener set to it, false otherwise.
     */
    public boolean hasOnItemClickListener() {
        return mOnClickListener != null;
    }

    /**
     * Removes the click listener from the drawer item
     */
    public DrawerItem removeOnItemClickListener() {
        mOnClickListener = null;
        notifyDataChanged();
        return this;
    }

    /**
     * Attaches the drawer item to an adapter
     *
     * @param adapter Adapter to attach to
     */
    public DrawerItem attachTo(ArrayAdapter<DrawerItem> adapter) {
        mAdapter = adapter;
        notifyDataChanged();
        return this;
    }

    /**
     * Detaches the drawer item from its adapter
     */
    public DrawerItem detach() {
        mAdapter = null;
        return this;
    }

    protected void notifyDataChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onClick(DrawerItem item, int position);
    }
}
