package com.heinrichreimersoftware.materialdrawerdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "material-drawer Demo";

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private DrawerView drawer;
    private CheckBox checkBoxList;
    private CheckBox checkBoxProfile;
    private CheckBox checkBoxDividers;
    private CheckBox checkBoxHeaders;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);

        checkBoxList = (CheckBox) findViewById(R.id.checkBoxList);
        checkBoxProfile = (CheckBox) findViewById(R.id.checkBoxProfile);
        checkBoxDividers = (CheckBox) findViewById(R.id.checkBoxDividers);
        checkBoxHeaders = (CheckBox) findViewById(R.id.checkBoxHeaders);


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ){

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        setSupportActionBar(toolbar);

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.color_primary_dark));
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(drawer);

        checkBoxList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDrawer();
            }
        });
        checkBoxProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDrawer();
            }
        });
        checkBoxDividers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDrawer();
            }
        });
        checkBoxHeaders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDrawer();
            }
        });

        updateDrawer();
    }

    public void updateDrawer(){
        Log.d(TAG, "updateDrawer()");
        drawer.clearItems();
        if (checkBoxList.isChecked()){

            checkBoxDividers.setActivated(true);

            if (checkBoxDividers.isChecked()){
                checkBoxHeaders.setActivated(true);
            }
            else {
                checkBoxHeaders.setActivated(false);
            }

            drawer.addItem(new DrawerItem()
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
                            .setTextSecondary(getString(R.string.lorem_ipsum_long))
            );

            Drawable icon1;
            if (Math.random() >= .5){
                icon1 = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                icon1 = getResources().getDrawable(R.drawable.cat_2);
            }
            drawer.addItem(new DrawerItem()
                            .setImage(icon1)
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
                            .setTextSecondary(getString(R.string.lorem_ipsum_long))
            );

            if (checkBoxDividers.isChecked()){
                drawer.addDivider();
            }

            Drawable icon2;
            if (Math.random() >= .5){
                icon2 = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                icon2 = getResources().getDrawable(R.drawable.cat_2);
            }
            drawer.addItem(new DrawerItem()
                            .setImage(icon2, DrawerItem.AVATAR)
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
                            .setTextSecondary(getString(R.string.lorem_ipsum_long))
            );

            if (checkBoxDividers.isChecked()) {
                if (checkBoxHeaders.isChecked()) {
                    drawer.addItem(new DrawerHeaderItem().setTitle(getString(R.string.lorem_ipsum_short)));
                } else {
                    drawer.addDivider();
                }
            }

            drawer.addItem(new DrawerItem()
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
            );

            Drawable icon3;
            if (Math.random() >= .5){
                icon3 = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                icon3 = getResources().getDrawable(R.drawable.cat_2);
            }
            drawer.addItem(new DrawerItem()
                            .setImage(icon3)
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
                            .setTextSecondary(getString(R.string.lorem_ipsum_long), DrawerItem.THREE_LINE)
            );
            drawer.selectItem(1);
            drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, int id, int position) {
                    drawer.selectItem(position);
                    Toast.makeText(MainActivity.this, "Clicked item #" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            checkBoxDividers.setActivated(false);
            checkBoxHeaders.setActivated(false);
        }
        if (checkBoxProfile.isChecked()){
            Drawable avatar;
            if (Math.random() >= .5){
                avatar = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                avatar = getResources().getDrawable(R.drawable.cat_2);
            }

            Drawable background;
            if (Math.random() >= .5){
                background = getResources().getDrawable(R.drawable.cat_wide_1);
            }
            else {
                background = getResources().getDrawable(R.drawable.cat_wide_2);
            }

            drawer.setProfile(new DrawerProfile()
                            .setAvatar(avatar)
                            .setBackground(background)
                            .setName(getString(R.string.lorem_ipsum_short))
                            .setDescription(getString(R.string.lorem_ipsum_medium))
            );
        }
        else {
            drawer.removeProfile();
        }
    }

    public void openDrawerFrameLayout(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_github) {
            String url = "https://github.com/HeinrichReimer/material-drawer";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}
