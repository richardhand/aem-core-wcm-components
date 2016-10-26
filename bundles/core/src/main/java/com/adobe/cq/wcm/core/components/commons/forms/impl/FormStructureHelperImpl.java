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

package com.adobe.cq.wcm.core.components.commons.forms.impl;

import com.adobe.cq.wcm.core.components.commons.forms.FormsConstants;
import com.day.cq.wcm.foundation.forms.FormStructureHelper;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component(immediate = true)
@Service(FormStructureHelper.class)
public class FormStructureHelperImpl implements FormStructureHelper {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormStructureHelperImpl.class.getName());

    @Override
    public Resource getFormResource(Resource resource) {
        if (resource == null) {
            return null;
        }
        if ( resource.getPath().lastIndexOf("/") == 0 ) {
            return null;
        }
        if ( resource.isResourceType(FormsConstants.RT_CORE_FORM_CONTAINER) ) {
            return resource;
        }
        return getFormResource(resource.getParent());
    }

    @Override
    public Iterable<Resource> getFormElements(Resource resource) {
        if (resource.isResourceType(FormsConstants.RT_CORE_FORM_CONTAINER)) {
            return resource.getChildren();
        }
        return Collections.<Resource>emptyList();
    }

    @Override
    public boolean canManage(Resource resource) {
        return getFormResource(resource) != null;
    }

    @Override
    public Resource checkFormStructure(Resource formResource) {
        if (formResource != null) {
            ResourceResolver resolver = formResource.getResourceResolver();
            if (formResource.isResourceType(FormsConstants.RT_CORE_FORM_CONTAINER)) {
                // add default action type, form id and action path
                ModifiableValueMap formProperties = formResource.adaptTo(ModifiableValueMap.class);
                if (formProperties != null) {
                    try {
                        if (formProperties.get(com.day.cq.wcm.foundation.forms.FormsConstants.START_PROPERTY_ACTION_TYPE,
                                String.class) == null) {
                            formProperties.put(com.day.cq.wcm.foundation.forms.FormsConstants.START_PROPERTY_ACTION_TYPE,
                                    com.day.cq.wcm.foundation.forms.FormsConstants.DEFAULT_ACTION_TYPE);
                            String defaultContentPath = "/content/usergenerated" +
                                    formResource.getPath().replaceAll("^.content", "").replaceAll("jcr.content.*", "") +
                                    "cq-gen" + System.currentTimeMillis() + "/";
                            formProperties.put(com.day.cq.wcm.foundation.forms.FormsConstants.START_PROPERTY_ACTION_PATH,
                                    defaultContentPath);
                        }
                        if (formProperties.get(com.day.cq.wcm.foundation.forms.FormsConstants.START_PROPERTY_FORMID,
                                String.class) == null) {
                            formProperties.put(com.day.cq.wcm.foundation.forms.FormsConstants.START_PROPERTY_FORMID,
                                    formResource.getPath().replaceAll("[/:.]", "_"));
                        }
                        resolver.commit();
                    } catch (PersistenceException e) {
                        LOGGER.error("Unable to add default action type and form id " + formResource, e);
                    }
                } else {
                    LOGGER.error("Resource is not adaptable to ValueMap - unable to add default action type and " +
                            "form id for " + formResource);
                }
            }
        }
        return null;
    }
}
