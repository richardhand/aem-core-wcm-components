/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
package com.adobe.cq.wcm.core.components.models;

import java.util.Collection;

/**
 * Defines the {@code List} Sling Model used for the {@code /apps/core/wcm/components/list} component.
 */
public interface List {

    /**
     * @return collection of {@link ListItem}s
     */
    Collection<ListItem> getListItems();

    /**
     * @return true if the page should be linked otherwise false
     */
    boolean linkItem();

    /**
     * @return true if description should be shown otherwise flase
     */
    boolean showDescription();

    /**
     * @return true if modification date should be shown otherwise false
     */
    boolean showModificationDate();

    /**
     * @return format to use for the display of the last modification date.
     */
    String getDateFormatString();
}
