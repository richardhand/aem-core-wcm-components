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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.component.annotations.Component;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.adobe.granite.ui.components.Value;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;

@Component(
        service = { Servlet.class },
        property = {
                "sling.servlet.resourceTypes="+ AllowedStylesDataSourceServlet.RESOURCE_TYPE,
                "sling.servlet.methods=GET",
                "sling.servlet.extensions=html"
        }
)
public class AllowedStylesDataSourceServlet extends SlingSafeMethodsServlet {

    public final static String RESOURCE_TYPE = "core/wcm/components/commons/style/v1/datasource/allowedstyles";
    private final static String DEFAULT_STYLE_TEXT = "None";
    private final static String DEFAULT_STYLE_VALUE = "";
    private final static String NN_STYLES = "styles";
    private final static String PN_STYLE_NAME = "name";

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response)
            throws ServletException, IOException {
        SimpleDataSource allowedStylesDataSource = new SimpleDataSource(getAllowedStyles(request).iterator());
        request.setAttribute(DataSource.class.getName(), allowedStylesDataSource);
    }

    private List<Resource> getAllowedStyles(@Nonnull SlingHttpServletRequest request) {
        List<Resource> allowedStyles = new ArrayList<>();
        ResourceResolver resolver = request.getResourceResolver();
        Resource contentResource = resolver.getResource((String) request.getAttribute(Value.CONTENTPATH_ATTRIBUTE));
        ContentPolicyManager policyMgr = resolver.adaptTo(ContentPolicyManager.class);
        ContentPolicy policy = policyMgr.getPolicy(contentResource);
        // add the default with empty value
        allowedStyles.add(new StyleResource(DEFAULT_STYLE_TEXT, DEFAULT_STYLE_VALUE, resolver));
        if (policy != null) {
            Resource policyRes = policy.adaptTo(Resource.class);
            Resource styles = policyRes.getChild(NN_STYLES);
            if (styles != null) {
                for (Resource style : styles.getChildren()) {
                    ValueMap props = style.getValueMap();
                    String styleName = props.get(PN_STYLE_NAME, "");
                    allowedStyles.add(new StyleResource(styleName, styleName, resolver));
                }
            }
        }
        return allowedStyles;
    }

    public class StyleResource extends TextValueDataResourceSource {

        private final String value;
        private final String text;

        StyleResource(String text, String value, ResourceResolver resourceResolver) {
            super(resourceResolver, StringUtils.EMPTY, RESOURCE_TYPE_NON_EXISTING);
            this.text = text;
            this.value = value;
        }

        @Override
        protected String getText() {
            return text;
        }

        @Override
        protected String getValue() {
            return value;
        }
    }

}
