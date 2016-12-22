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

/**
 * Interface for the Button Sling Model
 */
public interface Button {

    /**
     * Defines button type.
     */
    enum Type {
        /**
         * Button type used to submit forms.
         */
        SUBMIT("submit"),
        /**
         * Normal button.
         */
        BUTTON("button");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromString(String value) {
            for (Type type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * @return the type of the button.
     * <p>
     * Possible values: 'button', 'submit'
     * </p>
     */
    Type getType();

    /**
     * @return the caption of the button (text displayed on the button).
     */
    String getCaption();

    /**
     * @return value of the HTML <code>name</code> attribute.
     * <p>
     * Note: <code>{'name':'value'}</code> is sent as a request parameter when POST-ing the form
     * </p>
     */
    String getName();

    /**
     * @return value of the HTML <code>value</code> attribute.
     * <p>
     * Note: <code>{'name':'value'}</code> is sent as a request parameter when POST-ing the form
     * </p>
     */
    String getValue();

}
