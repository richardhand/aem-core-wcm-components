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
     * @return the message to be displayed when the constraint specified by {@link #getType()}
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
     * Gets the type of the input field such as text, textarea, date, email etc.
     * The types other than textarea are as defined under HTML5.
     * @return the type of the field
     */
    String getType();

    /**
     * @return the number of rows the text area should display
     */
    int getRows();

    /**
     * checks if the text field placeholder should be used for rendering the help message
     * @return {@code true} if the help message should be rendered as placeholder <br>
     *     {@code false} if the help message should be displayed independently outside the text field
     */
    boolean usePlaceholder();

    /**
     * @return the help message desribing the contents of the text field
     */
    String getHelpMessage();
}
