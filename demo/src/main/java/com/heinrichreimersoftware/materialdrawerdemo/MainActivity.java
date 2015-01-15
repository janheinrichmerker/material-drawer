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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

public class MainActivity extends ActionBarActivity implements BillingProcessor.IBillingHandler {

    private BillingProcessor bp;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private DrawerView drawer;
    private CheckBox checkBoxList;
    private CheckBox checkBoxFixedList;
    private CheckBox checkBoxProfile;
    private CheckBox checkBoxDividers;
    private CheckBox checkBoxHeaders;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = new BillingProcessor(this, getString(R.string.in_app_billing_public_license), this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);

        checkBoxList = (CheckBox) findViewById(R.id.checkBoxList);
        checkBoxFixedList = (CheckBox) findViewById(R.id.checkBoxFixedList);
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
        checkBoxFixedList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        drawer.clearItems();
        drawer.clearFixedItems();
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
        if (checkBoxFixedList.isChecked()){
            Drawable icon4;
            if (Math.random() >= .5){
                icon4 = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                icon4 = getResources().getDrawable(R.drawable.cat_2);
            }
            drawer.addFixedItem(new DrawerItem()
                            .setImage(icon4)
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
            );

            Drawable icon5;
            if (Math.random() >= .5){
                icon5 = getResources().getDrawable(R.drawable.cat_1);
            }
            else {
                icon5 = getResources().getDrawable(R.drawable.cat_2);
            }
            drawer.addFixedItem(new DrawerItem()
                            .setImage(icon5)
                            .setTextPrimary(getString(R.string.lorem_ipsum_short))
            );


            drawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, int id, int position) {
                    drawer.selectFixedItem(position);
                    Toast.makeText(MainActivity.this, "Clicked fixed item #" + position, Toast.LENGTH_SHORT).show();
                }
            });
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

        switch (item.getItemId()) {
            case R.id.action_github:
                String url = "https://github.com/HeinrichReimer/material-drawer";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.donation1:
                donate(1);
                break;
            case R.id.donation2:
                donate(2);
                break;
            case R.id.donation3:
                donate(3);
                break;
            case R.id.donation4:
                donate(4);
                break;
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

    /* Donation stuff via in app billing */

    public void donate(int index) {
        bp.purchase(this, "donate_" + index);
    }

    @Override
    public void onBillingInitialized() {}

    @Override
    public void onProductPurchased(String productId, TransactionDetails transactionDetails) {
        bp.consumePurchase(productId);
        Toast.makeText(this, R.string.thank_you, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        int errorMessageResId = 0;
        switch (errorCode){
            case 1:
                errorMessageResId = R.string.donation_error_1;
                break;
            case 2:
                errorMessageResId = R.string.donation_error_2;
                break;
            case 3:
                errorMessageResId = R.string.donation_error_3;
                break;
            case 4:
                errorMessageResId = R.string.donation_error_4;
                break;
            case 5:
                errorMessageResId = R.string.donation_error_5;
                break;
            case 6:
                errorMessageResId = R.string.donation_error_6;
                break;
            default:
                Toast.makeText(this, "Billing error: code = " + errorCode + ", error: " +
                        (error != null ? error.getMessage() : "?"), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, getString(errorMessageResId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null) bp.release();
        super.onDestroy();
    }
}
