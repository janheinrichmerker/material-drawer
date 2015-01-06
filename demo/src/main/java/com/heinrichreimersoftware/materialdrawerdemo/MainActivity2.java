package com.heinrichreimersoftware.materialdrawerdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;


public class MainActivity2 extends ActionBarActivity implements BillingProcessor.IBillingHandler  {

    private BillingProcessor bp;

    private Toolbar toolbar;

    private DrawerFrameLayout drawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjhzh8zf3W1ezb2fIlki3Udlr5UikqEVQb4d705GmzWEm9DSB7i3ZQI6vMncxmABK5X5SpljCUsTQ/GFCqJ0QVLjohn6/a95qJ2WxSmdnlYZYmTEUZUh8U4kDLFSlUgFor7VKd0Lij2gWECCuE4ZpWTxwoInbVa2/WBq0f1cYuayXC1YLKGBrImUUrIJg5diB00uIPNSks/oouPA+rKb5ITHq9WV1Di7pRijRwMe3IsjzyW9RqsK6pRusYRO7C5O53F1o9ClVpH+HGDQwjJtqvTZN3XcH3eQHqoKSWLDPnkQthW+ShpiRDlMdSd6wU7F3t8QYxNwRb57EQrdJIChhYwIDAQAB\n", this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawer = (DrawerFrameLayout) findViewById(R.id.drawer);


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
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

        drawer.setDrawerListener(drawerToggle);
        drawer.closeDrawer();


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
        if (errorCode != 110 && errorCode != 2) {
            Toast.makeText(this, "Billing error: code = " + errorCode + ", error: " +
                    (error != null ? error.getMessage() : "?"), Toast.LENGTH_LONG).show();
        }
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
