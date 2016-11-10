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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.wcm.core.components.models.form.DataSourceModel;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.day.cq.wcm.foundation.forms.FormsManager;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = DataSourceModel.class,
       resourceType = FormActionTypeDataSource.RESOURCE_TYPE)
public class FormActionTypeDataSource extends DataSourceModel {

    protected final static String RESOURCE_TYPE = "core/wcm/components/form/formcontainer/datasource/actiontypedatasource";
    public static final String NN_DIALOG = "cq:dialog";

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    private void initModel() {
        SimpleDataSource actionTypeDataSource = new SimpleDataSource(getActionTypeResources().iterator());
        initDataSource(request, actionTypeDataSource);
    }

    private List<Resource> getActionTypeResources() {
        List<Resource> actionTypeResources = new ArrayList<>();
        FormsManager formsManager = resourceResolver.adaptTo(FormsManager.class);
        Iterator<FormsManager.ComponentDescription> actions = formsManager.getActions();
        while (actions.hasNext()) {
            FormsManager.ComponentDescription description = actions.next();
            Resource dialogResource = resourceResolver.getResource(description.getResourceType() + "/" + NN_DIALOG);
            if (dialogResource != null) {
                actionTypeResources.add(new ActionTypeResource(description));
            }
        }
        return actionTypeResources;
    }

    public class ActionTypeResource extends TextValueDataResourceSource {

        private final FormsManager.ComponentDescription description;

        public ActionTypeResource(FormsManager.ComponentDescription description) {
            super(resourceResolver, StringUtils.EMPTY, NonExistingResource.RESOURCE_TYPE_NON_EXISTING);
            this.description = description;
        }

        @Override
        protected String getText() {
            return description.getTitle();
        }

        @Override
        protected String getValue() {
            return description.getResourceType();
        }
    }
}
