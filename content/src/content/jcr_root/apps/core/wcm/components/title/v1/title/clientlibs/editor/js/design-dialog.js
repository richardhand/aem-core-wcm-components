/*******************************************************************************
 * Copyright 2017 Adobe Systems Incorporated
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

    var DEFAULT_SIZE_SELECTOR       = "coral-select.core-title-default-size",
        ALLOWED_SIZES_SELECTOR      = ".core-title-allowed-sizes coral-checkbox";

    function updateDefaultSizeSelect() {

        var select = $(DEFAULT_SIZE_SELECTOR).get(0),
            $checkboxes = $(ALLOWED_SIZES_SELECTOR),
            checkedTotal = 0,
            selectValue = "";

        if (select === null || select === undefined) {
            return;
        }

        // clear the select items to work around a Coral.Select issue (CUI-5584)
        select.items.clear();

        // add the "default" item to the select
        var defaultItem = new Coral.Select.Item();
        defaultItem.content.textContent = "(default)";
        defaultItem.value = "";
        select.items.add(defaultItem);

        $checkboxes.each(function (i, checkbox) {
            if (checkbox.checked) {
                var sizeText = checkbox.label.innerHTML;
                var sizeValue = checkbox.value;
                var newItem = new Coral.Select.Item();

                newItem.content.textContent = sizeText;
                newItem.value = sizeValue;
                select.items.add(newItem);
                checkedTotal++;
                selectValue = sizeValue;
            }
        });

        // hide/show the select and set the select submit value
        // Note: we use Coral.commons.nextFrame to make sure that the select widget has been updated
        Coral.commons.nextFrame(function() {
            if (checkedTotal == 0) {
                // if no allowed size is checked: set the select submit value to "default"
                select.value = "";
                $(select).parent().hide();
            } else if (checkedTotal == 1) {
                // if only one allowed size is checked: set the select submit value to the checked item
                select.value = selectValue;
                $(select).parent().hide();
            } else {
                // if two or more allowed size are checked: set the select submit value to "default"
                select.value = "";
                $(select).parent().show();
            }
        });
    }

    // update the default size select when an allowed size is checked/unchecked
    $document.on("change", ALLOWED_SIZES_SELECTOR, function(e) {
        updateDefaultSizeSelect();
    });

    // update the default size select when the design title dialog is opened
    $document.on("foundation-contentloaded", function (e) {
        Coral.commons.ready($(ALLOWED_SIZES_SELECTOR), function(component) {
            updateDefaultSizeSelect();
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));