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

import com.day.cq.wcm.foundation.forms.FormStructureHelper;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import io.wcm.testing.mock.aem.junit.AemContext;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.FormContainer;
import com.adobe.cq.wcm.core.components.models.TextInput;
import com.day.cq.wcm.api.Page;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TextInputImplTest {

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
        slingBindings.put(RESOURCE_PROPERTY,resource);
        TextInput textInput = context.request().adaptTo(TextInput.class);
        assertEquals("text",textInput.getName());
        assertEquals("Label for Text Input",textInput.getLabel());
        assertEquals("",textInput.getValue());
        assertEquals("Text placeholder",textInput.getPlaceholder());
    }
    
    @Test
    public void testInputWithCusomtDataAndAttributes() {
        Resource resource = context.currentResource(TEXTINPUT2_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY,resource);
        TextInput textInput = context.request().adaptTo(TextInput.class);
        assertEquals("input-field-2",textInput.getName());
        assertEquals("label-input-field-2",textInput.getLabel());
        assertEquals("Prefilled Sample Input",textInput.getValue());
        assertEquals("Please enter the text",textInput.getPlaceholder());
    }
}
