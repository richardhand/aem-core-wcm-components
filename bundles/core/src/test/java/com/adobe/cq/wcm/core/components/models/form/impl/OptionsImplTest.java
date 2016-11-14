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
package com.adobe.cq.wcm.core.components.models.form.impl;

import java.util.List;

import org.apache.sling.api.resource.ValueMap;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import io.wcm.testing.mock.aem.junit.AemContext;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.form.Options;

public class OptionsImplTest {

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/options", "/content/options");

    @Test
    public void testOptionsField() throws Exception {
        Resource optionsRes = context.currentResource("/content/options");
        Options options = optionsRes.adaptTo(Options.class);
        List<Resource> optionItems = options.getOptionItems();
        assertEquals("name1", options.getName());
        assertTrue(options.multiSelection());
        assertFalse(options.collapsed());
        assertEquals("class1", options.getCssClass());
        assertNotNull(optionItems);
        assertTrue(optionItems.size() == 3);

        // test the first option item
        Resource item0 = optionItems.get(0);
        ValueMap props = item0.adaptTo(ValueMap.class);
        String text = props.get("text", String.class);
        String value = props.get("value", String.class);
        String selected = props.get("selected", String.class);
        assertEquals("t1", text);
        assertEquals("v1", value);
        assertEquals("true", selected);

    }

}
