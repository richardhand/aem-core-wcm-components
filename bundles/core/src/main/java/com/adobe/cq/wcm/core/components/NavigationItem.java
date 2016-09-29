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

package com.adobe.cq.wcm.core.components;

import org.apache.commons.lang.StringUtils;

import com.day.cq.wcm.api.Page;

/**
 * POJO for a navigation item. Holds navigation title, link and active state of the {@link Page}
 */
public class NavigationItem {

    private final String title;
    private final String link;
    private final Page page;
    private final boolean active;

    public NavigationItem(Page page, boolean active) {
        this.page = page;
        this.active = active;
        this.title = populateTitle();
        this.link = page.getPath();
    }

    private String populateTitle() {
        String title;
        if (StringUtils.isNotEmpty(page.getNavigationTitle())) {
            title = page.getNavigationTitle();
        } else if (StringUtils.isNotEmpty(page.getTitle())) {
            title = page.getTitle();
        } else {
            title = page.getName();
        }
        return title;
    }

    /**
     * Returns the navigation title of the {@Page}. If no navigation title is present it first falls back to the page title and then to the
     * page name
     *
     * @return Navigation title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the link to the related {@link Page}.
     *
     * @return Page path
     */
    public String getLink() {
        return link;
    }

    /**
     * Gets the active information of the current page
     *
     * @return true if it is the current page, otherwise false
     */
    public boolean isActive() {
        return active;
    }
}
