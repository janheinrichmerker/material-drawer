package com.heinrichreimersoftware.materialdrawerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivityAsync extends ActionBarActivity implements BillingProcessor.IBillingHandler {

    private static final int PROFILE_LOADER=1;
    private static final int ITEMS_LOADER=2;
    private static final int FIXED_ITEMS_LOADER=3;

    private BillingProcessor bp;

    private DrawerView drawer;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_async);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bp = new BillingProcessor(this, getString(R.string.in_app_billing_public_license), this);

        initMaterialDrawer();

        // load items synchronously without loader
        // populateDrawerItems(generateDrawerItems(this));
        // populateDrawerProfiles(generateDrawerProfiles(this));
        // populatedFixedItems(generateFixedDrawerItems(this));

        // load items asynchronously using loaders
        getSupportLoaderManager().initLoader(ITEMS_LOADER,null,new ItemsLoaderCallback(this));
        getSupportLoaderManager().initLoader(PROFILE_LOADER,null,new ProfilesLoaderCallback(this));
        getSupportLoaderManager().initLoader(FIXED_ITEMS_LOADER,null,new FixedItemsLoaderCallback(this));

    }

    public void populateDrawerProfiles(List<DrawerProfile> profiles) {
        drawer.clearProfiles();

        for (DrawerProfile profile : profiles) {
            drawer.addProfile(profile);
        }

    }

    public void populateDrawerItems(List<DrawerItem> items) {
        drawer.clearItems();

        for (DrawerItem item : items) {
            drawer.addItem(item);
        }

        drawer.selectItem(1);
    }

    public void populatedFixedItems(List<DrawerItem> items) {
        drawer.clearFixedItems();

        for (DrawerItem item : items) {
            drawer.addFixedItem(item);
        }

    }

    public void initMaterialDrawer() {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);

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

        drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                drawer.selectItem(position);
                Toast.makeText(MainActivityAsync.this, "Clicked item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        drawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                drawer.selectFixedItem(position);
                Toast.makeText(MainActivityAsync.this, "Clicked fixed item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        drawer.setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
            @Override
            public void onClick(DrawerProfile profile, long id) {
                Toast.makeText(MainActivityAsync.this, "Clicked profile *" + id, Toast.LENGTH_SHORT).show();
            }
        });
        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                Toast.makeText(MainActivityAsync.this, "Switched from profile *" + oldId + " to profile *" + newId, Toast.LENGTH_SHORT).show();
            }
        });
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

    public static class DrawerItemLoader extends AsyncTaskLoader<List<DrawerItem>> {

        private List<DrawerItem> items;

        public DrawerItemLoader(Context context) {
            super(context);
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (items != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(items);
            }

            if (takeContentChanged() || items == null) {
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * This is where the bulk of our work is done. This function is called in a background thread and should generate a
         * new set of data to be published by the loader.
         */
        @Override
        public List<DrawerItem> loadInBackground() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
            return generateDrawerItems(getContext());
        }

    }

    public static class FixedDrawerItemLoader extends AsyncTaskLoader<List<DrawerItem>> {

        private List<DrawerItem> items;

        public FixedDrawerItemLoader(Context context) {
            super(context);
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (items != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(items);
            }

            if (takeContentChanged() || items == null) {
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * This is where the bulk of our work is done. This function is called in a background thread and should generate a
         * new set of data to be published by the loader.
         */
        @Override
        public List<DrawerItem> loadInBackground() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            return generateFixedDrawerItems(getContext());
        }

    }
    public static class DrawerProfileLoader extends AsyncTaskLoader<List<DrawerProfile>> {

        private List<DrawerProfile> items;

        public DrawerProfileLoader(Context context) {
            super(context);
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (items != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(items);
            }

            if (takeContentChanged() || items == null) {
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * This is where the bulk of our work is done. This function is called in a background thread and should generate a
         * new set of data to be published by the loader.
         */
        @Override
        public List<DrawerProfile> loadInBackground() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
            return generateDrawerProfiles(getContext());
        }

    }

    protected static class ProfilesLoaderCallback implements LoaderManager.LoaderCallbacks<List<DrawerProfile>> {

        private MainActivityAsync context;

        public ProfilesLoaderCallback(MainActivityAsync context) {
            this.context = context;
        }

        @Override
        public android.support.v4.content.Loader<List<DrawerProfile>> onCreateLoader(int id, Bundle args) {
            if (id == PROFILE_LOADER)
                return new DrawerProfileLoader(context);
            else
                return null;

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<List<DrawerProfile>> loader) {

        }


        @Override
        public void onLoadFinished(android.support.v4.content.Loader<List<DrawerProfile>> loader, List<DrawerProfile> profiles) {
            if (loader.getId() == PROFILE_LOADER) {
                context.populateDrawerProfiles(profiles);
            }
        }
    }

    protected static class ItemsLoaderCallback implements LoaderManager.LoaderCallbacks<List<DrawerItem>> {

        private MainActivityAsync context;

        public ItemsLoaderCallback(MainActivityAsync context) {
            this.context = context;
        }

        @Override
        public android.support.v4.content.Loader<List<DrawerItem>> onCreateLoader(int id, Bundle args) {
            if (id == ITEMS_LOADER) {
                return new DrawerItemLoader(context);
            } else {
                return null;
            }

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<List<DrawerItem>> loader) {

        }


        @Override
        public void onLoadFinished(android.support.v4.content.Loader<List<DrawerItem>> loader, List<DrawerItem> items) {
            if (loader.getId() == ITEMS_LOADER) {
                context.populateDrawerItems(items);
            }
        }
    }

    protected static class FixedItemsLoaderCallback implements LoaderManager.LoaderCallbacks<List<DrawerItem>> {

        private MainActivityAsync context;

        public FixedItemsLoaderCallback(MainActivityAsync context) {
            this.context = context;
        }

        @Override
        public android.support.v4.content.Loader<List<DrawerItem>> onCreateLoader(int id, Bundle args) {
            if (id == FIXED_ITEMS_LOADER) {
                return new FixedDrawerItemLoader(context);
            } else {
                return null;
            }

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<List<DrawerItem>> loader) {

        }


        @Override
        public void onLoadFinished(android.support.v4.content.Loader<List<DrawerItem>> loader, List<DrawerItem> items) {
            if (loader.getId() == FIXED_ITEMS_LOADER) {
                context.populatedFixedItems(items);
            }
        }
    }

    protected static List<DrawerProfile> generateDrawerProfiles(Context context) {
        List<DrawerProfile> items = new ArrayList<DrawerProfile>();

        items.add(new DrawerProfile()
                        .setId(1)
                        .setRoundedAvatar((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_1))
                        .setBackground(context.getResources().getDrawable(R.drawable.cat_wide_1))
                        .setName(context.getString(R.string.lorem_ipsum_short))
                        .setDescription(context.getString(R.string.lorem_ipsum_medium))
        );

        items.add(new DrawerProfile()
                        .setId(2)
                        .setRoundedAvatar((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_2))
                        .setBackground(context.getResources().getDrawable(R.drawable.cat_wide_1))
                        .setName(context.getString(R.string.lorem_ipsum_short))
        );

        items.add(new DrawerProfile()
                        .setId(3)
                        .setRoundedAvatar((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_1))
                        .setBackground(context.getResources().getDrawable(R.drawable.cat_wide_2))
                        .setName(context.getString(R.string.lorem_ipsum_short))
                        .setDescription(context.getString(R.string.lorem_ipsum_medium))
        );

        return items;
    }

    protected  static List<DrawerItem> generateFixedDrawerItems(Context context) {

        List<DrawerItem> items = new ArrayList<DrawerItem>();

        items.add(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
        );

        items.add(new DrawerItem()
                        .setImage(context.getResources().getDrawable(R.drawable.ic_flag))
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
        );

        return items;
    }

    protected static List<DrawerItem> generateDrawerItems(Context context) {
        List<DrawerItem> items = new ArrayList<DrawerItem>();

        items.add(new DrawerItem()
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(context.getString(R.string.lorem_ipsum_long))
        );

        items.add(new DrawerItem()
                        .setImage(context.getResources().getDrawable(R.drawable.ic_mail))
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(context.getString(R.string.lorem_ipsum_long))
        );

        items.add(new DrawerHeaderItem());

        items.add(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_1))
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(context.getString(R.string.lorem_ipsum_long))
        );

        items.add(new DrawerHeaderItem().setTitle(context.getString(R.string.lorem_ipsum_short)));

        items.add(new DrawerItem()
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
        );

        items.add(new DrawerItem()
                        .setRoundedImage((BitmapDrawable) context.getResources().getDrawable(R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
                        .setTextPrimary(context.getString(R.string.lorem_ipsum_short))
                        .setTextSecondary(context.getString(R.string.lorem_ipsum_long), DrawerItem.THREE_LINE)
        );

        return items;
    }
}
