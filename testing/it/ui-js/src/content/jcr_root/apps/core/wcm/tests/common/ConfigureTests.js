/*
 *  Copyright 2016 Adobe Systems Incorporated
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

;(function(h) {

    /**
     * Open the Configure window.
     */
    window.CQ.CoreComponentsIT.OpenConfigureWindow = function (h, $) {
        return new h.TestCase("Open the Configure window")
            //click on the component to see the Editable Toolbar
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //press the Configure button
            .click(".coral-Button.cq-editable-action[title='Configure']")
            .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form")
        ;
    }

    window.CQ.CoreComponentsIT.OpenFullSreen = function (h,$) {
        return new h.TestCase("Open the full screen")
            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //Open the fullscreen
            .click(".coral-Icon.coral-Icon--fullScreen")
            .asserts.visible(".cq-dialog-header", true)
            .click(".coral-Icon.coral-Icon--fullScreen")
        ;
    }

    window.CQ.CoreComponentsIT.CloseConfigureWindow = function (h,$) {
        return new h.TestCase("Close the Configure Window")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--close")
        ;
    }

    window.CQ.CoreComponentsIT.FillNavigationLevel = function (h,$,selector, inputValue, itemNo, activeItemNo) {
        return new h.TestCase("Fill the Navigation Level")
        //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //change the Navigation Level to 2
            .fillInput(selector, inputValue)
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            .config.changeContext(function() {
                return hobs.find("iframe#ContentFrame").get(0);
            })

            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", itemNo)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--activ",activeItemNo)})

            .config.resetContext()
        ;
    }

    window.CQ.CoreComponentsIT.FillInput = function (h,$, selector, value, assertFunction) {
        return new h.TestCase("Fill a input with a value")
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .fillInput(selector, value)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(assertFunction)
        ;
    }

    window.CQ.CoreComponentsIT.CheckCheckBox = function (h,$, selector,assertFunction) {
        return new h.TestCase("Check a checkbox")
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .click(selector)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(assertFunction)
        ;
    }

    window.CQ.CoreComponentsIT.CheckNavigationLevel = function (h,$, selector, inputValue, itemNo, activeItemNo){
        return new h.TestCase("Check the Navigtion Level")
            //open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment or decrement the navigation level
            .click(selector)
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']",inputValue)})

            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .config.changeContext(function() {
                return hobs.find("iframe#ContentFrame").get(0);
            })

            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"iframe#ContentFrame",".breadcrumb-item", itemNo)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"iframe#ContentFrame",".breadcrumb-item--active", activeItemNo)})

            .config.resetContext()
        ;
    }

})(hobs);


