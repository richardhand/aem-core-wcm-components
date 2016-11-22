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

;(function(h,$) {

    var commons = window.CQ.CoreComponentsIT.commons;
    /**
     * Test case that gets executed before running the test suite
     */
    commons.executeBeforeTestSuite = new h.TestCase("Setup Before Testsuite")
        .execTestCase(h.steps.aem.commons.disableTutorials);

    /**
     * Test case that gets executed before each test
     * NOTE: each test case should create / use its on Page
     */
    commons.executeBeforeTest = function(h,$,component) {
       return new h.TestCase("Setup Before Test")
        // create the test page using our own test template
            .execFct(commons.createTestPage)
            // add the form button component
            .execFct(function (opts, done) {
                commons.createComponent(component, done)
            })
            // open the page in the editor
            .navigateTo("/editor.html" + commons.testPagePath + ".html")
        // wait for the event that signals that the page editor is 'really' ready to be used.
        // NOTE: This function does not work properly for Firefox , i get timeouts, mark may have a solution for this
        //.execFct(waitForPageEditor);
    }
    /**
     * Test case that gets executed after each test
     * NOTE: Clean up everything that was created
     */
    commons.executeAfterTest = new h.TestCase("Clean up after Test")
        .execFct(commons.deleteTestPage);

    commons.tcOpenConfigureDialog = new h.TestCase("Open Configure Dialog")
        //click on the component to see the Editable Toolbar
        .click(".cq-Overlay.cq-draggable.cq-droptarget")
        // check if its there
        .asserts.visible("#EditableToolbar")
        // click on the 'configure' button
        .click(".coral-Button.cq-editable-action[title='Configure']")
        // verify the dialog has become visible
        .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form")
    ;

    commons.tcOpenEditDialog = new h.TestCase("Open Edit Dialog")
        //click on the component to see the Editable Toolbar
        .click(".cq-Overlay.cq-draggable.cq-droptarget")
        // check if its there
        .asserts.visible("#EditableToolbar")
        // click on the 'Edit' button
        .click(".coral-Button.cq-editable-action[title='Edit']")
    ;

    /**
     * Sub Test Case that closes the dialog
     */
    commons.tcCloseConfigureDialog = new h.TestCase ("Close Configure Dialog")
    // close the dialog
        .click(".cq-dialog-actions .coral-Icon.coral-Icon--check");


})(hobs,jQuery);
