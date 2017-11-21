/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
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
package com.adobe.cq.wcm.core.components.sandbox.internal.models.v1;

import java.util.Calendar;

import javax.annotation.Nonnull;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.wcm.core.components.internal.Utils;
import com.adobe.cq.wcm.core.components.sandbox.models.ListItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

public class PageListItemImpl implements ListItem {

    private SlingHttpServletRequest request;
    private Resource item;
    private Page page;

    public PageListItemImpl(@Nonnull SlingHttpServletRequest request, @Nonnull Resource item) {
        this.request = request;
        this.item = item;
        this.page = getPage();
    }

    @Override
    public String getURL() {
        if (page != null) {
            return Utils.getURL(request, page);
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (page != null) {
            String title = page.getNavigationTitle();
            if (title == null) {
                title = page.getPageTitle();
            }
            if (title == null) {
                title = page.getTitle();
            }
            if (title == null) {
                title = page.getName();
            }
            return title;
        }
        return null;
    }

    @Override
    public String getDescription() {
        if (page != null) {
            return page.getDescription();
        }
        return null;
    }

    @Override
    public Calendar getLastModified() {
        if (page != null) {
            return page.getLastModified();
        }
        return null;
    }

    private Page getPage() {
        ResourceResolver resourceResolver = item.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            return pageManager.getContainingPage(item);
        }
        return null;
    }

}
