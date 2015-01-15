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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.heinrichreimersoftware.materialdrawer.DrawerView;

/**
 * Object to be used with {@link com.heinrichreimersoftware.materialdrawer.DrawerView} to display a profile in the md_drawer_view.
 * Can hold an image, a primary text, a secondary text and a listener.
 */
public class DrawerProfile {
    private Drawable mAvatar;
    private Drawable mBackground;
    private String mName;
    private String mDescription;

    private OnProfileClickListener mOnClickListener;
    private DrawerView mDrawerView;


    /**
     * Sets an avatar image to the md_drawer_view profile
     *
     * @param avatar Avatar image to set
     */
    public DrawerProfile setAvatar(Drawable avatar) {
        mAvatar = avatar;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets an avatar image to the md_drawer_view profile
     *
     * @param avatar Avatar image to set
     */
    public DrawerProfile setAvatar(Context context, Bitmap avatar) {
        mAvatar = new BitmapDrawable(context.getResources(), avatar);
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the avatar image of the md_drawer_view profile
     *
     * @return Avatar image of the md_drawer_view profile
     */
    public Drawable getAvatar() {
        return mAvatar;
    }

    /**
     * Gets whether the md_drawer_view profile has an avatar image set to it
     *
     * @return True if the md_drawer_view profile has an avatar image set to it, false otherwise.
     */
    public boolean hasAvatar() {
        return mAvatar != null;
    }

    /**
     * Removes the avatar image from the md_drawer_view profile
     */
    public DrawerProfile removeAvatar() {
        mAvatar = null;
        notifyDataChanged();
        return this;
    }


    /**
     * Sets a background to the md_drawer_view profile
     *
     * @param background Background to set
     */
    public DrawerProfile setBackground(Drawable background) {
        mBackground = background;
        notifyDataChanged();
        return this;
    }

    /**
     * Sets a background to the md_drawer_view profile
     *
     * @param background Background to set
     */
    public DrawerProfile setBackground(Context context, Bitmap background) {
        mBackground = new BitmapDrawable(context.getResources(), background);
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the background of the md_drawer_view profile
     *
     * @return Background of the md_drawer_view profile
     */
    public Drawable getBackground() {
        return mBackground;
    }

    /**
     * Gets whether the md_drawer_view profile has a background set to it
     *
     * @return True if the md_drawer_view profile has a background set to it, false otherwise.
     */
    public boolean hasBackground() {
        return mBackground != null;
    }

    /**
     * Removes the background from the md_drawer_view profile
     */
    public DrawerProfile removeBackground() {
        mBackground = null;
        notifyDataChanged();
        return this;
    }


    /**
     * Sets a name to the md_drawer_view profile
     *
     * @param name Name to set
     */
    public DrawerProfile setName(String name) {
        mName = name;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the name of the md_drawer_view profile
     *
     * @return Name of the md_drawer_view profile
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets whether the md_drawer_view profile has a name set to it
     *
     * @return True if the md_drawer_view profile has a name set to it, false otherwise.
     */
    public boolean hasName() {
        return mName != null && !mName.equals("");
    }

    /**
     * Removes the name from the md_drawer_view profile
     */
    public DrawerProfile removeName() {
        mName = null;
        notifyDataChanged();
        return this;
    }


    /**
     * Sets a description to the md_drawer_view profile
     *
     * @param description Description to set
     */
    public DrawerProfile setDescription(String description) {
        mDescription = description;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the description of the md_drawer_view profile
     *
     * @return Description of the md_drawer_view profile
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Gets whether the md_drawer_view profile has a description set to it
     *
     * @return True if the md_drawer_view profile has a description set to it, false otherwise.
     */
    public boolean hasDescription() {
        return mDescription != null && !mDescription.equals("");
    }

    /**
     * Removes the description from the md_drawer_view profile
     */
    public DrawerProfile removeDescription() {
        mDescription = null;
        notifyDataChanged();
        return this;
    }


    /**
     * Sets a click listener to the md_drawer_view profile
     *
     * @param listener Listener to set
     */
    public DrawerProfile setOnProfileClickListener(OnProfileClickListener listener) {
        mOnClickListener = listener;
        notifyDataChanged();
        return this;
    }

    /**
     * Gets the click listener of the md_drawer_view profile
     *
     * @return Click listener of the md_drawer_view profile
     */
    public OnProfileClickListener getOnProfileClickListener() {
        return mOnClickListener;
    }

    /**
     * Gets whether the md_drawer_view profile has a click listener set to it
     *
     * @return True if the md_drawer_view profile has a click listener set to it, false otherwise.
     */
    public boolean hasOnProfileClickListener() {
        return mOnClickListener != null;
    }

    /**
     * Removes the click listener from the md_drawer_view profile
     */
    public DrawerProfile removeOnProfileClickListener() {
        mOnClickListener = null;
        notifyDataChanged();
        return this;
    }


    /**
     * Attaches the md_drawer_view item to a md_drawer_view
     *
     * @param drawerView md_drawer_view to attach to
     */
    public DrawerProfile attachTo(DrawerView drawerView) {
        mDrawerView = drawerView;
        notifyDataChanged();
        return this;
    }

    /**
     * Detaches the md_drawer_view item from its md_drawer_view
     */
    public DrawerProfile detach() {
        mDrawerView = null;
        return this;
    }

    protected void notifyDataChanged() {
        if (mDrawerView != null) {
            mDrawerView.setProfile(this);
        }
    }


    public interface OnProfileClickListener {
        void onClick(DrawerProfile item);
    }
}
