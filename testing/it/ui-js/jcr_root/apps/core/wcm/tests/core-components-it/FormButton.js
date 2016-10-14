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

    window.CQ.CoreComponentsIT.DragDropFormButton = function (h, $) {
        return new h.TestCase("Drag and drop the form button")
            .navigateTo("/editor.html"+pageUrl+".html")
            .asserts.location("/editor.html"+pageUrl+".html", true)
            .click(".coral-Tab[title='Components']")
            .asserts.visible(".coral-Masonry .card-component", true)
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Core WCM Form Button Component (v1)')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            .asserts.visible(".cq-Overlay.cq-draggable.cq-droptarget")
            .wait(500)
        ;
    }
/*
    window.CQ.CoreComponentsIT.FormButton.OpenConfigureWindow = function (h, $) {
        return new h.TestCase("Open the Configure window")
        //click on the component to see the Editable Toolbar
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //press the Configure button
            .click(".coral-Button.cq-editable-action[title='Configure']")
            .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form")
            ;
    }
*/
    window.CQ.CoreComponentsIT.CheckConfigureButton = function (h, $){
        return new h.TestCase("Check the Configure button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //close the configure window
            .wait(500)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--close")
        ;
    }

    window.CQ.CoreComponentsIT.CheckEditableToolbar = function (h, $) {
        return new h.TestCase("Check the editable toolbar")
            //click on the component to see the Editable Toolbar
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //check de number of the button from the EditableToolbar
            //.asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".coral-Button.cq-editable-action", 7);})

            //copy the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Copy']")
            //paste the component
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //check if the Paste button appeared
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".coral-Button.cq-editable-action", 8);})

            //paste a component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})

            .wait(500)
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            //delete a component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Delete']")
            .wait(500)
            .click(".coral-Button.coral-Button--warning")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 1);})

            //press the Insert Component button
            .wait(500)
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Insert component']")
            .asserts.visible(".coral-Dialog-wrapper .coral-Dialog-content.InsertComponentDialog-components")
            .click(".coral-Dialog.InsertComponentDialog .coral-Dialog-wrapper .coral-Dialog-header .coral-Dialog-closeButton")

            //press the Group button
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Group']")
            .asserts.exists(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square.is-active[title='Group']")

            //press the Parent button
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Parent']")
            .asserts.exists(".cq-Overlay.cq-Overlay--component.cq-Overlay--container.is-selected.is-active")

            //test the cut button
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Layout Container')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            //click on the component to see the Editable Toolbar
            .wait(500)
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            //cut the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Cut']")
            .wait(500)
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-Overlay--component.cq-droptarget.cq-Overlay--placeholder:first")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .wait(500)
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})
            .wait(500)
        ;
    }

    new h.TestSuite("Core-Components Tests - Form Button", {path:"/apps/core/wcm/tests/core-components-it/FormButton.js",
        execBefore: hobs.steps.aem.commons.disableTutorials, register: true})
        .addTestCase(window.CQ.CoreComponentsIT.DragDropFormButton(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckConfigureButton(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbar(h,$))
    ;
})(hobs, jQuery);
