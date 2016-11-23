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
(function ($, channel, Coral) {
    'use strict';
    var TEXTFIELD_TYPES = ".cmp-form-textfield-types";
    var TEXTFIELD_ROWS = ".cmp-form-textfield-rows";

    function checkAndDisplay(element, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element.show();
        } else {
            element.hide();
        }
    }

    function initialise(dialog) {
        dialog = $(dialog);
        if (dialog.find(TEXTFIELD_TYPES).length > 0) {
            Coral.commons.ready(dialog.find(TEXTFIELD_TYPES)[0], function (component) {
                checkAndDisplay(dialog.find(TEXTFIELD_ROWS),
                    "textarea",
                    component.value);
                component.on("change", function (e) {
                    checkAndDisplay(dialog.find(TEXTFIELD_ROWS),
                        "textarea",
                        this.value);
                });
            });
        }
    }

    channel.on("foundation-contentloaded", function (e) {
        initialise(e.target);
    });

})(jQuery, jQuery(document), Coral);
