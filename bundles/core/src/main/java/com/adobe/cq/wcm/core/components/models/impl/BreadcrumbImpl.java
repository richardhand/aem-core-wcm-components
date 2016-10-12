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
package com.adobe.cq.wcm.core.components.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import com.adobe.cq.wcm.core.components.NavigationItem;
import com.adobe.cq.wcm.core.components.models.Breadcrumb;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = Breadcrumb.class,
       resourceType = BreadcrumbImpl.RESOURCE_TYPE)
public class BreadcrumbImpl implements Breadcrumb {

    public static final String RESOURCE_TYPE = "core/wcm/components/breadcrumb";
    protected static final boolean PROP_SHOW_HIDDEN_DEFAULT = false;
    protected static final String PN_SHOW_HIDDEN = "showHidden";
    protected static final String PN_DEFAULT_SHOW_HIDDEN = "defaultShowHidden";
    protected static final boolean PROP_HIDE_CURRENT_DEFAULT = false;
    protected static final String PN_HIDE_CURRENT = "hideCurrent";
    protected static final String PN_DEFAULT_HIDE_CURRENT = "defaultHideCurrent";
    protected static final int PROP_START_LEVEL_DEFAULT = 2;
    protected static final String PN_START_LEVEL = "startLevel";
    protected static final String PN_DEFAULT_START_LEVEL = "defaultStartLevel";

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    private boolean showHidden;
    private boolean hideCurrent;
    private int startLevel;
    List<NavigationItem> breadcrumbItems;

    @PostConstruct
    private void initModel() {
        startLevel = properties.get(PN_START_LEVEL, currentStyle.get(PN_DEFAULT_START_LEVEL, PROP_START_LEVEL_DEFAULT));
        showHidden = properties.get(PN_SHOW_HIDDEN, currentStyle.get(PN_DEFAULT_SHOW_HIDDEN, PROP_SHOW_HIDDEN_DEFAULT));
        hideCurrent = properties.get(PN_HIDE_CURRENT, currentStyle.get(PN_DEFAULT_HIDE_CURRENT, PROP_HIDE_CURRENT_DEFAULT));
    }

    @Override
    public Collection<NavigationItem> getBreadcrumbItems() {
        if (breadcrumbItems == null) {
            breadcrumbItems = new ArrayList<>();
            createBreadcrumbItems(breadcrumbItems);
        }
        return breadcrumbItems;
    }

    private List<NavigationItem> createBreadcrumbItems(List<NavigationItem> breadcrumbItems) {
        int currentLevel = currentPage.getDepth();
        addNavigationItems(breadcrumbItems, currentLevel);
        return breadcrumbItems;
    }

    private void addNavigationItems(List<NavigationItem> breadcrumbItems, int currentLevel) {
        while (startLevel < currentLevel) {
            Page page = currentPage.getAbsoluteParent(startLevel);
            if (page != null) {
                boolean isActivePage = page.equals(currentPage);
                if (isActivePage && hideCurrent) {
                    break;
                }
                if (checkIfNotHidden(page)) {
                    breadcrumbItems.add(new NavigationItem(page, isActivePage));
                }
                startLevel++;
            } else {
                break;
            }
        }
    }

    private boolean checkIfNotHidden(Page page) {
        return !page.isHideInNav() || showHidden;
    }
}
