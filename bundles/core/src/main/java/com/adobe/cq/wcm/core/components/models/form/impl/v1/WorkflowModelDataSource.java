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
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.wcm.core.components.commons.forms.FormsConstants;
import com.adobe.cq.wcm.core.components.models.form.DataSourceModel;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.model.WorkflowModel;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = DataSourceModel.class,
       resourceType = WorkflowModelDataSource.RESOURCE_TYPE)
public class WorkflowModelDataSource extends DataSourceModel {

    protected final static String RESOURCE_TYPE = FormsConstants.RT_CORE_FORM_CONTAINER_V1 + "/datasource/workflowmodeldatasource";

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    private void initModel() throws WorkflowException {
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        ArrayList<Resource> resources = new ArrayList<>();
        if(workflowSession != null) {
            WorkflowModel[] models = workflowSession.getModels();
            for (WorkflowModel model : models) {
                resources.add(new WorkflowModelResource(model));
            }
        }
        SimpleDataSource dataSource = new SimpleDataSource(resources.iterator());
        initDataSource(request, dataSource);
    }

    public class WorkflowModelResource extends TextValueDataResourceSource {

        private final WorkflowModel model;

        public WorkflowModelResource(WorkflowModel model) {
            super(resourceResolver, StringUtils.EMPTY, RESOURCE_TYPE_NON_EXISTING);
            this.model = model;
        }

        @Override
        protected String getText() {
            return model.getTitle();
        }

        @Override
        protected String getValue() {
            return model.getId();
        }
    }
}
