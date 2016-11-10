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
package com.adobe.cq.wcm.core.components.commons;

import javax.servlet.ServletRequest;

import org.apache.sling.api.resource.Resource;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.AuthoringUIMode;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

public final class AuthoringUtils {

    private static final String NN_INITIAL = "initial";
    private static final String NN_POLICIES = "policies";
    private static final String NN_STRUCTURE = "structure";

    /**
     * Checks if the components rendered in the passed request will be rendered for the Touch UI editor.
     *
     * @param request the request for which to check the editor type
     * @return {@code true} if the editor for the current request belongs to Touch UI, {@code false} otherwise
     */
    public static boolean isTouch(ServletRequest request) {
        AuthoringUIMode currentMode = AuthoringUIMode.fromRequest(request);
        return AuthoringUIMode.TOUCH == currentMode;
    }

    /**
     * Checks if the components rendered in the passed request will be rendered for the Classic UI editor.
     *
     * @param request the request for which to check the editor type
     * @return {@code true} if the editor for the current request belongs to Classic UI, {@code false} otherwise
     */
    public static boolean isClassic(ServletRequest request) {
        AuthoringUIMode currentMode = AuthoringUIMode.fromRequest(request);
        return AuthoringUIMode.CLASSIC == currentMode;
    }

    /**
     * Checks if the given page is part of the template editor
     *
     * @param page {@link Page} to check
     * @return true if the page is part of the template editor, otherwise false
     */
    public static boolean isPageOfAuthoredTemplate(Page page) {
        boolean isTemplatePage = false;
        Resource resource = page.adaptTo(Resource.class);
        if (resource != null) {
            if (resource.isResourceType(NameConstants.NT_PAGE) && resource.getParent() != null) {
                Resource parentResource = resource.getParent();
                if (parentResource != null && parentResource.isResourceType(NameConstants.NT_TEMPLATE) &&
                        parentResource.getChild(NN_INITIAL) != null && parentResource.getChild(NN_POLICIES) != null &&
                        parentResource.getChild(NN_STRUCTURE) != null) {
                    isTemplatePage = true;
                }
            }
        }
        return isTemplatePage;

    }
}
