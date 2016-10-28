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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.wcm.core.components.models.form.DataSourceModel;
import com.adobe.cq.wcm.core.components.models.form.TextField;
import com.adobe.granite.ui.components.ds.SimpleDataSource;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = DataSourceModel.class,
        resourceType = ElementTypeDatasource.RESOURCE_TYPE)
public class ElementTypeDatasource extends DataSourceModel {

    protected final static String RESOURCE_TYPE = "core/wcm/components/form/text/datasource/elementtypedatasource";

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    private void initModel() {
        List<Resource> elementTypeResources = new ArrayList<>();
        for(TextField.ELEMENT_TYPE type: TextField.ELEMENT_TYPE.values()) {
            elementTypeResources.add(new ElementTypeResource(type.toString()));
        }
        SimpleDataSource actionTypeDataSource = new SimpleDataSource(elementTypeResources.iterator());
        initDataSource(request, actionTypeDataSource);
    }

    public class ElementTypeResource extends TextValueDataResourceSource {

        private final String name;

        public ElementTypeResource(String name) {
            super(resourceResolver, StringUtils.EMPTY, NonExistingResource.RESOURCE_TYPE_NON_EXISTING);
            this.name = name;
        }

        @Override
        protected String getText() {
            return name;
        }

        @Override
        protected String getValue() {
            return name;
        }
    }
}
