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

import java.util.List;

/**
 * Component-Interface for the form container Sling Model
 */
public interface Container {

    /**
     * @return the action type of form.
     */
    String getActionType();

    /**
     * @return the content node paths of form fields (children of form container).
     */
    List<String> getFormFieldResourcePaths();

    /**
     * @return form submit method (method attribute of form).
     */
    String getMethod();

    /**
     * @return form submit action (used in action attribute of form).
     */
    String getAction();

    /**
     * @return form id (used in id attribute of form).
     */
    String getId();

    /**
     * @return form name (used in name attribute of form).
     */
    String getName();

    /**
     * @return form data enctype (used in enctype attribute of form).
     */
    String getEnctype();

    /**
     * @return resource type for the "new" section in form container where other input components will
     * be dropped.
     */
    String getResourceTypeForDropArea();

    /**
     * This method returns the redirect url property of this form. If the current sling request has a non-blank context path, the context
     * path is prepended to the redirect url if the redirect is an abolsute path starting with '/'. This method also appends ".html" to the
     * redirect path.
     * 
     * @return The form redirect url (used in the :redirect hidden input field of the form).
     */
    String getRedirect();
}
