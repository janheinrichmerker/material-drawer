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

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerFragmentItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;

import java.util.List;

@SuppressWarnings("unused")
@SuppressLint("Registered")
public class DrawerActivity extends AppCompatActivity {

    private DrawerFrameLayout mDrawer;
    private Toolbar mDefaultToolbar;
    private FrameLayout mFrame;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerItem.OnItemClickListener mOnItemClickListener;
    private DrawerItem.OnItemClickListener mOnFixedItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.md_drawer_activity);

        mDrawer = (DrawerFrameLayout) findViewById(R.id.mdDrawerLayout);
        mDefaultToolbar = (Toolbar) findViewById(R.id.mdToolbar);
        mFrame = (FrameLayout) findViewById(R.id.mdFrame);

        setSupportActionBar(mDefaultToolbar);

        mDrawer.closeDrawer();
    }

    /**
     * Set a {@link Toolbar} to act as the {@link android.support.v7.app.ActionBar}ActionBar for this Activity window.
     * You must take care of hiding the old {@link Toolbar} if necessary.
     *
     * @param toolbar Toolbar to set as the Activity's action bar
     */
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            if (toolbar != mDefaultToolbar && mDefaultToolbar.getVisibility() != View.GONE) {
                mDefaultToolbar.setVisibility(View.GONE);
            }

            super.setSupportActionBar(toolbar);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.md_content_description_drawer_open, R.string.md_content_description_drawer_close) {
                public void onDrawerClosed(View view) {
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    invalidateOptionsMenu();
                }
            };
            mDrawerToggle.syncState();
            mDrawer.setDrawerListener(mDrawerToggle);

            mDrawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, long id, int position) {
                    if (item instanceof DrawerFragmentItem && ((DrawerFragmentItem) item).hasFragment()) {
                        switchFragment(((DrawerFragmentItem) item).getFragment());
                    }
                    if (item.hasOnItemClickListener()) {
                        item.getOnItemClickListener().onClick(item, id, position);
                    } else {
                        if (hasOnItemClickListener()) {
                            mOnItemClickListener.onClick(item, id, position);
                        }
                    }
                }
            });
            mDrawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, long id, int position) {
                    if (item instanceof DrawerFragmentItem && ((DrawerFragmentItem) item).hasFragment()) {
                        switchFragment(((DrawerFragmentItem) item).getFragment());
                    }
                    if (item.hasOnItemClickListener()) {
                        item.getOnItemClickListener().onClick(item, id, position);
                    } else {
                        if (hasOnItemClickListener()) {
                            mOnFixedItemClickListener.onClick(item, id, position);
                        }
                    }
                }
            });
        }
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentById(R.id.mdFrame) == fragment) return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        if (fragmentManager.findFragmentById(R.id.mdFrame) != null) {
            fragmentTransaction.replace(R.id.mdFrame, fragment);
        }
        else {
            ((ViewGroup)findViewById(R.id.mdFrame)).removeAllViews();
            fragmentTransaction.add(R.id.mdFrame, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void setContentView(int layoutResID) {
        mFrame.removeAllViews();
        View.inflate(this, layoutResID, mFrame);
    }

    @Override
    public void setContentView(View view) {
        mFrame.removeAllViews();
        mFrame.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.removeAllViews();
        mFrame.addView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.addView(view, params);
    }

    /**
     * Gets whether the enhanced drawer indicator is enabled
     *
     * @return true if the enhanced drawer indicator is enabled, false otherwise
     */
    public boolean isDrawerIndicatorEnabled() {
        return mDrawerToggle != null && mDrawerToggle.isDrawerIndicatorEnabled();
    }

    /**
     * Enable or disable the drawer indicator. The indicator defaults to enabled.
     * When the indicator is disabled, the ActionBar will revert to displaying the home-as-up indicator provided by the {@link android.app.Activity}'s theme in the android.R.attr.homeAsUpIndicator attribute instead of the animated drawer glyph.
     *
     * @param enabled true to enable, false to disable
     */
    public DrawerActivity setDrawerIndicatorEnabled(boolean enabled) {
        if (mDrawerToggle != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(enabled);
        }
        return this;
    }

    /**
     * Set the up indicator to display when the drawer indicator is not
     * enabled.
     * <p>
     * If you pass <code>null</code> to this method, the default drawable from
     * the theme will be used.
     *
     * @param indicator A drawable to use for the up indicator, or null to use
     *                  the theme's default
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public void setHomeAsUpIndicator(Drawable indicator) {
        if (mDrawerToggle != null) {
            mDrawerToggle.setHomeAsUpIndicator(indicator);
        }
    }

    /**
     * Set the up indicator to display when the drawer indicator is not
     * enabled.
     * <p>
     * If you pass 0 to this method, the default drawable from the theme will
     * be used.
     *
     * @param resId Resource ID of a drawable to use for the up indicator, or 0
     *              to use the theme's default
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public void setHomeAsUpIndicator(int resId) {
        if (mDrawerToggle != null) {
            mDrawerToggle.setHomeAsUpIndicator(resId);
        }
    }

    /**
     * Returns the fallback listener for Navigation icon click events.
     *
     * @return The click listener which receives Navigation click events from Toolbar when
     * drawer indicator is disabled.
     * @see #setToolbarNavigationClickListener(android.view.View.OnClickListener)
     * @see #setDrawerIndicatorEnabled(boolean)
     * @see #isDrawerIndicatorEnabled()
     */
    public View.OnClickListener getToolbarNavigationClickListener() {
        if (mDrawerToggle != null) {
            return mDrawerToggle.getToolbarNavigationClickListener();
        }
        return null;
    }

    /**
     * When DrawerToggle is constructed with a Toolbar, it sets the click listener on
     * the Navigation icon. If you want to listen for clicks on the Navigation icon when
     * DrawerToggle is disabled ({@link #setDrawerIndicatorEnabled(boolean)}, you should call this
     * method with your listener and DrawerToggle will forward click events to that listener
     * when drawer indicator is disabled.
     *
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public void setToolbarNavigationClickListener(View.OnClickListener listener) {
        if (mDrawerToggle != null) {
            mDrawerToggle.setToolbarNavigationClickListener(listener);
        }
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
    public DrawerActivity setLoggingEnabled(boolean loggingEnabled) {
        mDrawer.setLoggingEnabled(loggingEnabled);
        return this;
    }

    /**
     * Resets the drawer theme
     */
    public DrawerActivity resetDrawerTheme() {
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
    public DrawerActivity setDrawerTheme(DrawerTheme theme) {
        mDrawer.setDrawerTheme(theme);
        return this;
    }

    /**
     * Sets the max drawer width from resources
     *
     * @param drawerMaxWidthResource Max drawer width resource to set
     */
    public DrawerActivity setDrawerMaxWidthResource(int drawerMaxWidthResource) {
        mDrawer.setDrawerMaxWidthResource(drawerMaxWidthResource);
        return this;
    }

    /**
     * Resets the max drawer width
     */
    public DrawerActivity resetDrawerMaxWidth() {
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
    public DrawerActivity setDrawerMaxWidth(int drawerMaxWidth) {
        mDrawer.setDrawerMaxWidth(drawerMaxWidth);
        return this;
    }

    /**
     * Adds a profile to the drawer view
     *
     * @param profile Profile to add
     */
    public DrawerActivity addProfile(DrawerProfile profile) {
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
    public DrawerActivity selectProfile(DrawerProfile profile) {
        mDrawer.selectProfile(profile);
        return this;
    }

    /**
     * Selects a profile from the drawer view
     *
     * @param id The profile ID
     */
    public DrawerActivity selectProfileById(long id) {
        mDrawer.selectProfileById(id);
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param profile Profile to remove
     */
    public DrawerActivity removeProfile(DrawerProfile profile) {
        mDrawer.removeProfile(profile);
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param id ID to remove
     */
    public DrawerActivity removeProfileById(long id) {
        mDrawer.removeProfileById(id);
        return this;
    }

    /**
     * Removes all profiles from the drawer view
     */
    public DrawerActivity clearProfiles() {
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
    public DrawerActivity setOnProfileClickListener(DrawerProfile.OnProfileClickListener listener) {
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
    public DrawerActivity removeOnProfileClickListener() {
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
    public DrawerActivity setOnProfileSwitchListener(DrawerProfile.OnProfileSwitchListener listener) {
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
    public DrawerActivity removeOnProfileSwitchListener() {
        mDrawer.removeOnProfileSwitchListener();
        return this;
    }


    /**
     * Adds items to the drawer
     *
     * @param items Items to add
     */
    public DrawerActivity addItems(List<DrawerItem> items) {
        mDrawer.addItems(items);
        return this;
    }

    /**
     * Adds items to the drawer
     *
     * @param items Items to add
     */
    public DrawerActivity addItems(DrawerItem... items) {
        mDrawer.addItems(items);
        return this;
    }

    /**
     * Adds an item to the drawer
     *
     * @param item Item to add
     */
    public DrawerActivity addItem(DrawerItem item) {
        mDrawer.addItem(item);
        return this;
    }

    /**
     * Adds a divider to the drawer
     */
    public DrawerActivity addDivider() {
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
    public DrawerActivity removeItem(DrawerItem item) {
        mDrawer.removeItem(item);
        return this;
    }

    /**
     * Removes an item from the drawer
     *
     * @param position Position to remove
     */
    public DrawerActivity removeItem(int position) {
        mDrawer.removeItem(position);
        return this;
    }

    /**
     * Removes an item from the drawer
     *
     * @param id ID to remove
     */
    public DrawerActivity removeItemById(long id) {
        mDrawer.removeItemById(id);
        return this;
    }

    /**
     * Removes all items from the drawer
     */
    public DrawerActivity clearItems() {
        mDrawer.clearItems();
        return this;
    }

    /**
     * Gets the item click listener of the drawer
     *
     * @return Item click listener of the drawer
     */
    public DrawerItem.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Sets an item click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerActivity setOnItemClickListener(DrawerItem.OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * Gets whether the drawer view has an item click listener set to it
     *
     * @return True if the drawer view has an item click listener set to it, false otherwise.
     */
    public boolean hasOnItemClickListener() {
        return mOnItemClickListener != null;
    }

    /**
     * Removes the item click listener from the drawer view
     */
    public DrawerActivity removeOnItemClickListener() {
        mOnItemClickListener = null;
        return this;
    }


    /**
     * Adds fixed items to the drawer
     *
     * @param items Items to add
     */
    public DrawerActivity addFixedItems(List<DrawerItem> items) {
        mDrawer.addFixedItems(items);
        return this;
    }

    /**
     * Adds fixed items to the drawer
     *
     * @param items Items to add
     */
    public DrawerActivity addFixedItems(DrawerItem... items) {
        mDrawer.addFixedItems(items);
        return this;
    }

    /**
     * Adds a fixed item to the drawer
     *
     * @param item Item to add
     */
    public DrawerActivity addFixedItem(DrawerItem item) {
        mDrawer.addFixedItem(item);
        return this;
    }

    /**
     * Adds a fixed divider to the drawer
     */
    public DrawerActivity addFixedDivider() {
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
    public DrawerActivity removeFixedItem(DrawerItem item) {
        mDrawer.removeFixedItem(item);
        return this;
    }

    /**
     * Removes a fixed item from the drawer
     *
     * @param position Position to remove
     */
    public DrawerActivity removeFixedItem(int position) {
        mDrawer.removeFixedItem(position);
        return this;
    }

    /**
     * Removes a fixed item from the drawer
     *
     * @param id ID to remove
     */
    public DrawerActivity removeFixedItemById(long id) {
        mDrawer.removeFixedItemById(id);
        return this;
    }

    /**
     * Removes all fixed items from the drawer
     */
    public DrawerActivity clearFixedItems() {
        mDrawer.clearFixedItems();
        return this;
    }

    /**
     * Gets the fixed item click listener of the drawer
     *
     * @return Item click listener of the drawer
     */
    public DrawerItem.OnItemClickListener getOnFixedItemClickListener() {
        return mOnFixedItemClickListener;
    }

    /**
     * Sets a fixed item click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerActivity setOnFixedItemClickListener(DrawerItem.OnItemClickListener listener) {
        mOnFixedItemClickListener = listener;
        return this;
    }

    /**
     * Gets whether the drawer has a fixed item click listener set to it
     *
     * @return True if the drawer has a fixed item click listener set to it, false otherwise.
     */
    public boolean hasOnFixedItemClickListener() {
        return mOnFixedItemClickListener != null;
    }

    /**
     * Removes the fixed item click listener from the drawer
     */
    public DrawerActivity removeOnFixedItemClickListener() {
        mOnFixedItemClickListener = null;
        return this;
    }


    /**
     * Opens the drawer
     */
    public void openDrawer() {
        mDrawer.openDrawer();
    }

    /**
     * Closes the drawer
     */
    public void closeDrawer() {
        mDrawer.closeDrawer();
    }
}
