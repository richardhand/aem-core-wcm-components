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
package com.adobe.cq.wcm.core.components.models.datasource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.SyntheticResource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.wcm.core.components.models.datasource.FormActionTypeDatasource;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.day.cq.wcm.foundation.forms.FormsManager;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = FormActionTypeDatasource.class,
       resourceType = FormActionTypeDatasourceImpl.RESOURCE_TYPE)
public class FormActionTypeDatasourceImpl implements FormActionTypeDatasource {

    protected final static String RESOURCE_TYPE = "core/wcm/components/form/formcontainer/datasource/actiontypedatasource";

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Override
    public void initDataSource(SlingHttpServletRequest request, DataSource dataSource) {
        request.setAttribute(DataSource.class.getName(), dataSource);
    }

    @PostConstruct
    private void initModel() {
        ArrayList<Resource> actionTypeResources = new ArrayList<>();
        FormsManager formsManager = resourceResolver.adaptTo(FormsManager.class);
        Iterator<FormsManager.ComponentDescription> actions = formsManager.getActions();
        while (actions.hasNext()) {
            FormsManager.ComponentDescription description = actions.next();
            actionTypeResources.add(new ActionTypeResource(resourceResolver, description));
        }
        SimpleDataSource actionTypeDataSource = new SimpleDataSource(actionTypeResources.iterator());
        initDataSource(request, actionTypeDataSource);
    }

    public static class ActionTypeResource extends SyntheticResource {

        protected static final String PN_VALUE = "value";
        protected static final String PN_TEXT = "text";

        private FormsManager.ComponentDescription description;
        private ValueMap valueMap;

        public ActionTypeResource(ResourceResolver resourceResolver, FormsManager.ComponentDescription description) {
            super(resourceResolver, StringUtils.EMPTY, NonExistingResource.RESOURCE_TYPE_NON_EXISTING);
            this.description = description;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            if (type == ValueMap.class) {
                if (valueMap == null) {
                    initValueMap();
                }
                return (AdapterType) valueMap;
            } else {
                return super.adaptTo(type);
            }
        }

        private void initValueMap() {
            valueMap = new ValueMapDecorator(new HashMap<String, Object>());
            valueMap.put(PN_VALUE, description.getResourceType());
            valueMap.put(PN_TEXT, description.getTitle());
        }
    }
}
