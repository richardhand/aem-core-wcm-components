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

    var SIZES_SELECTOR = "coral-select.core-title-sizes";

    // Remove the size widget when there is none or one option
    $document.on("foundation-contentloaded", function (e) {
        Coral.commons.ready($(SIZES_SELECTOR), function(component) {
            var select = component.get(0);
            if (select === null || select === undefined) {
                return;
            }
            var total = select.items.getAll().length;
            if (total == 0 || total == 1) {
                select.remove();
            }
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));