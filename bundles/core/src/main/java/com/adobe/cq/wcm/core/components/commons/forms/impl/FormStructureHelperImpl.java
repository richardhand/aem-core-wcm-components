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
import org.apache.sling.api.resource.Resource;

import java.util.Collections;

@Component(immediate = true)
@Service(FormStructureHelper.class)
public class FormStructureHelperImpl implements FormStructureHelper {

    @Override
    public Resource getFormResource(Resource formElement) {
        if (formElement == null) {
            return null;
        }
        if ( formElement.getPath().lastIndexOf("/") == 0 ) {
            return null;
        }
        if ( formElement.getResourceResolver().isResourceType(formElement, FormsConstants.RT_CORE_FORM_CONTAINER) ) {
            return formElement;
        }
        return getFormResource(formElement.getParent());
    }

    @Override
    public Iterable<Resource> getFormElements(Resource formResource) {
        if (formResource.getResourceResolver().isResourceType(formResource, FormsConstants.RT_CORE_FORM_CONTAINER)) {
            return formResource.getChildren();
        }
        return Collections.<Resource>emptyList();
    }

    @Override
    public boolean accepts(Resource formElement) {
        return getFormResource(formElement) != null;
    }

}
