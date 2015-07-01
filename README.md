material-drawer
===============
[ ![Download](https://api.bintray.com/packages/heinrichreimer/maven/material-drawer/images/download.svg) ](https://bintray.com/heinrichreimer/maven/material-drawer/_latestVersion)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-material--drawer-blue.svg?style=flat)](https://android-arsenal.com/details/1/1162)

Custom drawer implementation for Material design apps.

Information
----
I'm currently not actively developing this library because of school work. Until August I'll only merge pull requests. Thanks for your patience.

Demo
----
A demo app is available on Google Play:

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.materialdrawerdemo)

Screenshots
-----------

| ![material-drawer](http://i.imgur.com/XHgbWaE.png) | ![material-drawer](http://i.imgur.com/uHW9oOh.png) | ![material-drawer](http://i.imgur.com/WXmmc7a.png) |
|:-:|:-:|:-:|
| Fixed items | Select profile | Custom theme |

Dependency
----------

*material-drawer* is available on jCenter

**Gradle dependency:**

    dependencies {
	    compile 'com.heinrichreimersoftware.materialdrawer:library:2.1.3'
    }

Get the latest dependency with ["Gradle, please"][GP]

How-To-Use
----------
**Step 1:** Let your [`Activity`][ABA] extend [`DrawerActivity`][DA]:

    public class MainActivity extends DrawerActivity {}

**Step 2:** Set your content:

    setContentView(R.layout.activity_main);

**Step 3:** Set a profile:

    drawer.setProfile(
            new DrawerProfile()
                    .setRoundedAvatar((BitmapDrawable)getResources().getDrawable(R.drawable.profile_avatar))
                    .setBackground(getResources().getDrawable(R.drawable.profile_cover))
                    .setName(getString(R.string.profile_name))
                    .setDescription(getString(R.string.profile_description))
                    .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                        @Override
                        public void onClick(DrawerProfile drawerProfile, long id) {
                            Toast.makeText(MainActivity.this, "Clicked profile #" + id, Toast.LENGTH_SHORT).show();
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
                        public void onClick(DrawerItem drawerItem, long id, int position) {
                            Toast.makeText(MainActivity.this, "Clicked first item #" + id, Toast.LENGTH_SHORT).show();
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
                        public void onClick(DrawerItem drawerItem, long id, int position) {
                            Toast.makeText(MainActivity.this, "Clicked second item #" + id, Toast.LENGTH_SHORT).show();
                        }
                    })
            );

**Step 5:** Add `actionBarStyle` to your theme:

    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/color_primary</item>
        <item name="colorPrimaryDark">@color/color_primary_dark</item>
        <item name="colorAccent">@color/color_accent</item>
        <item name="actionBarStyle">@style/ThemeOverlay.AppCompat.Dark.ActionBar</item>
    </style>

**Step 6 (Optional):** Change the drawer theme:

The drawer gets themed based on your selected app theme but you can also modify it.


    setDrawerTheme(
            new DrawerTheme(this)
                    .setBackgroundColorRes(R.color.background)
                    .setTextColorPrimaryRes(R.color.primary_text)
                    .setTextColorSecondaryRes(R.color.secondary_text)
                    .setTextColorPrimaryInverseRes(R.color.primary_text_inverse)
                    .setTextColorSecondaryInverseRes(R.color.secondary_text_inverse)
                    .setHighlightColorRes(R.color.highlight)
    );

**Step 7 (Optional):** Set your own [`Toolbar`][T]:

You can set your own [`Toolbar`][T] as you do with [`ActionBarActivity`][ABA].

    setSupportActionBar(toolbar);

Pro Tip: Lollipop status bar
----------------------------

**Step 1:** Make your status bar transparent:

    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
    </style>

**That's it!** *material-drawer* takes care of the rest.

Info: [`DrawerFrameLayout`][DFL] & [`DrawerView`][DV]
----------------------------

Of course you can use [`DrawerFrameLayout`][DFL] and [`DrawerView`][DV] alone too. See the comments in the Java files for further information.

Changes
-------

* **Version 2.1.3:**
    * Better use of [Palette][P]
    * [Fragment][F]s support ([DrawerFragementItem][DFI])
    * Enable/disable the drawer toggle
    * Bug fixes
* **Version 2.1:**
    * Theming engine ([DrawerTheme][DT])
    * Ripples everywhere
    * Changed min SDK to 14 (Android 4.0)
    * Fixed issues with animations
    * Bug fixes
* **Version 2.0:**
    * Multiple profiles (#33)
    * Rounded avatars
    * Changeable drawer width (#30)
* **Version 1.4.2:**
    * Changed min SDK to 7 (Android 2.1)
    * Drawer max width can be changed (#30)
    * New image type `DrawerItem.SMALL_AVATAR` which doesn't get auto-tinted
    * Fixed issue #26
* **Version 1.4.1:**
    * Fixed bottom list
    * Improved sizing
    * Improved colors
    * Fixed issue #27
* **Version 1.3.2:**
    * Added [DrawerFrameLayout][DFL] as [DrawerLayout][DL] replacement
    * Active state tinting
    * List headers ([DrawerHeaderItem][DHI])
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

* [LinearListView][1] by [@frankiesardo][2] (Apache License 2.0)
* [ScrimInsetsScrollView][3] from the Google IO app 2014 by [@google][4] (Apache License 2.0)
* [ImageLoadingPattern][5] by [@Emanuel Vecchio][6] (Apache License 2.0)
* [RoundedAvatarDrawable][7] by [@Evelio Tarazona Cáceres][8] (Apache License 2.0)

Stats
-------

* ~7000 lines of code
* ~200kb

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


[GP]: http://gradleplease.appspot.com/#com.heinrichreimersoftware.materialdrawer

[1]: https://github.com/frankiesardo/LinearListView
[2]: https://github.com/frankiesardo
[3]: https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/widget/ScrimInsetsScrollView.java
[4]: https://github.com/google
[5]: https://github.com/rnrneverdies/ImageLoadingPattern
[6]: https://github.com/rnrneverdies
[7]: https://gist.github.com/eveliotc/6051367
[8]: https://github.com/eveliotc

[DA]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerActivity.java
[ABA]: http://developer.android.com/reference/android/support/v7/app/ActionBarActivity.html
[T]: http://developer.android.com/reference/android/support/v7/widget/Toolbar.html
[DT]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/theme/DrawerTheme.java
[DFL]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerFrameLayout.java
[DV]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerView.java
[DL]: https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html
[DHI]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/structure/DrawerHeaderItem.java
[DFI]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/structure/DrawerFragementItem.java
[P]: http://developer.android.com/reference/android/support/v7/graphics/Palette.html
[F]: http://developer.android.com/reference/android/support/v4/app/Fragment.html
