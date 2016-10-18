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
package com.adobe.cq.wcm.core.components.models.impl.form;

import com.day.cq.wcm.foundation.forms.FormStructureHelperFactory;
import com.day.cq.wcm.foundation.forms.FormsHelper;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.wcm.core.components.models.form.TextField;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = TextField.class,
    resourceType = TextFieldImpl.RESOURCE_TYPE)
public class TextFieldImpl implements TextField {

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/text";
    private static final String PROP_NAME_DEFAULT = "text";
    private static final String PROP_VALUE_DEFAULT = "";
    private static final String PROP_LABEL_DEFAULT = "Label for Text Input";
    private static final String PROP_PLACEHOLDER_DEFAULT = "Text placeholder";
    private static final String PROP_TITLE_DEFAULT = "Text input field";
    private static final String PROP_DESCRIPTION_DEFAULT = "";
    private static final boolean PROP_HIDE_TITLE_DEFAULT = false;
    private static final boolean PROP_MULTIVALUE_DEFAULT= false;
    private static final boolean PROP_READONLY_DEFAULT = false;
    private static final String PROP_DEFAULT_VALUE_DEFAULT = "";
    private static final boolean PROP_REQUIRED_DEFAULT = false;
    private static final String PROP_REQUIRED_MESSAGE_DEFAULT = "";
    private static final String PROP_CONSTRAINT_DEFAULT = null;
    private static final String PROP_CONSTRAINT_MESSAGE_DEFAULT = "";
    private static final String PROP_ROWS_DEFAULT = "";
    private static final String PROP_COLS_DEFAULT = "";
    private static final String PROP_WIDTH_DEFAULT = "";
    private static final String PROP_CSS_CLASS_DEFAULT = "";
    private static final boolean PROP_AUTOFOCUS_DEFAULT = false;
    private static final String PROP_SHOW_HIDE_EXPRESSION_DEFAULT = null;
    private static final String PROP_TYPE_DEFAULT = "text";

    private static final String PN_NAME = "name";
    private static final String PN_VALUE = "value";
    private static final String PN_LABEL = "label";
    private static final String PN_PLACEHOLDER = "placeholder";
    private static final String PN_TITLE = "jcr:title";
    private static final String PN_HIDE_TITLE = "hideTitle";
    private static final String PN_DESCRIPTION = "jcr:description";
    private static final String PN_MULTIVALUE= "multivalue";
    private static final String PN_READONLY = "readOnly";
    private static final String PN_DEFAULT_VALUE = "defaultValue";
    private static final String PN_REQUIRED = "required";
    private static final String PN_REQUIRED_MESSAGE = "requiredMessage";
    private static final String PN_CONSTRAINT = "constraintType";
    private static final String PN_CONSTRAINT_MESSAGE = "constraintMessage";
    private static final String PN_ROWS = "rows";
    private static final String PN_COLS = "cols";
    private static final String PN_WIDTH = "width";
    private static final String PN_CSS_CLASS = "css";
    private static final String PN_AUTOFOCUS = "autofocus";
    private static final String PN_SHOW_HIDE_EXPRESSION = "showHideExpression";
    private static final String PN_TYPE = "type";

    @Self
    private SlingHttpServletRequest slingRequest;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Resource resource;

    @Inject
    private FormStructureHelperFactory formStructureHelperFactory;

    private String [] prefillValues;

    @PostConstruct
    protected void initModel() {
        slingRequest.setAttribute(FormsHelper.REQ_ATTR_FORM_STRUCTURE_HELPER,
            formStructureHelperFactory.getFormStructureHelper(resource));
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
        if(this.isMultiValued()) {
            String values[]= this.getMultiValues();
            StringBuilder sb = new StringBuilder();
            for(String value:values) {
                sb.append(value);
            }
            return sb.toString();
        }
        String value = properties.get(PN_VALUE, PROP_VALUE_DEFAULT);
        if (value.equals(PROP_VALUE_DEFAULT) && prefillValues.length > 0) {
            value = prefillValues[0];
        }
        return value;
    }

    
    @Override
    public String[] getMultiValues() {
        if(properties.containsKey(PN_VALUE)) {
            return properties.get(PN_VALUE,String[].class);
        }
        return prefillValues;
    }

    @Override
    public String getType() {
        return properties.get(PN_TYPE,PROP_TYPE_DEFAULT);
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
    public boolean isMultiValued() {
        return properties.get(PN_MULTIVALUE,PROP_MULTIVALUE_DEFAULT);
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
    public boolean isAutofocus() {
        return properties.get(PN_AUTOFOCUS,PROP_AUTOFOCUS_DEFAULT);
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
         return properties.get(PN_PLACEHOLDER,PROP_PLACEHOLDER_DEFAULT);
    }

    @Override
    public String getConstraintType() {
        return properties.get(PN_CONSTRAINT,PROP_CONSTRAINT_DEFAULT);
    }

    @Override
    public String getConstraintMessage() {
        return properties.get(PN_CONSTRAINT_MESSAGE,PROP_CONSTRAINT_MESSAGE_DEFAULT);
    }

    @Override
    public String getWidth() {
        return properties.get(PN_WIDTH,PROP_WIDTH_DEFAULT);
    }

    @Override
    public String getCssClass() {
        return properties.get(PN_CSS_CLASS,PROP_CSS_CLASS_DEFAULT);
    }

    @Override
    public int getRows() {
        String rows = properties.get(PN_ROWS,PROP_ROWS_DEFAULT);
        if(rows.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(rows);
    }

    @Override
    public int getCols() {
        String cols = properties.get(PN_COLS,PROP_COLS_DEFAULT);
        if(cols.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(cols);
    }
}
