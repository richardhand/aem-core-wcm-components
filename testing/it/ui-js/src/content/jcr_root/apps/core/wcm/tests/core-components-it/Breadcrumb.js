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

    /**
     * Data for the sample page.
     */
    var samplePage = {
        title: "CoreComponent TestPage",
        template: "/conf/core-components/settings/wcm/templates/core-components",
        primaryType: "cq:PageContent",
        resourceType: "wcm/foundation/components/page",
        name: "page"

    }

    /**
     * Function used for creating a number of pages.
     */
    window.CQ.CoreComponentsIT.CreatePages = function (h, $, nrOfPages) {
        var createPagesTestCase = new hobs.TestCase("Create Pages");

        for (var i=0; i< nrOfPages; i++) {
            var pageName = samplePage.name+i;
            var pageTitle = samplePage.title+i;
            var template = samplePage.template;

            pageUrl+="/"+pageName
            createPagesTestCase.execTestCase(window.CQ.CoreComponentsIT.CreatePage(h, $, pageUrl, pageName, pageTitle, template));
        }

        return createPagesTestCase;
    }

    /**
     * Function used for deleting one page with all her child pages.
     */
    window.CQ.CoreComponentsIT.DeletePages = function (h, $, nrOfPages) {

        return new hobs.TestCase("Delete Pages")
            .execTestCase(window.CQ.CoreComponentsIT.DeletePage(h, $, window.CQ.CoreComponentsIT.pageRoot+"/"+samplePage.name+"0"));
        ;
    }

    /**
     * Drag and Drop a Breadcrumb component.
     */
    window.CQ.CoreComponentsIT.DragDropBreadcrumb = function (h, $) {
        return new h.TestCase("Drag and drop the breadcrumb")
            .execTestCase(window.CQ.CoreComponentsIT.CreatePages(h,$,4))
            .execTestCase(window.CQ.CoreComponentsIT.DragDropComponent(h,$,"Core WCM Breadcrumb Component",pageUrl))
        ;
    };

    /**
     * Check the navigation level for the breadcrumb.
     */
    window.CQ.CoreComponentsIT.CheckTheNavigationLevelTest = function (h, $) {
        return new h.TestCase("Check the navigation level")

            //decrement the navigation level to 1
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Decrement'","1",0,0))
            //increment the navigation level to 2
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Increment'","2",4,1))
            //increment the navigation level to 3
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Increment'","3",3,1))
            //increment the navigation level to 4
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Increment'","4",2,1))
            //increment the navigation level to 5
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Increment'","5",1,1))
            //increment the navigation level to 6
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Button[title='Increment'","6",0,1))
            //check the Hide Current
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Checkbox-input[name='./hideCurrent']","6",0,0))
            //check the Show Hidden
            .execTestCase(window.CQ.CoreComponentsIT.CheckNavigationLevel(h,$,".coral-Checkbox-input[name='./showHidden']","6",0,0))
            //fill the navigation level
            .execTestCase(window.CQ.CoreComponentsIT.FillNavigationLevel(h,$,".coral-Textfield.coral-InputGroup-input[id^='coral-id']","2",4,0))
            //click on the fullscreen button
            .execTestCase(window.CQ.CoreComponentsIT.OpenFullSreen(h,$))
            //close the configure window
            .execTestCase(window.CQ.CoreComponentsIT.CloseConfigureWindow(h,$))
        ;
    };

    new h.TestSuite("Core-Components Tests - Breadcrumb", {path:"/apps/core/wcm/tests/core-components-it/Breadcrumb.js",
        execBefore:window.CQ.CoreComponentsIT.ExecuteBefore(h,$,window.CQ.CoreComponentsIT.DragDropBreadcrumb(h,$)),
        execAfter:window.CQ.CoreComponentsIT.DeletePages(h, $,4), register: true,})
        .addTestCase(window.CQ.CoreComponentsIT.CheckTheNavigationLevelTest(h, $))
    ;

}(hobs, jQuery));