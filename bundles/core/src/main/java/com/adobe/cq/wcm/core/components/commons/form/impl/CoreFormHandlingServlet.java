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
package com.adobe.cq.wcm.core.components.commons.form.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.OptingServlet;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import com.adobe.cq.wcm.core.components.commons.form.FormConstants;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import com.day.cq.wcm.foundation.forms.FormsHandlingServletHelper;
import com.day.cq.wcm.foundation.security.SaferSlingPostValidator;

/**
 * This form handling servlet accepts POSTs to a form container
 * but only if the selector "form" and the extension "html" is used.
 */
@SuppressWarnings("serial")
@Component(metatype = true, label = "Core Form Handling Servlet",
        description = "Accepts posting to a form container component and performs validations")
@Service({Servlet.class, Filter.class})
@Properties({
        @Property(name = "sling.servlet.resourceTypes", value = { FormConstants.RT_CORE_FORM_CONTAINER_V1 }, propertyPrivate = true),
        @Property(name = "sling.servlet.methods", value = "POST", propertyPrivate = true),
        @Property(name = "sling.servlet.selectors", value = CoreFormHandlingServlet.SELECTOR, propertyPrivate = true),
        @Property(name = "sling.filter.scope", value = "request", propertyPrivate = true),
        @Property(name = "service.ranking", intValue = 610, propertyPrivate = true),
        @Property(name = "service.description", value = "Core Form Handling Servlet")

})
public class CoreFormHandlingServlet
        extends SlingAllMethodsServlet
        implements OptingServlet, Filter {

    private static final String EXTENSION = "html";
    private static final Boolean PROP_ALLOW_EXPRESSION_DEFAULT = true;
    static final String SELECTOR = "form";


    private Set<String> formResourceTypes = new HashSet<String>(Arrays.asList(FormConstants.RT_ALL_CORE_FORM_CONTAINER));

    @Property(value = {},
            label = "Parameter Name Whitelist",
            description = "List of name expressions that will pass request validation. A validation error will occur " +
                    "if any posted parameters are not in the whitelist and not defined on the form.")
    private static final String DATA_NAME_WHITELIST = "name.whitelist";
    private String[] dataNameWhitelist;

    private FormsHandlingServletHelper formsHandlingServletHelper;

    @Property(boolValue = true,
            label = "Allow Expressions",
            description = "Evaluate expressions on form submissions.")
    public final static String ALLOW_EXPRESSIONS = "allow.expressions";
    private boolean allowExpressions;

    @Reference
    private SaferSlingPostValidator validator;

    @Reference
    private FormStructureHelperFactory formStructureHelperFactory;

    @Activate
    protected void activate(ComponentContext componentContext) {
        Dictionary<String, Object> properties = componentContext.getProperties();
        dataNameWhitelist = PropertiesUtil.toStringArray(properties.get(DATA_NAME_WHITELIST));
        allowExpressions = PropertiesUtil.toBoolean(properties.get(ALLOW_EXPRESSIONS), PROP_ALLOW_EXPRESSION_DEFAULT);
        formsHandlingServletHelper = new FormsHandlingServletHelper(dataNameWhitelist, validator, formResourceTypes,
                allowExpressions, formStructureHelperFactory);
    }

    /**
     * @see org.apache.sling.api.servlets.OptingServlet#accepts(org.apache.sling.api.SlingHttpServletRequest)
     */
    public boolean accepts(final SlingHttpServletRequest request) {
        return EXTENSION.equals(request.getRequestPathInfo().getExtension());
    }

    /**
     * @see org.apache.sling.api.servlets.SlingAllMethodsServlet#doPost(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
     */
    protected void doPost(SlingHttpServletRequest request,
                          final SlingHttpServletResponse response)
            throws ServletException, IOException {
        formsHandlingServletHelper.doPost(request, response);
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {
        formsHandlingServletHelper.handleFilter(request, response, chain, EXTENSION, SELECTOR);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(final FilterConfig config) throws ServletException {
        // nothing to do!
    }
}
