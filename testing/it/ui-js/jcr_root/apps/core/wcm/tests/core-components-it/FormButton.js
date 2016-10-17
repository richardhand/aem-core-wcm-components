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

;(function(h, $){

    var pageUrl = window.CQ.CoreComponentsIT.pageRoot;
    pageUrl = pageUrl+"/page0"

    /**
     * Drag and Drop a Form Button component.
     */
    window.CQ.CoreComponentsIT.DragDropFormButton = function (h, $) {
        return new h.TestCase("Drag and drop the form button")
            .execTestCase(window.CQ.CoreComponentsIT.CreatePage(h,$,pageUrl, "page0","CoreComponent TestPage",
                "/conf/core-components/settings/wcm/templates/core-components"))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropConponent(h,$,"Core WCM Form Button Component (v1)",pageUrl))
        ;
    }

    /**
     * Check the Configure button for the component.
     */
    window.CQ.CoreComponentsIT.CheckConfigureButton = function (h, $){
        return new h.TestCase("Check the Configure button")

            //Change the Type of the button to submit
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //Check the type of the button
            .asserts.isTrue(function() {return hobs.find(".btn[type='button']","#ContentFrame")})

            .click(".coral-Button:contains('Button')")
            .click(".coral3-SelectList-item:contains('Submit')")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find(".btn[type='submit']","#ContentFrame")})

            //Change the Type of the button to reset
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .click(".coral-Button:contains('Submit')")
            .click(".coral3-SelectList-item:contains('Reset')")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find(".btn[type='reset']","#ContentFrame")})

            //Fill the Title
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .fillInput("[name='./title']", "Test Button")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkContentFromIFrame(h,"#ContentFrame",".btn", "Test Button")})

            //Fill the cssClass
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .fillInput("[name='./cssClass']", "coral-Icon")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find(".btn.coral-Icon","#ContentFrame")})

            //Check the Disable button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .click("[name='./disabled']")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find("[disabled]","#ContentFrame")})

            //Fill the Name
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .fillInput("[name='./name']", "button1")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find("[name='button1']","#ContentFrame")})

            //Fill the Value
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .fillInput("[name='./value']", "value1")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find("[value='value1']","#ContentFrame")})

            //Check the Autofocus button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .click("[name='./autofocus']")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return hobs.find("[autofocus]","#ContentFrame")})

            //Open the full screen
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            .click(".coral-Icon.coral-Icon--fullScreen")
            .asserts.visible(".cq-dialog-header", true)
            .wait(500)
            .click(".coral-Icon.coral-Icon--fullScreen")

            //Close the configure window
            .wait(500)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--close")
        ;
    }

    new h.TestSuite("Core-Components Tests - Form Button", {path:"/apps/core/wcm/tests/core-components-it/FormButton.js",
        execBefore: hobs.steps.aem.commons.disableTutorials, execAfter:window.CQ.CoreComponentsIT.DeletePage(h, $,pageUrl), register: true})
        .addTestCase(window.CQ.CoreComponentsIT.DragDropFormButton(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckConfigureButton(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbar(h,$, 8))
    ;
})(hobs, jQuery);
