![Icon](http://i.imgur.com/Dog6Eim.png)

material-drawer
===============

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-material--drawer-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1162)
[![JitPack](https://jitpack.io/v/com.heinrichreimersoftware/material-drawer.svg)](https://jitpack.io/#com.heinrichreimersoftware/material-drawer)
[![Build Status](https://travis-ci.org/HeinrichReimer/material-drawer.svg?branch=master)](https://travis-ci.org/HeinrichReimer/material-drawer)
[![Apache License 2.0](https://img.shields.io/github/license/HeinrichReimer/material-drawer.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


Custom drawer implementation for Material design apps.

Demo
----
A demo app is available on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.materialdrawerdemo">
	<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" />
</a>

Screenshots
-----------

| ![material-drawer](http://i.imgur.com/XHgbWaE.png) | ![material-drawer](http://i.imgur.com/uHW9oOh.png) | ![material-drawer](http://i.imgur.com/WXmmc7a.png) |
|:-:|:-:|:-:|
| Fixed items | Select profile | Custom theme |

Dependency
----------

*material-drawer* is available on [**jitpack.io**][J]

**Gradle dependency:**
````gradle
repositories {
    // ...
    maven { url 'https://jitpack.io' }
}
````
````gradle
dependencies {
    compile 'com.heinrichreimersoftware:material-drawer:2.3.2'
}
````

Get the latest dependency at [jitpack.io][J].

How-To-Use
----------
**Step 1:** Let your [`Activity`][ABA] extend [`DrawerActivity`][DA]:

````java
public class MainActivity extends DrawerActivity {}
````

**Step 2:** Set your content:

````java
setContentView(R.layout.activity_main);
````

**Step 3:** Set a profile:

````java
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
````

**Step 4:** Populate your drawer list:

````java
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
````

**Step 5:** Add `actionBarStyle` to your theme:

````xml
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorPrimary">@color/color_primary</item>
    <item name="colorPrimaryDark">@color/color_primary_dark</item>
    <item name="colorAccent">@color/color_accent</item>
    <item name="actionBarStyle">@style/ThemeOverlay.AppCompat.Dark.ActionBar</item>
</style>
````

**Step 6 (Optional):** Change the drawer theme:

The drawer gets themed based on your selected app theme but you can also modify it.

````java
setDrawerTheme(
        new DrawerTheme(this)
                .setBackgroundColorRes(R.color.background)
                .setTextColorPrimaryRes(R.color.primary_text)
                .setTextColorSecondaryRes(R.color.secondary_text)
                .setTextColorPrimaryInverseRes(R.color.primary_text_inverse)
                .setTextColorSecondaryInverseRes(R.color.secondary_text_inverse)
                .setHighlightColorRes(R.color.highlight)
);
````

**Step 7 (Optional):** Set your own [`Toolbar`][T]:

You can set your own [`Toolbar`][T] as you do with [`ActionBarActivity`][ABA].

````java
setSupportActionBar(toolbar);
````

Pro Tip: Lollipop status bar
----------------------------

**Step 1:** Make your status bar transparent:

````xml
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="android:windowDrawsSystemBarBackgrounds">true</item>
    <item name="android:statusBarColor">@android:color/transparent</item>
</style>
````

**That's it!** *material-drawer* takes care of the rest.

Info: [`DrawerFrameLayout`][DFL] & [`DrawerView`][DV]
----------------------------

Of course you can use [`DrawerFrameLayout`][DFL] and [`DrawerView`][DV] alone too. See the comments in the Java files for further information.

Open source libraries
-------

_material-drawer_ uses the following open source libraries or files:

* [LinearListView][1] by [@frankiesardo][2] (Apache License 2.0)
* [ScrimInsetsScrollView][3] from the Google IO app 2014 by [@google][4] (Apache License 2.0)
* [ImageLoadingPattern][5] by [@Emanuel Vecchio][6] (Apache License 2.0)
* [RoundedAvatarDrawable][7] by [@Evelio Tarazona Cáceres][8] (Apache License 2.0)

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

[1]: https://github.com/frankiesardo/LinearListView
[2]: https://github.com/frankiesardo
[3]: https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/widget/ScrimInsetsScrollView.java
[4]: https://github.com/google
[5]: https://github.com/rnrneverdies/ImageLoadingPattern
[6]: https://github.com/rnrneverdies
[7]: https://gist.github.com/eveliotc/6051367
[8]: https://github.com/eveliotc

[J]: https://jitpack.io/#com.heinrichreimersoftware/material-drawer
[ABA]: http://developer.android.com/reference/android/support/v7/app/ActionBarActivity.html
[DA]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerActivity.java
[T]: http://developer.android.com/reference/android/support/v7/widget/Toolbar.html
[DFL]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerFrameLayout.java
[DV]: https://github.com/HeinrichReimer/material-drawer/blob/master/library/src/main/java/com/heinrichreimersoftware/materialdrawer/DrawerView.java
