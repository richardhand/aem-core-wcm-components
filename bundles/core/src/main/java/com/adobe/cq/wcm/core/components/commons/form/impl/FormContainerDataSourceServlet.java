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
package com.adobe.cq.wcm.core.components.commons.form.impl;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.adobe.cq.wcm.core.components.models.form.DataSourceModel;
import com.adobe.cq.wcm.core.components.models.form.impl.v1.FormActionTypeDataSource;
import com.adobe.cq.wcm.core.components.models.form.impl.v1.FormActionTypeSettingsDataSource;
import com.adobe.cq.wcm.core.components.models.form.impl.v1.WorkflowModelDataSource;

@SlingServlet(
        resourceTypes = {
                FormActionTypeDataSource.RESOURCE_TYPE,
                FormActionTypeSettingsDataSource.RESOURCE_TYPE,
                WorkflowModelDataSource.RESOURCE_TYPE
        },
        methods = "GET",
        extensions = "html"
)
public class FormContainerDataSourceServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 9114656669504668093L;

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response)
            throws ServletException, IOException {
        request.adaptTo(DataSourceModel.class);
    }
}
