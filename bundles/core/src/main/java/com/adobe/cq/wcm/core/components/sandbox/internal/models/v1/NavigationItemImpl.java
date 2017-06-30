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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.adobe.cq.wcm.core.components.sandbox.models.NavigationItem;
import com.day.cq.wcm.api.Page;

public class NavigationItemImpl extends com.adobe.cq.wcm.core.components.internal.models.v1.NavigationItemImpl implements NavigationItem {

    protected SlingHttpServletRequest request;
    protected List<NavigationItem> children = Collections.emptyList();
    protected int level;


    public NavigationItemImpl(Page page, boolean active, SlingHttpServletRequest request, int level, List<NavigationItem> children) {
        super(page, active);
        this.request = request;
        this.level = level;
        this.children = children;
    }

    @Override
    public List<NavigationItem> getChildren() {
        return children;
    }

    @Override
    public String getURL() {
        String url = page.getVanityUrl();
        if (StringUtils.isEmpty(url)) {
            url = page.getPath() + ".html";
        }
        return request.getContextPath() + url;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
