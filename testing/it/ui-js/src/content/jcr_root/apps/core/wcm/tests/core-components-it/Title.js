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
     * Drag and Drop a Title component.
     */
    window.CQ.CoreComponentsIT.DragDropTitle = function (h, $) {
        return new h.TestCase("Drag and drop the form button")
            .execTestCase(window.CQ.CoreComponentsIT.CreatePage(h,$,pageUrl, "page0","CoreComponent TestPage",
                "/conf/core-components/settings/wcm/templates/core-components"))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropComponent(h,$,"Core WCM Title Component",pageUrl))
        ;
    }

    /**
     * Check the Edit button for the Title component.
     */
    window.CQ.CoreComponentsIT.CheckEditButtonTest = function (h, $) {
        return new h.TestCase("Check the edit button")
            //click on the component to see the Editable Toolbar
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Edit']")
            //check de number of the button from the EditableToolbar
            .assert.isTrue(function() {return hobs.find(".title.aem-GridColumn .cmp.cmp-title","#ContentFrame")})
            //.execFct(function() { hobs.find(".title.aem-GridColumn .cmp.cmp-title > h1","#ContentFrame").replaceWith("content test")})

            //get a new context
            .config.changeContext(function() {
                return hobs.find("iframe#ContentFrame").get(0);
            })
            .execFct(function() {
                hobs.find(".title.aem-GridColumn .cmp.cmp-title > h1").html("Content test")
            })
            .assert.isTrue(
                function() {
                    var actualValue = hobs.find('.title.aem-GridColumn .cmp.cmp-title > h1').html();
                    return actualValue === "Content test";
                }
            )
            //reset the new context
            .config.resetContext()

            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
        ;
    }

    window.CQ.CoreComponentsIT.CheckTitleType = function (h, $, index, value) {
        return new h.TestCase("Check title type")
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $, ".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".cq-dialog-content.coral-FixedColumn .coral-Button")
            .click(".coral-Overlay.coral3-Select-overlay.is-open .coral3-SelectList-item:eq("+index+")")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .assert.isTrue(function () {return h.find(".title.aem-GridColumn .cmp.cmp-title >"+ value,"#ContentFrame")})
        ;
    }

    /**
     * Function used for checking the type and the size of the Title component.
     */
    window.CQ.CoreComponentsIT.CheckTitleTypes = function (h, $) {
        var titleType = new h.TestCase ("Check title types");
        for (var i=0; i<= 6; i++) {
            var value = h.find(".coral-Overlay.coral3-Select-overlay.is-open .coral3-SelectList-item:eq(" + i + ")").val()
            titleType.execTestCase(window.CQ.CoreComponentsIT.CheckTitleType(h, $, i, value))
        }
        return titleType
    }

    /**
     * Check the Configure button for the Title component.
     */
    window.CQ.CoreComponentsIT.CheckConfigureButtonTest = function (h, $){
        return new h.TestCase("Check the Configure button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $, ".cq-Overlay.cq-draggable.cq-droptarget"))
            .assert.visible(".coral-Form-field.coral-Textfield[name='./jcr:title']")
            .fillInput(".coral-Form-field.coral-Textfield[name='./jcr:title']","Content name")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the title type
            .execTestCase(window.CQ.CoreComponentsIT.CheckTitleTypes(h, $))
            //click on the fullscreen button
            .execTestCase(window.CQ.CoreComponentsIT.OpenFullSreen(h,$))
            //close the configure window
            .execTestCase(window.CQ.CoreComponentsIT.CloseConfigureWindow(h,$))
        ;
    }

    new h.TestSuite("Core-Components Tests - Title", {path:"/apps/core/wcm/tests/core-components-it/Title.js",
        execBefore: window.CQ.CoreComponentsIT.ExecuteBefore(h,$,window.CQ.CoreComponentsIT.DragDropTitle(h,$)), execAfter:window.CQ.CoreComponentsIT.DeletePage(h, $,pageUrl), register: true})
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditButtonTest(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckConfigureButtonTest(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbarTest(h,$, 10,".cq-Overlay.cq-draggable.cq-droptarget:not(.cq-Overlay--container)"))
    ;
}(hobs, jQuery));
