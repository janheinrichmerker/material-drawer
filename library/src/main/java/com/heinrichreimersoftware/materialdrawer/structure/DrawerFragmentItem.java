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

package com.heinrichreimersoftware.materialdrawer.structure;

import android.support.v4.app.Fragment;

public class DrawerFragmentItem extends DrawerItem {

    private Fragment mFragment;

    /**
     * Sets a fragment to the drawer item
     *
     * @param fragment Fragment to set
     */
    public DrawerFragmentItem setFragment(Fragment fragment) {
        mFragment = fragment;
        notifyDataChanged();
        return this;
    }


    /**
     * Gets the fragment of the drawer item
     *
     * @return Fragment of the drawer item
     */
    public Fragment getFragment() {
        return mFragment;
    }

    /**
     * Gets whether the drawer item has a fragment set to it
     *
     * @return True if the drawer item has a fragment set to it, false otherwise.
     */
    public boolean hasFragment() {
        return mFragment != null;
    }

    /**
     * Removes the image from the drawer item
     */
    public DrawerFragmentItem removeFragment() {
        mFragment = null;
        notifyDataChanged();
        return this;
    }
}
