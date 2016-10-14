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

package com.adobe.cq.wcm.core.components.models.form;

import org.apache.sling.api.SlingHttpServletRequest;

import com.adobe.granite.ui.components.ds.DataSource;

/**
 * Interface for the Datasource Type Sling Model
 */
public abstract class DataSourceModel {

    /**
     * Sets the {@link DataSource} in the current {@link SlingHttpServletRequest}
     *
     * @param request    the current request
     * @param dataSource datasource to put in the current request
     */
    protected void initDataSource(SlingHttpServletRequest request, DataSource dataSource) {
        request.setAttribute(DataSource.class.getName(), dataSource);
    }
}
