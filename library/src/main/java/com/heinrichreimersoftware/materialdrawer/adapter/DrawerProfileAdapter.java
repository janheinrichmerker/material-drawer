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

package com.heinrichreimersoftware.materialdrawer.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.R;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to be used with {@link com.heinrichreimersoftware.materialdrawer.DrawerView} to display a list of drawer items.
 */
public class DrawerProfileAdapter extends ArrayAdapter<DrawerProfile> {

    private DrawerTheme drawerTheme;

    public DrawerProfileAdapter(Context context, List<DrawerProfile> dataSet) {
        super(context, R.layout.md_drawer_item, dataSet);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerProfile drawerProfile = getItem(position);
        DrawerTheme drawerTheme = this.drawerTheme;

        if (drawerProfile.hasDrawerTheme()) {
            drawerTheme = drawerProfile.getDrawerTheme();
        }

        if (convertView == null || !(convertView instanceof RelativeLayout)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.md_drawer_item, parent, false);
        }

        final ViewHolder viewHolder = new ViewHolder(convertView);

        int textColorPrimary = drawerTheme.getTextColorPrimary();

        if (drawerTheme.isLightTheme()) {
            viewHolder.getRoot().setForeground(ContextCompat.getDrawable(getContext(), R.drawable.md_selector_light));
        } else {
            viewHolder.getRoot().setForeground(ContextCompat.getDrawable(getContext(), R.drawable.md_selector_dark));
        }

        if (drawerTheme.getBackgroundColor() != 0) {
            viewHolder.getRoot().setBackgroundColor(drawerTheme.getBackgroundColor());
        } else {
            viewHolder.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }

        if (position == 0) {
            viewHolder.getRoot().setSelected(true);
            viewHolder.getRoot().setClickable(false);

            textColorPrimary = drawerTheme.getHighlightColor();
        } else {
            viewHolder.getRoot().setSelected(false);
            viewHolder.getRoot().setClickable(true);
        }

        if (drawerProfile.hasAvatar()) {
            viewHolder.getImageView().setVisibility(View.VISIBLE);
            viewHolder.getImageView().setImageDrawable(drawerProfile.getAvatar());

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.getImageView().getLayoutParams();
            layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.md_avatar_size);
            layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.md_baseline_content) - getContext().getResources().getDimensionPixelSize(R.dimen.md_baseline);

            int imagePaddingEnd = getContext().getResources().getDimensionPixelSize(R.dimen.md_baseline_content) - getContext().getResources().getDimensionPixelSize(R.dimen.md_baseline) - getContext().getResources().getDimensionPixelSize(R.dimen.md_avatar_size);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                viewHolder.getImageView().setPaddingRelative(0, 0, imagePaddingEnd, 0);
            } else {
                viewHolder.getImageView().setPadding(0, 0, imagePaddingEnd, 0);
            }

        } else {
            viewHolder.getImageView().setVisibility(View.GONE);
        }

        if (drawerProfile.hasName()) {
            viewHolder.getTextViewPrimary().setText(drawerProfile.getName());
            viewHolder.getTextViewPrimary().setTextColor(textColorPrimary);

            if (drawerProfile.hasDescription()) {
                viewHolder.getTextViewSecondary().setText(drawerProfile.getDescription());
                viewHolder.getTextViewSecondary().setTextColor(drawerTheme.getTextColorSecondary());
                viewHolder.getTextViewSecondary().setVisibility(View.VISIBLE);
                viewHolder.getTextViewSecondary().setMaxLines(1);
            } else {
                viewHolder.getTextViewSecondary().setVisibility(View.GONE);
            }
        } else if (drawerProfile.hasDescription()) {
            viewHolder.getTextViewPrimary().setText(drawerProfile.getDescription());
            viewHolder.getTextViewPrimary().setTextColor(textColorPrimary);

            viewHolder.getTextViewSecondary().setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }

    public void setDrawerTheme(DrawerTheme theme) {
        this.drawerTheme = theme;
        notifyDataSetChanged();
    }

    public List<DrawerProfile> getItems() {
        List<DrawerProfile> items = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }

    private static class ViewHolder {
        private final FrameLayout mRoot;
        private final ImageView mImageView;
        private final TextView mTextViewPrimary;
        private final TextView mTextViewSecondary;

        public ViewHolder(View root) {
            mRoot = (FrameLayout) root;
            mImageView = (ImageView) root.findViewById(R.id.mdImage);
            mTextViewPrimary = (TextView) root.findViewById(R.id.mdTextPrimary);
            mTextViewSecondary = (TextView) root.findViewById(R.id.mdTextSecondary);
        }

        public FrameLayout getRoot() {
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