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
 * The form text field
 */
public interface TextField extends FormField{

    /**
     * the type of constraint on the input field
     */
    enum CONSTRAINT_TYPE {
        TEXT,
        EMAIL,
        TEL,
        DATE,
        NUMBER,
        PASSWORD;

        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * the type of html element
     */
    enum ELEMENT_TYPE {
        INPUT,
        TEXTAREA;

        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * checks if the field should be rendered read only on the page
     * @return {@code true} if the field should be read-only <br>
     *     {@code false} otherwise
     */
    boolean isReadOnly();

    /**
     * @return the default value of the field
     */
    String getDefaultValue();

    /**
     * Gets the type of the input field such as date, email etc.
     * The types are as defined under HTML5.
     * @return the type of the field
     * @see com.adobe.cq.wcm.core.components.models.form.TextField.ELEMENT_TYPE
     */
    CONSTRAINT_TYPE getConstraintType();

    /**
     * @return the message to be displayed when the constraint specified by {@link #getConstraintType()}
     *      is not fulfilled
     */
    String getConstraintMessage();

    /**
     * TODO: check if it can be moved to the FormField with java with generic return type Object
     * Gets the value of the field if the field is single-valued.
     * If the field is multi-valued, ths will return the concatenation of all its values.
     * @return the value of the field
     */
    String getValue();

    /**
     * Gets the type of html element to use for rendering , textarea or input
     * @return the type of html element to use
     * @see com.adobe.cq.wcm.core.components.models.form.TextField.ELEMENT_TYPE
     */
    ELEMENT_TYPE getType();
}
