package com.heinrichreimersoftware.materialdrawerdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

public class MainActivity extends ActionBarActivity implements BillingProcessor.IBillingHandler {

    private BillingProcessor bp;

    private DrawerView drawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        bp = new BillingProcessor(this, getString(R.string.in_app_billing_public_license), this);


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

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.color_primary_dark));
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(drawer);


        drawer.addItem(new DrawerItem()
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(getString(R.string.lorem_ipsum_long))
        );

        drawer.addItem(new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_mail))
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(getString(R.string.lorem_ipsum_long))
        );

        drawer.addDivider();

        drawer.addItem(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) getResources().getDrawable(R.drawable.cat_1))
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(getString(R.string.lorem_ipsum_long))
        );

        drawer.addItem(new DrawerHeaderItem().setTitle(getString(R.string.lorem_ipsum_short)));

        drawer.addItem(new DrawerItem()
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
        );

        drawer.addItem(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) getResources().getDrawable(R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(getString(R.string.lorem_ipsum_long), DrawerItem.THREE_LINE)
        );

        drawer.selectItem(1);
        drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                drawer.selectItem(position);
                Toast.makeText(MainActivity.this, "Clicked item #" + position, Toast.LENGTH_SHORT).show();
            }
        });


        drawer.addFixedItem(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) getResources().getDrawable(R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
        );

        drawer.addFixedItem(new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_flag))
                        .setTextPrimary(getString(R.string.lorem_ipsum_short))
        );

        drawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                drawer.selectFixedItem(position);
                Toast.makeText(MainActivity.this, "Clicked fixed item #" + position, Toast.LENGTH_SHORT).show();
            }
        });


        drawer.addProfile(new DrawerProfile()
                        .setId(1)
                        .setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.cat_1))
                        .setBackground(getResources().getDrawable(R.drawable.cat_wide_1))
                        .setName(getString(R.string.lorem_ipsum_short))
                        .setDescription(getString(R.string.lorem_ipsum_medium))
        );

        drawer.addProfile(new DrawerProfile()
                        .setId(2)
                        .setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.cat_2))
                        .setBackground(getResources().getDrawable(R.drawable.cat_wide_1))
                        .setName(getString(R.string.lorem_ipsum_short))
        );

        drawer.addProfile(new DrawerProfile()
                        .setId(3)
                        .setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.cat_1))
                        .setBackground(getResources().getDrawable(R.drawable.cat_wide_2))
                        .setName(getString(R.string.lorem_ipsum_short))
                        .setDescription(getString(R.string.lorem_ipsum_medium))
        );


        drawer.setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
            @Override
            public void onClick(DrawerProfile profile, long id) {
                Toast.makeText(MainActivity.this, "Clicked profile *" + id, Toast.LENGTH_SHORT).show();
            }
        });
        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                Toast.makeText(MainActivity.this, "Switched from profile *" + oldId + " to profile *" + newId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openDrawerFrameLayout(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void openDrawerAsync(View view){
        Intent intent = new Intent(this, MainActivityAsync.class);
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
