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

;(function(h,$){

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
    };

    window.CQ.CoreComponentsIT.CreatePage = function (h, $, pageUrl, pageName, pageTitle) {
        return new h.TestCase("Create a page")
            .execFct(function () {
                createPage(pageUrl, pageName, pageTitle);
            })
            .wait(500)
        ;
    }

    window.CQ.CoreComponentsIT.DeletePage = function (h, $, pageUrl) {
        return new h.TestCase("Delete a page")
            .execFct(function () {
                deletePage(pageUrl);
            })
            .wait(500)
        ;
    }

    window.CQ.CoreComponentsIT.CreatePages = function (h, $, nrOfPages) {
        var createPagesTestCase = new hobs.TestCase("Create Pages");

        //var pageUrl = page.pageRoot;

        for (var i=0; i< nrOfPages; i++) {
            var pageName = samplePage.name+i;
            var pageTitle = samplePage.title+i;

            pageUrl+="/"+pageName
            createPagesTestCase.execTestCase(window.CQ.CoreComponentsIT.CreatePage(h, $, pageUrl, pageName, pageTitle));
        }

        return createPagesTestCase;
    }

    window.CQ.CoreComponentsIT.DeletePages = function (h, $, nrOfPages) {

        var deletePagesTestCase = new hobs.TestCase("Delete Pages");

        //var pageUrl = page.pageRoot;

        //for (var i=0; i<nrOfPages; i++)
        //{
        //var pageName = samplePage.name+i;
        //pageUrl+="/"+pageName
        //}


        /** this method is doesn't work. It is deleting only the last page. I need to make it to delete also the */
        for (var i=nrOfPages-1; i>= 0; i--)
        {
            deletePagesTestCase.execTestCase(window.CQ.CoreComponentsIT.DeletePage(h, $, pageUrl));
            pageUrl=pageUrl.split(samplePage.name+i)[0]
        }

        return deletePagesTestCase;
    }

    window.CQ.CoreComponentsIT.TestBreadcrumb = function (h, $) {
        return new h.TestCase("Navigate to Edit page")
            .navigateTo("/editor.html"+pageUrl+".html")
            .asserts.location("/editor.html"+pageUrl+".html", true)
            .click(".coral-Tab[title='Components']")
            .asserts.visible(".coral-Masonry .card-component", true)
            .cui.dragdrop(
                ".coral-Masonry-item :contains('Core WCM Breadcrumb Component')",
                ".cq-Overlay .cq-droptarget",
                {delayBefore: 2500}
            )
            //.assert.visible("#EditableToolbar", true)
            .assert.visible(".cq-Overlay.cq-draggable.cq-droptarget")
            .click(".cq-Overlay.cq-draggable.cq-droptarget")
            .click(".coral-Button.cq-editable-action[title='Configure']")
        ;
    }

    new h.TestSuite("Core-Components Tests - Breadcrumb", {path:"/apps/core/wcm/tests/core-components-it/Breadcrumb.js", execBefore: hobs.steps.aem.commons.disableTutorials, register: true})
        .addTestCase(window.CQ.CoreComponentsIT.CreatePages(h,$,4))
        .addTestCase(window.CQ.CoreComponentsIT.TestBreadcrumb(h, $))
        //.addTestCase(window.CQ.CoreComponentsIT.DeletePages(h, $,4))

    ;

})(hobs, jQuery);