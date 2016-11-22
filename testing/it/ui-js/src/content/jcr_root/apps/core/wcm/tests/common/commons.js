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

    var commons = window.CQ.CoreComponentsIT.commons;

    /**
     * The root below where we will add our test pages
     */
    commons.pageRoot = "/content/core-components/core-components-page";

    /**
     * The page title we use for our test page
     */
    commons.pageName = "testpage";

    /**
     * The test page path we create for each test
     */
    commons.testPagePath = commons.pageRoot + "/" + commons.pageName;

    /**
     * The Template to use.
     */
    commons.template = "/conf/core-components/settings/wcm/templates/core-components";

    /**
     * Helper function to send a post request to add a page
     * NOTE: Todo: the should actually create a randomised page name so that each test has a unique
     * url for the test page. This helps avoiding trouble when aborting a test before it was able
     * to clean up the test page by deleting it. The next run will then reuse the same testpage
     * which can cause trouble.
     *
     * NOTE 2: I use the POST request wizard to create a new page instead of a sling POST request,
     * this way I don't have to create all the nodes my self
     * @param opts passed by the hobbes execFct
     * @param done the function to call when the ajax call has finished
     */

    commons.createTestPage = function(opts, done){
        $.ajax({
            url: "/libs/wcm/core/content/sites/createpagewizard/_jcr_content",
            method: "POST",
            complete: done,
            data: {
                "template": commons.template,
                "parentPath": commons.pageRoot,
                "_charset_":"utf-8",
                "./jcr:title": commons.pageName
            }
        })
    };

    commons.deleteTestPage = function(opts,done){
        $.ajax({
            url: commons.testPagePath,
            method: "POST",
            complete: done,
            data: { ":operation": "delete" }
        })
    };

    /**
     * Helper function to send a post request to add a component
     *
     * @param resourceType components resource type
     * @param done the function to call when the ajax call has finished
     */
    commons.createComponent = function(resourceType,resourceName, done){
        $.ajax({
            url: commons.testPagePath + "/jcr:content/root/responsivegrid/"+resourceName,
            method: "POST",
            complete: done,
            data: {
                "./sling:resourceType":resourceType,
                "parentResourceType":"wcm/foundation/components/responsivegrid",
                ":order":"last",
                "_charset_":"utf-8"
            }
        })
    };

    commons.createComponent = function(resourceType,resourceName, done){
        $.ajax({
            url: commons.testPagePath + "/jcr:content/root/responsivegrid/",
            method: "POST",
            complete: done,
            data: {
                "./sling:resourceType":resourceType,
                "parentResourceType":"wcm/foundation/components/responsivegrid",
                ":order":"last",
                "_charset_":"utf-8",
                ":nameHint": resourceName
            }
        })
    };

    commons.createComponentInForm = function(resourceType,resourceName,parentResourceName, done){
        $.ajax({
            url: commons.testPagePath + "/jcr:content/root/responsivegrid/"+parentResourceName+"/responsivegrid",
            method: "POST",
            complete: done,
            data: {
                "./sling:resourceType":resourceType,
                "parentResourceType":"wcm/foundation/components/responsivegrid",
                ":order":"last",
                "_charset_":"utf-8",
                ":nameHint": resourceName
            }
        })
    };

})(hobs);

