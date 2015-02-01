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

package com.heinrichreimersoftware.materialdrawer;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.widget.LinearListView;
import com.heinrichreimersoftware.materialdrawer.widget.ScrimInsetsFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * View to be used with {@link android.support.v4.widget.DrawerLayout} to display a drawer which is fully compliant with the Material Design specification.
 */
public class DrawerView extends ScrimInsetsFrameLayout implements ScrimInsetsFrameLayout.OnInsetsCallback {

    private static final String TAG = "DrawerView";

    private DrawerProfile mProfile = null;

    private DrawerAdapter mAdapter;
    private DrawerAdapter mAdapterFixed;

    private DrawerItem.OnItemClickListener mOnItemClickListener;
    private DrawerItem.OnItemClickListener mOnFixedItemClickListener;

    private ScrollView scrollView;

    private LinearLayout layout;

    private FrameLayout frameLayoutProfile;
    private RelativeLayout relativeLayoutProfileContent;
    private ImageView imageViewBackgroundProfile;
    private ImageView imageViewAvatarProfile;
    private TextView textViewNameProfile;
    private TextView textViewDescriptionProfile;

    private LinearListView linearListView;

    private LinearLayout fixedListContainer;
    private LinearListView linearListViewFixed;

    private int statusBarHeight = 0;

    private int drawerMaxWidth = -1;

    public DrawerView(Context context) {
        this(context, null);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "DrawerView()");
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Log.d(TAG, "init()");
        inflate(context, R.layout.md_drawer_view, this);

        findViews();

        setClipToPadding(false);
        setBackgroundColor(getResources().getColor(R.color.md_drawer_background));
        setInsetForeground(new ColorDrawable(getResources().getColor(R.color.md_inset_foreground)));

        setOnInsetsCallback(this);

        mAdapter = new DrawerAdapter(context, new ArrayList<DrawerItem>());
        linearListView.setAdapter(mAdapter);

        mAdapterFixed = new DrawerAdapter(context, new ArrayList<DrawerItem>());
        linearListViewFixed.setAdapter(mAdapterFixed);

