/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2016 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package com.adobe.cq.wcm.core.components.models;

import com.day.cq.wcm.api.Page;


public interface NavigationItem {

    /**
     * Returns the navigation title of the {@link Page}. If no navigation title is present it first falls back to the page title and then to
     * the page name
     *
     * @return Navigation title
     */
    public String getTitle();

    /**
     * Returns the link to the related {@link Page}.
     *
     * @return Page path
     */
    public String getLink();

    /**
     * Gets the active information of the current page
     *
     * @return true if it is the current page, otherwise false
     */
    public boolean isActive();

    /**
     * @param active {@code true} if the page element should be marked as active element
     */
    void setActive(boolean active);
}
