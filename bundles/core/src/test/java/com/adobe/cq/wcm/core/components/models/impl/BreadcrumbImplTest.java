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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.ComponentAemContext;
import com.adobe.cq.wcm.core.components.context.MockStyle;
import com.adobe.cq.wcm.core.components.models.Breadcrumb;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.*;

public class BreadcrumbImplTest {

    private static final String CURRENT_PAGE = "/content/breadcrumb/women/shirts/devi-sleeveless-shirt";

    @Rule
    public AemContext context = ComponentAemContext.createContext("/breadcrumb", "/content/breadcrumb/women");
    private Breadcrumb underTest;

    @Before
    public void setUp() throws Exception {
        Page page = context.currentPage(CURRENT_PAGE);
        Resource resource = context.currentResource(CURRENT_PAGE
                + "/jcr:content/header/breadcrumb");
        SlingBindings slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        slingBindings.put(WCMBindings.CURRENT_STYLE, new MockStyle(resource));
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        context.request().setAttribute(SlingBindings.class.getName(), slingBindings);
        underTest = context.request().adaptTo(Breadcrumb.class);
    }

    @Test
    public void testGetShowHidden() throws Exception {
        assertTrue(underTest.getShowHidden());
    }

    @Test
    public void testGetHideCurrent() throws Exception {
        assertTrue(underTest.getHideCurrent());
    }

    @Test
    public void testGetBreadcrumbItems() throws Exception {
        assertEquals(3, underTest.getBreadcrumbItems().size());
    }
}