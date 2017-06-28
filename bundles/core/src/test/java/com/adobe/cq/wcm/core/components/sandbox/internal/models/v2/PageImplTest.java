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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.sandbox.models.Page;
import com.adobe.cq.wcm.core.components.testing.MockHtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class PageImplTest extends com.adobe.cq.wcm.core.components.internal.models.v1.PageImplTest {


    @Rule
    public AemContext aemContextAlt = CoreComponentTestContext.createContext(TEST_BASE, ROOT);

    private ClientLibrary mockClientLibrary;

    @Before
    @Override
    public void setUp() {
        pageClass = Page.class;
        aemContext = aemContextAlt;

        mockClientLibrary = Mockito.mock(ClientLibrary.class);
        when(mockClientLibrary.getPath()).thenReturn("/apps/wcm/core/page/clientlibs/favicon");
        when(mockClientLibrary.allowProxy()).thenReturn(true);
        aemContext.registerInjectActivateService(new MockHtmlLibraryManager(mockClientLibrary));
        aemContext.load().json(TEST_BASE + "/test-conf.json", "/conf/coretest/settings");
        aemContext.load().binaryFile(TEST_BASE + "/static.css", DESIGN_PATH + "/static.css");
        aemContext.load().json(TEST_BASE + "/default-tags.json", "/etc/tags/default");
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

    @Override
    protected Page getPageUnderTest(String pagePath) {
        return (Page)super.getPageUnderTest(pagePath);
    }
}
