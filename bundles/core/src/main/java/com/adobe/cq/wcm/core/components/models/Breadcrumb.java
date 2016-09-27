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

import java.util.Collection;

import com.adobe.cq.wcm.core.components.NavigationItem;

public interface Breadcrumb {

    /**
     * Controls if also hidden navigation items should be shown in the breadcrumb
     *
     * @return true if hidden items should be shown, otherwise false
     */
    boolean getShowHidden();

    /**
     * Controls if the current page should also be displayed in the breadcrumb
     *
     * @return true if the current page shouldn't be displayed in the breadcrumb, otherwise false
     */
    boolean getHideCurrent();

    /**
     * Create collection of pages for the breadcrumb component
     *
     * @return {@link Collection< NavigationItem >} of breadcrumb items
     */
    Collection<NavigationItem> getBreadcrumbItems();

}
