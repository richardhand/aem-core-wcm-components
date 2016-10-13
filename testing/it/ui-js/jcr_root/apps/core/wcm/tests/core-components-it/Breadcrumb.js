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
    var page = window.CQ.CoreComponentsIT.Pages;

    page.pageRoot = "/content/core-components/core-components-page"

    //Data for the sample page
    var samplePage = {
        title: "CoreComponent TestPage",
        template: "/conf/core-components/settings/wcm/templates/core-components",
        primaryType: "cq:PageContent",
        resourceType: "wcm/foundation/components/page",
        name: "page"

    }

    var pageUrl = page.pageRoot;

    /**
     * Create a page with a specific page name and title.
     */
    var createPage = function(pageUrl, pageName, title){
        return $.ajax({
            url: pageUrl,
            method: "POST",
            data: {
                ":name":pageName,
                "./jcr:primaryType": "cq:Page",
                "./jcr:content/jcr:primaryType": "cq:PageContent",
                "./jcr:content/jcr:title": title,
                "./jcr:content/cq:template": samplePage.template,
                "./jcr:content/sling:resourceType": "wcm/foundation/components/page",
                "./jcr:content/root/jcr:primaryType": "nt:unstructured",
                "./jcr:content/root/sling:resourceType": "wcm/foundation/components/responsivegrid",
                "./jcr:content/root/responsivegrid/jcr:primaryType": "nt:unstructured",
                "./jcr:content/root/responsivegrid/sling:resourceType": "wcm/foundation/components/responsivegrid",
                "./jcr:content/root/responsivegrid/cq:responsive/jcr:primaryType": "nt:unstructured"

            }
        })
    }

    /**
     * Delete a specific page.
     */

    var deletePage = function(deletePageUrl){
        return $.ajax({
            url: deletePageUrl,
            method: "POST",
            data: { ":operation": "delete" }
        })
    }

    window.CQ.CoreComponentsIT.Pages.CreatePage = function (h, $, pageUrl, pageName, pageTitle) {
        return new h.TestCase("Create a page")
            .execFct(function () {
                createPage(pageUrl, pageName, pageTitle);
            })
            .wait(500)
            ;
    }

    window.CQ.CoreComponentsIT.Pages.DeletePage = function (h, $, pageUrl) {
        return new h.TestCase("Delete a page")
            .execFct(function () {
                deletePage(pageUrl);
            })
            .wait(500)
            ;
    }

    window.CQ.CoreComponentsIT.Pages.CreatePages = function (h, $, nrOfPages) {
        var createPagesTestCase = new hobs.TestCase("Create Pages");

        for (var i=0; i< nrOfPages; i++) {
            var pageName = samplePage.name+i;
            var pageTitle = samplePage.title+i;

            pageUrl+="/"+pageName
            createPagesTestCase.execTestCase(window.CQ.CoreComponentsIT.Pages.CreatePage(h, $, pageUrl, pageName, pageTitle));
        }

        return createPagesTestCase;
    }

    window.CQ.CoreComponentsIT.Pages.DeletePages = function (h, $, nrOfPages) {

        var deletePagesTestCase = new hobs.TestCase("Delete Pages");

        for (var i=nrOfPages-1; i>= 0; i--)
        {
            deletePagesTestCase.execTestCase(window.CQ.CoreComponentsIT.Pages.DeletePage(h, $, pageUrl));
            var splitString = "/"+samplePage.name+i
            pageUrl = pageUrl.split(splitString)[0]
        }

        return deletePagesTestCase;
    }

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


    window.CQ.CoreComponentsIT.Breadcrumb.DragDropBreadcrumb = function (h, $) {
        return new h.TestCase("Drag and drop the breadcrumb")
            .navigateTo("/editor.html"+pageUrl+".html")
            .asserts.location("/editor.html"+pageUrl+".html", true)
            .click(".coral-Tab[title='Components']")
            .asserts.visible(".coral-Masonry .card-component", true)
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Core WCM Breadcrumb Component')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            .asserts.visible(".cq-Overlay.cq-draggable.cq-droptarget")
            .wait(500)
        ;
    };

    window.CQ.CoreComponentsIT.Breadcrumb.CheckEditableOptions = function (h, $) {
        return new h.TestCase("Check the editable options")
            .wait(500)
            //click on the component to see the Editable Toolbar
            //.click(".cq-Overlay.cq-draggable.cq-droptarget")
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //check de number of the button from the EditableToolbar
            //.asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".coral-Button.cq-editable-action", 7);})

            //copy the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Copy']")
            //paste the component
            //.click(".cq-Overlay.cq-draggable.cq-droptarget")
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            //check if the Paste button appeared
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".coral-Button.cq-editable-action", 8);})

            //paste a component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})

            .wait(500)
            //.click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            //delete a component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Delete']")
            .wait(500)
            .click(".coral-Button.coral-Button--warning")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 1);})

            //press the Insert Component button
            .wait(500)
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Insert component']")
            .asserts.visible(".coral-Dialog-wrapper .coral-Dialog-content.InsertComponentDialog-components")
            .click(".coral-Dialog.InsertComponentDialog .coral-Dialog-wrapper .coral-Dialog-header .coral-Dialog-closeButton")

            //press the Group button
            //.click(".cq-Overlay.cq-draggable.cq-droptarget")
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
            //.click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .wait(500)
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-draggable.cq-droptarget:eq(1)")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            //cut the component
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Cut']")
            .wait(500)
            //.click(".cq-Overlay.cq-Overlay--component.cq-droptarget.cq-Overlay--placeholder:first")
            .click("#OverlayWrapper")
            .click(".cq-Overlay.cq-Overlay--component.cq-droptarget.cq-Overlay--placeholder:first")
            .asserts.visible("#EditableToolbar")
            .wait(500)
            .click(".coral-Button.coral-Button--quiet.cq-editable-action.coral-Button--square[title='Paste']")
            .asserts.isTrue(function () { return window.CQ.CoreComponentsIT.Utils.checkNumberOfItems(h, ".cq-Overlay.cq-draggable.cq-droptarget", 2);})

        ;
    }

    window.CQ.CoreComponentsIT.Breadcrumb.CheckTheNavigationLevel = function (h, $) {
        return new h.TestCase("Check the navigation level")
            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 4)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 1)})
            //press the Configure button
            .click(".coral-Button.cq-editable-action[title='Configure']")
            .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form")
            //check the value of the navigation level
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","2")})

            //decrement the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Decrement']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","1")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","3")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 3)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","4")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 2)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","5")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 1)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //increment the navigation level
            .click(".coral-Button.coral-Button--secondary.coral-Button--square[title='Increment']")
            .asserts.isTrue(function(){return window.CQ.CoreComponentsIT.Utils.checkInputValue(h,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","6")})
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 1)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the "Hide Current checkbox"
            .click(".coral-Checkbox-input[name='./hideCurrent']")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--active", 0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //check the "Hide Current checkbox"
            .click(".coral-Checkbox-input[name='./showHidden']")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            //check the numbers of the breadcrumb items
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 0)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--activ",0)})

            //Open the Configure window
            .wait(500)
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //change the Navigation Level to 2
            .fillInput(".coral-Textfield.coral-InputGroup-input[id^='coral-id']", "2")
            //click on the check button
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--check")
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item", 4)})
            .asserts.isTrue(function() {return window.CQ.CoreComponentsIT.Utils.checkContentFromIFrame(h,"#ContentFrame",".core-breadcrumb-item--activ",0)})

            //Open the Configure window
            .execTestCase(window.CQ.CoreComponentsIT.OpenConfigureWindow(h, $))
            //Open the full screen
            .click(".coral-Icon.coral-Icon--fullScreen")
            .asserts.visible(".cq-dialog-header", true)
            .wait(500)
            .click(".coral-Icon.coral-Icon--fullScreen")

            //close the configure window
            .wait(500)
            .click(".cq-dialog-actions .coral-Icon.coral-Icon--close")
        ;
    };

    new h.TestSuite("Core-Components Tests - Breadcrumb", {path:"/apps/core/wcm/tests/core-components-it/Breadcrumb.js",
        execBefore: hobs.steps.aem.commons.disableTutorials, register: true})

        .addTestCase(window.CQ.CoreComponentsIT.Pages.CreatePages(h,$,4))
        .addTestCase(window.CQ.CoreComponentsIT.Breadcrumb.DragDropBreadcrumb(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.Breadcrumb.CheckTheNavigationLevel(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.Breadcrumb.CheckEditableOptions(h, $))
        .addTestCase(window.CQ.CoreComponentsIT.Pages.DeletePages(h, $,4))

    ;

})(hobs, jQuery);