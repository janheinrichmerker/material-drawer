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
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
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

import com.heinrichreimersoftware.materialdrawer.animation.AlphaSatColorMatrixEvaluator;
import com.heinrichreimersoftware.materialdrawer.animation.AnimatableColorMatrixColorFilter;
import com.heinrichreimersoftware.materialdrawer.animation.StepInterpolator;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.widget.LinearListView;
import com.heinrichreimersoftware.materialdrawer.widget.ScrimInsetsFrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * View to be used with {@link android.support.v4.widget.DrawerLayout} to display a drawer which is fully compliant with the Material Design specification.
 */
public class DrawerView extends ScrimInsetsFrameLayout implements ScrimInsetsFrameLayout.OnInsetsCallback {

    private static final String TAG = "DrawerView";

    public static final String STATE_PROFILE_LIST_OPEN = "mdProfileListOpen";
    public static final String STATE_LIST_SELECTION = "mdListSelection";
    public static final String STATE_FIXED_LIST_SELECTION = "mdFixedListSelection";

    private DrawerProfileAdapter mProfileAdapter;
    private DrawerAdapter mAdapter;
    private DrawerAdapter mAdapterFixed;

    private DrawerProfile.OnProfileClickListener onProfileClickListener;
    private DrawerProfile.OnProfileSwitchListener onProfileSwitchListener;

    private DrawerItem.OnItemClickListener mOnItemClickListener;
    private DrawerItem.OnItemClickListener mOnFixedItemClickListener;


    private ScrollView scrollView;

    private LinearLayout layout;

    private FrameLayout frameLayoutProfile;
    private RelativeLayout relativeLayoutProfileContent;

    private ImageView imageViewProfileAvatar;
    private ImageView imageViewProfileAvatarSecondary;
    private TextView textViewProfileAvatarCount;

    private ImageView imageViewProfileBackground;
    private ImageView imageViewProfileBackgroundOverlay;
    private LinearLayout linearLayoutProfileTextContainer;
    private TextView textViewProfileName;
    private TextView textViewProfileDescription;
    private ImageView imageViewOpenProfileListIcon;

    private LinearListView linearListViewProfileList;


    private LinearListView linearListView;

    private View fixedShadow;
    private View fixedDivider;
    private LinearLayout fixedListContainer;
    private LinearListView linearListViewFixed;

    private int statusBarHeight = 0;

    private int drawerMaxWidth = -1;

    private boolean profileListOpen = false;

    private static final Property<Drawable, Integer> PROPERTY_LEVEL = new Property<Drawable, Integer>(Integer.class, "level") {
        @Override
        public Integer get(Drawable object) {
            return object.getLevel();
        }

        @Override
        public void set(Drawable object, Integer value) {
            object.setLevel(value);
        }
    };

    private static final Property<ScrollView, Integer> PROPERTY_SCROLL_POSITION = new Property<ScrollView, Integer>(Integer.class, "scrollY") {
        @Override
        public Integer get(ScrollView object) {
            return object.getScrollY();
        }

        @Override
        public void set(ScrollView object, Integer value) {
            object.scrollTo(0, value);
        }
    };

