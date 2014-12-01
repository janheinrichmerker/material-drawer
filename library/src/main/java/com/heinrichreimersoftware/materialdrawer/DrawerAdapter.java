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
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to be used with {@link DrawerView} to display a list of drawer items.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    private int selectedPosition = -1;

    public DrawerAdapter(Context context, List<DrawerItem> dataSet) {
        super(context, R.layout.drawer_item, dataSet);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem drawerItem = getItem(position);

        if (drawerItem.isDivider()) {
            if (convertView == null || convertView instanceof RelativeLayout) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_divider_item, parent, false);
            }
        } else {
            if (convertView == null || !(convertView instanceof RelativeLayout)) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent, false);
            }

            final ViewHolder viewHolder = new ViewHolder(convertView);

            int colorPrimary = -1;

            if (position == selectedPosition){
                TypedValue a = new TypedValue();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    getContext().getTheme().resolveAttribute(android.R.attr.colorPrimary, a, true);
                    if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                        colorPrimary = a.data;
                    }
                }
                if (colorPrimary == -1) {
                    getContext().getTheme().resolveAttribute(R.attr.colorPrimary, a, true);
                    if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                        colorPrimary = a.data;
                    }
                }
            }

            if (drawerItem.hasImage()) {
                viewHolder.getImageView().setImageDrawable(drawerItem.getImage());

                int imageSize;
                int imageMarginEnd;

                if (drawerItem.getImageMode() == DrawerItem.AVATAR) {
                    imageSize = getContext().getResources().getDimensionPixelSize(R.dimen.avatar_size);
                } else {
                    imageSize = getContext().getResources().getDimensionPixelSize(R.dimen.icon_size);
                    if (colorPrimary > -1) {
                        viewHolder.getImageView().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
                    }
                }

                imageMarginEnd = getContext().getResources().getDimensionPixelSize(R.dimen.baseline_content) - getContext().getResources().getDimensionPixelSize(R.dimen.baseline_start) - imageSize;

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.getImageView().getLayoutParams();
                layoutParams.width = layoutParams.height = imageSize;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutParams.setMarginEnd(imageMarginEnd);
                } else {
                    layoutParams.rightMargin = imageMarginEnd;
                }

            }

            if (drawerItem.hasTextPrimary()) {
                viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());

                if (colorPrimary > -1) {
                    viewHolder.getTextViewPrimary().setTextColor(colorPrimary);
                }

                if (drawerItem.hasTextSecondary() && (drawerItem.getTextMode() == DrawerItem.TWO_LINE || drawerItem.getTextMode() == DrawerItem.THREE_LINE)) {
                    viewHolder.getTextViewSecondary().setText(drawerItem.getTextSecondary());
                    viewHolder.getTextViewSecondary().setVisibility(View.VISIBLE);

                    if (drawerItem.getTextMode() == DrawerItem.THREE_LINE) {
                        viewHolder.getTextViewSecondary().setMaxLines(2);
                    } else {
                        viewHolder.getTextViewSecondary().setMaxLines(1);
                    }

                } else {
                    viewHolder.getTextViewSecondary().setVisibility(View.GONE);
                }
            } else if (drawerItem.hasTextSecondary()) {
                viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());

                if (colorPrimary > -1) {
                    viewHolder.getTextViewPrimary().setTextColor(colorPrimary);
                }

                viewHolder.getTextViewSecondary().setVisibility(View.GONE);
            }
        }

        return convertView;
    }


    @Override
    public boolean isEnabled(int position) {
        return getItem(position).hasOnItemClickListener();
    }

    public List<DrawerItem> getItems() {
        List<DrawerItem> items = new ArrayList<DrawerItem>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }

    public void select(int position){
        if (position >= 0 && position < getCount()) {
            selectedPosition = position;
            notifyDataSetChanged();
        }
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    private static class ViewHolder {
        LinearLayout mRoot;
        ImageView mImageView;
        TextView mTextViewPrimary;
        TextView mTextViewSecondary;

        public ViewHolder(View root) {
            mRoot = (LinearLayout) root;
            mImageView = (ImageView) root.findViewById(R.id.mdImage);
            mTextViewPrimary = (TextView) root.findViewById(R.id.mdTextPrimary);
            mTextViewSecondary = (TextView) root.findViewById(R.id.mdTextSecondary);
        }

        public LinearLayout getRoot() {
            return mRoot;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getTextViewPrimary() {
            return mTextViewPrimary;
        }

        public TextView getTextViewSecondary() {
            return mTextViewSecondary;
        }
    }
}