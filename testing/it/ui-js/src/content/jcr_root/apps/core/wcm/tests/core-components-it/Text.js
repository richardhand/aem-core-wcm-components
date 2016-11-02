/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
;(function (h, $) {

    var pageUrl = window.CQ.CoreComponentsIT.pageRoot;
    pageUrl     = pageUrl + '/page0';

    /**
     * Drag and Drop a Title component.
     */
    window.CQ.CoreComponentsIT.DragDropText = function (h, $) {
        return new h.TestCase('Drag and drop the form button')
            .execTestCase(window.CQ.CoreComponentsIT.CreatePage(h, $, pageUrl, 'page0', 'CoreComponent TestPage',
                '/conf/core-components/settings/wcm/templates/core-components'))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropComponent(h, $, 'Core WCM Text Component', pageUrl));
    };

    /**
     * Check the Edit button for the Title component.
     */
    window.CQ.CoreComponentsIT.CheckEditButtonTest = function (h, $) {
        var testValue = '<b>This</b> is a <i>rich</i> <u>text</u>.'
        return new h.TestCase('Check the edit button')
            .execTestCase(window.CQ.CoreComponentsIT.OpenEditableToolbar(h,$,'.cq-Overlay.cq-draggable.cq-droptarget'))
            .click('.coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title="Edit"]')
            .asserts.isTrue(function() {return hobs.find('.title.aem-GridColumn .cmp.cmp-text','#ContentFrame')})

            //get a new context
            .config.changeContext(function() {
                return hobs.find('iframe#ContentFrame').get(0);
            })
            .execFct(function() {
                hobs.find('.text.aem-GridColumn > p').html(testValue);
            })
            //reset the new context
            .config.resetContext()

            .click('#OverlayWrapper')
            .click('.cq-Overlay.cq-draggable.cq-droptarget')
            .click('.cq-Overlay.cq-draggable.cq-droptarget')
            .asserts.isTrue(
                function() {
                    var actualValue = hobs.find('.coral-RichText-editable.coral-Form-field.coral-Textfield.coral-Textfield--multiline.coral-RichText > p').html();
                    return actualValue === testValue;
                }
            ).click('.cq-dialog-header-action.cq-dialog-cancel.coral-Button.coral-Button--square[title="Cancel"]')
            .config.changeContext(
                function() {
                    return hobs.find('iframe#ContentFrame').get(0);
                }
            ).asserts.isTrue(
                function() {
                    var actualValue = hobs.find('.section.text.aem-GridColumn > div.cmp.cmp-text > p').html();
                    return actualValue === testValue;
                }
            )
            .config.resetContext()
        ;
    };


    new h.TestSuite('Core-Components Tests - Text', {
        path      : '/apps/core/wcm/tests/core-components-it/Text.js',
        execBefore: window.CQ.CoreComponentsIT.ExecuteBefore(h, $, window.CQ.CoreComponentsIT.DragDropText(h, $)),
        execAfter : window.CQ.CoreComponentsIT.DeletePage(h, $, pageUrl), register: true
    })
        .addTestCase(window.CQ.CoreComponentsIT.CheckEditButtonTest(h, $))
    ;
}(hobs, jQuery));
