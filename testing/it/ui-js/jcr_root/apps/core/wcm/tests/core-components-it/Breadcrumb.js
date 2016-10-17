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

    /**
     * Data for the sample page.
     */
    var samplePage = {
        title: "CoreComponent TestPage",
        template: "/conf/core-components/settings/wcm/templates/core-components",
        primaryType: "cq:PageContent",
        resourceType: "wcm/foundation/components/page",
        name: "page"

    }

    /**
     * Function used for creating a number of pages.
     */
    window.CQ.CoreComponentsIT.CreatePages = function (h, $, nrOfPages) {
        var createPagesTestCase = new hobs.TestCase("Create Pages");

        for (var i=0; i< nrOfPages; i++) {
            var pageName = samplePage.name+i;
            var pageTitle = samplePage.title+i;
            var template = samplePage.template;

            pageUrl+="/"+pageName
            createPagesTestCase.execTestCase(window.CQ.CoreComponentsIT.CreatePage(h, $, pageUrl, pageName, pageTitle, template));
        }

        return createPagesTestCase;
    }

    /**
     * Function used for deleting one page with all her child pages.
     */
    window.CQ.CoreComponentsIT.DeletePages = function (h, $, nrOfPages) {

        return new hobs.TestCase("Delete Pages")
            .execTestCase(window.CQ.CoreComponentsIT.DeletePage(h, $, pageUrl+"/"+samplePage.name+"0"));
        ;
    }

    /**
     * Drag and Drop a Breadcrumb component.
     */
    window.CQ.CoreComponentsIT.DragDropBreadcrumb = function (h, $) {
        return new h.TestCase("Drag and drop the breadcrumb")
            .execTestCase(window.CQ.CoreComponentsIT.CreatePages(h,$,4))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropConponent(h,$,"Core WCM Breadcrumb Component",pageUrl))
        ;
    };

    /**
     * Check the navigation level for the breadcrumb.
     */
    window.CQ.CoreComponentsIT.CheckTheNavigationLevel = function (h, $) {
        return new h.TestCase("Check the navigation level")
            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 4)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 1)})
            //press the Configure button
            .click(".coral-Button.cq-editable-action[title='Configure']")
            .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form")
            //check the value of the navigation level
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","2")})

            //decrement the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Decrement']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","1")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","3")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 3)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","4")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 2)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","5")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 1)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","6")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the "Hide Current checkbox"
            .click(".coral-Checkbox-input[name='./hideCurrent']")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--active", 0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the "Hide Current checkbox"
            .click(".coral-Checkbox-input[name='./showHidden']")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--activ",0)})

            //Open the Configure window
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //change the Navigation Level to 2
            .fillInput(".coral-Textfield.coral-InputGroup-input[id^='coral-id']", "2")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item", 4)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame(h,"#ContentFrame",".breadcrumb-item--activ",0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //Open the full screen
            .click(".coral-Icon.coral-Icon--fullScreen")
            .asserts.visible(".cq-dialog-header", true)
            .wait(500)
            .click(".coral-Icon.coral-Icon--fullScreen")

            //close the configure window
            .wait(500)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--close")
        ;
    };

    new h.TestSuite("Core-Components Tests - Breadcrumb", {path:"/apps/core/wcm/tests/core-components-it/Breadcrumb.js",
        execBefore: hobs.steps.aem.commons.disableTutorials, execAfter:window.CQ.CoreComponentsIT.DeletePages(h, $,4), register: true})

        //.addTestCase(window.CQ.CoreComponentsIT.CreatePages(h,$,4))
        .addTestCase(window.CQ.CoreComponentsIT.DragDropBreadcrumb(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckTheNavigationLevel(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbar(h, $, 8))
        //.addTestCase(window.CQ.CoreComponentsIT.DeletePages(h, $,4))

    ;

}(hobs, jQuery));