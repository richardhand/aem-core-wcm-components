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

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.commons.ComponentUtils;
import com.adobe.cq.wcm.core.components.models.form.OptionItem;
import com.adobe.cq.wcm.core.components.models.form.Options;

@Model(adaptables = Resource.class,
       adapters = Options.class,
       resourceType = OptionsImpl.RESOURCE_TYPE)
public class OptionsImpl implements Options {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/options/v1/options";
    protected static final String PN_TYPE = "type";

    private static final String OPTION_ITEMS_PATH = "optionitems";
    private static final Logger log = LoggerFactory.getLogger(OptionsImpl.class);

    private static final String ID_PREFIX = "form-options";

    @ChildResource(optional = true)
    @Named(OPTION_ITEMS_PATH)
    private List<Resource> itemResources;

    @ValueMapValue(optional = true)
    private String name;

    @ValueMapValue(optional = true)
    private String helpMessage;

    @ValueMapValue(optional = true)
    private String caption;

    @ValueMapValue(name = OptionsImpl.PN_TYPE, optional = true)
    private String typeString;

    @Self
    private Resource resource;

    private List<OptionItem> optionItems;
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
        if(itemResources != null) {
            for(Resource itemResource: itemResources) {
                OptionItem optionItem = itemResource.adaptTo(OptionItem.class);
                if (optionItem != null && (optionItem.isDisabled() || StringUtils.isNotBlank(optionItem.getValue()))) {
                    optionItems.add(optionItem);
                }
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

    @Override
    public String getHelpMessage() {
        return helpMessage;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getType() {
        return Type.fromString(typeString).getValue();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    private void populateId() {
        try {
            id = ComponentUtils.getId(ID_PREFIX, resource.getPath());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public enum Type {
        CHECKBOX("checkbox"),
        RADIO("radio"),
        DROP_DOWN("drop-down"),
        MULTI_DROP_DOWN("multi-drop-down");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromString(String value) {
            for(Type type : Type.values()) {
                if(StringUtils.equals(value, type.value)) {
                    return type;
                }
            }
            return CHECKBOX;
        }

        public String getValue() {
            return value;
        }
    }
}
