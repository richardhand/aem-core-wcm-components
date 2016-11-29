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

/**
 * Tests for core form button
 */
;(function(h, $){

    //short cut
    var c = window.CQ.CoreComponentsIT.commons;

    /**
     * Before Test Case
     */
    var tcExecuteBeforeTest = new TestCase("Setup Before Test")
        // common set up
        .execTestCase(c.tcExecuteBeforeTest)
        // create the test page, store page path in 'testPagePath'
        .execFct(function (opts,done) {
            c.createPage(c.template, c.rootPage ,'page_' + Date.now(),"testPagePath",done)
        })
        // add the component, store component path in 'cmpPath'
        .execFct(function (opts, done){
            c.addComponent(c.rtFormButton, h.param("testPagePath")(opts)+c.relParentCompPath,"cmpPath",done)
        })
        // open the new page in the editor
        .navigateTo("/editor.html%testPagePath%.html");

    /**
     * After Test Case
     */
    var tcExecuteAfterTest = new TestCase("Clean up after Test")
        // common clean up
        .execTestCase(c.tcExecuteAfterTest)
        // delete the test page we created
        .execFct(function (opts, done) {
            c.deletePage(h.param("testPagePath")(opts), done);
        });

    /**
     * Test: Create a submit button
     */
    var createSubmitButton = new h.TestCase("Create a Submit Button",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //change the type of the button
        .click(".coral-Button:contains('Button')")
        .click(".coral3-SelectList-item:contains('Submit')")
        //set the button text
        .fillInput("[name='./title']","SUBMIT")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find(".btn[type='Submit']","#ContentFrame").size() == 1
        });

    /**
     * Test: Create a reset button
     */
    var createResetButton = new h.TestCase("Create a Reset Button",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //change the type of the button
        .click(".coral-Button:contains('Button')")
        .click(".coral3-SelectList-item:contains('Reset')")
        //set the button text
        .fillInput("[name='./title']","RESET")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find(".btn[type='Reset']","#ContentFrame").size() == 1
        });

    /**
     * Test: Set button text
     */
    var setButtonText = new h.TestCase("Set Button Text",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the button text
        .fillInput("[name='./title']","Test Button")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find(".btn","#ContentFrame").text().trim() == "Test Button"
        });

    /**
     * Test: Set css class
     */
    var setButtonCss = new h.TestCase("Set CSS for Button",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the button text
        .fillInput("[name='./cssClass']","dummyCSS")
        // set title of button
        .fillInput("[name='./title']","Test CSS")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find(".btn.dummyCSS","#ContentFrame").size() == 1
        });

    /**
     * Test: Set button as disabled
     */
    var setButtonDisabled = new h.TestCase("Set Button as Disabled",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the checkbox
        .click("[name='./disabled']")
        // set button title
        .fillInput("[name='./title']","DISABLE ME")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find("[disabled]","#ContentFrame").size() == 1
        });

    /**
     * Test: Set button name
     */
    var setButtonName = new h.TestCase("Set Button Name",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the button text
        .fillInput("[name='./name']","button1")
        // set button title
        .fillInput("[name='./title']","BUTTON WITH NAME")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)
        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find("[name='button1']","#ContentFrame").size() == 1
        });

    /**
     * Test: Set button value
     */
    var setButtonValue = new h.TestCase("Set Button Value",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the button text
        .fillInput("[name='./value']","thisisthevalue")
        // set button title
        .fillInput("[name='./title']","BUTTON WITH VALUE")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find("[value='thisisthevalue']","#ContentFrame").size() == 1
        });

    /**
     * Test: Set autofocus on button
     */
    var setButtonAutoFocus = new h.TestCase("Set Autofocus on Button",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest})

        // Open the edit dialog
        .execTestCase(c.tcOpenConfigureDialog("cmpPath"))
        //set the checkbox
        .click("[name='./autofocus']")
        // set title
        .fillInput("[name='./title']","AUTOFOCUS")
        // close the edit dialog
        .execTestCase(c.tcSaveConfigureDialog)

        //Check if the button tag is rendered with the correct type
        .asserts.isTrue(function() {
            return h.find("[autofocus]","#ContentFrame").size() == 1
        });

    /**
     * Test: The main test suite
     */
    new h.TestSuite("Core Components - Form Button",{path:"/apps/core/wcm/tests/core-components-it/FormButton.js",
        execBefore:c.tcExecuteBeforeTestSuite,
        execInNewWindow : true})

        .addTestCase(createSubmitButton)
        .addTestCase(createResetButton)
        .addTestCase(setButtonText)
        .addTestCase(setButtonCss)
        .addTestCase(setButtonDisabled)
        .addTestCase(setButtonName)
        .addTestCase(setButtonValue)
        .addTestCase(setButtonAutoFocus);
})(hobs, jQuery);
