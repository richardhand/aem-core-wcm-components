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

    /**
     * Open the Editable toolbar.
     */
    window.CQ.CoreComponentsIT.OpenEditableToolbar = function (h, $, selector) {
        return new h.TestCase("Open Editable Toolbar")
            //click on the component to see the Editable Toolbar
            .click("#OverlayWrapper")
            .click(selector)
            .asserts.visible("#EditableToolbar")
        ;
    }

    /**
     * Drag and Drop a component.
     */
    window.CQ.CoreComponentsIT.DragDropConponent = function (h, $, component, pageUrl) {
        return new h.TestCase("Drag and drop a component")
            .navigateTo("/editor.html" + pageUrl + ".html")
            .asserts.location("/editor.html" + pageUrl + ".html", true)
            .click(".coral-Tab[title='Components']")
            .asserts.visible(".coral-Masonry .card-component", true)
            .cui.dragdrop(
                ".coral-Masonry-item :contains('"+component+"')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            .asserts.visible(".cq-Overlay.cq-draggable.cq-droptarget")
            .wait(500)
            ;
    }

    /**
     * Check the button actions from the Editable Toolbar.
     */
    window.CQ.CoreComponentsIT.CheckEditableToolbar = function (h, $, numberOfButtons) {
        return new h.TestCase("Check the editable toolbar")

        //test the Copy btton
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Copy']")

            //test the Paste button
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            //check if the Paste button appeared
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".coral-Button.cq-editable-action", numberOfButtons);})
            //press the past button
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            //check if we have two components
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})

            //test the Delete button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget:eq(1)"))
            //click on the delete button
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Delete']")
            .click(".coral-Button.coral-Button--warning")
            //check if we have two components
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 1);})

            //test the Insert Component button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Insert component']")
            .wait(500)
            .asserts.visible(".coral-Dialog-wrapper .coral-Dialog-content.InsertComponentDialog-components")
            .click(".coral-Dialog.InsertComponentDialog .coral-Dialog-wrapper .coral-Dialog-header .coral-Dialog-closeButton")

            //test the Group button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Group']")
            .asserts.exists(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square.is-active[title='Group']")

            //test the Parent button
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Parent']")
            .asserts.exists(".cq-Overlay.cq-Overlay--component.cq-Overlay--container.is-selected.is-active")

            //test the Cut button
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Layout Container')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            //click on the component to see the Editable Toolbar
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget:eq(1)"))
            .wait(500)
            //cut the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Cut']")
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-Overlay--component.cq-droptarget.cq-Overlay--placeholder:first"))
            .wait(500)
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .wait(500)
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})
            .wait(500)
        ;
    }

})(hobs);

