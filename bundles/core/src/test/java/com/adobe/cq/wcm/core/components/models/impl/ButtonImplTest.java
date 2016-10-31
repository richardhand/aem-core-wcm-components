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

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;

import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import com.adobe.cq.wcm.core.components.models.Button;

public class ButtonImplTest {

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/button", "/content/buttons");

    /**
     * Tests an empty button.
     *
     * Note: the test button is created by the {@link SlingContext}.
     */
    @Test
    public void testEmptyButton() throws Exception {
        Map<String,Object> properties = new HashMap<>();
        properties.put("type", "reset");
        Resource buttonRes = context.create().resource("/content/button", properties);
        Button button = buttonRes.adaptTo(Button.class);
        assertEquals("reset", button.getType());
        assertEquals("", button.getTitle());
        assertEquals("", button.getCssClass());
        assertTrue(!button.isDisabled());
        assertEquals("", button.getName());
        assertEquals("", button.getValue());
        assertTrue(!button.isAutofocus());
    }

    /**
     * Tests a fully configured button.
     *
     * Note: the test button is loaded from a JSON file by the {@link SlingContext}.
     */
    @Test
    public void testJsonButton() throws Exception {
        Resource buttonsRes = context.currentResource("/content/buttons");
        Resource buttonRes1 = buttonsRes.getChild("button1");
        Button button = buttonRes1.adaptTo(Button.class);
        assertEquals("submit", button.getType());
        assertEquals("title1", button.getTitle());
        assertEquals("class1 class2", button.getCssClass());
        assertTrue(button.isDisabled());
        assertEquals("name1", button.getName());
        assertEquals("value1", button.getValue());
        assertTrue(button.isAutofocus());
    }

}
