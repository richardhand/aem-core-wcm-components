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
(function ($, Granite, ns, $document) {

    var FORM_OPTIONS_DIALOG_SELECTOR = ".core-wcm-form-options-v1",
        MULTI_SELECTION_FIELD_SELECTOR = "./multiSelection",
        OPTION_SELECTED_SELECTOR = "./selected",
        MULTIFIELD_ADD_BUTTON_SELECTOR = "coral-multifield-add",
        CHECKBOX_SELECTOR = "coral-checkbox",
        RADIO_SELECTOR = "coral-radio",
        GRANITE_UI_FOUNDATION_FIELD = "foundation-field";

    /**
     * Toggles checkboxes <-> radio buttons of the dialog depending on the value of the "./multiSelection" input field:
     * - if multiSelection is checked, checkboxes are displayed
     * - otherwise, radio buttons are displayed
     *
     * The transformation only applies to checkboxes / radio buttons named "./selected".
     */
    function toggleRadioCheckbox($dialog, initializeSelection) {
        // get the 'multiSelection' value
        var isMultiSelection = $dialog.find("input[name='" + MULTI_SELECTION_FIELD_SELECTOR + "']").is(":checked");

        // toggle the 'selected' input, which is either a checkbox or a radio button
        $dialog.find("input[name='" + OPTION_SELECTED_SELECTOR + "']").each(function() {
            var $input = $(this),
                $checkbox = $input.closest(CHECKBOX_SELECTOR),
                checkboxAPI = $checkbox.adaptTo(GRANITE_UI_FOUNDATION_FIELD),
                $radio = $input.closest(RADIO_SELECTOR),
                radioAPI = $radio.adaptTo(GRANITE_UI_FOUNDATION_FIELD);
            // uncheck all the checkboxes / radio buttons
            if (initializeSelection) {
                $input.attr("checked", false);
            }
            // if multiple selection of options is possible, display the checkboxes and hide/disable the radio buttons
            if (isMultiSelection) {
                $checkbox.show();
                $radio.hide();
                // enable the checkbox fields
                if (checkboxAPI) {
                    checkboxAPI.setDisabled(false);
                }
                // disable the radio button fields
                if (radioAPI) {
                    radioAPI.setDisabled(true);
                }
            // if multiple selection of options is possible, hide/disable the checkboxes and display the radio buttons
            } else {
                $checkbox.hide();
                $radio.show();
                // disable the checkbox fields
                if (checkboxAPI) {
                    checkboxAPI.setDisabled(true);
                }
                // enable the radio button fields
                if (radioAPI) {
                    radioAPI.setDisabled(false);
                }
            }
        });
    }

    // toggle radio <-> checkbox when the multi-selection field is checked/unchecked
    $document.on("click", "[name='" + MULTI_SELECTION_FIELD_SELECTOR + "']", function(){
        var $dialog = $(this).closest(FORM_OPTIONS_DIALOG_SELECTOR);
        toggleRadioCheckbox($dialog, true);
    });

    // toggle radio <-> checkbox when adding a new multifield item
    $document.on("click", "[" + MULTIFIELD_ADD_BUTTON_SELECTOR + "]", function(){
        var $dialog = $(this).closest(FORM_OPTIONS_DIALOG_SELECTOR);
        toggleRadioCheckbox($dialog, false);
    });

    // toggle radio <-> checkbox when the dialog is ready
    $document.on("dialog-ready", function() {
        var $dialog = $(this).find(FORM_OPTIONS_DIALOG_SELECTOR);
        Coral.commons.ready($dialog, function() {
            toggleRadioCheckbox($dialog, false);
        });
    });

    // toggle radio <-> checkbox when the dialog is opened in full screen
    $document.ready(function() {
        var $dialog = $(this).find(FORM_OPTIONS_DIALOG_SELECTOR);
        Coral.commons.ready($dialog, function() {
            toggleRadioCheckbox($dialog, false);
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));