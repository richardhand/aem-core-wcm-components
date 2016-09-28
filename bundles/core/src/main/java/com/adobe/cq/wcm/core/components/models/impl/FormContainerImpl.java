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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = {Resource.class},
        adapters = FormContainer.class,
        resourceType = FormContainerImpl.RESOURCE_TYPE)
public class FormContainerImpl implements FormContainer {

    protected static final String RESOURCE_TYPE = "core/wcm/components/formcontainer";

    @Inject
    @Named("actionType")
    @Optional
    private String actionType;

    @Self
    private Resource self;

    private List<Resource> formFields = new ArrayList<Resource>();
    private List<String> formFieldResourcePaths = new ArrayList<String>();

    @PostConstruct
    protected void init() {
        for (Resource child : self.getChildren()) {
            formFields.add(child);
            formFieldResourcePaths.add(child.getPath());
        }
    }

    public boolean isTouch() {
        // TODO: 9/27/2016
        return false;
    }

    public boolean isEmpty() {
        return formFields.isEmpty();
    }

    public String getActionType() {
        return actionType;
    }

    public List<String> getFormFieldResourcePaths() {
        return formFieldResourcePaths;
    }
}
