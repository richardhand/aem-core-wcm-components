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

import org.apache.commons.lang.StringUtils;

/**
 * Interface for the Form Options Sling Model
 */
public interface Options {

    /**
     * Defines the Options type.
     *
     * Possible values: checkbox, radio, drop-down, multi-drop-down
     */
    enum Type {
        CHECKBOX("checkbox"),
        RADIO("radio"),
        DROP_DOWN("drop-down"),
        MULTI_DROP_DOWN("multi-drop-down");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromString(String value) {
            for (Type type : Type.values()) {
                if (StringUtils.equals(value, type.value)) {
                    return type;
                }
            }
            return CHECKBOX;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * @return The name of the options field.
     */
    String getName();

    /**
     * Returns the list of all the options.
     *
     * @return {@link List} of {@link OptionItem}s
     */
    List<OptionItem> getOptionItems();

    /**
     * @return unique identifier for the form element
     */
    String getId();

    /**
     * @return the message for the help block
     */
    String getHelpMessage();

    /**
     * @return the caption for the options element
     */
    String getCaption();

    /**
     * @return the type of the options element.
     *
     * Possible values: checkbox, radio, drop-down, multi-drop-down
     */
    Type getType();

}
