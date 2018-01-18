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
package com.adobe.cq.wcm.core.components.extension.contentfragment.internal.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.cq.wcm.core.components.extension.contentfragment.models.ContentFragment;
import com.adobe.granite.ui.components.Config;
import com.adobe.granite.ui.components.ExpressionHelper;
import com.adobe.granite.ui.components.ExpressionResolver;
import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;


/**
 * Render condition that returns true if the referenced content fragment component renders in paragraph mode. This is
 * the case if the component configuration defines to render a single element (or if the referenced fragment contains
 * only one element) and if that element is a multiline text element (according to {@link ContentFragment.Element#isMultiLine()}).
 */
@Component(
    service = { Servlet.class },
    property = {
        "sling.servlet.resourceTypes="+ ParagraphModeRenderCondition.RESOURCE_TYPE,
        "sling.servlet.methods=GET"
    }
)
public class ParagraphModeRenderCondition extends SlingSafeMethodsServlet {

    public static final String RESOURCE_TYPE = "core/wcm/extension/components/contentfragment/v1/renderconditions/paragraphmode";

    /**
     * Name of the resource property containing the path to a
     * {@code /apps/core/wcm/extension/components/contentfragment} component to use for this render condition.
     * The value may contain expressions.
     */
    public final static String PN_COMPONENT_PATH = "componentPath";

    /**
     * Name of the property to override the fragment path of the persisted component configuration. The value may
     * contain expressions.
     */
    public final static String PN_FRAGMENT_PATH = "fragmentPath";

    /**
     * Name of the property to override the element name path of the persisted component configuration. The value may
     * contain expressions.
     */
    public final static String PN_ELEMENT_NAME = "elementName";

    @Reference
    private ExpressionResolver expressionResolver;

    @Reference
    private ModelFactory modelFactory;

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response)
            throws ServletException, IOException {
        // return false by default
        request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(false));

        // get component path
        Config config = new Config(request.getResource());
        String componentPath = getParameter(config, request, PN_COMPONENT_PATH, String.class);
        if (componentPath == null) {
            return;
        }

        // get component resource
        Resource component = request.getResourceResolver().getResource(componentPath);
        if (component == null) {
            return;
        }

        // override fragment path if set
        Map<String, Object> properties = new HashMap<>(component.getValueMap());
        String fragmentPath = getParameter(config, request, PN_FRAGMENT_PATH, String.class);
        if (!StringUtils.isEmpty(fragmentPath)) {
            properties.put(ContentFragment.PN_PATH, fragmentPath);
        }

        // override element name if set, get it as an object to be able to differentiate between:
        //   - null: don't override the property (the parameter is not set)
        //   - empty string: override the property (empty string means no element is configured)
        String elementName = (String) getParameter(config, request, PN_ELEMENT_NAME, Object.class);
        if (elementName != null) {
            properties.put(ContentFragment.PN_ELEMENT_NAMES, elementName);
        }

        // wrap component resource and use a decorated value map
        ValueMapDecorator decorator = new ValueMapDecorator(properties);
        component = new ResourceWrapper(component) {

            @Override
            public ValueMap getValueMap() {
                return decorator;
            }

            @Override
            public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
                if (type == ValueMap.class) {
                    return (AdapterType) getValueMap();
                }
                return super.adaptTo(type);
            }

        };

        // get the sling model
        ContentFragment fragment = modelFactory.getModelFromWrappedRequest(request, component, ContentFragment.class);
        if (fragment == null) {
            return;
        }

        // evaluate render condition
        List<ContentFragment.Element> elements = fragment.getElements();
        boolean isParagraphMode = elements != null && elements.size() == 1 && elements.get(0).isMultiLine();
        request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(isParagraphMode));
    }


    /**
     * Reads a parameter from the specified datasource configuration, resolving expressions using the
     * {@link ExpressionResolver}. If the parameter is not found, {@code null} is returned.
     */
    @Nullable
    private <T> T getParameter(@Nonnull Config config, @Nonnull SlingHttpServletRequest request, @Nonnull String name,
                             Class<T> type) {
        // get value from configuration
        String value = config.get(name, String.class);
        if (value == null) {
            return null;
        }

        // evaluate value using the expression helper
        ExpressionHelper expressionHelper = new ExpressionHelper(expressionResolver, request);
        return expressionHelper.get(value, type);
    }

}
