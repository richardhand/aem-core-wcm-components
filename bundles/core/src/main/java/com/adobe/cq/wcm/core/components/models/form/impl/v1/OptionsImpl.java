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
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.models.form.OptionItem;
import com.adobe.cq.wcm.core.components.models.form.Options;

@Model(adaptables = Resource.class,
       adapters = Options.class,
       resourceType = OptionsImpl.RESOURCE_TYPE)
public class OptionsImpl implements Options {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/options/v1/options";
    private static final String OPTION_ITEMS_PATH = "optionitems";

    private List<OptionItem> optionItems;

    @ValueMapValue(optional = true)
    private String name;

    @ValueMapValue(optional = true)
    private String helpMessage;

    @ValueMapValue(optional = true)
    private String caption;

    @Self
    private Resource resource;

    private String id;

    @Override
    public List<OptionItem> getOptionItems() {
        if(optionItems == null) {
            populateOptionItems();
        }
        return optionItems;
    }

    private void populateOptionItems() {
        this.optionItems = new ArrayList<>();
        Resource optionItemsResource = resource.getChild(OPTION_ITEMS_PATH);
        if(optionItemsResource != null) {
            for(Resource itemResource: optionItemsResource.getChildren()) {
                optionItems.add(new OptionItemImpl(itemResource));
            }
        }
    }

    @Override
    public String getId() {
        if(id == null) {
            populateId();
        }
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public String getCaption() {
        return caption;
    }

    private void populateId() {
        id = String.valueOf(resource.getPath().hashCode());
    }
}