    public DrawerView(Context context) {
        this(context, null);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "DrawerView()");
        init(context);
    }

    private void init(Context context) {
        Log.d(TAG, "init()");
        removeAllViews();
        inflate(context, R.layout.md_drawer_view, this);

        findViews();

        setClipToPadding(false);
        setBackgroundColor(getResources().getColor(R.color.md_drawer_background));
        setInsetForeground(new ColorDrawable(getResources().getColor(R.color.md_inset_foreground)));

        setOnInsetsCallback(this);

        mProfileAdapter = new DrawerProfileAdapter(context, new ArrayList<DrawerProfile>());
        linearListViewProfileList.setAdapter(mProfileAdapter);
        linearListViewProfileList.setClickable(true);
        linearListViewProfileList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                if (position != 0 && profileListOpen) {
                    selectProfile(mProfileAdapter.getItem(position));
                }
            }
        });

        mAdapter = new DrawerAdapter(context, new ArrayList<DrawerItem>());
        linearListView.setAdapter(mAdapter);
        linearListView.setClickable(true);
        linearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                if (!profileListOpen) {
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
            }
        });

        mAdapterFixed = new DrawerAdapter(context, new ArrayList<DrawerItem>());
        linearListViewFixed.setAdapter(mAdapterFixed);
        linearListViewFixed.setClickable(true);
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

        imageViewOpenProfileListIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleProfileList();
            }
        });

        frameLayoutProfile.setVisibility(GONE);
        layout.setPadding(0, statusBarHeight, 0, 0);
        updateListVisibility();
    }


    private void findViews() {
        Log.d(TAG, "findViews()");

        scrollView = (ScrollView) findViewById(R.id.mdScrollView);

        layout = (LinearLayout) findViewById(R.id.mdLayout);

        frameLayoutProfile = (FrameLayout) findViewById(R.id.mdLayoutProfile);
        relativeLayoutProfileContent = (RelativeLayout) findViewById(R.id.mdLayoutProfileContent);

        imageViewProfileAvatar = (ImageView) findViewById(R.id.mdAvatarProfile);
        imageViewProfileAvatarSecondary = (ImageView) findViewById(R.id.mdAvatarProfileSecondary);
        textViewProfileAvatarCount = (TextView) findViewById(R.id.mdAvatarProfileCount);

        imageViewProfileBackground = (ImageView) findViewById(R.id.mdProfileBackground);
        imageViewProfileBackgroundOverlay = (ImageView) findViewById(R.id.mdProfileBackgroundOverlay);
        linearLayoutProfileTextContainer = (LinearLayout) findViewById(R.id.mdProfileTextContainer);
        textViewProfileName = (TextView) findViewById(R.id.mdProfileName);
        textViewProfileDescription = (TextView) findViewById(R.id.mdProfileDescription);
        imageViewOpenProfileListIcon = (ImageView) findViewById(R.id.mdOpenProfileListIcon);
        linearListViewProfileList = (LinearListView) findViewById(R.id.mdProfileList);


        linearListView = (LinearListView) findViewById(R.id.mdList);

        fixedShadow = findViewById(R.id.mdFixedShadow);
        fixedDivider = findViewById(R.id.mdFixedDivider);
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

        int aspectRatioHeight = Math.round(getLayoutParams().width / 16 * 9) - statusBarHeight;
        int minHeight = getResources().getDimensionPixelSize(R.dimen.md_baseline);

        if (!mProfileAdapter.isEmpty()) {
            if (mProfileAdapter.getItem(0) != null && mProfileAdapter.getItem(0).hasAvatar()) {
                minHeight += getResources().getDimensionPixelSize(R.dimen.md_big_avatar_size);
            }
            if (mProfileAdapter.getItem(0) != null && mProfileAdapter.getItem(0).hasName()) {
                minHeight += getResources().getDimensionPixelSize(R.dimen.md_list_item_height);
            }
            if (mProfileAdapter.getItem(0) != null && mProfileAdapter.getItem(0).hasDescription()) {
                minHeight += getResources().getDimensionPixelSize(R.dimen.md_baseline);
            }
        }

        frameLayoutProfile.getLayoutParams().height = Math.max(aspectRatioHeight, minHeight) + statusBarHeight;
        relativeLayoutProfileContent.getLayoutParams().height = Math.max(aspectRatioHeight, minHeight);
    }

    private void updateProfile() {
        Log.d(TAG, "updateProfile()");
        if (mProfileAdapter.getCount() > 0) {

            final DrawerProfile currentProfile = mProfileAdapter.getItem(0);

            if (mProfileAdapter.getCount() > 2) {
                /* More than two profiles. Should show a little badge. */
                imageViewProfileAvatarSecondary.setVisibility(GONE);

                textViewProfileAvatarCount.setVisibility(VISIBLE);
                textViewProfileAvatarCount.setText("+" + (mProfileAdapter.getCount() - 1));
                textViewProfileAvatarCount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfileList();
                    }
                });

                if (currentProfile.getBackground() instanceof BitmapDrawable) {
                    Palette.generateAsync(((BitmapDrawable) currentProfile.getBackground()).getBitmap(), new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null) {
                                textViewProfileAvatarCount.setTextColor(vibrantSwatch.getTitleTextColor());
                                textViewProfileAvatarCount.getBackground().setColorFilter(vibrantSwatch.getRgb(), PorterDuff.Mode.SRC_IN);
                            }
                        }
                    });
                }
            } else if (mProfileAdapter.getCount() == 2) {
                /* Two profiles. Should show the second profile avatar. */
                final DrawerProfile secondProfile = mProfileAdapter.getItem(1);
                imageViewProfileAvatarSecondary.setVisibility(VISIBLE);
                imageViewProfileAvatarSecondary.setImageDrawable(secondProfile.getAvatar());
                imageViewProfileAvatarSecondary.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectProfile(secondProfile);
                    }
                });

                textViewProfileAvatarCount.setVisibility(GONE);
                closeProfileList();
            }

            if (currentProfile.getAvatar() != null) {
                imageViewProfileAvatar.setImageDrawable(currentProfile.getAvatar());
            }
            if (currentProfile.getName() != null && !currentProfile.getName().equals("")) {
                textViewProfileName.setText(currentProfile.getName());
            }

            if (currentProfile.getBackground() != null) {
                imageViewProfileBackground.setImageDrawable(currentProfile.getBackground());
            } else {
                int colorPrimary = getResources().getColor(R.color.primary_dark_material_light);
                TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
                try {
                    colorPrimary = a.getColor(0, 0);
                } finally {
                    a.recycle();
                }

                imageViewProfileBackground.setImageDrawable(new ColorDrawable(colorPrimary));
            }

            if (currentProfile.getDescription() != null && !currentProfile.getDescription().equals("")) {
                textViewProfileDescription.setVisibility(VISIBLE);
                textViewProfileDescription.setText(currentProfile.getDescription());
            } else {
                textViewProfileDescription.setVisibility(GONE);
            }

            if (currentProfile.hasOnProfileClickListener()) {
                frameLayoutProfile.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentProfile.getOnProfileClickListener().onClick(currentProfile, currentProfile.getId());
                    }
                });

                frameLayoutProfile.setEnabled(true);
            } else {
                if (hasOnProfileClickListener()) {
                    frameLayoutProfile.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getOnProfileClickListener().onClick(currentProfile, currentProfile.getId());
                        }
                    });

                    frameLayoutProfile.setEnabled(true);
                } else {
                    frameLayoutProfile.setEnabled(false);
                }
            }


            frameLayoutProfile.setVisibility(VISIBLE);
            layout.setPadding(0, 0, 0, 0);
        } else {
            frameLayoutProfile.setVisibility(GONE);
            layout.setPadding(0, statusBarHeight, 0, 0);
            closeProfileList();
        }
    }

    private void updateList() {
        Log.d(TAG, "updateList()");

        if (mAdapter.getCount() <= 1) {
            updateListVisibility();
        }
    }

    private void updateFixedList() {
        Log.d(TAG, "updateFixedList()");

        if (mAdapterFixed.getCount() <= 1) {
            updateListVisibility();
        }
    }

    private void updateListVisibility() {
        Log.d(TAG, "updateListVisibility()");

        if (profileListOpen && mProfileAdapter.getCount() > 0) {
            Log.d(TAG, "updateListVisibility() - show Profile List");
            linearListViewProfileList.setVisibility(VISIBLE);
        } else {
            Log.d(TAG, "updateListVisibility() - hide Profile List");
            linearListViewProfileList.setVisibility(GONE);
        }

        if (!profileListOpen && mAdapter.getCount() > 0) {
            Log.d(TAG, "updateListVisibility() - show Items List");
            linearListView.setVisibility(VISIBLE);
        } else {
            Log.d(TAG, "updateListVisibility() - hide Items List");
            linearListView.setVisibility(GONE);
        }

        if (mAdapterFixed.getCount() > 0) {
            fixedListContainer.setVisibility(VISIBLE);

            if ((profileListOpen && mProfileAdapter.getCount() > 0) || (!profileListOpen && mAdapter.getCount() > 0)) {
                fixedDivider.setVisibility(VISIBLE);
                fixedShadow.setVisibility(VISIBLE);
            } else {
                fixedDivider.setVisibility(GONE);
                fixedShadow.setVisibility(GONE);
            }
        } else {
            fixedListContainer.setVisibility(GONE);
        }

        layout.invalidate();
    }

    private boolean animateToProfile(DrawerProfile profile) {
        Log.d(TAG, "animateToProfile(*" + profile.getId() + ")");

        if (mProfileAdapter.getCount() > 1) {
            List<Animator> animators = new ArrayList<>();
            List<Animator.AnimatorListener> listeners = new ArrayList<>();

            final DrawerProfile oldProfile = mProfileAdapter.getItem(0);
            final DrawerProfile newProfile = profile;


            /* Background animation */

            AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator();
            final AnimatableColorMatrixColorFilter filter = new AnimatableColorMatrixColorFilter(evaluator.getColorMatrix());

            ObjectAnimator backgroundAnimator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());
            backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    imageViewProfileBackgroundOverlay.setColorFilter(filter.getColorFilter());
                }
            });
            animators.add(backgroundAnimator);

            listeners.add(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    imageViewProfileBackground.setImageDrawable(oldProfile.getBackground());

                    imageViewProfileBackgroundOverlay.setImageDrawable(newProfile.getBackground());
                    imageViewProfileBackgroundOverlay.setColorFilter(filter.getColorFilter());
                    imageViewProfileBackgroundOverlay.setVisibility(VISIBLE);

                    imageViewProfileAvatarSecondary.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageViewProfileBackground.setImageDrawable(newProfile.getBackground());

                    imageViewProfileBackgroundOverlay.setVisibility(GONE);

                    if (hasOnProfileSwitchListener()) {
                        onProfileSwitchListener.onSwitch(oldProfile, oldProfile.getId(), newProfile, newProfile.getId());
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });


            /* Text animation */

            AnimatorSet textSet = new AnimatorSet();

            AnimatorSet textOutSet = new AnimatorSet();
            textOutSet.playTogether(
                    ObjectAnimator.ofFloat(linearLayoutProfileTextContainer, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(linearLayoutProfileTextContainer, "translationX", 0, getWidth() / 4)
            );
            textOutSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (newProfile.hasName()) {
                        textViewProfileName.setText(newProfile.getName());
                        textViewProfileName.setVisibility(VISIBLE);
                    } else {
                        textViewProfileName.setVisibility(GONE);
                    }
                    if (newProfile.hasDescription()) {
                        textViewProfileDescription.setText(newProfile.getDescription());
                        textViewProfileDescription.setVisibility(VISIBLE);
                    } else {
                        textViewProfileDescription.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            AnimatorSet textInSet = new AnimatorSet();
            textInSet.playTogether(
                    ObjectAnimator.ofFloat(linearLayoutProfileTextContainer, "alpha", 0, 1),
                    ObjectAnimator.ofFloat(linearLayoutProfileTextContainer, "translationX", -getWidth() / 4, 0)
            );

            textSet.playSequentially(
                    textOutSet,
                    textInSet
            );
            animators.add(textSet);

            AnimatorSet profileSet = new AnimatorSet();
            if (mProfileAdapter.getCount() > 2) {
                AnimatorSet profileOutSet = new AnimatorSet();
                profileOutSet.playTogether(
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "alpha", 1, 0),
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleX", 1, 0.5f),
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleY", 1, 0.5f)
                );
                profileOutSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        linearListViewProfileList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(LinearListView parent, View view, int position, long id) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageViewProfileAvatar.setImageDrawable(newProfile.getAvatar());

                        linearListViewProfileList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(LinearListView parent, View view, int position, long id) {
                                if (position != 0) {
                                    selectProfile(mProfileAdapter.getItem(position));
                                }
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                AnimatorSet profileInSet = new AnimatorSet();
                profileInSet.playTogether(
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "alpha", 0, 1),
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleX", 0.5f, 1),
                        ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleY", 0.5f, 1)
                );

                profileSet.playSequentially(
                        profileOutSet,
                        profileInSet
                );
                animators.add(profileSet);
            } else {

                /* Avatar animation */

                int translation = imageViewProfileAvatarSecondary.getLeft() - getResources().getDimensionPixelSize(R.dimen.md_baseline);
                float scale = getResources().getDimension(R.dimen.md_avatar_size) / getResources().getDimension(R.dimen.md_big_avatar_size);
                float translationCorrect = (getResources().getDimension(R.dimen.md_avatar_size) - getResources().getDimension(R.dimen.md_big_avatar_size)) / 2;

                ObjectAnimator stepTranslateXAnimator = ObjectAnimator.ofFloat(imageViewProfileAvatar, "translationX", 0, translation + translationCorrect);
                stepTranslateXAnimator.setInterpolator(new StepInterpolator());
                animators.add(stepTranslateXAnimator);

                ObjectAnimator stepTranslateYAnimator = ObjectAnimator.ofFloat(imageViewProfileAvatar, "translationY", 0, translationCorrect);
                stepTranslateYAnimator.setInterpolator(new StepInterpolator());
                animators.add(stepTranslateYAnimator);

                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatar, "alpha", 1, 0, 1));
                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleX", 1, 0.5f, scale));
                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatar, "scaleY", 1, 0.5f, scale));
                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatarSecondary, "translationX", 0, -translation));
                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatarSecondary, "scaleX", 1, 1 / scale));
                animators.add(ObjectAnimator.ofFloat(imageViewProfileAvatarSecondary, "scaleY", 1, 1 / scale));

                listeners.add(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ViewHelper.setPivotX(imageViewProfileAvatarSecondary, 0);
                        ViewHelper.setPivotY(imageViewProfileAvatarSecondary, 0);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(TAG, "onAnimationEnd()");
                        imageViewProfileAvatar.setImageDrawable(newProfile.getAvatar());
                        imageViewProfileAvatarSecondary.setImageDrawable(oldProfile.getAvatar());

                        ViewHelper.setTranslationX(imageViewProfileAvatar, 0);
                        ViewHelper.setTranslationY(imageViewProfileAvatar, 0);
                        ViewHelper.setScaleX(imageViewProfileAvatar, 1);
                        ViewHelper.setScaleY(imageViewProfileAvatar, 1);
                        ViewHelper.setTranslationX(imageViewProfileAvatarSecondary, 0);
                        ViewHelper.setScaleX(imageViewProfileAvatarSecondary, 1);
                        ViewHelper.setScaleY(imageViewProfileAvatarSecondary, 1);

                        imageViewProfileAvatarSecondary.setClickable(true);

                        updateProfile();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            if (animators.size() > 0) {
            /* Play animation */
                AnimatorSet set = new AnimatorSet();
                set.playTogether(animators);
                set.setDuration(getResources().getInteger(R.integer.md_profile_switching_anim_time));
                textSet.setDuration(getResources().getInteger(R.integer.md_profile_switching_anim_time) / 2);
                profileSet.setDuration(getResources().getInteger(R.integer.md_profile_switching_anim_time) / 2);
                for (Animator.AnimatorListener listener : listeners) {
                    set.addListener(listener);
                }
                set.start();

                return true;
            }
        }
        return false;
    }

    public void toggleProfileList() {
        Log.d(TAG, "toggleProfileList()");
        if (profileListOpen) {
            closeProfileList();
        } else {
            openProfileList();
        }
    }

    public void openProfileList() {
        Log.d(TAG, "openProfileList()");
        if (!profileListOpen) {
            Log.d(TAG, "openProfileList() 1");
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(linearListView, "alpha", 1, 0f, 0f, 0f),
                    ObjectAnimator.ofFloat(linearListView, "translationY", 0, getResources().getDimensionPixelSize(R.dimen.md_list_item_height) / 4),
                    ObjectAnimator.ofFloat(linearListViewProfileList, "alpha", 0f, 1),
                    ObjectAnimator.ofFloat(linearListViewProfileList, "translationY", -getResources().getDimensionPixelSize(R.dimen.md_list_item_height) / 2, 0),
                    ObjectAnimator.ofInt(imageViewOpenProfileListIcon.getDrawable(), PROPERTY_LEVEL, 0, 10000),
                    ObjectAnimator.ofInt(scrollView, PROPERTY_SCROLL_POSITION, 0)
            );
            set.setDuration(getResources().getInteger(R.integer.md_profile_list_open_anim_time));
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    linearListViewProfileList.setVisibility(VISIBLE);
                    imageViewOpenProfileListIcon.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageViewOpenProfileListIcon.setClickable(true);

                    profileListOpen = true;

                    updateListVisibility();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            set.start();
        } else {
            Log.d(TAG, "openProfileList() 2");
            updateListVisibility();
        }
    }

    public void closeProfileList() {
        Log.d(TAG, "closeProfileList()");
        if (profileListOpen) {
            Log.d(TAG, "closeProfileList() 1");
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(linearListViewProfileList, "alpha", 1, 0f, 0f, 0f),
                    ObjectAnimator.ofFloat(linearListViewProfileList, "translationY", 0, -getResources().getDimensionPixelSize(R.dimen.md_list_item_height) / 4),
                    ObjectAnimator.ofFloat(linearListView, "alpha", 0f, 1),
                    ObjectAnimator.ofFloat(linearListView, "translationY", getResources().getDimensionPixelSize(R.dimen.md_list_item_height) / 2, 0),
                    ObjectAnimator.ofInt(imageViewOpenProfileListIcon.getDrawable(), PROPERTY_LEVEL, 10000, 0),
                    ObjectAnimator.ofInt(scrollView, PROPERTY_SCROLL_POSITION, 0)
            );
            set.setDuration(getResources().getInteger(R.integer.md_profile_list_open_anim_time));
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    linearListView.setVisibility(VISIBLE);
                    imageViewOpenProfileListIcon.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageViewOpenProfileListIcon.setClickable(true);

                    profileListOpen = false;

                    updateListVisibility();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            set.start();
        } else {
            Log.d(TAG, "closeProfileList() 2");
            updateListVisibility();
        }
    }


    /**
     * Sets the max drawer width
     *
     * @param drawerMaxWidth Max drawer width to set
     */
    public DrawerView setDrawerMaxWidth(int drawerMaxWidth) {
        this.drawerMaxWidth = drawerMaxWidth;
        updateDrawerWidth();
        return this;
    }

    /**
     * Sets the max drawer width from resources
     *
     * @param drawerMaxWidthResource Max drawer width resource to set
     */
    public DrawerView setDrawerMaxWidthResource(int drawerMaxWidthResource) {
        drawerMaxWidth = getResources().getDimensionPixelSize(drawerMaxWidthResource);
        updateDrawerWidth();
        return this;
    }

    /**
     * Resets the max drawer width
     */
    public DrawerView resetDrawerMaxWidth() {
        this.drawerMaxWidth = getResources().getDimensionPixelSize(R.dimen.md_drawer_max_width);
        updateDrawerWidth();
        return this;
    }

    /**
     * Gets the max drawer width
     */
    public int getDrawerMaxWidth() {
        return drawerMaxWidth;
    }


    /**
     * Adds a profile to the drawer view
     *
     * @param profile Profile to add
     */
    public DrawerView addProfile(DrawerProfile profile) {
        if (profile.getId() <= 0) {
            profile.setId(System.currentTimeMillis());
        }
        for (DrawerProfile oldProfile : mProfileAdapter.getItems()) {
            if (oldProfile.getId() == profile.getId()) {
                mProfileAdapter.remove(oldProfile);
                break;
            }
        }
        profile.attachTo(this);
        mProfileAdapter.add(profile);
        if (mProfileAdapter.getCount() == 1) {
            selectProfile(profile);
        }
        updateProfile();
        return this;
    }

    /**
     * Gets all profiles from the drawer view
     *
     * @return Profiles from the drawer view
     */
    public List<DrawerProfile> getProfiles() {
        return mProfileAdapter.getItems();
    }

    /**
     * Gets a profile from the drawer view
     *
     * @param id The profile ID
     * @return Profile from the drawer view
     */
    public DrawerProfile findProfileById(long id) {
        for (DrawerProfile profile : mProfileAdapter.getItems()) {
            if (profile.getId() == id) {
                return profile;
            }
        }
        return null;
    }

    /**
     * Selects a profile from the drawer view
     *
     * @param profile The profile
     */
    public DrawerView selectProfile(DrawerProfile profile) {
        if (mProfileAdapter.getItems().contains(profile)) {
            DrawerProfile oldProfile = mProfileAdapter.getItem(0);

            if (mProfileAdapter.getCount() > 1) {
                animateToProfile(profile);

                mProfileAdapter.remove(profile);
                mProfileAdapter.insert(profile, 0);
            } else {
                mProfileAdapter.remove(profile);
                mProfileAdapter.insert(profile, 0);

                updateProfile();

                if (hasOnProfileSwitchListener()) {
                    onProfileSwitchListener.onSwitch(oldProfile, oldProfile.getId(), profile, profile.getId());
                }
            }
        }
        return this;
    }

    /**
     * Selects a profile from the drawer view
     *
     * @param id The profile ID
     */
    public DrawerView selectProfileById(long id) {
        for (DrawerProfile profile : mProfileAdapter.getItems()) {
            if (profile.getId() == id) {
                selectProfile(profile);
                return this;
            }
        }
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param profile Profile to remove
     */
    public DrawerView removeProfile(DrawerProfile profile) {
        profile.detach();
        mProfileAdapter.remove(profile);
        updateProfile();
        return this;
    }

    /**
     * Removes a profile from the drawer view
     *
     * @param id ID to remove
     */
    public DrawerView removeProfileById(long id) {
        for (DrawerProfile profile : mProfileAdapter.getItems()) {
            if (profile.getId() == id) {
                profile.detach();
                mProfileAdapter.remove(profile);
                updateProfile();
                return this;
            }
        }
        return this;
    }

    /**
     * Removes all profiles from the drawer view
     */
    public DrawerView clearProfiles() {
        for (DrawerProfile profile : mProfileAdapter.getItems()) {
            profile.detach();
        }
        mProfileAdapter.clear();
        updateProfile();
        return this;
    }


    /**
     * Sets a profile click listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerView setOnProfileClickListener(DrawerProfile.OnProfileClickListener listener) {
        onProfileClickListener = listener;
        return this;
    }

    /**
     * Gets the profile click listener of the drawer
     *
     * @return Profile click listener of the drawer
     */
    public DrawerProfile.OnProfileClickListener getOnProfileClickListener() {
        return onProfileClickListener;
    }

    /**
     * Gets whether the drawer has a profile click listener set to it
     *
     * @return True if the drawer has a profile click listener set to it, false otherwise.
     */
    public boolean hasOnProfileClickListener() {
        return onProfileClickListener != null;
    }

    /**
     * Removes the profile click listener from the drawer
     */
    public DrawerView removeOnProfileClickListener() {
        onProfileClickListener = null;
        return this;
    }


    /**
     * Sets a profile switch listener to the drawer
     *
     * @param listener Listener to set
     */
    public DrawerView setOnProfileSwitchListener(DrawerProfile.OnProfileSwitchListener listener) {
        onProfileSwitchListener = listener;
        return this;
    }

    /**
     * Gets the profile switch listener of the drawer
     *
     * @return Profile switch listener of the drawer
     */
    public DrawerProfile.OnProfileSwitchListener getOnProfileSwitchListener() {
        return onProfileSwitchListener;
    }

    /**
     * Gets whether the drawer has a profile switch listener set to it
     *
     * @return True if the drawer has a profile switch listener set to it, false otherwise.
     */
    public boolean hasOnProfileSwitchListener() {
        return onProfileSwitchListener != null;
    }

    /**
     * Removes the profile switch listener from the drawer
     */
    public DrawerView removeOnProfileSwitchListener() {
        onProfileSwitchListener = null;
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
            if (item.getId() <= 0) {
                item.setId(System.currentTimeMillis());
            }
            for (DrawerItem oldItem : mAdapter.getItems()) {
                if (oldItem.getId() == item.getId()) {
                    mAdapter.remove(oldItem);
                    break;
                }
            }

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
        if (item.getId() <= 0) {
            item.setId(System.currentTimeMillis());
        }
        for (DrawerItem oldItem : mAdapter.getItems()) {
            if (oldItem.getId() == item.getId()) {
                mAdapter.remove(oldItem);
                break;
            }
        }

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
    public DrawerItem findItemById(long id) {
        for (DrawerItem item : mAdapter.getItems()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Selects an item from the drawer view
     *
     * @param position The item position
     */
    public DrawerView selectItem(int position) {
        mAdapterFixed.clearSelection();
        mAdapter.select(position);
        return this;
    }

    /**
     * Gets the selected item position of the drawer view
     *
     * @return Position of the selected item
     */
    public int getSelectedPosition() {
        return mAdapter.getSelectedPosition();
    }

    /**
     * Selects an item from the drawer view
     *
     * @param id The item ID
     */
    public DrawerView selectItemById(long id) {
        mAdapterFixed.clearSelection();

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (mAdapter.getItem(i).getId() == id) {
                mAdapter.select(i);
                return this;
            }
        }
        return this;
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
     * Removes an item from the drawer view
     *
     * @param id ID to remove
     */
    public DrawerView removeItemById(long id) {
        for (DrawerItem item : mAdapter.getItems()) {
            if (item.getId() == id) {
                mAdapter.remove(item);
                updateList();
                return this;
            }
        }
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
            if (item.getId() <= 0) {
                item.setId(System.currentTimeMillis());
            }
            for (DrawerItem oldItem : mAdapterFixed.getItems()) {
                if (oldItem.getId() == item.getId()) {
                    mAdapterFixed.remove(oldItem);
                    break;
                }
            }

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
        if (item.getId() <= 0) {
            item.setId(System.currentTimeMillis());
        }
        for (DrawerItem oldItem : mAdapterFixed.getItems()) {
            if (oldItem.getId() == item.getId()) {
                mAdapterFixed.remove(oldItem);
                break;
            }
        }

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
    public DrawerItem findFixedItemById(long id) {
        for (DrawerItem item : mAdapterFixed.getItems()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Selects a fixed item from the drawer view
     *
     * @param position The item position
     */
    public DrawerView selectFixedItem(int position) {
        mAdapter.clearSelection();
        mAdapterFixed.select(position);
        return this;
    }

    /**
     * Gets the selected fixed item position of the drawer view
     *
     * @return Position of the selected item
     */
    public int getSelectedFixedPosition() {
        return mAdapterFixed.getSelectedPosition();
    }

    /**
     * Selects a fixed item from the drawer view
     *
     * @param id The item ID
     */
    public DrawerView selectFixedItemById(long id) {
        mAdapter.clearSelection();

        int count = mAdapterFixed.getCount();
        for (int i = 0; i < count; i++) {
            if (mAdapterFixed.getItem(i).getId() == id) {
                mAdapterFixed.select(i);
                return this;
            }
        }
        return this;
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
     * Removes a fixed item from the drawer view
     *
     * @param id ID to remove
     */
    public DrawerView removeFixedItemById(long id) {
        for (DrawerItem item : mAdapterFixed.getItems()) {
            if (item.getId() == id) {
                mAdapterFixed.remove(item);
                updateFixedList();
                return this;
            }
        }
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
     * Removes the fixed item click listener from the drawer view.
     */
    public DrawerView removeOnFixedItemClickListener() {
        mOnFixedItemClickListener = null;
        updateFixedList();
        return this;
    }


    @Override
    public void onInsetsChanged(Rect insets) {
        Log.d(TAG, "onInsetsChanged()");
        if (statusBarHeight != insets.top) {
            statusBarHeight = insets.top;
            updateProfileSpacing();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        Log.d(TAG, "onSizeChanged(" + w + ", " + h + ", " + oldW + ", " + oldH + ")");
        super.onSizeChanged(w, h, oldW, oldH);

        if (w != oldW) {
            if (oldW == 0) {
                updateListVisibility();
            }

            if (drawerMaxWidth <= 0) {
                if (getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT && getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                    setDrawerMaxWidth(getLayoutParams().width);
                } else {
                    resetDrawerMaxWidth();
                }
            }

            updateDrawerWidth();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState()");
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putParcelable("instanceState", super.onSaveInstanceState());


        savedInstanceState.putBoolean(STATE_PROFILE_LIST_OPEN, profileListOpen);
        savedInstanceState.putInt(STATE_LIST_SELECTION, mAdapter.getSelectedPosition());
        savedInstanceState.putInt(STATE_FIXED_LIST_SELECTION, mAdapterFixed.getSelectedPosition());

        return savedInstanceState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState()");
        if (state instanceof Bundle) {
            Bundle savedInstanceState = (Bundle) state;

            if (savedInstanceState.containsKey(STATE_PROFILE_LIST_OPEN)) {
                if (savedInstanceState.getBoolean(STATE_PROFILE_LIST_OPEN, false)) {
                    openProfileList();
                } else {
                    closeProfileList();
                }
            } else {
                closeProfileList();
            }
            if (savedInstanceState.containsKey(STATE_LIST_SELECTION)) {
                int listSelection = savedInstanceState.getInt(STATE_LIST_SELECTION, -1);
                if (listSelection >= 0 && listSelection < mAdapter.getCount()) {
                    selectItem(listSelection);
                } else {
                    if (savedInstanceState.containsKey(STATE_FIXED_LIST_SELECTION)) {
                        int fixedListSelection = savedInstanceState.getInt(STATE_FIXED_LIST_SELECTION, -1);
                        if (fixedListSelection >= 0 && fixedListSelection < mAdapterFixed.getCount()) {
                            selectFixedItem(fixedListSelection);
                        }
                    }
                }
            }

            state = savedInstanceState.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
