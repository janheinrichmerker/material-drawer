package com.heinrichreimersoftware.materialdrawer.animation;

import android.view.animation.Interpolator;

public class StepInterpolator implements Interpolator {

    private int steps;

    public StepInterpolator(int steps){
        this.steps = steps;
    }

    public StepInterpolator(){
        this.steps = 1;
    }

    @Override
    public float getInterpolation(float input) {
        return Math.round(input * steps) / steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
