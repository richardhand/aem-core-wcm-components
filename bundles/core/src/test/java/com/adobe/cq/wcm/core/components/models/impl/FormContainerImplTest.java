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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.*;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.FormContainer;
import com.day.cq.wcm.api.Page;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormContainerImplTest {

    private static final String CONTAINING_PAGE = "/content/we-retail/demo-page";
    
    private static final String FORM1_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/formcontainer";
    
    private static final String FORM2_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/formcontainer_350773202";
    
    private static final String formFields[]={"text","text_185087333"};

    private static final String RESOURCE_PROPERTY = "resource";
    
    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/formcontainer", "/content/we-retail/demo-page");
    
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
    public void testFormWithCustomAttributesAndFields() {
        Resource resource = context.currentResource(FORM1_PATH);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY,resource);
        FormContainer formContainer = context.request().adaptTo(FormContainer.class);
        assertEquals(formContainer.getActionType(), "foundation/components/form/actions/store");
        assertEquals(formContainer.getEnctype(),"application/x-www-form-urlencoded");
        assertEquals(formContainer.getMethod(),"GET");
        assertTrue(StringUtils.isNotBlank(formContainer.getName()));
        assertTrue(StringUtils.isNotBlank(formContainer.getId()));
        assertEquals(formContainer.getAction(),CONTAINING_PAGE+".html");
        List<String> fieldPaths = formContainer.getFormFieldResourcePaths();
        assertEquals(fieldPaths.size(),formFields.length);
        for(int i=0;i<fieldPaths.size();i++){
            assertEquals(fieldPaths.get(i),FORM1_PATH+"/"+formFields[i]);   
        }
    }
    
    @Test
    @Ignore
    public void testFormWithoutCustomAttributesAndField(){
        Resource resource = context.currentResource(FORM2_PATH);
        slingBindings.put(WCMBindings.PROPERTIES,resource.adaptTo(ValueMap.class));
        FormContainer formContainer = context.request().adaptTo(FormContainer.class);
        assertEquals(formContainer.getActionType(), "foundation/components/form/actions/store");
        assertEquals(formContainer.getEnctype(),"multipart/form-data");
        assertEquals(formContainer.getMethod(),"POST");
        assertEquals(formContainer.getName(),"demo-page");
        assertEquals(formContainer.getId(),"demo-page");
        assertEquals(formContainer.getAction(),"/content/usergenerated/we-retail/demo-page/cq-gen1475666626866/");
        List<String> fieldPaths = formContainer.getFormFieldResourcePaths();
        assertEquals(fieldPaths.size(),0);
    }
}
