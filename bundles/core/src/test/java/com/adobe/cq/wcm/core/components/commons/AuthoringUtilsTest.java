/*******************************************************************************
 * Copyright 2015 Adobe Systems Incorporated
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
package com.adobe.cq.wcm.core.components.commons;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.day.cq.wcm.api.AuthoringUIMode;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthoringUtilsTest {

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/commons/authoringutils",
            "/conf/we-retail/settings/wcm/templates/product-page")
            ;
    @Mock
    private SlingHttpServletRequest request;


    @Test
    public void testIsTouch() throws Exception {
        when(request.getAttribute(AuthoringUIMode.REQUEST_ATTRIBUTE_NAME)).thenReturn(AuthoringUIMode.TOUCH);
        assertTrue("Expected to detect touch mode.", AuthoringUtils.isTouch(request));
    }

    @Test
    public void testIsClassic() throws Exception {
        when(request.getAttribute(AuthoringUIMode.REQUEST_ATTRIBUTE_NAME)).thenReturn(AuthoringUIMode.CLASSIC);
        assertTrue("Expected to detect classic mode.", AuthoringUtils.isClassic(request));
    }

    @Test
    public void testIsPageOfAuthoredTemplate() throws Exception {
        Page page = context.currentPage("/conf/we-retail/settings/wcm/templates/product-page/initial");
        assertTrue("Expected that page is part of template editor", AuthoringUtils.isPageOfAuthoredTemplate(page));
    }
}
