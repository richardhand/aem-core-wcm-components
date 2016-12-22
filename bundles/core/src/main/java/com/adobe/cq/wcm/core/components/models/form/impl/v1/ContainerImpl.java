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
package com.adobe.cq.wcm.core.components.models.form.impl.v1;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.wcm.core.components.commons.forms.FormsConstants;
import com.adobe.cq.wcm.core.components.models.Constants;
import com.adobe.cq.wcm.core.components.models.form.Container;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = Container.class,
       resourceType = ContainerImpl.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME,
          extensions = Constants.EXPORTER_EXTENSION)
public class ContainerImpl implements Container {

    protected static final String RESOURCE_TYPE = FormsConstants.RT_CORE_FORM_CONTAINER_V1;

    private static final String PN_RESOURCE_TYPE = "sling:" + SlingConstants.PROPERTY_RESOURCE_TYPE;
    private static final String PN_REDIRECT_TYPE = "redirect";

    private static final String PROP_METHOD_DEFAULT = "POST";
    private static final String PROP_ENCTYPE_DEFAULT = "multipart/form-data";

    @Self
    private SlingHttpServletRequest slingRequest;

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    @Default(values = PROP_METHOD_DEFAULT)
    private String method;

    @ValueMapValue
    @Default(values = PROP_ENCTYPE_DEFAULT)
    private String enctype;

    @ValueMapValue
    @Default(values = "")
    private String id;

    @ValueMapValue(optional = true)
    private String actionType;

    @ValueMapValue(name = PN_RESOURCE_TYPE)
    @Default(values = "")
    private String dropAreaResourceType;

    @ValueMapValue(name = PN_REDIRECT_TYPE, optional = true)
    private String redirectURL;

    private String name;
    private String action;
    private List<Resource> formFields;
    private List<String> formFieldResourcePaths;

    @ScriptVariable
    private Resource resource;

    @OSGiService
    private FormStructureHelperFactory formStructureHelperFactory;

    @PostConstruct
    protected void initModel() {
        slingRequest.setAttribute(FormsHelper.REQ_ATTR_FORM_STRUCTURE_HELPER,
                formStructureHelperFactory.getFormStructureHelper(resource));
        this.action = currentPage.getPath() + ".html";
        if (StringUtils.isEmpty(id)) {
            id = FormsHelper.getFormId(slingRequest);
        }
        this.name = id;
        this.dropAreaResourceType += "/new";
        if (redirectURL != null) {
            String contextPath = slingRequest.getContextPath();
            if (StringUtils.isNotBlank(contextPath) && redirectURL.startsWith("/")) {
                redirectURL = contextPath + redirectURL;
            }
            redirectURL = !redirectURL.endsWith(".html") ? (redirectURL + ".html") : redirectURL;
        }
    }

    @Override
    public String getActionType() {
        return actionType;
    }

    @Override
    public List<String> getFormFieldResourcePaths() {
        if (formFieldResourcePaths == null) {
            formFields = new ArrayList<Resource>();
            formFieldResourcePaths = new ArrayList<String>();
            for (Resource child : resource.getChildren()) {
                formFields.add(child);
                formFieldResourcePaths.add(child.getPath());
            }
        }
        return formFieldResourcePaths;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEnctype() {
        return this.enctype;
    }

    @Override
    public String getResourceTypeForDropArea() {
        return dropAreaResourceType;
    }

    @Override
    public String getRedirect() {
        return redirectURL;
    }
}
