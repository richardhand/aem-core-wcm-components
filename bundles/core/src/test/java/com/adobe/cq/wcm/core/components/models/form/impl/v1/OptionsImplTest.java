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

import java.util.List;

import org.apache.sling.api.resource.ValueMap;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.wcm.core.components.models.form.OptionItem;
import io.wcm.testing.mock.aem.junit.AemContext;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.form.Options;

public class OptionsImplTest {

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/options", "/content/options");

    @Test
    public void testCheckboxOptionsType() throws Exception {
        Resource optionsRes = context.currentResource("/content/options/checkbox");
        Options options = optionsRes.adaptTo(Options.class);
        assertEquals("name1", options.getName());
        assertEquals("caption1", options.getCaption());
        assertEquals("helpMessage1", options.getHelpMessage());
        assertEquals("checkbox", options.getType());

        List<OptionItem> optionItems = options.getOptionItems();
        assertNotNull(optionItems);
        assertTrue(optionItems.size() == 3);

        // test the first option item
        OptionItem item = optionItems.get(0);
        evaluateOptionItem(item, "t1", "v1", true, true);
        item = optionItems.get(1);
        evaluateOptionItem(item, "t2", "v2", true, false);
        item = optionItems.get(2);
        evaluateOptionItem(item, "t3", "v3", false, false);
    }

    @Test
    public void testRadioOptionsType() throws Exception {
        Resource optionsRes = context.currentResource("/content/options/radio");
        Options options = optionsRes.adaptTo(Options.class);
        assertEquals("radio", options.getType());
    }

    @Test
    public void testDropDownOptionsType() throws Exception {
        Resource optionsRes = context.currentResource("/content/options/drop-down");
        Options options = optionsRes.adaptTo(Options.class);
        assertEquals("drop-down", options.getType());
    }

    @Test
    public void testMulitDropDownOptionsType() throws Exception {
        Resource optionsRes = context.currentResource("/content/options/multi-drop-down");
        Options options = optionsRes.adaptTo(Options.class);
        assertEquals("multi-drop-down", options.getType());
    }

    private void evaluateOptionItem(OptionItem item, String text, String value, boolean selected, boolean disabled) {
        assertEquals(text, item.getText());
        assertEquals(value, item.getValue());
        assertEquals(selected, item.isSelected());
        assertEquals(disabled, item.isDisabled());
    }

}
