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
     * Drag and Drop a component.
     */
    window.CQ.CoreComponentsIT.DragDropComponent = function (h, $, component, pageUrl) {
        return new h.TestCase("Drag and drop a component")
            .navigateTo("/editor.html" + pageUrl + ".html")
            .asserts.location("/editor.html" + pageUrl + ".html", true)
            .assert.visible("button.toggle-sidepanel[title='Toggle Side Panel']")
            .if(
                function() { return h.find('#SidePanel.sidepanel-closed').length != 0 },
                new hobs.TestCase("Open SidePanel").click("button.toggle-sidepanel[title='Toggle Side Panel']")
                   .wait(250),null, {timeout: 100}
            )
            .assert.visible(".coral-Tab[title='Components']")
            .click(".coral-Tab[title='Components']")
            .wait(500)
            .asserts.visible(".coral-Masonry .card-component")
            .asserts.visible(".coral-Masonry-item :contains('"+component+"')")
            .asserts.visible(".cq-Overlay .cq-droptarget")
            .cui.dragdrop(
                ".coral-Masonry-item :contains('"+component+"')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            .asserts.visible(".cq-Overlay.cq-draggable.cq-droptarget")
        ;
    }

    /**
     * Test Case executed before every test case.
     */
    window.CQ.CoreComponentsIT.ExecuteBefore = function (h, $, testCase) {
        return new h.TestCase("Execute before")
            .execTestCase(hobs.steps.aem.commons.disableTutorials)
            .execTestCase(testCase)
        ;
    }
})(hobs);

