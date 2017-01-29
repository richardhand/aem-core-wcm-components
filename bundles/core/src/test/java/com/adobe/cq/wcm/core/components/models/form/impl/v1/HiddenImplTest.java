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
package com.adobe.cq.wcm.core.components.models.form.impl.v1;

import com.adobe.cq.wcm.core.components.models.form.Field;
import io.wcm.testing.mock.aem.junit.AemContext;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.forms.FormStructureHelper;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HiddenImplTest {

    private static final String CONTAINING_PAGE = "/content/we-retail/demo-page";

    private static final String HIDDENINPUT1_PATH = CONTAINING_PAGE + "/jcr:content/root/responsivegrid/container/hidden_1";

    private static final String HIDDENINPUT2_PATH = CONTAINING_PAGE + "/jcr:content/root/responsivegrid/container/hidden_2";

    private static final String RESOURCE_PROPERTY = "resource";

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/hidden", "/content/we-retail/demo-page");

    private SlingBindings slingBindings;

    @Before
    public void setUp() {
        Page page = context.currentPage(CONTAINING_PAGE);
        slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        context.registerService(FormStructureHelperFactory.class, new FormStructureHelperFactory() {
            @Override
            public FormStructureHelper getFormStructureHelper(Resource formElement) {
                return null;
            }
        });
    }

    @Test
    public void testDefaultInput() {
        Resource resource = context.currentResource(HIDDENINPUT1_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY, resource);
        Field hiddenField = context.request().adaptTo(Field.class);
        assertEquals("hidden", hiddenField.getName());
        assertEquals("", hiddenField.getValue());
        assertEquals("", hiddenField.getId());
    }

    @Test
    public void testInputWithCustomData() {
        Resource resource = context.currentResource(HIDDENINPUT2_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY, resource);
        Field hiddenField = context.request().adaptTo(Field.class);
        assertEquals("Custom_Name", hiddenField.getName());
        assertEquals("Custom value", hiddenField.getValue());
        assertEquals("hidden-field-id", hiddenField.getId());
    }
}
