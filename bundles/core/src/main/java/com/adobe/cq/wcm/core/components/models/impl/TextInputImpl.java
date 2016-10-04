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

import com.day.cq.wcm.foundation.forms.FormsHelper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.wcm.core.components.models.TextInput;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = TextInput.class,
        resourceType = TextInputImpl.RESOURCE_TYPE)
public class TextInputImpl implements TextInput {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/text";
    private static final String PROP_NAME_DEFAULT = "text";
    private static final String PROP_VALUE_DEFAULT = "";
    private static final String PROP_LABEL_DEFAULT = "Label for Text Input";
    private static final String PROP_PLACEHOLDER_DEFAULT = "Text placeholder";
    private static final String PN_NAME = "name";
    private static final String PN_VALUE = "value";
    private static final String PN_LABEL = "label";
    private static final String PN_PLACEHOLDER = "placeholder";

    @Self
    private SlingHttpServletRequest slingRequest;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Resource resource;

    private String [] prefillValues;

    @PostConstruct
    protected void initModel() {
        prefillValues = FormsHelper.getValues(slingRequest, resource);
        if (prefillValues == null) {
            prefillValues = new String[0];
        }
    }

    @Override
    public String getName() {
        return properties.get(PN_NAME, PROP_NAME_DEFAULT);
    }

    @Override
    public String getValue() {
        String value = properties.get(PN_VALUE, PROP_VALUE_DEFAULT);
        if (value.equals(PROP_VALUE_DEFAULT) && prefillValues.length > 0) {
            value = prefillValues[0];
        }
        return value;
    }

    @Override
    public String getLabel() {
        return properties.get(PN_LABEL, PROP_LABEL_DEFAULT);
    }

    @Override
    public String getPlaceholder() {
        return properties.get(PN_PLACEHOLDER, PROP_PLACEHOLDER_DEFAULT);
    }
}
