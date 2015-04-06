package com.heinrichreimersoftware.materialdrawer.structure;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Jitesh Lalwani on 1/4/15.
 */
public class Utilities {

<<<<<<< HEAD
=======
    public static int degree;
>>>>>>> Added Arcdegree and selection colors
    /**
     * Gets medium font.
     *
     * @param context the context
     * @return the medium font
     */
    public static Typeface getMediumFont(Context context) {
        Typeface typeFaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        return typeFaceRegular;
    }

    /**
     * Gets regular font.
     *
     * @param context the context
     * @return the regular font
     */
    public static Typeface getRegularFont(Context context) {
        Typeface typeFaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        return typeFaceRegular;
    }
}
