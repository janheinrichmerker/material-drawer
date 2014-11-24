material-drawer
===============
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-material--drawer-blue.svg?style=flat)](https://android-arsenal.com/details/1/1162)

Custom drawer implementation for Material design apps.

How-To-Use
----------
**Step 1:** Include it in your layout:

    <android.support.v4.widget.DrawerLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/drawerLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true">
        
        <com.heinrichreimersoftware.material_drawer.DrawerView
            android:id="@+id/drawer"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            android:fitsSystemWindows="true"/>
    
    </android.support.v4.widget.DrawerLayout>

**Step 2:** Find views:

    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    DrawerView drawer = (DrawerView) findViewById(R.id.drawer);

**Step 3:** Set a profile:

    drawer.setProfile(
            new DrawerProfile()
                    .setAvatar(getResources().getDrawable(R.drawable.profile_avatar))
                    .setBackground(getResources().getDrawable(R.drawable.profile_background))
                    .setName(getString(R.string.profile_name))
                    .setDescription(getString(R.string.profile_description))
                    .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                        @Override
                        public void onClick(DrawerProfile drawerProfile) {
                            Toast.makeText(YourActivity.this, "Clicked profile", Toast.LENGTH_SHORT).show();
                        }
                    })
            );

**Step 4:** Populate your drawer list:

    drawer.addItem(
            new DrawerItem()
                    .setImage(getResources().getDrawable(R.drawable.ic_first_item))
                    .setTextPrimary(getString(R.string.title_first_item))
                    .setTextSecondary(getString(R.string.description_first_item))
                    .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                        @Override
                        public void onClick(DrawerItem drawerItem, int position) {
                            Toast.makeText(YourActivity.this, "Clicked first item", Toast.LENGTH_SHORT).show();
                        }
                    })
            );
    drawer.addDivider();
    drawer.addItem(
            new DrawerItem()
                    .setImage(getResources().getDrawable(R.drawable.ic_second_item))
                    .setTextPrimary(getString(R.string.title_second_item))
                    .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                        @Override
                        public void onClick(DrawerItem drawerItem, int position) {
                            Toast.makeText(YourActivity.this, "Clicked second item", Toast.LENGTH_SHORT).show();
                        }
                    })
            );

Tip: Lollipop status bar
----------------------------

**Step 1:** Add `fitSystemWindows="true"` to your DrawerLayout and your DrawerView in XML.

**Step 2:** Set custom status bar color for your DrawerLayout:

    drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.your_chosen_color));

**Step 3:** Use [Toolbar][2] instead of [ActionBar][3].

**Step 4:** Make your status bar transparent:

    <style name="Theme.MyApp" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
    </style>

**That's it!** *material-drawer* takes care of the rest.

For further information see Chris Banes' [answer][1] on [stackoverflow.com][1].

Dependency
----------

*material-drawer* is available on Maven Central

**Gradle dependency:**

    dependencies {
	    compile 'com.heinrichreimersoftware.material_drawer:library:1.1'
    }

License
-------

    Copyright 2013 Heinrich Reimer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: http://stackoverflow.com/questions/26440879/how-do-i-use-drawerlayout-to-display-over-the-actionbar-toolbar-and-under-the-st
[2]: http://developer.android.com/reference/android/support/v7/widget/Toolbar.html
[3]: http://developer.android.com/reference/android/support/v7/app/ActionBar.html
