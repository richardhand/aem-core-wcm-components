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

    var commons = window.CQ.CoreComponentsIT.commons;

    var executeBeforeTitleTest = new h.TestCase("Setup Before Test")
            // create the test page using our own test template
            .execFct(commons.createTestPage)
            // add the title component
            .execFct(function (opts, done) {
                commons.createComponent("core/wcm/components/title", done)
            })
            // open the page in the editor
            .navigateTo("/editor.html" + commons.testPagePath + ".html")
        ;

    /**
     * Set the title value using the edit dialog.
     */
    var setTitleValueUsingEditDialog = new h.TestCase("Set Title value using edit dialog",{
            execBefore: executeBeforeTitleTest,
            execAfter: commons.executeAfterTest})

            //click on the component to see the Editable Toolbar
            .execTestCase(commons.tcOpenEditDialog)
            .click(".cq-editable-action[title='Edit']")
            .assert.isTrue(function() {return hobs.find(".title.aem-GridColumn .cmp.cmp-title","#ContentFrame")})

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
        ;

    /**
     * Set the title value using the design dialog.
     */
    var setTitleValueUsingDesignDialog = new h.TestCase("Set Title value using design dialog",{
            execBefore: executeBeforeTitleTest,
            execAfter: commons.executeAfterTest})

            .execTestCase(commons.tcOpenConfigureDialog)
            .assert.visible(".coral-Form-field.coral-Textfield[name='./jcr:title']")
            .fillInput("[name='./jcr:title']","Content name")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
        ;

    /**
     * Check the existance of all available title types.
     */
    var checkExistenceOfTitleTypes = new h.TestCase("Check the existence of all title types",{
            execBefore: executeBeforeTitleTest,
            execAfter: commons.executeAfterTest})

            .execTestCase(commons.tcOpenConfigureDialog)
            .click(".cq-dialog-content.coral-FixedColumn .coral-Button")
            .assert.exist(".coral3-SelectList-item[value='h1']")
            .assert.exist(".coral3-SelectList-item[value='h2']")
            .assert.exist(".coral3-SelectList-item[value='h3']")
            .assert.exist(".coral3-SelectList-item[value='h4']")
            .assert.exist(".coral3-SelectList-item[value='h5']")
            .assert.exist(".coral3-SelectList-item[value='h6']")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
    ;

    /**
     * Check the title type.
     */
    var checkTitleType = new h.TestCase("Check the title type",{
            execBefore: executeBeforeTitleTest,
            execAfter: commons.executeAfterTest})

            .execTestCase(commons.tcOpenConfigureDialog)
            .click(".cq-dialog-content.coral-FixedColumn .coral-Button")
            .click(".coral3-SelectList-item[value='h5']")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .assert.isTrue(function () {return h.find(".title.aem-GridColumn .cmp.cmp-title > H5","#ContentFrame")})
        ;

    new h.TestSuite("Core-Components Tests - Title", {path:"/apps/core/wcm/tests/core-components-it/Title.js",
        execBefore:commons.executeBeforeTestSuite})
        .addTestCase(setTitleValueUsingEditDialog)
        .addTestCase(setTitleValueUsingDesignDialog)
        .addTestCase(checkExistenceOfTitleTypes)
        .addTestCase(checkTitleType)
    ;
}(hobs, jQuery));
