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

    var pageUrl = window.CQ.CoreComponentsIT.pageRoot+"/page0";

    /**
     * Drag and Drop a Form Button component.
     */
    window.CQ.CoreComponentsIT.DragDropFormButton = function (h, $) {
        return new h.TestCase("Drag and drop the form button")
            .execTestCase(window.CQ.CoreComponentsIT.CreatePage(h,$,pageUrl, "page0","CoreComponent TestPage",
                "/conf/core-components/settings/wcm/templates/core-components"))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropComponent(h,$,"Core WCM Form Button Component (v1)",pageUrl))
        ;
    }

    /**
     * Change the button type.
     */
    window.CQ.CoreComponentsIT.ChangeButtonType = function (h,$, fromType, toType) {
        return new h.TestCase("Change the button type")
            //open the configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //Check the type of the button
            .asserts.isTrue(function() {return hobs.find(".btn[type='"+fromType.toLowerCase()+"']","#ContentFrame")})
            //change the type of the button
            .click(".coral-Button:contains('"+fromType+"')")
            .click(".coral3-SelectList-item:contains('"+toType+"')")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //Check the type of the button
            .asserts.isTrue(function() {return hobs.find(".btn[type='"+toType.toLowerCase()+"']","#ContentFrame")})
        ;
    }

    /**
     * Check the Configure button for the component.
     */
    window.CQ.CoreComponentsIT.CheckConfigureButtonTest = function (h, $){
        return new h.TestCase("Check the Configure button")

            //Change the Type of the button to Submit
            .execTestCase(window.CQ.CoreComponentsIT.ChangeButtonType(h,$,"Button","Submit"))
            //Change the Type of the button to Reset
            .execTestCase(window.CQ.CoreComponentsIT.ChangeButtonType(h,$,"Submit","Reset"))
            //Change the Type of the button to Button
            .execTestCase(window.CQ.CoreComponentsIT.ChangeButtonType(h,$,"Reset","Button"))
            //Fill the Title
            .execTestCase(window.CQ.CoreComponentsIT.FillInput(h,$,"[name='./title']","Test Button",
                function() {return window.CQ.CoreComponentsIT.checkContentFromIFrame(h,"#ContentFrame",".btn", "Test Button")}))
            //Fill the cssClass
            .execTestCase(window.CQ.CoreComponentsIT.FillInput(h,$,"[name='./cssClass']","coral-Icon",
                function() {return hobs.find(".btn.coral-Icon","#ContentFrame")}))
            //Check the Disable button
            .execTestCase(window.CQ.CoreComponentsIT.CheckCheckBox(h,$,"[name='./disabled']",
                function() {return hobs.find("[disabled]","#ContentFrame")}))
            //Fill the Name
            .execTestCase(window.CQ.CoreComponentsIT.FillInput(h,$,"[name='./name']","button1",
                function() {return hobs.find("[name='button1']","#ContentFrame")}))
            //Fill the Value
            .execTestCase(window.CQ.CoreComponentsIT.FillInput(h,$,"[name='./value']","value1",
                function() {return hobs.find("[value='value1']","#ContentFrame")}))
            //Check the Autofocus button
            .execTestCase(window.CQ.CoreComponentsIT.CheckCheckBox(h,$,"[name='./autofocus']",
                function() {return hobs.find("[autofocus]","#ContentFrame")}))
            //click on the fullscreen button
            .execTestCase(window.CQ.CoreComponentsIT.OpenFullSreen(h,$))
            //close the configure window
            .execTestCase(window.CQ.CoreComponentsIT.CloseConfigureWindow(h,$))
        ;
    }

    new h.TestSuite("Core-Components Tests - Form Button", {path:"/apps/core/wcm/tests/core-components-it/FormButton.js",
        execBefore: window.CQ.CoreComponentsIT.ExecuteBefore(h,$,window.CQ.CoreComponentsIT.DragDropFormButton(h,$)), execAfter:window.CQ.CoreComponentsIT.DeletePage(h, $,pageUrl), register: true})
        .addTestCase(window.CQ.CoreComponentsIT.CheckConfigureButtonTest(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbarTest(h,$, 9))
    ;
})(hobs, jQuery);
