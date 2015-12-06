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

package com.heinrichreimersoftware.materialdrawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;

import java.util.List;

@SuppressWarnings("unused")
public class DrawerFrameLayout extends DrawerLayout {

    private final DrawerView mDrawer;

    public DrawerFrameLayout(Context context) {
        this(context, null);
    }

    public DrawerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        inflate(context, R.layout.md_drawer_frame_layout, this);
        mDrawer = (DrawerView) findViewById(R.id.mdDrawer);

        setDrawerShadow(R.drawable.md_drawer_shadow, GravityCompat.START);


        TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});

        int colorPrimaryDark = a.getColor(0, 0);
        if (colorPrimaryDark != 0) {
            setStatusBarBackgroundColor(colorPrimaryDark);
        } else {
            setStatusBarBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }

        a = getContext().obtainStyledAttributes(attrs, new int[]{R.attr.drawerMaxWidth});

        int drawerMaxWidth = a.getDimensionPixelSize(0, 0);
        if (drawerMaxWidth != 0) {
            setDrawerMaxWidth(drawerMaxWidth);
        } else {
            resetDrawerMaxWidth();
        }
        a.recycle();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, 0, params);
    }

    /**
     * Gets whether debug logging is enabled
     */
    public boolean getLoggingEnabled() {
        return mDrawer.getLoggingEnabled();
    }

    /**
     * Sets whether debug logging is enabled
     *
     * @param loggingEnabled whether or not to enable debug logging
     */
    public DrawerFrameLayout setLoggingEnabled(boolean loggingEnabled) {
        mDrawer.setLoggingEnabled(loggingEnabled);
        return this;
    }

    /**
     * Resets the drawer theme
     */
    public DrawerFrameLayout resetDrawerTheme() {
        mDrawer.resetDrawerTheme();
        return this;
    }

    /**
     * Gets the drawer theme
     */
    public DrawerTheme getDrawerTheme() {
        return mDrawer.getDrawerTheme();
    }

    /**
     * Sets the drawer theme
     *
     * @param theme Theme to set
     */
    public DrawerFrameLayout setDrawerTheme(DrawerTheme theme) {
        mDrawer.setDrawerTheme(theme);
        return this;
    }

    /**
     * Sets the max drawer width from resources
     *
     * @param drawerMaxWidthResource Max drawer width resource to set
     */
    public DrawerFrameLayout setDrawerMaxWidthResource(int drawerMaxWidthResource) {
        mDrawer.setDrawerMaxWidthResource(drawerMaxWidthResource);
        return this;
    }

    /**
     * Resets the max drawer width
     */
    public DrawerFrameLayout resetDrawerMaxWidth() {
        mDrawer.resetDrawerMaxWidth();
        return this;
    }

    /**
     * Gets the max drawer width
     */
    public int getDrawerMaxWidth() {
        return mDrawer.getDrawerMaxWidth();
    }

    /**
     * Sets the max drawer width
     *
     * @param drawerMaxWidth Max drawer width to set
     */
    public DrawerFrameLayout setDrawerMaxWidth(int drawerMaxWidth) {
        mDrawer.setDrawerMaxWidth(drawerMaxWidth);
        return this;
    }

    /**
     * Adds a profile to the drawer view
     *
     * @param profile Profile to add
     */
    public DrawerFrameLayout addProfile(DrawerProfile profile) {
        mDrawer.addProfile(profile);
        return this;
    }

    /**
     * Gets all profiles from the drawer view
     *
     * @return Profiles from the drawer view
     */
    public List<DrawerProfile> getProfiles() {
        return mDrawer.getProfiles();
    }

    /**
     * Gets a profile from the drawer view
     *
     * @param id The profile ID
     * @return Profile from the drawer view
     */
    public DrawerProfile findProfileById(long id) {
        return mDrawer.findProfileById(id);
    }

    /**
     * Selects a profile from the drawer view
     *
     * @param profile The profile
     */
    public DrawerFrameLayout selectProfile(DrawerProfile profile) {
        mDrawer.selectProfile(profile);
        return this;
    }

    /**
     * Selects a profile from the drawer view
     *
     * @param id The profile ID
     */
    public DrawerFrameLayout selectProfileById(long id) {
        mDrawer.selectProfileById(id);
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param profile Profile to remove
     */
    public DrawerFrameLayout removeProfile(DrawerProfile profile) {
        mDrawer.removeProfile(profile);
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param id ID to remove
     */
    public DrawerFrameLayout removeProfileById(long id) {
        mDrawer.removeProfileById(id);
        return this;
    }

    /**
     * Removes all profiles from the drawer view
     */
    public DrawerFrameLayout clearProfiles() {
        mDrawer.clearProfiles();
        return this;
    }

    /**
     * Gets the profile click listener of the drawer
     *
     * @return Profile click listener of the drawer
     */
    public DrawerProfile.OnProfileClickListener getOnProfileClickListener() {
        return mDrawer.getOnProfileClickListener();
    }

    /**
     * Sets a profile click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerFrameLayout setOnProfileClickListener(DrawerProfile.OnProfileClickListener listener) {
        mDrawer.setOnProfileClickListener(listener);
        return this;
    }

    /**
     * Gets whether the drawer has a profile click listener set to it
     *
     * @return True if the drawer has a profile click listener set to it, false otherwise.
     */
    public boolean hasOnProfileClickListener() {
        return mDrawer.hasOnProfileClickListener();
    }

    /**
     * Removes the profile click listener from the drawer
     */
    public DrawerFrameLayout removeOnProfileClickListener() {
        mDrawer.removeOnProfileClickListener();
        return this;
    }

    /**
     * Gets the profile switch listener of the drawer
     *
     * @return Profile switch listener of the drawer
     */
    public DrawerProfile.OnProfileSwitchListener getOnProfileSwitchListener() {
        return mDrawer.getOnProfileSwitchListener();
    }

    /**
     * Sets a profile switch listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerFrameLayout setOnProfileSwitchListener(DrawerProfile.OnProfileSwitchListener listener) {
        mDrawer.setOnProfileSwitchListener(listener);
        return this;
    }

    /**
     * Gets whether the drawer has a profile switch listener set to it
     *
     * @return True if the drawer has a profile switch listener set to it, false otherwise.
     */
    public boolean hasOnProfileSwitchListener() {
        return mDrawer.hasOnProfileSwitchListener();
    }

    /**
     * Removes the profile switch listener from the drawer
     */
    public DrawerFrameLayout removeOnProfileSwitchListener() {
        mDrawer.removeOnProfileSwitchListener();
        return this;
    }


    /**
     * Adds items to the drawer
     *
     * @param items Items to add
     */
    public DrawerFrameLayout addItems(List<DrawerItem> items) {
        mDrawer.addItems(items);
        return this;
    }

    /**
     * Adds items to the drawer
     *
     * @param items Items to add
     */
    public DrawerFrameLayout addItems(DrawerItem... items) {
        mDrawer.addItems(items);
        return this;
    }

    /**
     * Adds an item to the drawer
     *
     * @param item Item to add
     */
    public DrawerFrameLayout addItem(DrawerItem item) {
        mDrawer.addItem(item);
        return this;
    }

    /**
     * Adds a divider to the drawer
     */
    public DrawerFrameLayout addDivider() {
        mDrawer.addDivider();
        return this;
    }

    /**
     * Gets all items from the drawer
     *
     * @return Items from the drawer
     */
    public List<DrawerItem> getItems() {
        return mDrawer.getItems();
    }

    /**
     * Gets an item from the drawer
     *
     * @param position The item position
     * @return Item from the drawer
     */
    public DrawerItem getItem(int position) {
        return mDrawer.getItem(position);
    }

    /**
     * Gets an item from the drawer
     *
     * @param id The item ID
     * @return Item from the drawer
     */
    public DrawerItem findItemById(long id) {
        return mDrawer.findItemById(id);
    }

    /**
     * Selects an item from the drawer
     *
     * @param position The item position
     */
    public void selectItem(int position) {
        mDrawer.selectItem(position);
    }

    /**
     * Gets the selected item position of the drawer
     *
     * @return Position of the selected item
     */
    public int getSelectedPosition() {
        return mDrawer.getSelectedPosition();
    }

    /**
     * Selects an item from the drawer
     *
     * @param id The item ID
     */
    public void selectItemById(long id) {
        mDrawer.selectItemById(id);
    }

    /**
     * Removes an item from the drawer
     *
     * @param item Item to remove
     */
    public DrawerFrameLayout removeItem(DrawerItem item) {
        mDrawer.removeItem(item);
        return this;
    }

    /**
     * Removes an item from the drawer
     *
     * @param id ID to remove
     */
    public DrawerFrameLayout removeItemById(long id) {
        mDrawer.removeItemById(id);
        return this;
    }

    /**
     * Removes an item from the drawer
     *
     * @param position Position to remove
     */
    public DrawerFrameLayout removeItem(int position) {
        mDrawer.removeItem(position);
        return this;
    }

    /**
     * Removes all items from the drawer
     */
    public DrawerFrameLayout clearItems() {
        mDrawer.clearItems();
        return this;
    }

    /**
     * Gets the item click listener of the drawer
     *
     * @return Item click listener of the drawer
     */
    public DrawerItem.OnItemClickListener getOnItemClickListener() {
        return mDrawer.getOnItemClickListener();
    }

    /**
     * Sets an item click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerFrameLayout setOnItemClickListener(DrawerItem.OnItemClickListener listener) {
        mDrawer.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Gets whether the drawer has an item click listener set to it
     *
     * @return True if the drawer has an item click listener set to it, false otherwise.
     */
    public boolean hasOnItemClickListener() {
        return mDrawer.hasOnItemClickListener();
    }

    /**
     * Removes the item click listener from the drawer
     */
    public DrawerFrameLayout removeOnItemClickListener() {
        mDrawer.removeOnItemClickListener();
        return this;
    }


    /**
     * Adds fixed items to the drawer
     *
     * @param items Items to add
     */
    public DrawerFrameLayout addFixedItems(List<DrawerItem> items) {
        mDrawer.addFixedItems(items);
        return this;
    }

    /**
     * Adds fixed items to the drawer
     *
     * @param items Items to add
     */
    public DrawerFrameLayout addFixedItems(DrawerItem... items) {
        mDrawer.addFixedItems(items);
        return this;
    }

    /**
     * Adds a fixed item to the drawer
     *
     * @param item Item to add
     */
    public DrawerFrameLayout addFixedItem(DrawerItem item) {
        mDrawer.addFixedItem(item);
        return this;
    }

    /**
     * Adds a fixed divider to the drawer
     */
    public DrawerFrameLayout addFixedDivider() {
        mDrawer.addFixedDivider();
        return this;
    }

    /**
     * Gets all fixed items from the drawer
     *
     * @return Items from the drawer
     */
    public List<DrawerItem> getFixedItems() {
        return mDrawer.getFixedItems();
    }

    /**
     * Gets a fixed item from the drawer
     *
     * @param position The item position
     * @return Item from the drawer
     */
    public DrawerItem getFixedItem(int position) {
        return mDrawer.getFixedItem(position);
    }

    /**
     * Gets a fixed item from the drawer
     *
     * @param id The item ID
     * @return Item from the drawer
     */
    public DrawerItem findFixedItemById(long id) {
        return mDrawer.findFixedItemById(id);
    }

    /**
     * Selects a fixed item from the drawer
     *
     * @param position The item position
     */
    public void selectFixedItem(int position) {
        mDrawer.selectFixedItem(position);
    }

    /**
     * Gets the selected fixed item position of the drawer
     *
     * @return Position of the selected item
     */
    public int getSelectedFixedPosition() {
        return mDrawer.getSelectedFixedPosition();
    }

    /**
     * Selects a fixed item from the drawer
     *
     * @param id The item ID
     */
    public void selectFixedItemById(long id) {
        mDrawer.selectFixedItemById(id);
    }

    /**
     * Removes a fixed item from the drawer
     *
     * @param item Item to remove
     */
    public DrawerFrameLayout removeFixedItem(DrawerItem item) {
        mDrawer.removeFixedItem(item);
        return this;
    }

    /**
     * Removes a fixed item from the drawer
     *
     * @param position Position to remove
     */
    public DrawerFrameLayout removeFixedItem(int position) {
        mDrawer.removeFixedItem(position);
        return this;
    }

    /**
     * Removes a fixed item from the drawer
     *
     * @param id ID to remove
     */
    public DrawerFrameLayout removeFixedItemById(long id) {
        mDrawer.removeFixedItemById(id);
        return this;
    }

    /**
     * Removes all fixed items from the drawer
     */
    public DrawerFrameLayout clearFixedItems() {
        mDrawer.clearFixedItems();
        return this;
    }

    /**
     * Gets the fixed item click listener of the drawer
     *
     * @return Item click listener of the drawer
     */
    public DrawerItem.OnItemClickListener getOnFixedItemClickListener() {
        return mDrawer.getOnFixedItemClickListener();
    }

    /**
     * Sets a fixed item click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerFrameLayout setOnFixedItemClickListener(DrawerItem.OnItemClickListener listener) {
        mDrawer.setOnFixedItemClickListener(listener);
        return this;
    }

    /**
     * Gets whether the drawer has a fixed item click listener set to it
     *
     * @return True if the drawer has a fixed item click listener set to it, false otherwise.
     */
    public boolean hasOnFixedItemClickListener() {
        return mDrawer.hasOnFixedItemClickListener();
    }

    /**
     * Removes the fixed item click listener from the drawer
     */
    public DrawerFrameLayout removeOnFixedItemClickListener() {
        mDrawer.removeOnFixedItemClickListener();
        return this;
    }


    /**
     * Opens the drawer
     */
    public void openDrawer() {
        openDrawer(mDrawer);
    }

    /**
     * Closes the drawer
     */
    public void closeDrawer() {
        closeDrawer(mDrawer);
    }
}
