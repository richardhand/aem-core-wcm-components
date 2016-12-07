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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.wcm.core.components.models.form.OptionItem;

public class OptionItemImpl implements OptionItem {

    private static final String PN_TEXT = "text";
    private static final String PN_VALUE = "value";
    private static final String PN_DISABLED = "disabled";
    private static final String PN_SELECTED = "selected";

    private String text;
    private String value;
    private boolean disabled;
    private boolean selected;

    public OptionItemImpl(Resource resource) {
        ValueMap valueMap = resource.adaptTo(ValueMap.class);
        this.text = valueMap.get(PN_TEXT, StringUtils.EMPTY);
        this.value = valueMap.get(PN_VALUE, StringUtils.EMPTY);
        this.disabled = valueMap.get(PN_DISABLED, false);
        this.selected = valueMap.get(PN_SELECTED, false);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String getValue() {
        return value;
    }
}
