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

package com.adobe.cq.wcm.core.components.internal.servlets;


import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.granite.ui.components.Value;
import com.adobe.granite.ui.components.ds.DataSource;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.google.common.base.Function;
import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AllowedStylesDataSourceServletTest {

    private static final String CONTENT_PATH = "/tmp/page/jcr:content/par/title";

    @Rule
    public AemContext context = CoreComponentTestContext.createContext("/servlet/allowed-styles-datasource", "/tmp");

    @Mock
    private ContentPolicyManager contentPolicyManager;

    @Mock
    private ContentPolicy policy;

    private SlingHttpServletRequest request;

    AllowedStylesDataSourceServlet dataSourceServlet;

    @Before
    public void init() throws Exception {
        context.registerAdapter(ResourceResolver.class, ContentPolicyManager.class, new Function<ResourceResolver, ContentPolicyManager>() {
            @Nullable
            @Override
            public ContentPolicyManager apply(@Nullable ResourceResolver input) {
                return contentPolicyManager;
            }
        });

        dataSourceServlet = new AllowedStylesDataSourceServlet();
        request = context.request();
        request.setAttribute(Value.CONTENTPATH_ATTRIBUTE, CONTENT_PATH);

        when(contentPolicyManager.getPolicy(any(Resource.class))).thenReturn(policy);

    }

    @Test
    public void testDataSourceAllowNoStyle() throws Exception {

        when(policy.adaptTo(Resource.class)).thenReturn(context.resourceResolver().getResource("/tmp/no-style-policy"));

        dataSourceServlet.doGet(request, context.response());
        DataSource dataSource = (DataSource) context.request().getAttribute(DataSource.class.getName());
        assertNotNull(dataSource);
        assertTrue("Expected not empty dataSource", dataSource.iterator().hasNext());
        final List<TextValueDataResourceSource> styles = new ArrayList<>();
        dataSource.iterator().forEachRemaining(resource -> {
            assertTrue("Expected StyleResource class",
                    TextValueDataResourceSource.class.isAssignableFrom(resource.getClass()));
            styles.add((TextValueDataResourceSource) resource);
        });
        assertEquals("Unexpected number of styles", 1, styles.size());
        TextValueDataResourceSource style = styles.get(0);
        assertEquals(style.getText(), "None");
        assertEquals(style.getValue(), "");
    }

    @Test
    public void testDataSource() throws Exception {
        when(policy.adaptTo(Resource.class)).thenReturn(context.resourceResolver().getResource("/tmp/foobar-style-policy"));

        dataSourceServlet.doGet(request, context.response());
        DataSource dataSource = (DataSource) context.request().getAttribute(DataSource.class.getName());
        assertNotNull(dataSource);
        assertTrue("Expected not empty dataSource", dataSource.iterator().hasNext());
        final List<TextValueDataResourceSource> styles = new ArrayList<>();
        dataSource.iterator().forEachRemaining(resource -> {
            assertTrue("Expected StyleResource class",
                    TextValueDataResourceSource.class.isAssignableFrom(resource.getClass()));
            styles.add((TextValueDataResourceSource) resource);
        });
        assertEquals("Unexpected number of styles", 3, styles.size());
        styles.forEach(style -> {
            assertTrue("Expected text in (None, foo1, foo2): " + style.getText(),
                    style.getText().matches("None|foo[1|2]"));
            assertTrue("Expected value in ('', bar1, bar2): " + style.getValue(),
                    style.getValue().matches("|bar[1|2]"));
        });

    }
}
