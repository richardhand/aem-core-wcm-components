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
     * Open the Editable Toolbar.
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
     * Check the Copy and Paste buttons.
     */
    window.CQ.CoreComponentsIT.CheckCopyPasteButton = function (h,$,numberOfButtons){
        return new h.TestCase("Check the Copy Paste button")

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

            ;
    }

    /**
     * Check the Delete button.
     */
    window.CQ.CoreComponentsIT.CheckDeleteButton = function (h, $) {
        return new h.TestCase("Check the Delete button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget:eq(1)"))
            //click on the delete button
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Delete']")
            .click(".coral-Button.coral-Button--warning")
            //check if we have two components
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 1);})

            ;
    }

    /**
     * Check the Insert Component button.
     */
    window.CQ.CoreComponentsIT.CheckInsertComponentButton = function (h, $) {
        return new h.TestCase("Check the Insert Component button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Insert component']")
            .asserts.visible(".coral-Dialog-wrapper .coral-Dialog-content.InsertComponentDialog-components")
            .click(".coral-Dialog.InsertComponentDialog .coral-Dialog-wrapper .coral-Dialog-header .coral-Dialog-closeButton")
            ;
    }

    /**
     * Check the Group button.
     */
    window.CQ.CoreComponentsIT.CheckGroupButton = function (h,$) {
        return new h.TestCase("Check the Group button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Group']")
            .asserts.exists(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square.is-active[title='Group']")
            ;
    }

    /**
     * Check the Parent button.
     */
    window.CQ.CoreComponentsIT.CheckParentButton = function (h,$) {
        return new h.TestCase("Check the Parent button")
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Parent']")
            .asserts.exists(".cq-Overlay.cq-Overlay--component.cq-Overlay--container.is-selected.is-active")
            ;
    }

    /**
     * Check the Cut button.
     */
    window.CQ.CoreComponentsIT.CheckCutButton = function (h,$) {
        return new h.TestCase("Check the Cut button")
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Layout Container')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            //click on the component to see the Editable Toolbar
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-draggable.cq-droptarget:not(.cq-Overlay--container)"))
            //cut the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Cut']")
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,".cq-Overlay.cq-Overlay--component.cq-droptarget.cq-Overlay--placeholder:first"))
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})
            ;
    }

    /**
     * Check the button actions from the Editable Toolbar.
     */
    window.CQ.CoreComponentsIT.CheckEditableToolbarTest = function (h, $, numberOfButtons) {

        return new h.TestCase("Check the editable toolbar")
        //test the Copy btton
            .execTestCase(window.CQ.CoreComponentsIT.CheckCopyPasteButton(h, $,numberOfButtons))
            //test the Delete button
            .execTestCase(window.CQ.CoreComponentsIT.CheckDeleteButton(h,$))
            //test the Insert Component button
            .execTestCase(window.CQ.CoreComponentsIT.CheckInsertComponentButton(h,$))
            //test the Group button
            .execTestCase(window.CQ.CoreComponentsIT.CheckGroupButton(h,$))
            //test the Parent button
            .execTestCase(window.CQ.CoreComponentsIT.CheckParentButton(h,$))
            //test the Cut button
            .execTestCase(window.CQ.CoreComponentsIT.CheckCutButton(h,$))
            ;
    }

})(hobs);
