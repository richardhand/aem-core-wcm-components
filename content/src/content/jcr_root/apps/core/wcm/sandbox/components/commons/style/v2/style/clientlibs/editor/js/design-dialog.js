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
 * - Set the style ID hidden parameter with a timestamp when a new style is added
 */
(function ($, Granite, ns, $document) {

    var MULTIFIELD_STYLES_SELECTOR = "coral-multifield.core-styletab-styles",
        STYLE_ID_SELECTOR = ".core-styletab-style-ID";

    // Set the style ID hidden parameter when a new style is added
    $document.on("change", MULTIFIELD_STYLES_SELECTOR, function(e) {
        // if the style ID value is not defined, we set it with a timestamp
        $(STYLE_ID_SELECTOR).each(function(index, element) {
            var value = $(element).val();
            if (!value) {
                $(element).val(Date.now());
            }
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));