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

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.form.DataSourceModel;
import com.day.cq.wcm.foundation.forms.FormsManager;
import io.wcm.testing.mock.aem.junit.AemContext;

import static com.adobe.cq.wcm.core.components.models.form.impl.TextValueDataResourceSource.*;
import static com.adobe.cq.wcm.core.components.models.form.impl.TextValueDataResourceSource.PN_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormActionTypeDataSourceTest {

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/formactiontype", "/libs");

    @Mock
    private FormsManager formsManager;

    @Mock
    private FormsManager.ComponentDescription description;

    @Mock
    private ResourceResolver resourceResolver;

    private DataSourceModel underTest;
    private MockSlingHttpServletRequest spyRequest;

    @Before
    public void setUp() throws Exception {
        MockSlingHttpServletRequest request = context.request();
        spyRequest = spy(request);
        when(spyRequest.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(FormsManager.class)).thenReturn(this.formsManager);
        ArrayList<FormsManager.ComponentDescription> componentDescriptions = new ArrayList<>();
        componentDescriptions.add(description);
        when(this.formsManager.getActions()).thenReturn(componentDescriptions.iterator());
        when(description.getTitle()).thenReturn("Form Action");
        when(description.getResourceType()).thenReturn("form/action");
        underTest = spyRequest.adaptTo(DataSourceModel.class);
    }

    @Test
    public void testDataSource() throws Exception {
        com.adobe.granite.ui.components.ds.DataSource dataSource = (com.adobe.granite.ui.components.ds.DataSource) spyRequest.getAttribute(
                com.adobe.granite.ui.components.ds.DataSource.class.getName());
        assertNotNull(dataSource);
        Iterator<Resource> iterator = dataSource.iterator();
        Resource resource = iterator.next();
        ValueMap valueMap = resource.adaptTo(ValueMap.class);
        assertEquals("Form Action", valueMap.get(PN_TEXT, String.class));
        assertEquals("form/action", valueMap.get(PN_VALUE, String.class));
    }
}