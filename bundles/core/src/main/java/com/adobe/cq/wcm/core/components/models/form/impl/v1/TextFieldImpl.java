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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.wcm.core.components.models.form.TextField;
import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import com.day.cq.wcm.foundation.forms.FormsHelper;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = TextField.class,
    resourceType = TextFieldImpl.RESOURCE_TYPE)
public class TextFieldImpl implements TextField {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/text/v1/text";

    private static final String PROP_NAME_DEFAULT = "text";
    private static final String PROP_VALUE_DEFAULT = "";
    private static final String PROP_TITLE_DEFAULT = "Text input field";
    private static final String PROP_DESCRIPTION_DEFAULT = "";
    private static final boolean PROP_HIDE_TITLE_DEFAULT = false;
    private static final boolean PROP_READONLY_DEFAULT = false;
    private static final String PROP_DEFAULT_VALUE_DEFAULT = "";
    private static final boolean PROP_REQUIRED_DEFAULT = false;
    private static final String PROP_REQUIRED_MESSAGE_DEFAULT = "";
    private static final String PROP_CONSTRAINT_MESSAGE_DEFAULT = "";
    private static final String PROP_SHOW_HIDE_EXPRESSION_DEFAULT = null;
    private static final String PROP_TYPE_DEFAULT = "text";
    private static final String PROP_HELP_MESSAGE_DEFAULT = "";
    private static final boolean PROP_USE_PLACEHOLDER_DEFAULT = false;
    private static final Integer PROP_ROWS_DEFAULT = 2;

    private static final String PN_NAME = "name";
    private static final String PN_VALUE = "value";
    private static final String PN_TITLE = "jcr:title";
    private static final String PN_HIDE_TITLE = "hideTitle";
    private static final String PN_DESCRIPTION = "jcr:description";
    private static final String PN_READONLY = "readOnly";
    private static final String PN_DEFAULT_VALUE = "defaultValue";
    private static final String PN_REQUIRED = "required";
    private static final String PN_REQUIRED_MESSAGE = "requiredMessage";
    private static final String PN_CONSTRAINT_MESSAGE = "constraintMessage";
    private static final String PN_SHOW_HIDE_EXPRESSION = "showHideExpression";
    private static final String PN_TYPE = "type";
    private static final String PN_HELP_MESSAGE = "helpMessage";
    private static final String PN_USE_PLACEHOLDER = "usePlaceholder";
    private static final String PN_ROWS = "rows";

    @Self
    private SlingHttpServletRequest slingRequest;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Resource resource;

    @Inject
    private FormStructureHelperFactory formStructureHelperFactory;

    private String [] prefillValues;

    private String id = null;

    @PostConstruct
    protected void initModel() {
        slingRequest.setAttribute(FormsHelper.REQ_ATTR_FORM_STRUCTURE_HELPER,
            formStructureHelperFactory.getFormStructureHelper(resource));
        prefillValues = FormsHelper.getValues(slingRequest, resource);
        if (prefillValues == null) {
            prefillValues = new String[]{this.getDefaultValue()};
        }
    }

    @Override
    public String getId(){
        if(id == null) {
            id = this.getName() + System.currentTimeMillis();
        }
        return id;
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
    public String getType() {
        return properties.get(PN_TYPE, PROP_TYPE_DEFAULT);
    }

    @Override
    public String getTitle() {
        return properties.get(PN_TITLE, PROP_TITLE_DEFAULT);
    }

    @Override
    public boolean isTitleHidden() {
        return properties.get(PN_HIDE_TITLE,PROP_HIDE_TITLE_DEFAULT);
    }

    @Override
    public String getDescription() {
        return properties.get(PN_DESCRIPTION,PROP_DESCRIPTION_DEFAULT);
    }

    @Override
    public boolean isReadOnly() {
        return properties.get(PN_READONLY,PROP_READONLY_DEFAULT);
    }

    @Override
    public String getDefaultValue() {
        return properties.get(PN_DEFAULT_VALUE,PROP_DEFAULT_VALUE_DEFAULT);
    }

    @Override
    public boolean getRequired() {
        return properties.get(PN_REQUIRED,PROP_REQUIRED_DEFAULT);
    }

    @Override
    public String getRequiredMessage() {
        return properties.get(PN_REQUIRED_MESSAGE,PROP_REQUIRED_MESSAGE_DEFAULT);
    }

    @Override
    public String getShowHideExpression() {
        return properties.get(PN_SHOW_HIDE_EXPRESSION, PROP_SHOW_HIDE_EXPRESSION_DEFAULT);
    }

    @Override
    public String getPlaceholder() {
        return this.getHelpMessage();
    }

    @Override
    public String getConstraintMessage() {
        return properties.get(PN_CONSTRAINT_MESSAGE,PROP_CONSTRAINT_MESSAGE_DEFAULT);
    }

    @Override
    public int getRows() {
        return properties.get(PN_ROWS, PROP_ROWS_DEFAULT);
    }

    @Override
    public boolean usePlaceholder() {
        return properties.get(PN_USE_PLACEHOLDER, PROP_USE_PLACEHOLDER_DEFAULT);
    }

    @Override
    public String getHelpMessage() {
        return properties.get(PN_HELP_MESSAGE, PROP_HELP_MESSAGE_DEFAULT).trim();
    }
}
