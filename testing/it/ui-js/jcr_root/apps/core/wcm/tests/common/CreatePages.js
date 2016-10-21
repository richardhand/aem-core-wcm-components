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
     * Create a page with a specific page name and title.
     */
    var createPage = function(pageUrl, pageName, title, template){
        return $.ajax({
            url: pageUrl,
            method: "POST",
            data: {
                ":name":pageName,
                "./jcr:primaryType": "cq:Page",
                "./jcr:content/jcr:primaryType": "cq:PageContent",
                "./jcr:content/jcr:title": title,
                "./jcr:content/cq:template": template,
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

    window.CQ.CoreComponentsIT.CreatePage = function (h, $, pageUrl, pageName, pageTitle, pageTemplate) {
        return new h.TestCase("Create a page")
            .execFct(function (opts, done) {
                createPage(pageUrl, pageName, pageTitle, pageTemplate).then(done);
            })
        ;
    }

    window.CQ.CoreComponentsIT.DeletePage = function (h, $, pageUrl) {
        return new h.TestCase("Delete a page")
            .execFct(function (opts, done) {
                deletePage(pageUrl).then(done);
            })
        ;
    }

})(hobs);
