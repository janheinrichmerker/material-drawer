package com.heinrichreimersoftware.materialdrawer.structure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.R;

/**
 * Created by Jitesh Lalwani on 2/4/15.
 */
public class ArcView extends View {


    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.arc_color));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);

  /*
   * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
   *
   * oval - The bounds of oval used to define the shape and size of the arc
   * startAngle - Starting angle (in degrees) where the arc begins
   * sweepAngle - Sweep angle (in degrees) measured clockwise
   * useCenter - If true, include the center of the oval in the arc, and close it if it is being stroked. This will draw a wedge
   * paint - The paint used to draw the arc
   */


        oval.set(convertDpToPixels(7), convertDpToPixels(7), convertDpToPixels(73), convertDpToPixels(73));
        canvas.drawArc(oval, 0, Utilities.degree, true, paint);
    }

    private int convertDpToPixels(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

}
