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

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.models.form.Options;

@Model(adaptables = Resource.class,
       adapters = Options.class,
       resourceType = OptionsImpl.RESOURCE_TYPE)
public class OptionsImpl implements Options {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/options";

    @Inject
    @Optional
    @Named("optionitems")
    private List<Resource> optionItems;

    @ValueMapValue
    @Default(values = "")
    private String name;

    @ValueMapValue
    @Default(values = "")
    private String cssClass;

    @ValueMapValue
    @Default(booleanValues = false)
    private boolean multiSelection;

    @ValueMapValue
    @Default(booleanValues = false)
    private boolean collapsed;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }

    @Override
    public List<Resource> getOptionItems() {
        return optionItems;
    }

    @Override
    public boolean multiSelection() {
        return multiSelection;
    }

    @Override
    public boolean collapsed() {
        return collapsed;
    }

}
