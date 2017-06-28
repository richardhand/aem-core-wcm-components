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
 * Edit dialog of the Style Tab:
 * - The Style tab is removed when the list of styles defined in the policy is empty
 */
(function ($, Granite, ns, $document) {

    var STYLE_SELECTOR = "coral-select.core-styletab-componentstyle";

    // Remove the style tab if the list of styles defined in the policy is empty
    $document.on("foundation-contentloaded", function (e) {
        Coral.commons.ready($(STYLE_SELECTOR), function(component) {

            // To remove the style tab, we need to remove both the style tab header and the style tab panel.
            // The tab panel is easily retrieved as it contains the select dropdown.
            // The tab header is decoupled from the tab panel. To retrieve it:
            // - we iterate over the different tab panels
            // - when we find the style tab panel, we use the same index to retrieve the style tab header

            var select = component.get(0),
                tabs = $(select).closest("coral-tabview").get(0);

            if (select === null || select === undefined || tabs === null || tabs === undefined) {
                return;
            }

            var tabList = tabs.tabList,
                tabPanelStack = tabs.panelStack,
                styleTabPanel = $(select).closest("coral-panel").get(0),
                styleTabHeader;

            // retrieve the style tab header
            for (var i = 0; i < tabPanelStack.items.length; i++) {
                var panel = tabPanelStack.items.getAll()[i];
                if (panel == styleTabPanel) {
                    styleTabHeader = tabList.items.getAll()[i];
                    break;
                }
            }

            // remove the style tab
            var count = select.items.getAll().length;
            if (count == 0 || count == 1) {
                // remove the style tab panel
                tabPanelStack.items.remove(styleTabPanel);
                // remove the style tab header
                tabList.items.remove(styleTabHeader);
            }
        });
    });

}(jQuery, Granite, Granite.author, jQuery(document)));