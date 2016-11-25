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

import com.adobe.cq.wcm.core.components.models.form.TextField;
import com.adobe.cq.wcm.core.components.models.form.TextField.TYPE;
import com.day.cq.wcm.foundation.forms.FormStructureHelper;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TextFieldImplTest {

    private static final String CONTAINING_PAGE = "/content/we-retail/demo-page";

    private static final String TEXTINPUT1_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/formcontainer/text";

    private static final String TEXTINPUT2_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/formcontainer/text_185087333";

    private static final String RESOURCE_PROPERTY = "resource";

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/text", "/content/we-retail/demo-page");

    private SlingBindings slingBindings;

    @Before
    public void setUp() {
        Page page = context.currentPage(CONTAINING_PAGE);
        slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        context.registerService(FormStructureHelperFactory.class, new FormStructureHelperFactory(){
            @Override
            public FormStructureHelper getFormStructureHelper(Resource formElement) {
                return null;
            }
        });
    }

    @Test
    public void testDefaultInput(){
        Resource resource = context.currentResource(TEXTINPUT1_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY, resource);
        TextField textField = context.request().adaptTo(TextField.class);
        assertEquals("text",textField.getName());
        assertEquals("Text input field",textField.getTitle());
        assertEquals(false,textField.isTitleHidden());
        assertEquals("",textField.getDescription());
        assertEquals(false,textField.getRequired());
        assertEquals("",textField.getRequiredMessage());
        assertEquals(null,textField.getShowHideExpression());
        assertEquals("",textField.getPlaceholder());
        assertEquals(false,textField.isReadOnly());
        assertEquals("",textField.getDefaultValue());
        assertEquals(TYPE.TEXT,textField.getType());
        assertEquals("", textField.getConstraintMessage());
        assertEquals("",textField.getValue());
        assertEquals(2, textField.getRows());
        assertEquals(false, textField.usePlaceholder());
        assertEquals("",textField.getHelpMessage());
    }

    @Test
    public void testInputWithCusomtDataAndAttributes() {
        Resource resource = context.currentResource(TEXTINPUT2_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY, resource);
        TextField textField = context.request().adaptTo(TextField.class);
        assertEquals("Custom Name", textField.getName());
        assertEquals("Custom title",textField.getTitle());
        assertEquals(true,textField.isTitleHidden());
        assertEquals("Custom description",textField.getDescription());
        assertEquals(true,textField.getRequired());
        assertEquals("please fill the field",textField.getRequiredMessage());
        assertEquals("((givenName.equals(\"\"Referees\"\")))",textField.getShowHideExpression());
        assertEquals("Custom help/placeholder message",textField.getPlaceholder());
        assertEquals(true,textField.isReadOnly());
        assertEquals("Custom default value",textField.getDefaultValue());
        assertEquals(TYPE.EMAIL,textField.getType());
        assertEquals("The value should be a valid email address", textField.getConstraintMessage());
        assertEquals("Prefilled Sample Input",textField.getValue());
        assertEquals(3, textField.getRows());
        assertEquals(true, textField.usePlaceholder());
        assertEquals("Custom help/placeholder message", textField.getHelpMessage());
    }
}
