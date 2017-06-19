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
package com.adobe.cq.wcm.core.components.sandbox.models.form;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionsTypeTest {

    @Test
    public void testFromString() throws Exception {
        // default is Checkbox
        Options.Type defaultType = Options.Type.fromString("abc");
        assertEquals(Options.Type.CHECKBOX, defaultType);
        Options.Type radioType = Options.Type.fromString("radio");
        assertEquals(Options.Type.RADIO, radioType);
        Options.Type dropDownType = Options.Type.fromString("drop-down");
        assertEquals(Options.Type.DROP_DOWN, dropDownType);
        Options.Type multiDropDownType = Options.Type.fromString("multi-drop-down");
        assertEquals(Options.Type.MULTI_DROP_DOWN, multiDropDownType);
        Options.Type checkboxType = Options.Type.fromString("checkbox");
        assertEquals(Options.Type.CHECKBOX, checkboxType);
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals("checkbox", Options.Type.CHECKBOX.getValue());
    }
}