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
 * A base interface to be extended by all the different types of form fields.
 * It contains commons attributes to be present in  all the form fields.
 */
public interface FormField {

    /**
     * @return the id of the field
     */
    String getId();

    /**
     * @return the name of the field
     */
    String getName();

    /**
     * @return the title (label) of the field
     */
    String getTitle();

    /**
     * Checks if the title should be hidden on the page or not.
     * @return {@code true} if the title should be hidden <br>
     *     {@code false} otherwise
     */
    boolean isTitleHidden();

    /**
     * @return The description of the field
     */
    String getDescription();

    /**
     * Checks if the user must provide input for this field.
     * @return {@code true} if the field must have a input <br>
     *     {@code false} otherwise
     */
    boolean getRequired();

    /**
     * @return The message to be displayed if the field is required
     *          but has not been filled by the user
     * @see #getRequired() 
     */
    String getRequiredMessage();

    /**
     * @return The expression to be evaluated to check if the field should be shown or be hidden on the page.
     */
    String getShowHideExpression();

    /**
     * @return value of placeholder attribute. 
     */
    String getPlaceholder();
}
