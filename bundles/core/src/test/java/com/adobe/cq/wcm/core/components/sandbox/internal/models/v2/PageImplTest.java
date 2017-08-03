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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.SlingModelFilter;
import com.adobe.cq.wcm.core.components.sandbox.models.Page;
import com.adobe.cq.wcm.core.components.testing.MockHtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.ClientLibrary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class PageImplTest extends com.adobe.cq.wcm.core.components.internal.models.v1.PageImplTest {

    private ClientLibrary mockClientLibrary;

    @Mock
    protected SlingModelFilter slingModelFilterMock;

    @Mock
    protected ModelFactory modelFactoryMock;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        pageClass = Page.class;
        mockClientLibrary = Mockito.mock(ClientLibrary.class);

        MockitoAnnotations.initMocks(this);
        aemContext.registerService(SlingModelFilter.class, slingModelFilterMock);
        aemContext.registerService(ModelFactory.class, modelFactoryMock);

        when(mockClientLibrary.getPath()).thenReturn("/apps/wcm/core/page/clientlibs/favicon");
        when(mockClientLibrary.allowProxy()).thenReturn(true);
        aemContext.registerInjectActivateService(new MockHtmlLibraryManager(mockClientLibrary));
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
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
    public void testGetCssClasses() throws Exception {
        Page page = getPageUnderTest(PAGE);
        String cssClasses = page.getCssClassNames();
        assertEquals("The CSS classes of the page are not expected: " + PAGE, "class1 class2", cssClasses);
    }
    @Test
    public void testGetExportedType() throws Exception {
        Page page = getPageUnderTest(PAGE);
        assertEquals("core/wcm/components/page", page.getExportedType());
    }

    @Test
    public void testGetExportedEmptyItems() throws Exception {
        Mockito.doReturn(Collections.EMPTY_LIST).when(slingModelFilterMock).filterChildResources(Mockito.any());

        Page page = getPageUnderTest(PAGE);
        assertThat(page.getExportedItems(), is(Collections.EMPTY_MAP));
    }

    @Test
    public void testGetExportedItems() throws Exception {
        final String RESOURCE_NAME = "Res1";
        Resource res = mockResource(RESOURCE_NAME);

        ComponentExporter componentExporter = Mockito.mock(ComponentExporter.class);
        Mockito.when(modelFactoryMock.getModelFromWrappedRequest(Mockito.any(), Mockito.any(), Mockito.eq(ComponentExporter.class)))
                .thenReturn(componentExporter);

        Map<String, ComponentExporter> exportedItems = new LinkedHashMap<>();
        exportedItems.put(RESOURCE_NAME, componentExporter);

        Page page = getPageUnderTest(PAGE);
        Assert.assertEquals(page.getExportedItems(), exportedItems);

    }

    @Test
    public void testGetEmptyExportedItemsOrder() {
        Mockito.doReturn(Collections.EMPTY_LIST).when(slingModelFilterMock).filterChildResources(Mockito.any());

        Page page = getPageUnderTest(PAGE);
        assertThat(page.getExportedItemsOrder(), is(ArrayUtils.EMPTY_STRING_ARRAY));
    }

    @Test
    public void testGetExportedItemsOrder() {
        final String RESOURCE_NAME = "Res1";
        Resource res = mockResource(RESOURCE_NAME);

        Mockito.when(modelFactoryMock.getModelFromWrappedRequest(Mockito.any(), Mockito.eq(res), Mockito.eq(ComponentExporter.class)))
                .thenReturn(Mockito.mock(ComponentExporter.class));

        Page page = getPageUnderTest(PAGE);
        Assert.assertArrayEquals(page.getExportedItemsOrder(), new String[] {RESOURCE_NAME});
    }

    @Override
    protected Page getPageUnderTest(String pagePath) {
        return (Page)super.getPageUnderTest(pagePath);
    }

    private Resource mockResource(String name) {
        Resource res = Mockito.mock(Resource.class);
        when(res.getName()).thenReturn(name);
        Mockito.doReturn(Arrays.asList(res)).when(slingModelFilterMock).filterChildResources(Mockito.any());
        return res;
    }
}
