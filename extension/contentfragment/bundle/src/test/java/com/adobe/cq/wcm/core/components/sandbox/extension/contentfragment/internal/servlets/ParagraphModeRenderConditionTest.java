/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
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
package com.adobe.cq.wcm.core.components.sandbox.extension.contentfragment.internal.servlets;

import com.adobe.cq.wcm.core.components.sandbox.extension.contentfragment.models.ContentFragment;
import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ParagraphModeRenderConditionTest extends AbstractServletTest {

    ParagraphModeRenderCondition servlet;
    private static final String renderConditionPath = "/apps/core/wcm/extension/sandbox/components/contentfragment" +
            "/v1/contentfragment/cq:dialog/content/items/tabs/items/properties/items/column/items" +
            "/paragraphControls/items/content/granite:rendercondition";

    private ModelFactory modelFactory;

    @Before
    public void before() throws Exception {
        // create the servlet to test
        servlet = new ParagraphModeRenderCondition();
        Whitebox.setInternalState(servlet, "expressionResolver", expressionResolver);
        modelFactory = mock(ModelFactory.class);
        Whitebox.setInternalState(servlet, "modelFactory", modelFactory);
    }

    @Test
    public void testFalseRenderCondition() throws ServletException, IOException {
        // get datasource resource
        ResourceResolver resolver = CONTEXT.resourceResolver();
        Resource renderConditionMock = resolver.resolve(renderConditionPath);
        final String CF_STRUCTURED_SINGLE_ELEMENT = "structured-single-element";
        ModifiableValueMap map = renderConditionMock.adaptTo(ModifiableValueMap.class);
        map.put("componentPath", TEST_CONTAINER_PATH + "/" + CF_STRUCTURED_SINGLE_ELEMENT);
        map.put("fragmentPath", CONTENT_FRAGMENTS_PATH + "/structured");
        map.put("elementName", "second");

        ContentFragment cf = getTestContentFragment(CF_STRUCTURED_SINGLE_ELEMENT);
        when(modelFactory.getModelFromWrappedRequest(
                any(SlingHttpServletRequest.class),
                any(Resource.class),
                any(Class.class))).thenReturn(cf);

        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(resolver, CONTEXT.bundleContext());
        request.setResource(renderConditionMock);
        // call the servlet
        servlet.doGet(request, new MockSlingHttpServletResponse());

        // return the resulting datasource
        RenderCondition a = (RenderCondition) request.getAttribute(RenderCondition.class.getName());
        assertEquals("Invalid value of render condition", a.check(), false);
    }

    @Test
    public void testTrueRenderCondition() throws ServletException, IOException {
        // get datasource resource
        ResourceResolver resolver = CONTEXT.resourceResolver();
        Resource renderConditionMock = resolver.resolve(renderConditionPath);
        final String CF_STRUCTURED_SINGLE_TEXT_ELEMENT = "structured-single-element-main";
        ModifiableValueMap map = renderConditionMock.adaptTo(ModifiableValueMap.class);
        map.put("componentPath", TEST_CONTAINER_PATH + "/"
                + CF_STRUCTURED_SINGLE_TEXT_ELEMENT);
        map.put("fragmentPath", CONTENT_FRAGMENTS_PATH + "/structured");
        map.put("elementName", "main");

        ContentFragment cf = getTestContentFragment(CF_STRUCTURED_SINGLE_TEXT_ELEMENT);
        when(modelFactory.getModelFromWrappedRequest(
                any(SlingHttpServletRequest.class),
                any(Resource.class),
                any(Class.class))).thenReturn(cf);

        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(resolver, CONTEXT.bundleContext());
        request.setResource(renderConditionMock);
        // call the servlet
        servlet.doGet(request, new MockSlingHttpServletResponse());

        // return the resulting datasource
        RenderCondition a = (RenderCondition) request.getAttribute(RenderCondition.class.getName());
        assertEquals("Invalid value of render condition", a.check(), true);
    }

}