        updateProfile();
        updateList();
        updateFixedList();
    }


    private void findViews() {
        Log.d(TAG, "findViews()");
        scrollView = (ScrollView) findViewById(R.id.mdScrollView);

        layout = (LinearLayout) findViewById(R.id.mdLayout);

        frameLayoutProfile = (FrameLayout) findViewById(R.id.mdLayoutProfile);
        relativeLayoutProfileContent = (RelativeLayout) findViewById(R.id.mdLayoutProfileContent);
        imageViewBackgroundProfile = (ImageView) findViewById(R.id.mdBackgroundProfile);
        imageViewAvatarProfile = (ImageView) findViewById(R.id.mdAvatarProfile);
        textViewNameProfile = (TextView) findViewById(R.id.mdNameProfile);
        textViewDescriptionProfile = (TextView) findViewById(R.id.mdDescriptionProfile);

        linearListView = (LinearListView) findViewById(R.id.mdLinearListView);

        fixedListContainer = (LinearLayout) findViewById(R.id.mdFixedListContainer);
        linearListViewFixed = (LinearListView) findViewById(R.id.mdLinearListViewFixed);
    }

    private void updateDrawerWidth() {
        Log.d(TAG, "updateDrawerWidth()");

        int viewportWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int viewportHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        //Minus the width of the vertical nav bar
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int navigationBarWidthResId = getResources().getIdentifier("navigation_bar_width", "dimen", "android");
            if (navigationBarWidthResId > 0) {
                viewportWidth -= getResources().getDimensionPixelSize(navigationBarWidthResId);
            }
        }

        int viewportMin = Math.min(viewportWidth, viewportHeight);

        //App bar size
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
        int actionBarSize = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());

        int width = viewportMin - actionBarSize;

        getLayoutParams().width = Math.min(width, drawerMaxWidth);

        updateProfileSpacing();
    }

    private void updateProfileSpacing() {
        Log.d(TAG, "updateProfileSpacing()");
        frameLayoutProfile.getLayoutParams().height = Math.round(getLayoutParams().width / 16 * 9);
        relativeLayoutProfileContent.getLayoutParams().height = Math.round(getLayoutParams().width / 16 * 9) - statusBarHeight;
    }

    private void updateListSpacing() {
        Log.d(TAG, "updateListSpacing()");
        if (mAdapterFixed.getCount() > 0) {
            scrollView.setPadding(0, 0, 0, linearListViewFixed.getHeight());
        } else {
            scrollView.setPadding(0, 0, 0, 0);
        }
    }

    private void updateProfile() {
        Log.d(TAG, "updateProfile()");
        if (mProfile != null) {
            if (mProfile.getAvatar() != null) {
                imageViewAvatarProfile.setImageDrawable(mProfile.getAvatar());
            }
            if (mProfile.getName() != null && !mProfile.getName().equals("")) {
                textViewNameProfile.setText(mProfile.getName());
            }

            if (mProfile.getBackground() != null) {
                imageViewBackgroundProfile.setImageDrawable(mProfile.getBackground());
            } else {
                int colorPrimary = getResources().getColor(R.color.primary_dark_material_light);
                TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
                try {
                    colorPrimary = a.getColor(0, 0);
                } finally {
                    a.recycle();
                }

                imageViewBackgroundProfile.setImageDrawable(new ColorDrawable(colorPrimary));
            }

            if (mProfile.getDescription() != null && !mProfile.getDescription().equals("")) {
                textViewDescriptionProfile.setVisibility(VISIBLE);
                textViewDescriptionProfile.setText(mProfile.getDescription());
            } else {
                textViewDescriptionProfile.setVisibility(GONE);
            }

            if (mProfile.hasOnProfileClickListener()) {
                frameLayoutProfile.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProfile.getOnProfileClickListener().onClick(mProfile);
                    }
                });

                frameLayoutProfile.setEnabled(true);
            } else {
                frameLayoutProfile.setEnabled(false);
            }
            frameLayoutProfile.setVisibility(VISIBLE);
            layout.setPadding(0, 0, 0, 0);
        } else {
            frameLayoutProfile.setVisibility(GONE);
            layout.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void updateList() {
        Log.d(TAG, "updateList()");
        if (mAdapter.getCount() == 0) {
            linearListView.setVisibility(GONE);
        } else {
            linearListView.setVisibility(VISIBLE);
        }

        linearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                DrawerItem item = mAdapter.getItem(position);
                if (!item.isHeader()) {
                    if (item.hasOnItemClickListener()) {
                        item.getOnItemClickListener().onClick(item, item.getId(), position);
                    } else {
                        if (hasOnItemClickListener()) {
                            mOnItemClickListener.onClick(item, item.getId(), position);
                        }
                    }
                }
            }
        });
        updateListSpacing();
    }

    private void updateFixedList() {
        Log.d(TAG, "updateFixedList()");
        if (mAdapterFixed.getCount() == 0) {
            fixedListContainer.setVisibility(GONE);
        } else {
            fixedListContainer.setVisibility(VISIBLE);
        }

        linearListViewFixed.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                DrawerItem item = mAdapterFixed.getItem(position);
                if (!item.isHeader()) {
                    if (item.hasOnItemClickListener()) {
                        item.getOnItemClickListener().onClick(item, item.getId(), position);
                    } else {
                        if (hasOnItemClickListener()) {
                            mOnFixedItemClickListener.onClick(item, item.getId(), position);
                        }
                    }
                }
            }
        });
        updateListSpacing();
    }


    /**
     * Sets the max drawer width
     *
     * @param drawerMaxWidth Max drawer width to set
     */
    public DrawerView setDrawerMaxWidth(int drawerMaxWidth){
        Log.d(TAG, "setDrawerMaxWidth(" + drawerMaxWidth + ")");
        this.drawerMaxWidth = drawerMaxWidth;
        updateDrawerWidth();
        return this;
    }

    /**
     * Sets the max drawer width from resources
     *
     * @param drawerMaxWidthResource Max drawer width resource to set
     */
    public DrawerView setDrawerMaxWidthResource(int drawerMaxWidthResource){
        drawerMaxWidth = getResources().getDimensionPixelSize(drawerMaxWidthResource);
        Log.d(TAG, "setDrawerMaxWidthResource(" + drawerMaxWidth + ")");
        updateDrawerWidth();
        return this;
    }

    /**
     * Resets the max drawer width
     */
    public DrawerView resetDrawerMaxWidth(){
        Log.d(TAG, "resetDrawerMaxWidth()");
        this.drawerMaxWidth = getResources().getDimensionPixelSize(R.dimen.md_drawer_max_width);
        updateDrawerWidth();
        return this;
    }

    /**
     * Gets the max drawer width
     */
    public int getDrawerMaxWidth(){
        return drawerMaxWidth;
    }


    /**
     * Sets a profile to the drawer view
     *
     * @param profile Profile to set
     */
    public DrawerView setProfile(DrawerProfile profile) {
        mProfile = profile;
        updateProfile();
        return this;
    }

    /**
     * Gets the profile of the drawer view
     *
     * @return Profile of the drawer view
     */
    public DrawerProfile getProfile() {
        return mProfile;
    }

    /**
     * Removes the profile from the drawer view
     */
    public DrawerView removeProfile() {
        mProfile = null;
        updateProfile();
        return this;
    }


    /**
     * Adds items to the drawer view
     *
     * @param items Items to add
     */
    public DrawerView addItems(List<DrawerItem> items) {
        mAdapter.setNotifyOnChange(false);
        for (DrawerItem item : items) {
            item.attachTo(mAdapter);
            mAdapter.add(item);
        }
        mAdapter.setNotifyOnChange(true);
        mAdapter.notifyDataSetChanged();
        updateList();
        return this;
    }

    /**
     * Adds an item to the drawer view
     *
     * @param item Item to add
     */
    public DrawerView addItem(DrawerItem item) {
        item.attachTo(mAdapter);
        mAdapter.add(item);
        updateList();
        return this;
    }

    /**
     * Adds a divider to the drawer view
     */
    public DrawerView addDivider() {
        addItem(new DrawerHeaderItem());
        return this;
    }

    /**
     * Gets all items from the drawer view
     *
     * @return Items from the drawer view
     */
    public List<DrawerItem> getItems() {
        return mAdapter.getItems();
    }

    /**
     * Gets an item from the drawer view
     *
     * @param position The item position
     * @return Item from the drawer view
     */
    public DrawerItem getItem(int position) {
        return mAdapter.getItem(position);
    }

    /**
     * Gets an item from the drawer view
     *
     * @param id The item ID
     * @return Item from the drawer view
     */
    public DrawerItem findItemById(int id) {
        for (int i = 0; i < mAdapter.getCount(); i++){
            if (mAdapter.getItem(i).getId() == id) {
                return mAdapter.getItem(i);
            }
        }
        return null;
    }

    /**
     * Selects an item from the drawer view
     *
     * @param position The item position
     */
    public void selectItem(int position) {
        mAdapterFixed.clearSelection();
        mAdapter.select(position);
    }

    /**
     * Gets the selected item position of the drawer view
     *
     * @return Position of the selected item
     */
    public int getSelectedPosition(){
        return mAdapter.getSelectedPosition();
    }

    /**
     * Selects an item from the drawer view
     *
     * @param id The item ID
     */
    public void selectItemById(int id) {
        mAdapterFixed.clearSelection();

        for (int i = 0; i < mAdapter.getCount(); i++){
            if (mAdapter.getItem(i).getId() == id) {
                mAdapter.select(i);
            }
        }
    }

    /**
     * Removes an item from the drawer view
     *
     * @param item Item to remove
     */
    public DrawerView removeItem(DrawerItem item) {
        item.detach();
        mAdapter.remove(item);
        updateList();
        return this;
    }

    /**
     * Removes an item from the drawer view
     *
     * @param position Position to remove
     */
    public DrawerView removeItem(int position) {
        mAdapter.getItem(position).detach();
        mAdapter.remove(mAdapter.getItem(position));
        updateList();
        return this;
    }

    /**
     * Removes all items from the drawer view
     */
    public DrawerView clearItems() {
        for (DrawerItem item : mAdapter.getItems()) {
            item.detach();
        }
        mAdapter.clear();
        updateList();
        return this;
    }


    /**
     * Sets an item click listener to the drawer view
     *
     * @param listener Listener to set
     */
    public DrawerView setOnItemClickListener(DrawerItem.OnItemClickListener listener) {
        mOnItemClickListener = listener;
        updateList();
        return this;
    }

    /**
     * Gets the item click listener of the drawer view
     *
     * @return Item click listener of the drawer view
     */
    public DrawerItem.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
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
    public DrawerView removeOnItemClickListener() {
        mOnItemClickListener = null;
        updateList();
        return this;
    }


    /**
     * Adds fixed items to the drawer view
     *
     * @param items Items to add
     */
    public DrawerView addFixedItems(List<DrawerItem> items) {
        mAdapterFixed.setNotifyOnChange(false);
        for (DrawerItem item : items) {
            item.attachTo(mAdapterFixed);
            mAdapterFixed.add(item);
        }
        mAdapterFixed.setNotifyOnChange(true);
        mAdapterFixed.notifyDataSetChanged();
        updateFixedList();
        return this;
    }

    /**
     * Adds a fixed item to the drawer view
     *
     * @param item Item to add
     */
    public DrawerView addFixedItem(DrawerItem item) {
        item.attachTo(mAdapterFixed);
        mAdapterFixed.add(item);
        updateFixedList();
        return this;
    }

    /**
     * Adds a fixed divider to the drawer view
     */
    public DrawerView addFixedDivider() {
        addFixedItem(new DrawerHeaderItem());
        return this;
    }

    /**
     * Gets all fixed items from the drawer view
     *
     * @return Items from the drawer view
     */
    public List<DrawerItem> getFixedItems() {
        return mAdapterFixed.getItems();
    }

    /**
     * Gets a fixed item from the drawer view
     *
     * @param position The item position
     * @return Item from the drawer view
     */
    public DrawerItem getFixedItem(int position) {
        return mAdapterFixed.getItem(position);
    }

    /**
     * Gets a fixed item from the drawer view
     *
     * @param id The item ID
     * @return Item from the drawer view
     */
    public DrawerItem findFixedItemById(int id) {
        for (int i = 0; i < mAdapterFixed.getCount(); i++){
            if (mAdapterFixed.getItem(i).getId() == id) {
                return mAdapterFixed.getItem(i);
            }
        }
        return null;
    }

    /**
     * Selects a fixed item from the drawer view
     *
     * @param position The item position
     */
    public void selectFixedItem(int position) {
        mAdapter.clearSelection();
        mAdapterFixed.select(position);
    }

    /**
     * Gets the selected fixed item position of the drawer view
     *
     * @return Position of the selected item
     */
    public int getSelectedFixedPosition(){
        return mAdapterFixed.getSelectedPosition();
    }

    /**
     * Selects a fixed item from the drawer view
     *
     * @param id The item ID
     */
    public void selectFixedItemById(int id) {
        mAdapter.clearSelection();

        for (int i = 0; i < mAdapterFixed.getCount(); i++){
            if (mAdapterFixed.getItem(i).getId() == id) {
                mAdapterFixed.select(i);
            }
        }
    }

    /**
     * Removes a fixed item from the drawer view
     *
     * @param item Item to remove
     */
    public DrawerView removeFixedItem(DrawerItem item) {
        item.detach();
        mAdapterFixed.remove(item);
        updateFixedList();
        return this;
    }

    /**
     * Removes a fixed item from the drawer view
     *
     * @param position Position to remove
     */
    public DrawerView removeFixedItem(int position) {
        mAdapterFixed.getItem(position).detach();
        mAdapterFixed.remove(mAdapterFixed.getItem(position));
        updateFixedList();
        return this;
    }

    /**
     * Removes all fixed items from the drawer view
     */
    public DrawerView clearFixedItems() {
        for (DrawerItem item : mAdapterFixed.getItems()) {
            item.detach();
        }
        mAdapterFixed.clear();
        updateFixedList();
        return this;
    }


    /**
     * Sets a fixed item click listener to the drawer view
     *
     * @param listener Listener to set
     */
    public DrawerView setOnFixedItemClickListener(DrawerItem.OnItemClickListener listener) {
        mOnFixedItemClickListener = listener;
        updateFixedList();
        return this;
    }

    /**
     * Gets the fixed item click listener of the drawer view
     *
     * @return Item click listener of the drawer view
     */
    public DrawerItem.OnItemClickListener getOnFixedItemClickListener() {
        return mOnFixedItemClickListener;
    }

    /**
     * Gets whether the drawer view has a fixed item click listener set to it
     *
     * @return True if the drawer view has a fixed item click listener set to it, false otherwise.
     */
    public boolean hasOnFixedItemClickListener() {
        return mOnFixedItemClickListener != null;
    }

    /**
     * Removes the fixed item click listener from the drawer view
     */
    public DrawerView removeOnFixedItemClickListener() {
        mOnFixedItemClickListener = null;
        updateFixedList();
        return this;
    }


    @Override
    public void onInsetsChanged(Rect insets) {
        Log.d(TAG, "onInsetsChanged()");
        statusBarHeight = insets.top;
        updateProfileSpacing();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged()");
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw) {
            if(drawerMaxWidth <= 0) {
                if(getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT && getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT){
                    setDrawerMaxWidth(getLayoutParams().width);
                }
                else{
                    resetDrawerMaxWidth();
                }
            }

            updateDrawerWidth();
        }
    }
}
