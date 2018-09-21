/*
 *  Copyright 2018 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/* global jQuery */
(function($) {
    "use strict";

    var selectors = {
        clientLibs: '[data-cmp-policydialog-page-v2-hook="clientLibs"]',
        clientLib: {
            self: '[data-cmp-policydialog-page-v2-hook="clientLib"]',
            category: '[data-cmp-policydialog-page-v2-hook="clientLibCategory"]',
            jsHead: '[data-cmp-policydialog-page-v2-hook="clientLibJsHead"]'
        },
        jsHeadInput: '[data-cmp-policydialog-page-v2-hook="jsHeadInput"]'
    };

    $(document).on("dialog-loaded", function(event) {
        var dialog = event.dialog[0];
        var clientLibs = dialog.querySelectorAll(selectors.clientLib.self);
        var jsHeadInput = dialog.querySelector(selectors.jsHeadInput);
        var jsHeadCategories;

        // update js head hidden input for current selection
        function updateJsHeadInput() {
            jsHeadCategories = [];

            for (var i = 0; i < clientLibs.length; i++) {
                var category = clientLibs[i].querySelector(selectors.clientLib.category);
                var jsHeadCheckbox = clientLibs[i].querySelector(selectors.clientLib.jsHead);

                if (category && jsHeadCheckbox) {
                    var categoryField = $(category).adaptTo("foundation-field");

                    if (jsHeadCheckbox.checked) {
                        jsHeadCategories.push(categoryField.getValue());
                    }
                }
            }

            jsHeadInput.value = jsHeadCategories.join(" ");
        }

        if (jsHeadInput) {
            jsHeadCategories = jsHeadInput.value === "" ? [] : jsHeadInput.value.split(",");

            for (var i = 0; i < clientLibs.length; i++) {
                var clientLibsMultifield = dialog.querySelector(selectors.clientLibs);
                var category = clientLibs[i].querySelector(selectors.clientLib.category);
                var jsHeadCheckbox = clientLibs[i].querySelector(selectors.clientLib.jsHead);

                if (category) {
                    var categoryField = $(category).adaptTo("foundation-field");

                    // populate js head checkboxes from hidden input
                    if (jsHeadCategories.indexOf(categoryField.getValue()) !== -1) {
                        jsHeadCheckbox.checked = true;
                    }
                }

                $(clientLibsMultifield).on("foundation-field-change", function() {
                    updateJsHeadInput();
                });
            }
        }
    });

})(jQuery);
