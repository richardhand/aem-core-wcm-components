/*******************************************************************************
 * Copyright 2017 Adobe Systems Incorporated
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.adobe.cq.wcm.core.components.sandbox.internal.models.v2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.sandbox.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.sandbox.context.MockStyle;
import com.adobe.cq.wcm.core.components.sandbox.models.Page;
import com.adobe.cq.wcm.core.components.testing.MockHtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyMapping;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class PageImplTest {

    private static final String TEST_BASE = "/page";
    private static final String CONTEXT_PATH = "/core";
    private static final String ROOT = "/content/page";
    private static final String PAGE = ROOT + "/templated-page";
    private static final String DESIGN_PATH = "/etc/designs/mysite";
    private static final String POLICIES_MAPPING_PATH = "policies/jcr:content";

    private static final String FN_FAVICON_ICO = "favicon.ico";
    private static final String FN_FAVICON_PNG = "favicon_32.png";
    private static final String FN_TOUCH_ICON_60 = "touch-icon_60.png";
    private static final String FN_TOUCH_ICON_76 = "touch-icon_76.png";
    private static final String FN_TOUCH_ICON_120 = "touch-icon_120.png";
    private static final String FN_TOUCH_ICON_152 = "touch-icon_152.png";

    private static final String PN_FAVICON_ICO = "faviconIco";
    private static final String PN_FAVICON_PNG = "faviconPng";
    private static final String PN_TOUCH_ICON_60 = "touchIcon60";
    private static final String PN_TOUCH_ICON_76 = "touchIcon76";
    private static final String PN_TOUCH_ICON_120 = "touchIcon120";
    private static final String PN_TOUCH_ICON_152 = "touchIcon152";

    @Rule
    public AemContext aemContext = CoreComponentTestContext.createContext("/page", ROOT);

    private ClientLibrary mockClientLibrary;

    @Before
    public void setUp() {
        mockClientLibrary = Mockito.mock(ClientLibrary.class);
        when(mockClientLibrary.getPath()).thenReturn("/apps/wcm/core/page/clientlibs/favicon");
        when(mockClientLibrary.allowProxy()).thenReturn(true);
        aemContext.registerInjectActivateService(new MockHtmlLibraryManager(mockClientLibrary));
        aemContext.load().json(TEST_BASE + "/test-conf.json", "/conf/coretest/settings");
        aemContext.load().binaryFile(TEST_BASE + "/static.css", DESIGN_PATH + "/static.css");
        aemContext.load().json(TEST_BASE + "/default-tags.json", "/etc/tags/default");
    }

    @Test
    public void testGetLastModifiedDate() throws ParseException {
        Page page = getPageUnderTest(PAGE);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        calendar.setTime(sdf.parse("2016-01-20T10:33:36.000+0100"));
        assertEquals(page.getLastModifiedDate(), calendar);
    }

    @Test
    public void testGetLanguage() {
        Page page = getPageUnderTest(PAGE);
        assertEquals("en-GB", page.getLanguage());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFavicons() {
        Page page = getPageUnderTest(PAGE);
        page.getFavicons();
    }

    @Test
    public void testGetFaviconClientLibPath() throws Exception {
        Page page = getPageUnderTest(PAGE);
        String faviconClientLibPath = page.getFaviconClientLibPath();
        assertEquals("/etc.clientlibs/wcm/core/page/clientlibs/favicon", faviconClientLibPath);
    }

    @Test
    public void testTitle() {
        Page page = getPageUnderTest(PAGE);
        assertEquals("Templated Page", page.getTitle());
    }

    @Test
    public void testDesign() {
        Page page = getPageUnderTest(PAGE);
        assertEquals(DESIGN_PATH, page.getDesignPath());
        assertEquals(DESIGN_PATH + "/static.css", page.getStaticDesignPath());
    }

    @Test
    public void testDefaultDesign() {
        Page page = getPageUnderTest(PAGE, null);
        assertNull(page.getDesignPath());
        assertNull(page.getStaticDesignPath());
    }

    @Test
    public void testKeywords() {
        Page page = getPageUnderTest(PAGE);
        String[] keywordsArray = page.getKeywords();
        assertEquals(3, keywordsArray.length);
        Set<String> keywords = new HashSet<>(keywordsArray.length);
        keywords.addAll(Arrays.asList(keywordsArray));
        assertTrue(keywords.contains("one") && keywords.contains("two") && keywords.contains("three"));
    }

    @Test
    public void testGetClientLibCategories() throws Exception {
        Page page = getPageUnderTest(PAGE);
        assertEquals("coretest.product-page", page.getClientLibCategories()[0]);
    }

    @Test
    public void testGetTemplateName() throws Exception {
        Page page = getPageUnderTest(PAGE);
        assertEquals("product-page", page.getTemplateName());
    }

    private Page getPageUnderTest(String pagePath) {
        return getPageUnderTest(pagePath, DESIGN_PATH);
    }

    private Page getPageUnderTest(String pagePath, String designPath) {
        Resource resource = aemContext.currentResource(pagePath);
        com.day.cq.wcm.api.Page page = spy(aemContext.currentPage(pagePath));
        SlingBindings slingBindings = (SlingBindings) aemContext.request().getAttribute(SlingBindings.class.getName());
        Design design = mock(Design.class);
        if (designPath != null) {
            when(design.getPath()).thenReturn(designPath);
        } else {
            when(design.getPath()).thenReturn(Designer.DEFAULT_DESIGN_PATH);
        }
        Resource templateResource = aemContext.resourceResolver().getResource("/conf/coretest/settings/wcm/templates/product-page");
        Template template = mock(Template.class);
        when(template.hasStructureSupport()).thenReturn(true);
        when(template.adaptTo(Resource.class)).thenReturn(templateResource);
        when(page.getTemplate()).thenReturn(template);

        ContentPolicyMapping mapping = templateResource.getChild(POLICIES_MAPPING_PATH).adaptTo(ContentPolicyMapping.class);
        ContentPolicy contentPolicy = mapping.getPolicy();
        Style style;
        slingBindings.put(WCMBindings.CURRENT_DESIGN, design);
        if (contentPolicy != null) {
            Resource contentPolicyResource = aemContext.resourceResolver().getResource(contentPolicy.getPath());
            style = new MockStyle(contentPolicyResource, contentPolicyResource.adaptTo(ValueMap.class));

        } else {
            style = mock(Style.class);
            when(style.get(anyString(), Matchers.anyObject())).thenAnswer(
                    invocationOnMock -> invocationOnMock.getArguments()[1]
            );
        }
        slingBindings.put(WCMBindings.CURRENT_STYLE, style);
        slingBindings.put(SlingBindings.RESOLVER, aemContext.request().getResourceResolver());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        slingBindings.put(WCMBindings.PAGE_MANAGER, aemContext.pageManager());
        slingBindings.put(SlingBindings.RESOURCE, resource);
        slingBindings.put(WCMBindings.PAGE_PROPERTIES, page.getProperties());
        MockSlingHttpServletRequest request = aemContext.request();
        request.setContextPath(CONTEXT_PATH);
        request.setResource(resource);
        return request.adaptTo(Page.class);
    }
}
