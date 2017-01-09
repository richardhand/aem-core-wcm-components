/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.adobe.cq.wcm.core.components.models.impl.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.Page;
import com.adobe.cq.wcm.core.components.testing.MockAdapterFactory;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.designer.Design;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PageImplTest {

    private static final String TEST_BASE = "/page";
    private static final String CONTEXT_PATH = "/core";
    private static final String ROOT = "/content/page";
    private static final String PAGE = ROOT + "/templated-page";
    private static final String DESIGN_PATH = "/etc/designs/mysite";
    private static final String FN_ICO_FAVICON = "favicon.ico";
    private static final String FN_PNG_FAVICON = "favicon_32.png";
    private static final String FN_TOUCH_ICON_60 = "touch-icon_60.png";
    private static final String FN_TOUCH_ICON_76 = "touch-icon_76.png";
    private static final String FN_TOUCH_ICON_120 = "touch-icon_120.png";
    private static final String FN_TOUCH_ICON_152 = "touch-icon_152.png";

    private SlingBindings slingBindings;

    @ClassRule
    public final static AemContext aemContext = CoreComponentTestContext.createContext("/page", ROOT);

    @BeforeClass
    public static void setUp() {
        aemContext.load().json(TEST_BASE + "/test-conf.json", "/conf/we-retail/settings");
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_ICO_FAVICON, DESIGN_PATH + "/" + FN_ICO_FAVICON);
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_PNG_FAVICON, DESIGN_PATH + "/" + FN_PNG_FAVICON);
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_TOUCH_ICON_60, DESIGN_PATH + "/" + FN_TOUCH_ICON_60);
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_TOUCH_ICON_76, DESIGN_PATH + "/" + FN_TOUCH_ICON_76);
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_TOUCH_ICON_120, DESIGN_PATH + "/" + FN_TOUCH_ICON_120);
        aemContext.load().binaryFile(TEST_BASE + "/" + FN_TOUCH_ICON_152, DESIGN_PATH + "/" + FN_TOUCH_ICON_152);
        aemContext.load().binaryFile(TEST_BASE + "/static.css", DESIGN_PATH + "/static.css");
        aemContext.load().json(TEST_BASE + "/default-tags.json", "/etc/tags/default");
        aemContext.registerInjectActivateService(new MockAdapterFactory());
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

    @Test
    public void testFavicons() {
        Page page = getPageUnderTest(PAGE);
        assertEquals(DESIGN_PATH + "/"+FN_ICO_FAVICON, page.getICOFavicon());
        assertEquals(DESIGN_PATH + "/"+FN_PNG_FAVICON, page.getPNGFavicon());
        assertEquals(DESIGN_PATH + "/"+FN_TOUCH_ICON_60, page.getTouchIcon60());
        assertEquals(DESIGN_PATH + "/"+FN_TOUCH_ICON_76, page.getTouchIcon76());
        assertEquals(DESIGN_PATH + "/"+FN_TOUCH_ICON_120, page.getTouchIcon120());
        assertEquals(DESIGN_PATH + "/"+FN_TOUCH_ICON_152, page.getTouchIcon152());
    }

    @Test
    public void testTitle() {
        Page page = getPageUnderTest(PAGE);
        assertEquals("Templated Page", page.getTitle());
    }

    @Test
    public void testDesign() {
        Page page = getPageUnderTest(PAGE);
        assertEquals(DESIGN_PATH + ".css", page.getDesignPath());
        assertEquals(DESIGN_PATH + "/static.css", page.getStaticDesignPath());
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
        assertEquals("we-retail.product-page", page.getClientLibCategories()[0]);
    }

    @Test
    public void testGetTemplateName() throws Exception {
        Page page = getPageUnderTest(PAGE);
        assertEquals("product-page", page.getTemplateName());
    }

    private Page getPageUnderTest(String pagePath) {
        Resource resource = aemContext.currentResource(pagePath);
        com.day.cq.wcm.api.Page page = spy(aemContext.currentPage(pagePath));
        slingBindings = (SlingBindings) aemContext.request().getAttribute(SlingBindings.class.getName());
        Design design = mock(Design.class);
        when(design.getPath()).thenReturn(DESIGN_PATH);

        Resource templateResource = aemContext.resourceResolver().getResource("/conf/we-retail/settings/wcm/templates/product-page");
        Template template = mock(Template.class);
        when(template.hasStructureSupport()).thenReturn(true);
        when(template.adaptTo(Resource.class)).thenReturn(templateResource);
        when(page.getTemplate()).thenReturn(template);


        slingBindings.put(WCMBindings.CURRENT_DESIGN, design);
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
