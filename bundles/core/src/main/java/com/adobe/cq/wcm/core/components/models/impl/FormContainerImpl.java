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

import com.adobe.cq.wcm.core.components.models.FormContainer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = FormContainer.class,
        resourceType = FormContainerImpl.RESOURCE_TYPE)
public class FormContainerImpl implements FormContainer {

    protected static final String RESOURCE_TYPE = "core/wcm/components/formcontainer";
    private static final String PN_ACTION_TYPE = "actionType";

    @Self
    private SlingHttpServletRequest slingRequest;

    @ScriptVariable
    private ValueMap properties;
    
    private String method;
    
    private String enctype;
    
    private String action;
    
    private String id;
    
    private String name;

    private List<Resource> formFields;

    private List<String> formFieldResourcePaths;

    @PostConstruct
    protected void initModel() {
        this.method = properties.get("method","POST");
        this.enctype = properties.get("enctype","multipart/form-data");
        PageManager pageManager = slingRequest.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(slingRequest.getResource());
        this.action = currentPage.getPath()+".html";
        this.id = currentPage.getName();
        this.name = currentPage.getName();
    }

    @Override
    public String getActionType() {
        return properties.get(PN_ACTION_TYPE, String.class);
    }

    @Override
    public List<String> getFormFieldResourcePaths() {
        if (formFieldResourcePaths == null) {
            formFields = new ArrayList<Resource>();
            formFieldResourcePaths = new ArrayList<String>();
            for (Resource child : slingRequest.getResource().getChildren()) {
                formFields.add(child);
                formFieldResourcePaths.add(child.getPath());
            }
        }
        return formFieldResourcePaths;
    }

    @Override
    public String getMethod(){
        return this.method;
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public String getId(){
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
}
