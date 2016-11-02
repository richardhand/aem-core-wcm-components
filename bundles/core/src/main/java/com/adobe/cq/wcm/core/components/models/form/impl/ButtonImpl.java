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
package com.adobe.cq.wcm.core.components.models.form.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.models.form.Button;

@Model(adaptables = Resource.class,
        adapters = Button.class,
        resourceType = ButtonImpl.RESOURCE_TYPE)
public class ButtonImpl implements Button {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/button";

    @ValueMapValue
    @Default(values = "")
    private String type;

    @ValueMapValue
    @Default(values = "")
    private String title;

    @ValueMapValue
    @Default(values = "")
    private String cssClass;

    @ValueMapValue
    @Default(booleanValues = false)
    private boolean disabled;

    @ValueMapValue
    @Default(values = "")
    private String name;

    @ValueMapValue
    @Default(values = "")
    private String value;

    @ValueMapValue
    @Default(booleanValues = false)
    private boolean autofocus;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isAutofocus() {
        return autofocus;
    }
}
