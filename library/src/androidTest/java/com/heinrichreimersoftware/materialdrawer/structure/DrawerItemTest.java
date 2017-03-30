package com.heinrichreimersoftware.materialdrawer.structure;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.test.AndroidTestCase;
import android.test.mock.MockResources;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;

/**
 * Created by serayuzgur on 24/10/15.
 */
public class DrawerItemTest extends AndroidTestCase {

    public void testWriteToParcel() throws Exception {


        DrawerItem test = new DrawerItem()
                .setRoundedImage(new BitmapDrawable(MockResources.getSystem(), makeBitmap()))
                .setTextPrimary("TestPrimary")
                .setTextSecondary("TestSecondary");

        Parcel parcel = Parcel.obtain();
        test.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        DrawerItem createdFromParcel = DrawerItem.CREATOR.createFromParcel(parcel);
        assertEquals(test, createdFromParcel);


    }

    static Bitmap makeBitmap() {
        return makeBitmap(10, 10);
    }

    static Bitmap makeBitmap(int width, int height) {

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }
}