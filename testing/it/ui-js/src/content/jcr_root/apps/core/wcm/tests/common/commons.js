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

    // hobs.config.pacing_delay = 150;

    // shortcut
    var c = window.CQ.CoreComponentsIT.commons;

    // the root page defined in the test content package
    c.rootPage = "/content/core-components/core-components-page";
    // the template defined in the test content package
    c.template = "/conf/core-components/settings/wcm/templates/core-components";
    // relative path from page node to the root layout container
    c.relParentCompPath = "/jcr:content/root/responsivegrid/";

    // core component resource types
    // text component
    c.rtText  =  "core/wcm/components/text";
    // text component
    c.rtTitle  =  "core/wcm/components/title";
    // breadcrumb component
    c.rtBreadcrumb = "core/wcm/components/breadcrumb";
    // form container
    c.rtFormContainer = "core/wcm/components/form/container";
    // form button
    c.rtFormButton = "core/wcm/components/form/button";
    // form button
    c.rtFormText = "core/wcm/components/form/text";

    /**
     * Creates a CQ page via POST request, the same as send by the create page wizard.
     *
     * @param templatePath Mandatory. Path to the template e.g. "/conf/we-retail/settings/wcm/templates/content-page"
     * @param parentPath Mandatory. Path to the parent page e.g. "/content/we-retail/language-masters/en"
     * @param pageName Mandatory. Page name to be set for the page.
     * @param dynParName Optional. Hobbes dynamic param to store the generated page path.
     * @param done Optional. Callback to be executed when async method has finished.
     */
    c.createPage = function (templatePath, parentPath, pageName, dynParName, done) {
        // mandatory check
        if (parentPath == null || templatePath == null || pageName == null){
            console.log("createPage failed! mandatory parameter(s) missing!");
            done(); return;
        }

        // the ajax call
        jQuery.ajax({
            url: "/libs/wcm/core/content/sites/createpagewizard/_jcr_content",
            method: "POST",
            // POST data to be send in the request
            data: {
                "template": templatePath,
                "parentPath": parentPath,
                "_charset_": "utf-8",
                "./jcr:title": pageName,
                "pageName": pageName
            }
        })
            // when the request was successful
            .done(function(data,textStatus,jqXHR) {
                // extract the created page path from the returned HTML
                var path = jQuery(data).find("#Path").text();
                // get the page name
                var name = path.substring(path.lastIndexOf("/")+1,path.length);
                // if the page already existed it will stupidly postfix it with a number this can lead to problems
                // so at least we should log a warning
                if (pageName != name ){
                    console.log("createPage failed! page was created with different name!");
                }
                // store the page path and name as dynamic data for reuse in hobs functions
                if (dynParName != null) {
                    hobs.param(dynParName,path);
                }
            })
            // request fails
            .fail(function (jqXHR,textStatus,errorThrown){
                // log an error
                console.log("createPage failed! POST failed with: " + textStatus + "," + errorThrown);
            })
            // always executed, fail or success
            .then(function(){
                if (done != null) done();
            })

    };

    /**
     * Deletes a page.
     *
     * @param pagePath Mandatory. testPagePath path to the page to be deleted
     * @param done Optional. callback to be executed when the async method has finished.
     */
    c.deletePage = function(pagePath,done){
        // mandatory check
        if (pagePath == null ){
            console.log("deletePage failed! mandatory parameter(s) missing!");
            done(); return;
        }
        jQuery.ajax({
            url: pagePath,
            method: "POST",
            data: {
                ":operation": "delete"
            }
        })
            .fail(function(jqXHR,textStatus,errorThrown){
                console.log("deletePage failed: POST failed with " + textStatus + "," + errorThrown);
            })
            // always executed, fail or success
            .then(function(){
                if (done != null) {done();}
            })
    };

    /**
     * Adds a component to a page.
     *
     * @param component          mandatory components resource type
     * @param parentCompPath     mandatory absolute path to the parent component
     * @param dynParName         optional name of hobbes param to store the new components absolute path
     * @param done               optional call back to execute when async method has finished
     * @param nameHint           optional hint for the component nodes name, if empty component name is taken
     * @param order              optional where to place component e.g. 'before product_grid', if empty, 'last' is used
     */
    c.addComponent = function(component, parentCompPath,dynParName, done, nameHint , order){
        // mandatory check
        if (component == null || parentCompPath == null ){
            console.log("addComponent failed! mandatory parameter(s) missing!");
            done(); return;
        }

        // default settings
        if (nameHint == null) nameHint = component.substring(component.lastIndexOf("/")+1);
        if (order == null) order = "last";

        // the ajax call
        jQuery.ajax({
            url: parentCompPath,
            method: "POST",
            data: {
                "./sling:resourceType":component,
                ":order":order,
                "_charset_":"utf-8",
                ":nameHint": nameHint

            }
        })
            .done(function(data,textStatus,jqXHR) {
                // extract the component path from the returned HTML
                if(dynParName != null){
                    h.param(dynParName,jQuery(data).find("#Path").text());
                }
            })
            // in case of failure
            .fail(function(jqXHR,textStatus,errorThrown){
                console.log("addComponent failed: POST failed with " + textStatus + "," + errorThrown);
            })
            // always executed, fail or success
            .then(function(){
                if (done != null) {done();}
            })
    };

    /**
     * Sets properties of a repository node.
     *
     * @param componentPath     Mandatory. absolute path to the node
     * @param data              Mandatory. object with properties to be set on the node.
     * @param done              Optional. callback function when post has returned
     */
    c.editNodeProperties = function (componentPath,data,done){
        // check mandatory
        if ( componentPath == null || data == null ){
            console.log("editNodeProperties failed! Mandatory param(s) missing.");
            if(done != null) done(); return;
        }
        $.ajax({
            url: componentPath,
            method: "POST",
            // POST data to be send in the request
            data: data
        })
            // in case of failure
            .fail(function(jqXHR,textStatus,errorThrown){
                console.log("editNodeProperties failed: POST failed with " + textStatus + "," + errorThrown);
            })
            .then(function(){
                if(done != null) done();
            })
    };

    /**
     * returns the content frame.
     */
    c.getContentFrame = function () {
        return h.find('iframe#ContentFrame').get(0);
    };

    /**
     * Requests the JSON output and stores it in a hobbes dynamic parameter.
     * Does several retries and works async. so callback method 'done' should be set to avoid problems.
     *
     * @param url           mandatory. the JSON Url to request.
     * @param dynParName    mandatory. the hobbes param to store the JSON object in.
     * @param done          mandatory. the callback to execute when finished
     * @param maxRetries    optional. number of retries, default 10
     * @param timeout       optional. timout in milliseconds between retries, default 500
     */
    c.getJSON = function(url,dynParName,done,maxRetries,timeout){
        // check mandatory
        if (url == null || dynParName == null || done == null){
            console.log("getJSON failed! Mandatory param(s) missing.")
            done(); return;
        }

        // check defaults
        if(maxRetries == null) maxRetries = 10;
        if(timeout == null) timeout = 500;

        // retry counter
        var retries = 0;

        // the polling function
        var poll = function(){
            $.ajax({
                url: url,
                method: "GET",
                dataType: "json"
            })
                .done(function(data){
                    h.param(dynParName,data);
                    done();
                })
                .fail(function(jqXHR,textStatus,errorThrown){
                    // check if max retries was reached
                    if (retries++ === maxRetries){
                        console.log("getJSON failed! GET failed with " + textStatus + "," + errorThrown);
                        done();
                        return;
                    }
                    // set for next retry
                    setTimeout(poll,timeout);
                })
        };
        // start polling
        poll();
    };

    /**
     * Opens the configuration dialog for a component. Uses 'data-path' attribute to identify the correct
     * component.
     *
     * @param cmpPath   mandatory. the absolute component path, used as the value for 'data-path' attribue
     */
    c.tcOpenConfigureDialog = function(cmpPath) {
        return new TestCase("Open Configure Dialog")
            //click on the component to see the Editable Toolbar
            .click(".cq-Overlay.cq-draggable.cq-droptarget%dataPath%",{
                before: function() {
                    // set the data-path attribute so we target the correct component
                    h.param("dataPath", "[data-path='" + h.param(cmpPath)() + "']");
                }
            })

            // make sure its visible
            .asserts.visible("#EditableToolbar")
            // click on the 'configure' button
            .click(".coral-Button.cq-editable-action[title='Configure']")
            // verify the dialog has become visible
            .asserts.visible(".cq-dialog.foundation-form.foundation-layout-form");
    };


    /**
     * Closes any open configuration dialog
     */
    c.tcSaveConfigureDialog = new TestCase ("Save Configure Dialog")
        // if full Screen mode was used make sure the click waits for the navigation change
        .ifElse(
        // check if the dialog opened in a different URL
        function(){ return hobs.context().window.location.pathname.startsWith("/mnt/override")}
        ,
        TestCase("Close Fullscreen Dialog")
            .click(".cq-dialog-actions .coral-Button[title='Done']",{expectNav:true})
        ,
        TestCase("Close Modal Dialog")
            .click(".cq-dialog-actions .coral-Button[title='Done']",{expectNav:false})
        ,{ timeout:10 });


    /**
     * Opens the inline editor for a component. Uses 'data-path' attribute to identify the correct
     * component.
     *
     * @param cmpPath   mandatory. the absolute component path, used as the value for 'data-path' attribue
     */
    c.tcOpenInlineEditor = function(cmpPath) {
        return new TestCase("Open Inline Editor")
            //click on the component to see the Editable Toolbar
            .click(".cq-Overlay.cq-draggable.cq-droptarget%dataPath%", {
                before: function () {
                    // set the data-path attribute so we target the correct component
                    h.param("dataPath", "[data-path='" + h.param(cmpPath)() + "']");
                }
            })

            // check if its there
            .asserts.visible("#EditableToolbar")
            // click on the 'Edit' button
            .click(".coral-Button.cq-editable-action[title='Edit']")
            // go to the content frame
            .config.changeContext(c.getContentFrame)
            // wait for editable attribute to appear
            .assert.exist("[contenteditable='true']")
            // go back to previous context
            .config.resetContext();
    };

    /**
     * Closes any previously opened inline editor by clicking on the save button
     */
    c.tcSaveInlineEditor = new TestCase("Save Inline Editor")
        //click on the component to see the Editable Toolbar
        .click(".coral-Button[title='Save']");

    /**
     * Common stuff that should be done before each test case starts.
     */
    c.tcExecuteBeforeTest = new TestCase("Common Set up")
        // reset the context
        .config.resetContext();
    // TODO: Make sure we start in edit mode on the page
    // TODO: Start with hidden side panel

    /**
     * Common stuff that should be done at the end of each test case.
     */
    c.tcExecuteAfterTest = new TestCase("Common Clean Up")
        // reset the context
        .config.resetContext();

    /**
     * Stuff that should be done before a testsuite starts
     */
    c.tcExecuteBeforeTestSuite =  new TestCase("Setup Before Testsuite")
        // disable annoying tutorial popups
        .execTestCase(hobs.steps.aem.commons.disableTutorials)


}(hobs, jQuery));