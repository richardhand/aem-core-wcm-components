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
package com.adobe.cq.wcm.core.components.models.impl.v1;

import com.day.cq.wcm.foundation.forms.FormStructureHelper;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import io.wcm.testing.mock.aem.junit.AemContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.servlethelpers.MockRequestDispatcherFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.adobe.cq.sightly.WCMBindings;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.form.Container;
import com.day.cq.wcm.api.Page;

import java.util.List;

import javax.servlet.RequestDispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContainerImplTest {

    private static final String CONTAINING_PAGE = "/content/we-retail/demo-page";
    
    private static final String FORM1_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/container";
    
    private static final String FORM2_PATH = CONTAINING_PAGE+"/jcr:content/root/responsivegrid/container_350773202";
    
    private static final String formFields[]={"text","text_185087333"};

    private static final String RESOURCE_PROPERTY = "resource";
    
    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/form/container", "/content/we-retail/demo-page");
    
    private SlingBindings slingBindings;

    @Mock
    private FormStructureHelperFactory formStructureHelperFactory;

    @Mock
    private FormStructureHelper formStructureHelper;

    @Mock
    private MockRequestDispatcherFactory requestDispatcherFactory;

    @Mock
    private RequestDispatcher requestDispatcher;
    
    @Before
    public void setUp() {
        Page page = context.currentPage(CONTAINING_PAGE);
        slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindings.CURRENT_PAGE, page);
        context.registerService(FormStructureHelperFactory.class, formStructureHelperFactory);
    }
    
    @Test
    public void testFormWithCustomAttributesAndFields() {
        Resource resource = context.currentResource(FORM1_PATH);
        when(formStructureHelperFactory.getFormStructureHelper(resource)).thenReturn(formStructureHelper);
        slingBindings.put(WCMBindings.PROPERTIES, resource.adaptTo(ValueMap.class));
        slingBindings.put(RESOURCE_PROPERTY,resource);
        MockSlingHttpServletRequest request = context.request();
        request.setRequestDispatcherFactory(requestDispatcherFactory);
        when(requestDispatcherFactory.getRequestDispatcher((Resource)any(), (RequestDispatcherOptions)any()))
                .thenReturn(requestDispatcher);
        Container container = request.adaptTo(Container.class);
        assertEquals(container.getActionType(), "foundation/components/form/actions/store");
        assertEquals(container.getEnctype(),"application/x-www-form-urlencoded");
        assertEquals(container.getMethod(),"GET");
        assertTrue(StringUtils.isNotBlank(container.getName()));
        assertTrue(StringUtils.isNotBlank(container.getId()));
        assertEquals(container.getAction(),CONTAINING_PAGE+".html");
    }
    
    @Test
    @Ignore
    public void testFormWithoutCustomAttributesAndField(){
        Resource resource = context.currentResource(FORM2_PATH);
        slingBindings.put(WCMBindings.PROPERTIES,resource.adaptTo(ValueMap.class));
        Container container = context.request().adaptTo(Container.class);
        assertEquals(container.getActionType(), "foundation/components/form/actions/store");
        assertEquals(container.getEnctype(),"multipart/form-data");
        assertEquals(container.getMethod(),"POST");
        assertEquals(container.getName(),"demo-page");
        assertEquals(container.getId(),"demo-page");
        assertEquals(container.getAction(),"/content/usergenerated/we-retail/demo-page/cq-gen1475666626866/");
    }
}
