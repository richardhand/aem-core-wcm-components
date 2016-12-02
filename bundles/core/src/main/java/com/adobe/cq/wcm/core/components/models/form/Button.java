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
     * @return the type of the button.
     * <p>
     * Possible values: 'button', 'submit'
     * </p>
     */
    public String getType();

    /**
     * @return the caption of the button (text displayed on the button).
     */
    public String getCaption();

    /**
     * @return the name of the button.
     * <p>
     * Note: {'name':'value'} is sent as a request parameter when POST-ing the form
     * </p>
     */
    public String getName();

    /**
     * @return the value of the button.
     * <p>
     * Note: {'name':'value'} is sent as a request parameter when POST-ing the form
     * </p>
     */
    public String getValue();

}
