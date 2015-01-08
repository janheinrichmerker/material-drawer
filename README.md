material-drawer
===============
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-material--drawer-blue.svg?style=flat)](https://android-arsenal.com/details/1/1162)

Custom drawer implementation for Material design apps.

Demo
----
A demo app is available on Google Play:

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.materialdrawerdemo)

Screenshots
-----------

| ![material-drawer](http://i.imgur.com/JEbu2nG.png) | ![material-drawer](http://i.imgur.com/wJaJUcF.png) | ![material-drawer](http://i.imgur.com/QKEwO58.png) |
|:-:|:-:|:-:|
| Active state tinting | Scrollable drawer | Few items |

How-To-Use
----------
**Step 1:** Include it in your layout:

    <com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/drawer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true">
        
        <!-- Your main content here -->
    
    </com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout>

**Step 2:** Find view:

    DrawerFrameLayout drawer = (DrawerFrameLayout) findViewById(R.id.drawer);

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
                        public void onClick(DrawerItem drawerItem, int id, int position) {
                            Toast.makeText(YourActivity.this, "Clicked first item (#" + id + ")", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DrawerItem drawerItem, ind id, int position) {
                            Toast.makeText(YourActivity.this, "Clicked second item (#" + id + ")", Toast.LENGTH_SHORT).show();
                        }
                    })
            );

Pro Tip: Lollipop status bar
----------------------------

**Step 1:** Add `fitSystemWindows="true"` attribute to your [DrawerFrameLayout][9] in XML.

**Step 2:** Use [Toolbar][2] instead of [ActionBar][3].

**Step 3:** Make your status bar transparent:

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
	    compile 'com.heinrichreimersoftware.materialdrawer:library:1.3.2'
    }

Get the latest dependency with ["Gradle, please"][4]

Changes
-------

* **Version 1.3.2:**
    * Added [DrawerFrameLayout][9] as [DrawerLayout][11] replacement
    * Active state tinting
    * List headers ([DrawerHeaderItem][10])
    * Fixed spacing
* **Version 1.2:**
    * Drawer items can now contain an ID
    * Image setters work with Bitmap too
* **Version 1.1.1:**
    * You can change items that you got via `drawer.getItem(position)`. Changes will update the adapter
    * Fixed auto drawer width
    * Fixed title padding

Open source libraries
-------

_material-drawer_ uses the following open source libraries or files:

* [LinearListView][5] by [@frankiesardo][6] (Apache License 2.0)
* [ScrimInsetsScrollView][7] from the Google IO app 2014 by [@google][8] (Apache License 2.0)
* [Android In-App Billing v3][12] by [@anjlab][13] (Apache License 2.0)

License
-------

    Copyright 2015 Heinrich Reimer

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
[4]: http://gradleplease.appspot.com/#materialdrawer
[5]: https://github.com/frankiesardo/LinearListView
[6]: https://github.com/frankiesardo
[7]: https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/widget/ScrimInsetsScrollView.java
[8]: https://github.com/google
[9]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerFrameLayout.java
[10]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/structure/DrawerHeaderItem.java
[11]: https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html
[12]: https://github.com/anjlab/android-inapp-billing-v3
[13]: https://github.com/anjlab/
