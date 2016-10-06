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

package com.adobe.cq.wcm.core.components.models.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import com.adobe.cq.wcm.core.components.commons.AuthoringUtils;
import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = Title.class,
       resourceType = TitleImpl.RESOURCE_TYPE)
public class TitleImpl implements Title {
    protected static final String RESOURCE_TYPE = "core/wcm/components/title";

    private static final String PROP_TITLE = "jcr:title";
    private static final String PROP_DEFAULT_TYPE = "defaultType";
    private static final String VIA_RESOURCE = "resource";

    @ScriptVariable
    private Page currentPage;

    @ScriptVariable
    private Style currentStyle;

    @Inject
    @Via(VIA_RESOURCE)
    @Named(PROP_TITLE)
    @Default(values = StringUtils.EMPTY)
    private String title;

    @Inject
    @Via(VIA_RESOURCE)
    @Optional
    private String type;

    @Override
    public String getText() {
        String text = title;
        if (StringUtils.isEmpty(text) && !AuthoringUtils.isPageOfAuthoredTemplate(currentPage)) {
            text = StringUtils.defaultIfEmpty(currentPage.getTitle(),
                    StringUtils.defaultIfEmpty(currentPage.getPageTitle(), currentPage.getName()));
        }
        return text;
    }

    @Override
    public String getElement() {
        Heading heading = Heading.getHeading(type);
        if (heading == null) {
            heading = Heading.getHeading(currentStyle.get(PROP_DEFAULT_TYPE, String.class));
        }
        if (heading != null) {
            return heading.getElement();
        }
        return null;
    }

    private enum Heading {

        H1("h1"),
        H2("h2"),
        H3("h3"),
        H4("h4"),
        H5("h5"),
        H6("h6");

        private String element;

        Heading(String element) {
            this.element = element;
        }

        private static Heading getHeading(String value) {
            for (Heading heading : values()) {
                if (StringUtils.equalsIgnoreCase(heading.element, value)) {
                    return heading;
                }
            }
            return null;
        }

        public String getElement() {
            return element;
        }
    }


}
