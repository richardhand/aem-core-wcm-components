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
 * In the edit dialog, the widget to set a style is removed if the list of styles is empty
 */
(function ($, Granite, ns, $document) {

    var SIZES_SELECTOR = "coral-select.core-cmp-styles";

    $document.on("foundation-contentloaded", function (e) {
        Coral.commons.ready($(SIZES_SELECTOR), function(component) {

            var select = component.get(0);
            if (select === null || select === undefined) {
                return;
            }

            // Remove the style widget when there is no option
            var total = select.items.getAll().length;
            if (total == 0 || total == 1) {
                $(select).parent().remove();
            }
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));