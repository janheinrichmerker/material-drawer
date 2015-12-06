/*
 * Copyright 2012 Frankie Sardo
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

package com.heinrichreimersoftware.materialdrawer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.heinrichreimersoftware.materialdrawer.R;

/**
 * An extension of a linear layout that supports the divider API of Android
 * 4.0+. You can populate this layout with data that comes from a
 * {@link android.widget.ListAdapter}
 */
public class LinearListView extends IcsLinearLayout {

    private static final int[] R_styleable_LinearListView = new int[]{
        /* 0 */android.R.attr.entries,
        /* 1 */R.attr.dividerThickness
    };

    private static final int LinearListView_entries = 0;
    private static final int LinearListView_dividerThickness = 1;

    private View mEmptyView;
    private ListAdapter mAdapter;
    private boolean mAreAllItemsSelectable;
    private final DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }

    };
    private OnItemClickListener mOnItemClickListener;

    public LinearListView(Context context) {
        this(context, null);
    }

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R_styleable_LinearListView);

        // Use the thickness specified, zero being the default
        final int thickness = a.getDimensionPixelSize(
                LinearListView_dividerThickness, 0);
        if (thickness != 0) {
            setDividerThickness(thickness);
        }

        CharSequence[] entries = a.getTextArray(LinearListView_entries);
        if (entries != null) {
            setAdapter(new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, entries));
        }

        a.recycle();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void setOrientation(int orientation) {
        if (orientation != getOrientation()) {
            int tmp = mDividerHeight;
            mDividerHeight = mDividerWidth;
            mDividerWidth = tmp;
        }
        super.setOrientation(orientation);
    }

    /**
     * Set the divider thickness size in pixel. That means setting the divider
     * height if the layout has an HORIZONTAL orientation and setting the
     * divider width otherwise.
     *
     * @param thickness The divider thickness in pixel.
     */
    public void setDividerThickness(int thickness) {
        if (getOrientation() == VERTICAL) {
            mDividerHeight = thickness;
        } else {
            mDividerWidth = thickness;
        }
        requestLayout();
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets the data behind this LinearListView.
     *
     * @param adapter The ListAdapter which is responsible for maintaining the data
     *                backing this list and for producing a view to represent an
     *                item in that data set.
     * @see #getAdapter()
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
            mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
        }

        setupChildren();

    }

    /**
     * @return The callback to be invoked with an item in this LinearListView has
     * been clicked, or null id no callback has been set.
     */
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Register a callback to be invoked when an item in this LinearListView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Call the OnItemClickListener, if it is defined.
     *
     * @param view     The view within the LinearListView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     * @return True if there was an assigned OnItemClickListener that was
     * called, false otherwise is returned.
     */
    public boolean performItemClick(View view, int position, long id) {
        if (mOnItemClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            mOnItemClickListener.onItemClick(this, view, position, id);
            return true;
        }

        return false;
    }

    /**
     * When the current adapter is empty, the LinearListView can display a special
     * view call the empty view. The empty view is used to provide feedback to
     * the user that no data is available in this LinearListView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        final ListAdapter adapter = getAdapter();
        final boolean empty = ((adapter == null) || adapter.isEmpty());
        updateEmptyStatus(empty);
    }

    /**
     * Update the status of the list based on the empty parameter. If empty is
     * true and we have an empty view, display it. In all the other cases, make
     * sure that the layout is VISIBLE and that the empty view is GONE (if
     * it's not null).
     */
    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list
                // view is visible
                setVisibility(View.VISIBLE);
            }
        } else {
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    private void setupChildren() {

        removeAllViews();

        updateEmptyStatus((mAdapter == null) || mAdapter.isEmpty());

        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View child = mAdapter.getView(i, null, this);
            if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
                child.setOnClickListener(new InternalOnClickListener(i));
            }
            addViewInLayout(child, -1, child.getLayoutParams(), true);
        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * LinearListView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this LinearListView has
         * been clicked.
         * <p/>
         * Implementers can call getItemAtPosition(position) if they need to
         * access the data associated with the selected item.
         *
         * @param parent   The LinearListView where the click happened.
         * @param view     The view within the LinearListView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        void onItemClick(LinearListView parent, View view, int position, long id);
    }

    /**
     * Internal OnClickListener that this view associate of each of its children
     * so that they can respond to OnItemClick listener's events. Avoid setting
     * an OnClickListener manually. If you need it you can wrap the child in a
     * simple {@link android.widget.FrameLayout}.
     */
    private class InternalOnClickListener implements OnClickListener {

        private final int mPosition;

        public InternalOnClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if ((mOnItemClickListener != null) && (mAdapter != null)) {
                mOnItemClickListener.onItemClick(LinearListView.this, v,
                        mPosition, mAdapter.getItemId(mPosition));
            }
        }
    }
}