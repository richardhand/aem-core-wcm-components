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
(function (window, $, channel, Granite, Coral) {
    "use strict";

    // class of the edit dialog content
    var CLASS_EDIT_DIALOG = "cmp-contentfragment__edit-dialog";

    // field selectors
    var SELECTOR_FRAGMENT_PATH = "[name='./fragmentPath']";
    var SELECTOR_ELEMENT_NAMES_CONTAINER = "[data-element-names-container='true']";
    var SELECTOR_ELEMENT_NAMES = "[data-granite-coral-multifield-name='./elementNames']";
    var SELECTOR_SINGLE_TEXT_ELEMENT = "[data-single-text-selector='true']";
    var SELECTOR_ELEMENT_NAMES_ADD = SELECTOR_ELEMENT_NAMES + " > [is=coral-button]";
    var SELECTOR_VARIATION_NAME = "[name='./variationName']";
    var SELECTOR_DISPLAY_MODE = "[name='./displayMode']";
    var SELECTOR_DISPLAY_MODE_CHECKED = "[name='./displayMode']:checked";
    var SELECTOR_PARAGRAPH_CONTROLS = ".cmp-contentfragment__edit-dialog-paragraph-controls";
    var SELECTOR_PARAGRAPH_SCOPE = "[name='./paragraphScope']";
    var SELECTOR_PARAGRAPH_RANGE = "[name='./paragraphRange']";
    var SELECTOR_PARAGRAPH_HEADINGS = "[name='./paragraphHeadings']";

    // mode in which only one multiline text element could be selected for display
    var SINGLE_TEXT_DISPLAY_MODE = "singleText";

    // ui helper
    var ui = $(window).adaptTo("foundation-ui");

    // dialog texts
    var confirmationDialogTitle = Granite.I18n.get("Warning");
    var confirmationDialogMessage = Granite.I18n.get("Please confirm replacing the current content fragment and its configuration");
    var confirmationDialogCancel = Granite.I18n.get("Cancel");
    var confirmationDialogConfirm = Granite.I18n.get("Confirm");
    var errorDialogTitle = Granite.I18n.get("Error");
    var errorDialogMessage = Granite.I18n.get("Failed to load the elements of the selected content fragment");

    // the fragment path field (foundation autocomplete)
    var fragmentPath;

    // the paragraph controls (field set)
    var paragraphControls;
    // the tab containing paragraph control
    var paragraphControlsTab;

    // the path of the component being edited
    var componentPath;
    // the resource path of the paragraph controls field set
    var paragraphControlsPath;

    // keeps track of the current fragment path
    var currentFragmentPath;

    // initial state of the paragraph controls (if set)
    var initialParagraphScope;
    var initialParagraphRange;
    var initialParagraphHeadings;

    var editDialog;

    var elementsController;

    var ElementsController = function () {
        // container which contains either single elements select field or a multifield of element selectors
        this.elementNamesContainer = editDialog.querySelector(SELECTOR_ELEMENT_NAMES_CONTAINER);
        // element container resource path
        this.elementsContainerPath = this.elementNamesContainer.dataset.fieldPath;
        this.fetchedState = null;
        this._updateFields();
    };

    ElementsController.prototype._updateFields = function() {
        this.elementNames = editDialog.querySelector(SELECTOR_ELEMENT_NAMES);
        this.addElement = this.elementNames ? this.elementNames.querySelector(SELECTOR_ELEMENT_NAMES_ADD) : undefined;
        this.singleTextSelector = editDialog.querySelector(SELECTOR_SINGLE_TEXT_ELEMENT);
        // the variation name field (select)
        this.variationName = editDialog.querySelector(SELECTOR_VARIATION_NAME);
        this.variationNamePath = this.variationName.dataset.fieldPath;
    };

    ElementsController.prototype.disableFields = function() {
        if (this.addElement) {
            this.addElement.setAttribute("disabled", "");
        }
        if (this.singleTextSelector) {
            this.singleTextSelector.setAttribute("disabled", "");
        }
        if (this.variationName) {
            this.variationName.setAttribute("disabled", "");
        }
    };
    
    ElementsController.prototype.resetFields = function() {
        if (this.elementNames) {
            this.elementNames.items.clear();
        }
        if (this.singleTextSelector) {
            this.singleTextSelector.value = "";
        }
        if (this.variationName) {
            this.variationName.value = "";
        }
    }

    ElementsController.prototype.prepareRequest = function(displayMode, type) {
        if (typeof displayMode === "undefined") {
            displayMode = editDialog.querySelector(SELECTOR_DISPLAY_MODE_CHECKED).value;
        }
        var data = {
            fragmentPath: fragmentPath.value,
            displayMode: displayMode
        };
        var url;
        if (type === "variation") {
            url = Granite.HTTP.externalize(this.variationNamePath) + ".html";
        } else if (type === "elements") {
            url = Granite.HTTP.externalize(this.elementsContainerPath) + ".html";
        }
        var request = $.get({
            url: url,
            data: data
        });
        return request;
    };

    ElementsController.prototype.testGetHTML = function(displayMode, callback) {
        var elementNamesRequest = this.prepareRequest(displayMode, "elements");
        var variationNameRequest = this.prepareRequest(displayMode, "variation");
        var self = this;
        // wait for requests to load
        $.when(elementNamesRequest, variationNameRequest).then(function (result1, result2) {
            // get the fields from the resulting markup and create a test state
            self.fetchedState = {
                elementNames: $(result1[0]).find(SELECTOR_ELEMENT_NAMES)[0],
                singleTextSelector: $(result1[0]).find(SELECTOR_SINGLE_TEXT_ELEMENT)[0],
                variationName: $(result2[0]).find(SELECTOR_VARIATION_NAME)[0],
                elementNamesContainerHTML: result1[0],
                variationNameHTML: result2[0]
            };
            callback();

        }, function () {
            // display error dialog if one of the requests failed
            ui.prompt(errorDialogTitle, errorDialogMessage, "error");
        });
    };

    /**
     * Checks if the current states of element names, single text selector and variation name select match with the
     * fetched state.
     *
     * @returns {boolean} true if the states match or if there was no current state or fetched state, false otherwise
     */
    ElementsController.prototype.testStateForUpdate = function() {
        if (!this.fetchedState) {
            return true;
        }
        // check if some element names are currently configured
        if (this.elementNames && this.elementNames.items.length > 0) {
            // if we're unsetting the current fragment we need to reset the config
            if (!this.fetchedState.elementNames) {
                return false;
            }
            // compare the items of the current and new element names fields
            var currentItems = this.elementNames.template.content.querySelectorAll("coral-select-item");
            var newItems = this.fetchedState.elementNames.template.content.querySelectorAll("coral-select-item");
            if (!itemsAreEqual(currentItems, newItems)) {
                return false;
            }
        }

        if (this.singleTextSelector && this.singleTextSelector.items.length > 0) {
            // if we're unsetting the current fragment we need to reset the config
            if (!this.fetchedState.singleTextSelector) {
                return false;
            }
            // compare the items of the current and new element names fields
            var currentItems = this.singleTextSelector.querySelectorAll("coral-select-item");
            var newItems = this.fetchedState.singleTextSelector.querySelectorAll("coral-select-item");
            if (!itemsAreEqual(currentItems, newItems)) {
                return false;
            }
        }

        // check if a varation is currently configured
        if (this.variationName.value && this.variationName.value !== "master") {
            // if we're unsetting the current fragment we need to reset the config
            if (!this.fetchedState.variationName) {
                return false;
            }
            // compare the items of the current and new variation name fields
            if (!itemsAreEqual(this.variationName.items.getAll(), this.fetchedState.variationName.items.getAll())) {
                return false;
            }
        }

        return true;
    }

    ElementsController.prototype.saveFetchedState = function() {
        if (!this.fetchedState) {
            return;
        }
        if (this.fetchedState.elementNames) {
            this._updateElementsDOM(this.fetchedState.elementNames);
        } else {
            this._updateElementsDOM(this.fetchedState.singleTextSelector);
        }
        this._updateVariationDOM(this.fetchedState.variationName);
    };

    ElementsController.prototype.fetchAndUpdateElementsHTML = function(displayMode) {
        var elementNamesRequest = this.prepareRequest(displayMode, "elements");
        var self = this;
        // wait for requests to load
        $.when(elementNamesRequest).then(function (result) {
            self._updateElementsHTML(result);
        }, function () {
            // display error dialog if one of the requests failed
            ui.prompt(errorDialogTitle, errorDialogMessage, "error");
        });
    };

    /**
     * Updates inner html of element container.
     * @param {String} html - outerHTML value for elementNamesContainer
     */
    ElementsController.prototype._updateElementsHTML = function(html) {
        this.elementNamesContainer.innerHTML = $(html)[0].innerHTML;
        this._updateFields();
    };

    /**
     * Updates dom of element container.
     * @param {String} html - outerHTML value for elementNamesContainer
     */
    ElementsController.prototype._updateElementsDOM = function(dom) {
        if (dom.tagName === "CORAL-MULTIFIELD") {
            // replace the element names multifield's template
            this.elementNames.template = dom.template;
        } else {
            dom.value = this.singleTextSelector.value;
            this.singleTextSelector.parentNode.replaceChild(dom, this.singleTextSelector);
            this.singleTextSelector = dom;
            this.singleTextSelector.removeAttribute("disabled");
        }
        this._updateFields();
    };

    /**
     * Updates dom of variation name select dropdown.
     * @param {HTMLElement} dom - dom for variation name dropdown
     */
    ElementsController.prototype._updateVariationDOM = function(dom) {
        // replace the variation name select, keeping its value
        dom.value = this.variationName.value;
        this.variationName.parentNode.replaceChild(dom, this.variationName);
        this.variationName = dom;
        this.variationName.removeAttribute("disabled");
        this._updateFields();
    };

    function initialize(dialog) {
        // get path of component being edited
        var content = dialog.querySelector("." + CLASS_EDIT_DIALOG);
        componentPath = content.dataset.componentPath;
        editDialog = dialog;

        // get the fields
        fragmentPath = dialog.querySelector(SELECTOR_FRAGMENT_PATH);
        paragraphControls = dialog.querySelector(SELECTOR_PARAGRAPH_CONTROLS);
        paragraphControlsPath = paragraphControls.dataset.fieldPath;
        paragraphControlsTab = dialog.querySelector("coral-tabview").tabList.items.getAll()[1];

        // initialize state variables
        currentFragmentPath = fragmentPath.value;
        elementsController = new ElementsController();
        var scope = paragraphControls.querySelector(SELECTOR_PARAGRAPH_SCOPE + "[checked]");
        if (scope) {
            initialParagraphScope = scope.value;
            initialParagraphRange = paragraphControls.querySelector(SELECTOR_PARAGRAPH_RANGE).value;
            initialParagraphHeadings = paragraphControls.querySelector(SELECTOR_PARAGRAPH_HEADINGS).value;
        }

        // disable add button and variation name if no content fragment is currently set
        if (!currentFragmentPath) {
            elementsController.disableFields();
        }
        // enable / disable the paragraph controls
        setParagraphControlsState();
        // hide/show paragraph control tab
        updateParagraphControlTabState();

        // register change listener
        $(fragmentPath).on("foundation-field-change", onFragmentPathChange);
        $(document).on("change", SELECTOR_PARAGRAPH_SCOPE, setParagraphControlsState);
        var $radioGroup = $(dialog).find(SELECTOR_DISPLAY_MODE).closest(".coral-RadioGroup");
        $radioGroup.on("change", function(e) {
            elementsController.fetchAndUpdateElementsHTML(e.target.value);
            updateParagraphControlTabState();
        });
    }

    /**
     * Executes after the fragment path has changed. Shows a confirmation dialog to the user if the current
     * configuration is to be reset and updates the fields to reflect the newly selected content fragment.
     */
    function onFragmentPathChange() {
        // if the fragment was reset (i.e. the fragment path was deleted)
        if (!fragmentPath.value) {
            // confirm change (if necessary)
            confirmFragmentChange(null, null, function () {
                // disable add button and variation name
                if (addElement) {
                    addElement.setAttribute("disabled", "");
                }
                variationName.setAttribute("disabled", "");

                // update (hide) paragraph controls
                updateParagraphControlTabState();
            });
            // don't do anything else
            return;
        }

        elementsController.testGetHTML(editDialog.querySelector(SELECTOR_DISPLAY_MODE_CHECKED).value, function() {
            // check if we can keep the current configuration, in which case no confirmation dialog is necessary
            var canKeepConfig = elementsController.testStateForUpdate();
            if (canKeepConfig) {
                currentFragmentPath = fragmentPath.value;
                // its okay to save fetched state
                elementsController.saveFetchedState();
                return;
            }
            // else show a confirmation dialog
            confirmFragmentChange(elementsController.saveFetchedState, elementsController);
        });

    }

    /**
     * Presents the user with a confirmation dialog if the current configuration needs to be reset as a result
     * of the content fragment change. Executes the specified callback after the user confirms, or if the current
     * configuration can be kept (in which case no dialog is shown).
     *
     * @param newElementNames the element names field reflecting the newly selected fragment (null if fragment was unset
     *                        or component is in single text element mode)
     * @param newSingleTextSelect the coral select field representing multiline text elements in newly selected fragment
     *                        (null if fragment was unset or component is in multiple elements mode)
     * @param newVariationName the variation name field reflecting the newly selected fragment (null if fragment was unset)
     * @param callback a callback to execute after the change is confirmed
     * @param scope - the object defining "this" for callback
     */
    function confirmFragmentChange(callback, scope) {

        ui.prompt(confirmationDialogTitle, confirmationDialogMessage, "warning", [{
            text: confirmationDialogCancel,
            handler: function () {
                // reset the fragment path to its previous value
                requestAnimationFrame(function() {
                    fragmentPath.value = currentFragmentPath;
                });
            }
        }, {
            text: confirmationDialogConfirm,
            primary: true,
            handler: function () {
                // reset the current configuration
                elementsController.resetFields();
                // update the current fragment path
                currentFragmentPath = fragmentPath.value;
                // execute callback
                callback.call(scope);
            }
        }]);
    }

    /**
     * Compares two arrays containing select items, returning true if the arrays have the same size and all contained
     * items have the same value and label.
     */
    function itemsAreEqual(a1, a2) {
        // verify that the arrays have the same length
        if (a1.length !== a2.length) {
            return false;
        }
        for (var i = 0; i < a1.length; i++) {
            var item1 = a1[i];
            var item2 = a2[i];
            if (item1.attributes.value.value !== item2.attributes.value.value
                || item1.textContent !== item2.textContent) {
                // the values or labels of the current items didn't match
                return false;
            }
        }
        return true;
    }

    /**
     * Enables or disables the paragraph range and headings field depending on the state of the paragraph scope field.
     */
    function setParagraphControlsState () {
        // get the selected scope radio button (might not be present at all)
        var scope = paragraphControls.querySelector(SELECTOR_PARAGRAPH_SCOPE + "[checked]");
        if (scope) {
            // enable or disable range and headings fields according to the scope value
            var range = paragraphControls.querySelector(SELECTOR_PARAGRAPH_RANGE);
            var headings = paragraphControls.querySelector(SELECTOR_PARAGRAPH_HEADINGS);
            if (scope.value === "range") {
                range.removeAttribute("disabled");
                headings.removeAttribute("disabled");
            } else {
                range.setAttribute("disabled", "");
                headings.setAttribute("disabled", "");
            }
        }
    }

    // Toggles the display of paragraph control tab depening on display mode
    function updateParagraphControlTabState() {
        var displayMode = editDialog.querySelector(SELECTOR_DISPLAY_MODE_CHECKED).value;
        if (displayMode === SINGLE_TEXT_DISPLAY_MODE) {
            paragraphControlsTab.hidden = false;
        } else {
            paragraphControlsTab.hidden = true;
        }
    }

    /**
     * Initializes the dialog after it has loaded.
     */
    channel.on("foundation-contentloaded", function (e) {
        if (e.target.getElementsByClassName(CLASS_EDIT_DIALOG).length > 0) {
            Coral.commons.ready(e.target, function (dialog) {
                initialize(dialog);
            });
        }
    });

})(window, jQuery, jQuery(document), Granite, Coral);
