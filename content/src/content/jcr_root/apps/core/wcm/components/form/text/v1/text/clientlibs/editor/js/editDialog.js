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

    var EDIT_DIALOG = ".cmp-form-textfield-editDialog";
    var TEXTFIELD_TYPES = ".cmp-form-textfield-types";
    var TEXTFIELD_ROWS = ".cmp-form-textfield-rows";
    var TEXTFIELD_REQUIRED = ".cmp-form-textfield-required";
    var TEXTFIELD_CONSTRAINTMESSAGE = ".cmp-form-textfield-constraintmessage";
    var TEXTFIELD_REQUIREDMESSAGE = ".cmp-form-textfield-requiredmessage";
    var TEXTFIELD_READONLY = ".cmp-form-textfield-readonly";
    var TEXTFIELD_READONLYSELECTED_ALERT = ".cmp-form-textfield-readonlyselected-alert";
    var TEXTFIELD_REQUIREDSELECTED_ALERT = ".cmp-form-textfield-requiredselected-alert";

    function checkAndDisplay(element, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element.show();
        } else {
            element.hide();
        }
    }

    function handleTextarea(dialog) {
        var component = dialog.find(TEXTFIELD_TYPES)[0];
        var textfieldRows = dialog.find(TEXTFIELD_ROWS);
        checkAndDisplay(textfieldRows,
            "textarea",
            component.value);
        component.on("change", function () {
            checkAndDisplay(textfieldRows,
                "textarea",
                component.value);
        });
    }

    function handleConstraintMessage(dialog) {
        var component = dialog.find(TEXTFIELD_TYPES)[0];
        var constraintMessage = dialog.find(TEXTFIELD_CONSTRAINTMESSAGE);
        var displayConstraintMessage = component.value !== "text" && component.value !== "textarea";
        checkAndDisplay(constraintMessage,
            true,
            displayConstraintMessage);
        component.on("change", function () {
            displayConstraintMessage = this.value !== "text" && this.value !== "textarea";
            checkAndDisplay(constraintMessage,
                true,
                displayConstraintMessage);
        });
    }

    function handleRequiredMessage(dialog) {
        var component = dialog.find(TEXTFIELD_REQUIRED)[0];
        var requiredMessage = dialog.find(TEXTFIELD_REQUIREDMESSAGE);
        checkAndDisplay(requiredMessage,
            true,
            component.checked);
        component.on("change", function () {
            checkAndDisplay(requiredMessage,
                true,
                component.checked);
        });
    }

    function handleExclusion(component1, component2, alert) {
        component1.on("change", function () {
            if (this.checked && component2.checked) {
                alert.show();
                component2.set("checked", false, true);
            }
        });
    }

    function initialise(dialog) {
        dialog = $(dialog);
        handleTextarea(dialog);
        handleConstraintMessage(dialog);
        handleRequiredMessage(dialog);

        var readonly = dialog.find(TEXTFIELD_READONLY)[0];
        var required = dialog.find(TEXTFIELD_REQUIRED)[0];
        handleExclusion(readonly,
            required,
            dialog.find(TEXTFIELD_REQUIREDSELECTED_ALERT)[0]);
        handleExclusion(required,
            readonly,
            dialog.find(TEXTFIELD_READONLYSELECTED_ALERT)[0]);
    }

    channel.on("foundation-contentloaded", function (e) {
        if ($(e.target).find(EDIT_DIALOG).length > 0) {
            Coral.commons.ready(e.target, function (component) {
                initialise(component);
            });
        }
    });

})(jQuery, jQuery(document), Coral);
