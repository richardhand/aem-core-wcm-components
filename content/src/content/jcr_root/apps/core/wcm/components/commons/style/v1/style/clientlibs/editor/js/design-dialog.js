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

/**
 * Design dialog of the Style Tab:
 * - The options of the select field to define the default value are dynamically added/removed/updated
 *   when the multifield options are edited
 */
(function ($, Granite, ns, $document) {

    var DEFAULT_STYLE_SELECTOR = "coral-select.core-styletab-style-default",
        MULTIFIELD_STYLES_SELECTOR = "coral-multifield.core-styletab-styles",
        STYLE_NAME_SELECTOR = "coral-multifield .core-styletab-style-name";

    // Hide the dropdown to select the default style if the allowed styles defined in the multifield are empty,
    // show it otherwise
    function hideShowSelect(select, multifield) {

        if (select === null || select === undefined || multifield === null || multifield === undefined) {
            return;
        }

        if (multifield.items.length == 0) {
            $(select).parent().hide();
        } else {
            $(select).parent().show();
        }
    }

    // Update the select field that defines the default value
    function updateDefaultStyleSelect() {

        var select = $(DEFAULT_STYLE_SELECTOR).get(0),
            multifield = $(MULTIFIELD_STYLES_SELECTOR).get(0);

        if (select === null || select === undefined || multifield === null || multifield === undefined) {
            return;
        }

        // clear the select items to work around a Coral.Select issue (CUI-5584)
        select.items.clear();

        // add the default option
        var noneItem = new Coral.Select.Item();
        noneItem.content.textContent = "None";
        noneItem.value = "";
        select.items.add(noneItem);

        // for each multifield style name, add an option to the default styles dropdown
        $(STYLE_NAME_SELECTOR).each(function (i, style) {
            var newItem = new Coral.Select.Item();
            newItem.content.textContent = $(style).val();
            newItem.value = $(style).val();
            select.items.add(newItem);
        });

        // Note: we use Coral.commons.nextFrame to make sure that the select widget has been updated
        Coral.commons.nextFrame(function() {
            hideShowSelect(select, multifield);
        });
    }

    // Update the default size select when a style is added, removed or updated
    $document.on("change", MULTIFIELD_STYLES_SELECTOR, function(e) {
        updateDefaultStyleSelect();
    });

    // When loading the dialog, hide/show the dropdown to select the default style
    $document.on("foundation-contentloaded", function (e) {
        Coral.commons.ready($(DEFAULT_STYLE_SELECTOR), function(select) {
            var multifield = $(MULTIFIELD_STYLES_SELECTOR).get(0);
            hideShowSelect(select, multifield);
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));