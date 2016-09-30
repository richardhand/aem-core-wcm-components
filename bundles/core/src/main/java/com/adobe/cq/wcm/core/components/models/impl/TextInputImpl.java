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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.wcm.core.components.models.TextInput;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class,
    adapters = TextInput.class,
    resourceType = TextInputImpl.RESOURCE_TYPE)
public class TextInputImpl implements TextInput {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/text";
    private static final String PN_ACTION_TYPE = "actionType";


    @Inject
    @Default(values = "TextInput")
    private String name;

    @Inject
    @Default(values = "")
    private String value;

    @Inject
    @Default(values = "Label for Text Input")
    private String label;

    @Inject
    @Default(values = "Text placeholder")
    private String placeholder;

    @PostConstruct
    protected void initModel() {
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }
}
