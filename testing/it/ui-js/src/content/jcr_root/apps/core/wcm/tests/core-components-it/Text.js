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

    var testValue = '<b>This</b> is a <i>rich</i> <u>text</u>.'
    var commons = window.CQ.CoreComponentsIT.commons;

    var executeBeforeTextTest = new h.TestCase("Setup Before Test")
        // create the test page using our own test template
        .execFct(commons.createTestPage)
        // add the text component
        .execFct(function (opts, done) {
            commons.createComponent("core/wcm/components/text", done)
        })
        // open the page in the editor
        .navigateTo("/editor.html" + commons.testPagePath + ".html")
        ;

    /**
     * Check the Edit button for the Text component.
     */
    var setTextValueUsingEditDialog = new h.TestCase('"Set Text value using edit dialog"',{
            execBefore: executeBeforeTextTest,
            execAfter: commons.executeAfterTest})

            .execTestCase(commons.tcOpenEditDialog)
            .assert.isTrue(function() {return hobs.find('.title.aem-GridColumn .cmp.cmp-text','#ContentFrame')})

            //get a new context
            .config.changeContext(function() {
                return hobs.find('iframe#ContentFrame').get(0);
            })
            .execFct(function() {
                hobs.find('.text.aem-GridColumn > p').html(testValue);
            })

            .assert.isTrue(
            function() {
                var actualValue = hobs.find('.text.aem-GridColumn > p').html();
                return actualValue === testValue;
            }
            )
            //reset the new context
            .config.resetContext()
        ;


    var setTextValueUsingDesignDialog = new h.TestCase("Set Text value using design dialog",{
            execBefore: executeBeforeTextTest,
            execAfter: commons.executeAfterTest})

            .execTestCase(commons.tcOpenConfigureDialog)

            .fillInput(".coral-Form-field.coral-Textfield[name='./text']",'<p>'+testValue+'</p>')

            .click('.cq-dialog-header-action.cq-dialog-submit.coral-Button.coral-Button--square[title="Done"]')

            .execTestCase(commons.tcOpenConfigureDialog)
            .assert.isTrue(
                function() {
                    var actualValue = hobs.find('.coral-RichText-editable.coral-Form-field.coral-Textfield.coral-Textfield--multiline.coral-RichText > p').html();
                    return actualValue === testValue;
                }
            )
            .click('.cq-dialog-header-action.cq-dialog-cancel.coral-Button.coral-Button--square[title="Cancel"]')

            .config.changeContext(
                function() {
                    return hobs.find('iframe#ContentFrame').get(0);
                }
            )
            .assert.isTrue(
                function() {
                    var actualValue = hobs.find('.text.aem-GridColumn >> p').html();
                    return actualValue === testValue;
                }
            )
            .config.resetContext()
        ;


    new h.TestSuite('Core-Components Tests - Text', {path: '/apps/core/wcm/tests/core-components-it/Text.js',
        execBefore:commons.executeBeforeTestSuite
    })
        .addTestCase(setTextValueUsingEditDialog)
        .addTestCase(setTextValueUsingDesignDialog)
    ;
}(hobs, jQuery));
