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

package com.heinrichreimersoftware.material_drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.material_drawer.structure.DrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to be used with {@link DrawerView} to display a list of drawer items.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    public DrawerAdapter(Context context, List<DrawerItem> dataSet) {
        super(context, R.layout.drawer_item, dataSet);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem drawerItem = getItem(position);

        if(drawerItem.isDivider()){
            if (convertView == null || convertView instanceof RelativeLayout) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_divider_item, parent, false);
            }
        }

        else{
            if (convertView == null || !(convertView instanceof RelativeLayout)) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent, false);
            }

            ViewHolder viewHolder = new ViewHolder(convertView);

            if(drawerItem.hasImage()){
                viewHolder.getImageView().setImageDrawable(drawerItem.getImage());

                ViewGroup.LayoutParams layoutParams = viewHolder.getImageView().getLayoutParams();
                if(drawerItem.getImageMode() == DrawerItem.AVATAR){
                    layoutParams.width = layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.avatar_size);
                    viewHolder.getRoot().getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.list_item_height_image_text);
                }
                else{
                    layoutParams.width = layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.icon_size);
                    viewHolder.getRoot().getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.list_item_height_icon_text);
                }
            }

            if(drawerItem.hasTextPrimary()){
                viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());
                if(drawerItem.hasTextSecondary() && (drawerItem.getTextMode() == DrawerItem.TWO_LINE || drawerItem.getTextMode() == DrawerItem.THREE_LINE)){
                    viewHolder.getTextViewSecondary().setText(drawerItem.getTextSecondary());
                    if(drawerItem.getTextMode() == DrawerItem.THREE_LINE){
                        viewHolder.getTextViewSecondary().setLines(2);
                        viewHolder.getTextViewSecondary().setVisibility(View.VISIBLE);
                        viewHolder.getRoot().getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.list_item_height_icon_text_three_line);
                    }
                    else{
                        viewHolder.getTextViewSecondary().setLines(1);
                        viewHolder.getTextViewSecondary().setVisibility(View.VISIBLE);
                        viewHolder.getRoot().getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.list_item_height_icon_text_two_line);
                    }
                }
                else{
                    viewHolder.getTextViewSecondary().setVisibility(View.GONE);
                }
            }
            else if(drawerItem.hasTextSecondary()){
                viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());
                viewHolder.getTextViewSecondary().setVisibility(View.GONE);
            }
        }

        return convertView;
    }


    @Override
    public boolean isEnabled(int position) {
        return getItem(position).hasOnItemClickListener();
    }

    public List<DrawerItem> getItems(){
        List<DrawerItem> items = new ArrayList<DrawerItem>();
        for(int i = 0; i < getCount(); i++){
            items.add(getItem(i));
        }
        return items;
    }

    private static class ViewHolder{
        RelativeLayout mRoot;
        ImageView mImageView;
        TextView mTextViewPrimary;
        TextView mTextViewSecondary;

        public ViewHolder(View root) {
            mRoot = (RelativeLayout) root;
            mImageView = (ImageView) root.findViewById(R.id.mdImage);
            mTextViewPrimary = (TextView) root.findViewById(R.id.mdTextPrimary);
            mTextViewSecondary = (TextView) root.findViewById(R.id.mdTextSecondary);
        }

        public RelativeLayout getRoot() {
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