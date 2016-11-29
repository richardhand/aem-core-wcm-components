/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
(function ($, channel) {
    'use strict';
    var INPUT_FIELD = ".cmp-form-field input",
        REQUIRED_MSG_ATTRIBUTE = "required-message",
        CONSTRAINT_MSG_ATTRIBUTE = "constraint-message";

    channel.ready(function () {
        $(INPUT_FIELD).each(function (index) {
            this.oninvalid = function (e) {
                e.target.setCustomValidity("");
                if (e.target.validity.typeMismatch) {
                    if (this.hasAttribute(CONSTRAINT_MSG_ATTRIBUTE)) {
                        e.target.setCustomValidity(this.getAttribute(CONSTRAINT_MSG_ATTRIBUTE));
                    }
                } else if (e.target.validity.valueMissing) {
                    if (this.hasAttribute(REQUIRED_MSG_ATTRIBUTE)) {
                        e.target.setCustomValidity(this.getAttribute(REQUIRED_MSG_ATTRIBUTE));
                    }
                }
            }
            this.oninput = function (e) {
                e.target.setCustomValidity("");
            }
        });
    });

})(jQuery, jQuery(document));
