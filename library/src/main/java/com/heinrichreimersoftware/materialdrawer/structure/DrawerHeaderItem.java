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

/**
 * {@link DrawerItem} which should only display a divider.
 */
public class DrawerHeaderItem extends DrawerItem {
    public DrawerHeaderItem() {
        setIsHeader(true);
    }


    /**
     * Sets a title to the header
     *
     * @param title Title to set
     */
    public DrawerItem setTitle(String title) {
        setTextPrimary(title);
        return this;
    }

    /**
     * Gets the title of the header
     *
     * @return Title of the header
     */
    public String getTitle() {
        return getTextPrimary();
    }

    /**
     * Gets whether the header has a title set to it
     *
     * @return True if the header has a title set to it, false otherwise.
     */
    public boolean hasTitle() {
        return hasTextPrimary();
    }


    /**
     * Removes the title from the header
     */
    public DrawerItem removeTitle() {
        removeTextPrimary();
        return this;
    }
}
