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
    var from_mail = "from@adobe.com"
    var mail1 = "mail1@adobe.com"
    var mail2 = "mail2@adobe.com"
    var subject = "This is a subject"

    var textContent = "Text field content"
    var formIdentifier = "content_core-components"

    var contentPath = "/content/usergenerated/core-components/core-components-page/cq/template"
    var thankYouPagePath = "/content/we-retail/language-masters/en/user/account/sign-up/thank-you"
    var loadPath = "/content/usergenerated/core-components-new/core-components-page/cq/timestamp"

    var jsonFile = ""

    var createFormContentNode = function(url){
        return $.ajax({
            url: url,
            method: "POST",
            data: {
                "./jcr:primaryType": "sling:Folder",
                "./sling:resourceType": "sling:Folder",
                "./core-components-page/jcr:primaryType": "sling:Folder",
                "./core-components-page/sling:resourceType": "sling:Folder",
                "./core-components-page/cq/jcr:primaryType": "sling:Folder",
                "./core-components-page/cq/formPath": "/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer",
                "./core-components-page/cq/sling:resourceType": "foundation/components/form/actions/showbulkeditor",
                "./core-components-page/cq/timestamp/jcr:primaryType": "sling:Folder",
                "./core-components-page/cq/timestamp/field_name": "The new text field content"
            }
        })
    }

    window.CQ.CoreComponentsIT.FormContainer.CreateContentNode = function (h, $, url) {
        return new h.TestCase("Create a new node")
            .execFct(function (opts, done) {
                createFormContentNode(url).then(done);
            })
        ;
    }

    var getJsonFile = function(url) {
        return $.ajax(url + ".json");
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckDataStore = function (h, $, url) {
        return new h.TestCase("Create a page")

            .execFct(function(opts, done) {
                getJsonFile(url).done(function(response) {
                    jsonFile = response;
                    done();
                });
            })

            .assert.isTrue(function () {
                code = true;
                if (jsonFile === null || jsonFile === ""){
                    code=false;
                }

                return code;
            })
        ;
    }

    /**
     * Drag and Drop a Form Container component.
     */
    window.CQ.CoreComponentsIT.FormContainer.DragDropFormContainer = function (h, $) {
        return new h.TestCase('Drag and drop the form container')
            .execTestCase(window.CQ.CoreComponentsIT.CreatePage(h, $, pageUrl, 'page0', 'CoreComponent TestPage',
                '/conf/core-components/settings/wcm/templates/core-components'))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropComponent(h, $, 'Core WCM Form Container Component', pageUrl))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.DragDropFormComponentInContainer(h, $))
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.DragDropFormComponentInContainer = function (h, $) {
        return new h.TestCase('Drag and drop a form component in the container')
            .assert.exist(".coral-Masonry-item:contains('Core WCM Form Text field')")
            .assert.exist("#OverlayWrapper .cq-Overlay--placeholder[data-path='/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer/*']")
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Core WCM Form Text field')",
                "#OverlayWrapper .cq-Overlay--placeholder[data-path='/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer/*']",
                {delayBefore: 2500}
            )
            //Set the element name and the title for the Text field
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$,"div[data-path='/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer/text']"))
            .fillInput("input[name='./name']","field_name")
            .fillInput("input[name='./jcr:title']", "Text fiels title")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            .assert.exist(".coral-Masonry-item:contains('Core WCM Form Button Component')")
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Core WCM Form Button Component')",
                "#OverlayWrapper .cq-Overlay--placeholder[data-path='/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer/*']",
                {delayBefore: 2500}
            )
            //Set the type and the title pf the button
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$,"div[data-path='/content/core-components/core-components-page/page0/jcr:content/root/responsivegrid/formcontainer/button']"))
            .assert.isTrue(function() {return hobs.find(".btn[type='button']","#ContentFrame")})
            .click(".coral-Button:contains('Button')")
            .click(".coral3-SelectList-item:contains('Submit')")
            .fillInput("input[name='./title']", "Submit button")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.OpenFormType = function (h,$, formType) {
        return new h.TestCase('Open the mail form')
            .click(".coral-Tab:contains('Form')")
            .click(".coral-Form-field.cmp-action-type-selection.coral3-Select >button")
            .click(".coral3-SelectList-item:contains('"+formType+"')")
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.NavigateToEditor = function (h,$) {
        return new h.TestCase('Navigate to the edit page')
            .navigateTo("/editor.html" + pageUrl + ".html")
            .assert.location("/editor.html" + pageUrl + ".html", true)

            .if(
                function() { return h.find("button.is-selected:contains('Preview')").length != 0 },
                new hobs.TestCase("Open SidePanel").click("button.editor-GlobalBar-item:contains('Edit')")
                    .wait(250),null, {timeout: 100}
            )
            .if(
                function() { return h.find('#SidePanel.sidepanel-closed').length != 0 },
                new hobs.TestCase("Open SidePanel").click("button.toggle-sidepanel[title='Toggle Side Panel']")
                   .wait(250),null, {timeout: 100}
            )
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckFillMailForm = function (h,$) {
        return new h.TestCase('Check the mail form')
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--placeholder"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$, 'Mail'))

            //Fill in the From
            .fillInput(".action-type-dialog:not(.hide) >div:contains('From') >input", from_mail)

            //Fill in the Mailto
            .click(".action-type-dialog:not(.hide) >div:contains('Mailto') >coral-multifield >button")
            .fillInput("input[name='./mailto']", mail1)
            .click(".action-type-dialog:not(.hide) >div:contains('Mailto') >coral-multifield >button")
            .fillInput("input[name='./mailto']:eq(1)", mail2)

            //Fill in the CC
            .click(".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >coral-multifield >button")
            .fillInput("input[name='./cc']", mail1)
            .click(".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >coral-multifield >button")
            .fillInput("input[name='./cc']:eq(1)", mail2)

            //Fill in the BCC
            .click(".action-type-dialog:not(.hide) >div:contains('BCC') >coral-multifield >button")
            .fillInput("input[name='./bcc']", mail1)
            .click(".action-type-dialog:not(.hide) >div:contains('BCC') >coral-multifield >button")
            .fillInput("input[name='./bcc']:eq(1)", mail2)

            //Fill in the Subject
            .fillInput(".action-type-dialog:not(.hide) >div:contains('Subject') >input", subject)

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckMailPersistence = function (h,$) {
        return new h.TestCase('Check the persistence of the mail addresses')
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$, 'Mail'))

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./from']", from_mail)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./mailto']:first", mail1)})
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./mailto']:eq(1)", mail2)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./cc']:first", mail1)})
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./cc']:eq(1)", mail2)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./bcc']:first", mail1)})
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./bcc']:eq(1)", mail2)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkInputValue(h,"input[name='./subject']", subject)})

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckMailAddressNumber = function (h,$){
        return new h.TestCase('Check the number of the mail addresses and the Delete button')
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$, 'Mail'))

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('Mailto') >> coral-Multifield-item", 2)})
            .click(".action-type-dialog:not(.hide) >div:contains('Mailto') >> coral-Multifield-item.last-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('Mailto') >> coral-Multifield-item", 1)})
            .click(".action-type-dialog:not(.hide) >div:contains('Mailto') >> coral-Multifield-item.first-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('Mailto') >> coral-Multifield-item", 0)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >> coral-Multifield-item", 2)})
            .click(".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >> coral-Multifield-item.last-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >> coral-Multifield-item", 1)})
            .click(".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >> coral-Multifield-item.first-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('CC'):not(:contains('BCC')) >> coral-Multifield-item", 0)})

            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('BCC') >> coral-Multifield-item", 2)})
            .click(".action-type-dialog:not(.hide) >div:contains('BCC') >> coral-Multifield-item.last-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('BCC') >> coral-Multifield-item", 1)})
            .click(".action-type-dialog:not(.hide) >div:contains('BCC') >> coral-Multifield-item.first-of-type > button.coral-Multifield-remove")
            .assert.isTrue(function() { return window.CQ.CoreComponentsIT.checkNumberOfItems
            (h,".action-type-dialog:not(.hide) >div:contains('BCC') >> coral-Multifield-item", 0)})

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckMailForm = function (h,$) {
        return new h.TestCase('Check the mail form')
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckFillMailForm(h,$))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckMailPersistence(h,$))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckMailAddressNumber(h,$))
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.SetAndCheckThankYouPage = function (h,$) {
        return new h.TestCase('Set the Thank you page')
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.NavigateToEditor(h,$))
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$,'Store Content'))

            .click(".coral-Form-fieldwrapper.cmp-redirect-selection .coral-InputGroup-button >button")

            .click("coral-columnview-item-content[title='we-retail']")
            .click("coral-columnview-item-content[title='language-masters']")
            .click("coral-columnview-item-content[title='en']")
            .click("coral-columnview-item-content[title='user']")
            .click("coral-columnview-item-content[title='account']")
            .click("coral-columnview-item-content[title='sign-up']")
            .click("coral-columnview-item-content[title='thank-you']")

            .click("coral-columnview-item:contains('thank-you') .foundation-collection-item-thumbnail")
            .click("button.granite-pickerdialog-submit:contains('Select')")
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SubmitFormContent(h,$))

            //Check the redirection after the form submit
            .assert.location("/editor.html/content/we-retail/language-masters/en/user/account/sign-up/thank-you.html", true)
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.SubmitFormContent = function (h,$) {
        return new h.TestCase('Submit the Form Content')
            //Submit the form content
            .click("button[data-layer='Preview']")

            //get a new context
            .config.changeContext(function() {
                return hobs.find("iframe#ContentFrame").get(0);
            })

            .fillInput("input[id='field_name']", textContent)
            .click("button:contains('Submit button')")

            .config.resetContext()
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.SetAndCheckStartWorkflow = function (h,$) {
        return new h.TestCase('Set and check the Start Workflow')
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.NavigateToEditor(h,$))
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$,'Store Content'))

            //Set the Start Workflow
            .click(".coral-Form-fieldwrapper.cmp-workflow-selection:contains('Start Workflow') .coral-Button")
            .click(".coral3-Select-overlay.is-open .coral3-SelectList-item:contains('Publish Example')")

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SubmitFormContent(h,$))

            //Check if the workflow is started
            .navigateTo("/libs/cq/workflow/admin/console/content/instances.html")
            .assert.location("/libs/cq/workflow/admin/console/content/instances.html")
            .assert.isTrue(function () {
                return hobs.find(".foundation-collection-item.foundation-collection-navigator.coral-Table-row:contains('Publish Example')").length >=1;
            })
            .assert.isTrue(function () {
                return hobs.find(".foundation-collection-item.foundation-collection-navigator.coral-Table-row:contains('Publish Example'):first > td:contains('RUNNING')")
            })
            .assert.isTrue(function () {
                return hobs.find(".foundation-collection-item.foundation-collection-navigator.coral-Table-row:contains('Publish Example'):first > td:contains('"+contentPath+"')")
            })
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.SetAndCheckContentPath = function (h,$) {
        return new h.TestCase('Set and check the Content Path')
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.NavigateToEditor(h,$))
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.OpenFormType(h,$,'Store Content'))

            //Set a different content path
            .fillInput("input[name='./action']",contentPath)
            .click("button:contains('View Data')")

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SubmitFormContent(h,$))

            //Check if data are saved in the Bulk Editor
            .navigateTo("/etc/importers/bulkeditor.html?rootPath=%2Fcontent%2Fusergenerated%2Fcore-components%2Fcore-components-page%2Fcq&initialSearch=true&contentMode=false&spc=true&cs=field_name&cv=field_name")
            .assert.isTrue(window.CQ.CoreComponentsIT.compareLocation("/etc/importers/bulkeditor.html?rootPath=%2Fcontent%2Fusergenerated%2Fcore-components%2Fcore-components-page%2Fcq&initialSearch=true&contentMode=false&spc=true&cs=field_name&cv=field_name"))
            .assert.isTrue(function (){
                return hobs.find(".x-grid3-body:contains('"+textContent+"')")
            })
            .assert.isTrue(function (){
                return hobs.find(".x-grid3-body:contains('"+contentPath+"')")
            })

            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckDataStore(h, $, contentPath))
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckStoreContentForm = function (h,$){
        return new h.TestCase('Check the Store Content')
            //Set and check the Content Path
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SetAndCheckContentPath(h,$))
            //Set the "Thank You Page"
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SetAndCheckThankYouPage(h,$))
            //Set the Start Workflow
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.SetAndCheckStartWorkflow(h,$))
        ;
    }

    window.CQ.CoreComponentsIT.FormContainer.CheckAdvancedOptions = function (h,$) {
        return new h.TestCase('Check Advanced Options')

            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.NavigateToEditor(h,$))

            //Test the content path
            .execTestCase(window.CQ.CoreComponentsIT.FormContainer.CreateContentNode(h,$,"/content/usergenerated/core-components-new"))

            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h,$, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
            .click(".coral-Tab:contains('Advanced')")
            .fillInput("input[name='./formid']",formIdentifier)
            .fillInput("input[name='./loadPath']",loadPath)

            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")

            //get a new context
            .config.changeContext(function() {
                return hobs.find("iframe#ContentFrame").get(0);
            })

            .assert.isTrue(function () {
                return hobs.find("form[id='"+formIdentifier+"'][name='"+formIdentifier+"']")
            })

            .assert.isTrue(function () {
                return hobs.find("input[id='field_name']").filter(function() {
                    return $(this).val() === "The new text field content";
                })
            })

            .config.resetContext()
        ;
    }

    new h.TestSuite("Core-Components Tests - Form Container", {path:"/apps/core/wcm/tests/core-components-it/FormContainer.js",
        execBefore: window.CQ.CoreComponentsIT.ExecuteBefore(h,$,window.CQ.CoreComponentsIT.FormContainer.DragDropFormContainer(h,$)),
        execAfter:window.CQ.CoreComponentsIT.DeletePage(h, $,pageUrl),
        register: true})
        .addTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckMailForm(h,$))
        .addTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckStoreContentForm(h,$))
        .addTestCase(window.CQ.CoreComponentsIT.FormContainer.CheckAdvancedOptions(h,$))
        .addTestCase(window.CQ.CoreComponentsIT.OpenFullSreen(h,$))
        //.addTestCase(window.CQ.CoreComponentsIT.CheckEditableToolbarTest(h,$, 9, ".cq-Overlay.cq-draggable.cq-droptarget.cq-Overlay--container"))
    ;

}(hobs, jQuery));

