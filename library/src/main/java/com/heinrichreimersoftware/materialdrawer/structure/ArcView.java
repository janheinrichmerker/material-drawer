package com.heinrichreimersoftware.materialdrawer.structure;

import android.content.Context;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> Added Arcdegree and selection colors
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.R;

/**
 * Created by Jitesh Lalwani on 2/4/15.
 */
<<<<<<< HEAD
public class ArcView extends View{
=======
public class ArcView extends View {

>>>>>>> Added Arcdegree and selection colors

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.arc_color));
<<<<<<< HEAD
        paint.setStrokeWidth(5);
=======
        paint.setStrokeWidth(10);
>>>>>>> Added Arcdegree and selection colors
        paint.setStyle(Paint.Style.STROKE);

        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
<<<<<<< HEAD
=======

>>>>>>> Added Arcdegree and selection colors
  /*
   * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
   *
   * oval - The bounds of oval used to define the shape and size of the arc
   * startAngle - Starting angle (in degrees) where the arc begins
   * sweepAngle - Sweep angle (in degrees) measured clockwise
   * useCenter - If true, include the center of the oval in the arc, and close it if it is being stroked. This will draw a wedge
   * paint - The paint used to draw the arc
   */
<<<<<<< HEAD
//            oval.set(50, 50, 150, 150);
//            canvas.drawArc(oval, 0, 45, true, paint);
//
//            oval.set(200, 150, 450, 350);
//            canvas.drawArc(oval, 0, 270, true, paint);

        oval.set(20, 20, 200, 200);
        canvas.drawArc(oval, 0, 270, false, paint);
    }
=======

        oval.set(convertDpToPixels(7), convertDpToPixels(7), convertDpToPixels(73), convertDpToPixels(73));
        canvas.drawArc(oval, 0, Utilities.degree, true, paint);
    }

    private int convertDpToPixels(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

>>>>>>> Added Arcdegree and selection colors
}
