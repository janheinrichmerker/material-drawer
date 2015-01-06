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

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.widget.LinearListView;
import com.heinrichreimersoftware.materialdrawer.widget.ScrimInsetsScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * View to be used with {@link android.support.v4.widget.DrawerLayout} to display a md_drawer which is fully compliant with the Material Design specification.
 */
public class DrawerView extends ScrimInsetsScrollView implements ScrimInsetsScrollView.OnInsetsCallback {
    private DrawerProfile mProfile = null;

    private DrawerAdapter mAdapter;
    private DrawerItem.OnItemClickListener mOnItemClickListener;

    private LinearLayout layout;

    private FrameLayout frameLayoutProfile;
    private RelativeLayout relativeLayoutProfileContent;
    private ImageView imageViewBackgroundProfile;
    private ImageView imageViewAvatarProfile;
    private TextView textViewNameProfile;
    private TextView textViewDescriptionProfile;

    private LinearListView linearListView;

    private int statusBarHeight = 0;

    public DrawerView(Context context) {
        this(context, null);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.md_drawer, this);

        findViews();

        setClipToPadding(false);
        setBackgroundColor(getResources().getColor(R.color.md_drawer_background));
        setInsetForeground(new ColorDrawable(getResources().getColor(R.color.md_secondary)));

        setOnInsetsCallback(this);

        mAdapter = new DrawerAdapter(context, new ArrayList<DrawerItem>());
        linearListView.setAdapter(mAdapter);

        updateProfile();
        updateList();
    }

    private void findViews() {
        layout = (LinearLayout) findViewById(R.id.mdLayout);

        frameLayoutProfile = (FrameLayout) findViewById(R.id.mdLayoutProfile);
        relativeLayoutProfileContent = (RelativeLayout) findViewById(R.id.mdLayoutProfileContent);
        imageViewBackgroundProfile = (ImageView) findViewById(R.id.mdBackgroundProfile);
        imageViewAvatarProfile = (ImageView) findViewById(R.id.mdAvatarProfile);
        textViewNameProfile = (TextView) findViewById(R.id.mdNameProfile);
        textViewDescriptionProfile = (TextView) findViewById(R.id.mdDescriptionProfile);

        linearListView = (LinearListView) findViewById(R.id.mdLinearListViewPrimary);
    }

    private void updateSpacing() {
        if (mProfile != null && mProfile.getAvatar() != null && mProfile.getName() != null && !mProfile.getName().isEmpty()) {
            frameLayoutProfile.getLayoutParams().height = Math.round(getLayoutParams().width / 16 * 9);
            relativeLayoutProfileContent.getLayoutParams().height = Math.round(getLayoutParams().width / 16 * 9) - statusBarHeight;

            layout.setPadding(0, 0, 0, 0);
        } else {
            layout.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void updateProfile() {
        if (mProfile != null) {
            if (mProfile.getAvatar() != null) {
                imageViewAvatarProfile.setImageDrawable(mProfile.getAvatar());
            }
            if (mProfile.getName() != null && !mProfile.getName().isEmpty()) {
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
        } else {
            frameLayoutProfile.setVisibility(GONE);
        }
        updateSpacing();
    }

    private void updateList() {
        if (mAdapter.getCount() == 0) {
            linearListView.setVisibility(GONE);
        } else {
            linearListView.setVisibility(VISIBLE);
        }

        linearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                DrawerItem item = mAdapter.getItem(position);
                if (!item.isDivider()) {
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
    }


    /**
     * Sets a profile to the md_drawer view
     *
     * @param profile Profile to set
     */
    public DrawerView setProfile(DrawerProfile profile) {
        mProfile = profile;
        updateProfile();
        return this;
    }

    /**
     * Gets the profile of the md_drawer view
     *
     * @return Profile of the md_drawer view
     */
    public DrawerProfile getProfile() {
        return mProfile;
    }

    /**
     * Removes the profile from the md_drawer view
     */
    public DrawerView removeProfile() {
        mProfile = null;
        updateProfile();
        return this;
    }


    /**
     * Adds items to the md_drawer view
     *
     * @param items Items to add
     */
    public DrawerView addItems(List<DrawerItem> items) {
        for (DrawerItem item : items) {
            item.attachTo(mAdapter);
        }
        mAdapter.addAll(items);
        updateList();
        return this;
    }

    /**
     * Adds an item to the md_drawer view
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
     * Adds a divider to the md_drawer view
     */
    public DrawerView addDivider() {
        addItem(new DrawerHeaderItem());
        return this;
    }

    /**
     * Gets all items from the md_drawer view
     *
     * @return Items from the md_drawer view
     */
    public List<DrawerItem> getItems() {
        return mAdapter.getItems();
    }

    /**
     * Gets an item from the md_drawer view
     *
     * @param position The item position
     * @return Item from the md_drawer view
     */
    public DrawerItem getItem(int position) {
        return mAdapter.getItem(position);
    }

    /**
     * Gets an item from the md_drawer view
     *
     * @param id The item ID
     * @return Item from the md_drawer view
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
     * Selects an item from the md_drawer view
     *
     * @param position The item position
     */
    public void selectItem(int position) {
        mAdapter.select(position);
    }

    /**
     * Gets the selected item position of the md_drawer view
     *
     * @return Position of the selected item
     */
    public int getSelectedPosition(){
        return mAdapter.getSelectedPosition();
    }

    /**
     * Selects an item from the md_drawer view
     *
     * @param id The item ID
     */
    public void selectItemById(int id) {
        for (int i = 0; i < mAdapter.getCount(); i++){
            if (mAdapter.getItem(i).getId() == id) {
                mAdapter.select(i);
            }
        }
    }

    /**
     * Removes an item from the md_drawer view
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
     * Removes an item from the md_drawer view
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
     * Removes all items from the md_drawer view
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
     * Sets an item click listener to the md_drawer view
     *
     * @param listener Listener to set
     */
    public DrawerView setOnItemClickListener(DrawerItem.OnItemClickListener listener) {
        mOnItemClickListener = listener;
        updateList();
        return this;
    }

    /**
     * Gets the item click listener of the md_drawer view
     *
     * @return Item click listener of the md_drawer view
     */
    public DrawerItem.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Gets whether the md_drawer view has an item click listener set to it
     *
     * @return True if the md_drawer view has an item click listener set to it, false otherwise.
     */
    public boolean hasOnItemClickListener() {
        return mOnItemClickListener != null;
    }

    /**
     * Removes the item click listener from the md_drawer view
     */
    public DrawerView removeOnItemClickListener() {
        mOnItemClickListener = null;
        updateList();
        return this;
    }


    @Override
    public void onInsetsChanged(Rect insets) {
        statusBarHeight = insets.top;
        updateSpacing();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw) {
            //Window width
            int windowWidth = ((Activity) getContext()).getWindow().getDecorView().getWidth();

            //Minus the width of the vertical nav bar
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                int navigationBarWidthResId = getResources().getIdentifier("navigation_bar_width", "dimen", "android");
                if (navigationBarWidthResId > 0) {
                    windowWidth -= getResources().getDimensionPixelSize(navigationBarWidthResId);
                }
            }

            //App bar size
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
            int actionBarSize = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());

            int width = windowWidth - actionBarSize;

            int maxWidth = getResources().getDimensionPixelSize(R.dimen.md_drawer_max_width);

            getLayoutParams().width = Math.min(width, maxWidth);

            updateSpacing();
        }
    }
}
