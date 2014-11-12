package com.heinrichreimersoftware.material_drawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MaterialDrawerAdapter extends RecyclerView.Adapter<MaterialDrawerAdapter.ViewHolder> {

    private List<MaterialDrawerItem> mDataSet;

    public MaterialDrawerAdapter(List<MaterialDrawerItem> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        /* TODO: Get element from your dataset at this position and replace the contents of the view with that element */
        MaterialDrawerItem drawerItem = mDataSet.get(position);
        if(drawerItem.hasImage()){
            viewHolder.getImageView().setImageDrawable(drawerItem.getImage());
        }
        if(drawerItem.hasTextPrimary()){
            viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());
            if(drawerItem.hasTextSecondary() && (drawerItem.getTextMode() == MaterialDrawerItem.TWO_LINE || drawerItem.getTextMode() == MaterialDrawerItem.THREE_LINE)){
                viewHolder.getTextViewSecondary().setText(drawerItem.getTextSecondary());
                if(drawerItem.getTextMode() == MaterialDrawerItem.THREE_LINE){
                    viewHolder.getTextViewSecondary().setLines(2);
                }
                else{
                    viewHolder.getTextViewSecondary().setLines(1);
                }
            }
        }
        else if(drawerItem.hasTextSecondary()){
            viewHolder.getTextViewPrimary().setText(drawerItem.getTextPrimary());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRoot;
        ImageView mImageView;
        TextView mTextViewPrimary;
        TextView mTextViewSecondary;

        public ViewHolder(View root) {
            super(root);
            mRoot = (RelativeLayout) root;
            mImageView = (ImageView) root.findViewById(R.id.image);
            mTextViewPrimary = (TextView) root.findViewById(R.id.textPrimary);
            mTextViewSecondary = (TextView) root.findViewById(R.id.textSecondary);
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