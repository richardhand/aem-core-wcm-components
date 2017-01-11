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

import java.util.Map;

import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.context.MockStyle;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.testing.MockAdapterFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.*;

public class ImageImplTest {

    @Rule
    public final AemContext aemContext = CoreComponentTestContext.createContext("/image", "/content");

    private static final String TEST_ROOT = "/content";
    private static final String PAGE = TEST_ROOT + "/test";
    private static final String IMAGE_PATH = PAGE + "/jcr:content/root/image";
    private static final String IMAGE3_PATH = PAGE + "/jcr:content/root/image3";
    private static final String IMAGE_BINARY_NAME = "Adobe_Systems_logo_and_wordmark.svg.png";
    private static final String CONTEXT_PATH = "/core";
    private static final String IMAGE_TITLE_ALT = "Adobe Logo";
    private static final String IMAGE_FILE_REFERENCE = "/content/dam/core/images/Adobe_Systems_logo_and_wordmark.svg.png";
    private static final String IMAGE_LINK = "https://www.adobe.com";

    private SlingBindings slingBindings;

    @Before
    public void setUp() {
        aemContext.load().json("/image/test-conf.json", "/conf");
        aemContext.registerInjectActivateService(new MockAdapterFactory());
        Page page = aemContext.currentPage(PAGE);
        slingBindings = (SlingBindings) aemContext.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        slingBindings.put(WCMBindings.PAGE_MANAGER, aemContext.pageManager());
    }

    @Test
    public void testImageWithTwoOrMoreSmartSizes() throws Exception {
        String escapedResourcePath = Text.escapePath(IMAGE_PATH);
        Image image = getImageUnderTest(IMAGE_PATH);
        assertEquals(IMAGE_TITLE_ALT, image.getAlt());
        assertEquals(IMAGE_TITLE_ALT, image.getTitle());
        assertEquals(IMAGE_FILE_REFERENCE, image.getFileReference());
        assertEquals(ResourceUtil.getName(IMAGE_FILE_REFERENCE), image.getFileName());
        assertFalse(image.isDecorative());
        assertTrue(image.isLazyLoadingEnabled());
        String expectedJson = "{\"smartImages\":[\"/core/content/test/jcr%3acontent/root/image.img.600.png\"," +
                "\"/core/content/test/jcr%3acontent/root/image.img.700.png\",\"/core/content/test/jcr%3acontent/root/image.img.800.png\"],\"smartSizes\":[600,700,800],\"lazyEnabled\":true}";
        compareJSON(expectedJson, image.getJson());
        assertFalse(image.isTitleAsPopup());
        assertEquals(IMAGE_LINK, image.getLink());
        assertEquals(CONTEXT_PATH + escapedResourcePath + ".img.png", image.getSrc());
    }

    private void compareJSON(String expectedJson, String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map expectedMap = objectMapper.readValue(expectedJson, Map.class);
        Map jsonMap = objectMapper.readValue(json, Map.class);
        assertEquals(expectedMap, jsonMap);
    }

    @Test
    public void testImageWithOneSmartSize() throws Exception {
        String escapedResourcePath = Text.escapePath(IMAGE3_PATH);
        Image image = getImageUnderTest(IMAGE3_PATH);
        assertEquals(IMAGE_TITLE_ALT, image.getAlt());
        assertEquals(IMAGE_TITLE_ALT, image.getTitle());
        assertNull("Did not expect a file reference.", image.getFileReference());
        assertEquals(IMAGE_BINARY_NAME, image.getFileName());
        assertFalse("Did not expect a decorative image.", image.isDecorative());
        assertFalse("Image should not load lazily.", image.isLazyLoadingEnabled());
        assertFalse("Image should not display a caption popup.", image.isTitleAsPopup());
        assertEquals(IMAGE_LINK, image.getLink());
        assertEquals(CONTEXT_PATH + escapedResourcePath + ".img.600.png", image.getSrc());
        String expectedJson = "{\"smartImages\":[\"/core/content/test/jcr%3acontent/root/image3.img.600.png\"],\"smartSizes\":[600]," +
                "\"lazyEnabled\":false}";
        compareJSON(expectedJson, image.getJson());
    }

    private Image getImageUnderTest(String resourcePath) {
        Resource resource = aemContext.currentResource(resourcePath);
        ContentPolicyMapping mapping = resource.adaptTo(ContentPolicyMapping.class);
        ContentPolicy contentPolicy = mapping.getPolicy();
        Resource contentPolicyResource = aemContext.resourceResolver().getResource(contentPolicy.getPath());
        Style style = new MockStyle(contentPolicyResource, contentPolicyResource.adaptTo(ValueMap.class));
        slingBindings.put(WCMBindings.CURRENT_STYLE, style);
        slingBindings.put(SlingBindings.RESOURCE, resource);
        MockSlingHttpServletRequest request = aemContext.request();
        request.setContextPath(CONTEXT_PATH);
        request.setResource(resource);
        return request.adaptTo(Image.class);
    }

}
