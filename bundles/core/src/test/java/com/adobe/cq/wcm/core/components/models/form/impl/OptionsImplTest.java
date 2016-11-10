/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2016 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
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
