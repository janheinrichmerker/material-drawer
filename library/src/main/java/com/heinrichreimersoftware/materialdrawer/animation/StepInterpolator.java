/*
 * Copyright 2015 Heinrich Reimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heinrichreimersoftware.materialdrawer.animation;

import android.view.animation.Interpolator;

public class StepInterpolator implements Interpolator {

    private int steps;

    public StepInterpolator(int steps) {
        this.steps = steps;
    }

    public StepInterpolator() {
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
