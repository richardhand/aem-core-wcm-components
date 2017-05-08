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

/**
 * Tests for the core page component.
 */
;(function(h, $){

    // shortcut
    var c = window.CQ.CoreComponentsIT.commons;
    var tag1 = "We.Retail : Activity / Biking"
    var tag2 = "We.Retail : Activity / Hiking"
    var pageTitle = "This is the page title"
    var navTitle = "This is the navigation title"
    var subtitle = "This is the page subtitle"
    var description = "This is the page description"
    var onTime =  "May 03, 2017 03:03 pm"
    var offTime = "May 25, 2054 03:04 pm"
    var vanityURL = "test/test-Page-URL"
    var language = "Romanian"
    var design = "/etc/designs/we-retail/images/flags"
    var alias  = "This is an alias"
    var allowedTemplate = "allowedTemplates"
    var loginPage = "/content/core-components/core-components-page"
    var exportConfiguration = "/etc/contentsync/templates/dps-default"
    var contextHubPath = "/etc/cloudsettings/default/contexthub/device"
    var segmentsPath = "/etc/segmentation/contexthub/male"

    /**
     * Test: Check the type used when one type is defined in the policy.
     */
    openPageProperties = new h.TestCase("Open the page property")
        //select the page
            .execFct(function(opts, done){
                c.setPageName(h.param("testPagePath")(opts),"testPageName",done);
            })
            .click('coral-columnview-item:contains("%testPageName%") coral-columnview-item-thumbnail')
            .click("button.cq-siteadmin-admin-actions-properties-activator")

        ;

    /**
     * Before Test Case
     */
    var tcExecuteBeforeTest = new TestCase("Setup Before Test")
        // common set up
        .execTestCase(c.tcExecuteBeforeTest)
        // create the test page, store page path in 'testPagePath'
        .execFct(function (opts,done) {
            c.createPage(c.template, c.rootPage ,'page_' + Date.now(),"testPagePath",done)
        })

        // open the new page in the editor
        .navigateTo("/sites.html%testPagePath%")

        .execTestCase(openPageProperties)
    ;

    /**
     * After Test Case
     */
    var tcExecuteAfterTest = new TestCase("Clean up after Test")
    // common clean up
        .execTestCase(c.tcExecuteAfterTest)
        // delete the test page we created
        .execFct(function (opts, done) {
            c.deletePage(h.param("testPagePath")(opts), done);
        });

    /**
     * Test: Check the Basic Title and Tags options of a page properties.
     */
    var basicTitleAndTagsPageProperties = new h.TestCase("Basic Title and Tags page properties",{
        execBefore: tcExecuteBeforeTest,
        execAfter: tcExecuteAfterTest
    })
            /***** Insert information for 'Title and Tags' *****/

            //open the Basic tab
            .click("coral-tab-label:contains('Basic')")
            //check if the "Basic" option was selected
            .assert.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Basic')").size() == 1
            })

            //check the page title
            .assert.isTrue(function(opts){
                return h.find("input[name='./jcr:title']").val() === h.param("testPageName")(opts)
            })
            //change the page title
            .fillInput("input[name='./jcr:title']","Page")

            //add two tags
            .click("foundation-autocomplete.cq-ui-tagfield button")
            .click("coral-columnview-item-content[title='We.Retail']")
            .click("coral-columnview-item-content[title='Activity']")
            .click("coral-columnview-item:contains('Biking') coral-columnview-item-thumbnail")
            .click("coral-columnview-item:contains('Hiking') coral-columnview-item-thumbnail")
            .click("button.granite-pickerdialog-submit")
            //check if tags were added
            .assert.exist("coral-taglist[name='./cq:tags'] coral-tag:contains('"+tag1+"')")
            .assert.exist("coral-taglist[name='./cq:tags'] coral-tag:contains('"+tag2+"')")

            //detele a tag
            .click("coral-taglist[name='./cq:tags'] coral-tag:contains('"+tag2+"') > button")
            .assert.exist("coral-taglist[name='./cq:tags'] coral-tag:contains('"+tag2+"')", false)

            //set the Hide in Navigation
            .click("input[name='./hideInNav']")

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Basic')")

            //check the page title
            .assert.isTrue(function(opts){
                return h.find("input[name='./jcr:title']").val() === "Page"
            })
            //check if the tags were saved
            .assert.exist("coral-taglist[name='./cq:tags'] coral-tag:contains('"+tag1+"')")

            //check if 'Hide in Navigation' is checked
            .assert.isTrue(function(opts){
                return h.find("coral-checkbox[name='./hideInNav'][checked]")
            })
    ;

    /**
     * Test: Check the Basic More titles and descriptions options of a page properties.
     */
    var basicTitlesAndDescriptionsPageProperties = new h.TestCase("Basic Titles and Descriptions page properties",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            /***** Insert information for 'More Titles and Description' *****/

            //open the Basic tab
            .click("coral-tab-label:contains('Basic')")
            //check if the "Basic" option was selected
            .assert.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Basic')").size() == 1
            })

            .simulate("input[name='./pageTitle']", "key-sequence",
                {sequence: pageTitle})
            .simulate("input[name='./navTitle']", "key-sequence",
                {sequence: navTitle})
            .simulate("input[name='./subtitle']", "key-sequence",
                {sequence: subtitle})
            .simulate("textarea[name='./jcr:description']", "key-sequence",
                {sequence: description})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Basic')")

            //check the saved data
            .assert.isTrue(function(opts){
                return h.find("input[name='./pageTitle']").val() === pageTitle
            })
            .assert.isTrue(function(opts){
                return h.find("input[name='./navTitle']").val() === navTitle
            })
            .assert.isTrue(function(opts){
                return h.find("input[name='./subtitle']").val() === subtitle
            })
            .assert.isTrue(function(opts){
                return h.find("textarea[name='./jcr:description").val() === description
            })
    ;

    /**
     * Test: Check the Basic On/Off time options of a page properties.
     */
    var basicOnOffTimePageProperties = new h.TestCase("Basic On/Off time page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })

            /***** Insert information for On/Off time *****/

            //open the Basic tab
            .click("coral-tab-label:contains('Basic')")
            //check if the "Basic" option was selected
            .assert.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Basic')").size() == 1
            })

            // open calendar for OnTime
            .click("coral-datepicker[name='./onTime'] button:has(coral-icon.coral-Icon--calendar)")
            // choose next month
            .click("coral-datepicker[name='./onTime'] button.coral-Calendar-nextMonth")
            // select first day
            .click("coral-datepicker[name='./onTime'] td a:contains('1'):eq(0)", {delay: 1000})
            // open calendar for OffTime
            .click("coral-datepicker[name='./offTime'] button:has(coral-icon.coral-Icon--calendar)")
            // choose next month
            .click("coral-datepicker[name='./offTime'] button.coral-Calendar-nextMonth")
            // select second day
            .click("coral-datepicker[name='./offTime'] td a:contains('2'):eq(0)", {delay: 1000})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Basic')")

            //check the on time
            .assert.isTrue(function(opts){
                return h.find("input[name='./onTime']").val() !== ""
            })
            //check the off time
            .assert.isTrue(function(opts){
                return h.find("input[name='./offTime']").val() !== ""
            })
    ;

    /**
     * Test: Check the Basic vanity URL options of a page properties.
     */
    var basicVanityUrlPageProperties = new h.TestCase("Basic Vanity URL page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })

            /***** Insert information for 'Vanity URL' *****/

            //open the Basic tab
            .click("coral-tab-label:contains('Basic')")
            //check if the "Basic" option was selected
            .assert.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Basic')").size() == 1
            })

            //add a vanity url
            .click("coral-multifield[data-granite-coral-multifield-name='./sling:vanityPath'] > button")
            .simulate("input[name='./sling:vanityPath']", "key-sequence",
                {sequence: vanityURL})
            //delete a vanity Url
            .click("coral-multifield[data-granite-coral-multifield-name='./sling:vanityPath'] button.coral-Multifield-remove")
            //add again the vanity url
            .click("coral-multifield[data-granite-coral-multifield-name='./sling:vanityPath'] > button")
            .simulate("input[name='./sling:vanityPath']", "key-sequence",
                {sequence: vanityURL})

            //set the Redirect Vanity URL
            .click("input[name='./sling:redirect']")

            /*****  Check if data are saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Basic')")

            //check if the vanity url was saved
            .assert.isTrue(function(opts){
                return h.find("input[name='./sling:vanityPath']").val() === vanityURL
            })
            //check if 'Redirect Vanity URL' is checked
            .assert.isTrue(function(opts){
                return h.find("coral-checkbox[name='./sling:redirect'][checked]")
            })
    ;

    /**
     * Test: Check the Advanced options of a page properties.
     */
    var advancedSettingsPageProperties = new h.TestCase("Advanced Settings page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            /***** Insert information for 'Settings' *****/

            //open the Advanced tab
            .click("coral-tab-label:contains('Advanced')")
            //check if the "Advanced" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Advanced')").size() == 1
            })

            //test the Settings options

            //set the language
            .click("coral-select[name='./jcr:language'] > button")
            .click("coral-select[name='./jcr:language'] coral-selectlist-item:contains('"+language+"')")
            //set the desigh path
            .fillInput("foundation-autocomplete[name='./cq:designPath'] input.coral-Textfield", design)
            //set the alias
            .simulate("input[name='./sling:alias']", "key-sequence",
                {sequence: alias})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Advanced')")

            //check the language
            .assert.isTrue(function(opts){
                return h.find("coral-select[name='./jcr:language'] span:contains('"+language+"')")
            })
            //check the design
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:designPath']").val() === design
            })
            //check the alias
            .assert.isTrue(function(opts){
                return h.find("input[name='./sling:alias']").val() === alias
            })
    ;

    var advancedTemplatesSettingsPageProperties = new h.TestCase("Advanced Templates page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            /***** Insert information for 'Settings' *****/

            //open the Advanced tab
            .click("coral-tab-label:contains('Advanced')")
            //check if the "Advanced" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Advanced')").size() == 1
            })

            //test the template settings
            .click("coral-multifield[data-granite-coral-multifield-name='./cq:allowedTemplates'] > button")
            .simulate("input[name='./cq:allowedTemplates']", "key-sequence",
                {sequence: allowedTemplate})
            //detele the allowed template
            .click("coral-multifield[data-granite-coral-multifield-name='./cq:allowedTemplates'] button.coral-Multifield-remove")
            //add again the allowed template
            .click("coral-multifield[data-granite-coral-multifield-name='./cq:allowedTemplates'] > button")
            .simulate("input[name='./cq:allowedTemplates']", "key-sequence",
                {sequence: allowedTemplate})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Advanced')")

            //check the saved template
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:allowedTemplates']").val() === allowedTemplate
            })
    ;

    var advancedAuthenticationPageProperties = new h.TestCase("Advanced Authentication page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            /***** Insert information for 'Settings' *****/

            //open the Advanced tab
            .click("coral-tab-label:contains('Advanced')")
            //check if the "Advanced" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Advanced')").size() == 1
            })

            //test the authenticatiion requirement
            .click("input[name='./cq:authenticationRequired']")
            .simulate("foundation-autocomplete[name='./cq:loginPath'] input.coral-Textfield", "key-sequence",
                {sequence: loginPage+"{enter}"})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Advanced')")

            //check the Enable check
            .assert.isTrue(function(opts){
                return h.find("coral-checkbox[name='./cq:authenticationRequired'] checked")
            })
            //check the login page
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:loginPath']").val() === loginPage
            })
    ;

    var advancedExportPageProperties = new h.TestCase("Advanced Export page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })

            /***** Insert information for 'Settings' *****/

            //open the Advanced tab
            .click("coral-tab-label:contains('Advanced')")
            //check if the "Advanced" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Advanced')").size() == 1
            })

            //tests for the export options
            .simulate("foundation-autocomplete[name='./cq:exportTemplate'] input.coral-Textfield", "key-sequence",
                {sequence: exportConfiguration+"{enter}"})

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Advanced')")

            //check the Export Configuration
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:exportTemplate']").val() === exportConfiguration
            })
        ;

    /**
     * Test: Check the Thumbnail options of a page properties.
     */
    var thumbnailPageProperties = new h.TestCase("Thumbnail page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            .click("coral-tab-label:contains('Thumbnail')")
            //check if the "Thumbnail" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Thumbnail')").size() == 1
            })

            .click("button:contains('Generate Preview')",{delay: 500})
            .click("button:contains('Revert')")
        ;

    /**
     * Test: Check the Social Media options of a page properties.
     */
    var socialMediaPageProperties = new h.TestCase("Social Media page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            .click("coral-tab-label:contains('Social Media')")
            //check if the "Social Media" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Social Media')").size() == 1
            })

            //test social media sharing
            .click("input[name='./socialMedia'][value='facebook']")
            .click("input[name='./socialMedia'][value='pinterest']")
            .click("foundation-autocomplete[name='./variantPath'] button[title='Open Selection Dialog']")
            .click("form.granite-pickerdialog-content button:contains('Cancel')")

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Social Media')")

            //check if facebook is checked
            .assert.isTrue(function(opts){
                return h.find("coral-checkbox[name='./socialMedia'][value='facebook'] checked")
            })
            //check if pinterest is checked
            .assert.isTrue(function(opts){
                return h.find("coral-checkbox[name='./socialMedia'][value='pinterest'] checked")
            })
        ;

    /**
     * Test: Check the Cloud Services options of a page properties.
     */
    var cloudServicesPageProperties = new h.TestCase("Cloud Services page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            .click("coral-tab-label:contains('Cloud Services')")
            //check if the "Cloud Services" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Cloud Services')").size() == 1
            })

            .click("span:contains('Add Configuration')")
            .click("coral-selectlist span:contains('Facebook Connect')")
            .click("span:contains('Add Configuration')")
            .click("coral-selectlist span:contains('Twitter Connect')")
            //detele the Twitter Connect
            .click("button[data-title='Twitter Connect']")

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Cloud Services')")

            .asserts.isTrue(function() {
                return h.find("coral-select[name='./cq:cloudserviceconfigs'] span:contains('We.Retail Facebook Social Login')").size() == 1
            })

    ;

    /**
     * Test: Check the Personalization options of a page properties.
     */
    var personalizationPageProperties = new h.TestCase("Personalization page property",{
            execBefore: tcExecuteBeforeTest,
            execAfter: tcExecuteAfterTest
        })
            .click("coral-tab-label:contains('Personalization')",{delay:1000})
            //check if the "Personalization" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Personalization')").size() == 1
            })
            //set the contextHub path
            .fillInput("foundation-autocomplete[name='./cq:contextHubPath'] input.coral-Textfield", contextHubPath)
            //set the segments path
            .fillInput("foundation-autocomplete[name='./cq:contextHubSegmentsPath'] input.coral-Textfield", segmentsPath)
            //add a brand
            .click("button:contains('Add Brand')")
            .click(".groupedServices-ServiceSelector-service-title")

            /*****  Check if the date is saved *****/

            //save the configuration and open again the page property
            .click("coral-buttongroup button:contains('Save & Close')")
            .execTestCase(openPageProperties)
            .click("coral-tab-label:contains('Personalization')",{delay:1000})

            //check the contextHub path
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:contextHubPath']").val() === contextHubPath
            })
            //check the segments path
            .assert.isTrue(function(opts){
                return h.find("input[name='./cq:contextHubSegmentsPath']").val() === segmentsPath
            })
            //check the brand
            .assert.exist("section.coral-Form-fieldset h4:contains('We.Retail')")
    ;

    /**
     * Test: Check the Permissions options of a page properties.
     */
    var permissionsPageProperties = new h.TestCase("Permissions page property",{
            execBefore: tcExecuteBeforeTest//,
            //execAfter: tcExecuteAfterTest
        })
            .click("coral-tab-label:contains('Permissions')",{delay:1000})
            //check if the "Permissions" option was selected
            .asserts.isTrue(function() {
                return h.find("coral-tab.is-selected coral-tab-label:contains('Permissions')").size() == 1
            })

            .click("button:contains('Add Permissions')")
            //.fillInput("foundation-autocomplete.js-cq-sites-CreatePermissionsDialog-authorizableList input","admin")
            .simulate("foundation-autocomplete.js-cq-sites-CreatePermissionsDialog-authorizableList input", "key-sequence",
                {sequence: "administrators"+"{enter}"})
            .click(".coral-Dialog-wrapper:contains('Add Permissions') input[name='read']")
            .click(".coral-Dialog-wrapper:contains('Add Permissions') input[name='create']")
            .click(".coral-Dialog-wrapper:contains('Add Permissions') input[name='delete']")
            .click(".coral-Dialog-wrapper:contains('Add Permissions') button:contains('Add')")

            .click("button:contains('Edit Closed User Group')")
            .simulate("foundation-autocomplete.js-cq-sites-CUGPermissionsDialog-authorizableList input", "key-sequence",
                {sequence: "administrators{enter}"})
            //.simulate("foundation-autocomplete.js-cq-sites-CUGPermissionsDialog-authorizableList input", "keydown",
                //{keyCode: 13})
            .wait("1000")
            .click(".coral-Dialog-wrapper:contains('Edit Closed') button[title='Remove']")
            .simulate("foundation-autocomplete.js-cq-sites-CUGPermissionsDialog-authorizableList input", "key-sequence",
                {sequence: "administrators{enter}"})
            //.simulate("foundation-autocomplete.js-cq-sites-CUGPermissionsDialog-authorizableList input", "keydown",
                //{keyCode: 13})
            .click(".coral-Dialog-wrapper:contains('Edit Closed') button:contains('Save')")

            .click("button:contains('Effective Permissions')")
            .click(".coral-Dialog-wrapper:contains('Effective Permissions') button[title='Close']")
        ;

    /**
     * The main test suite for Page component
     */
    new h.TestSuite("Core Components - Page", {path:"/apps/core/wcm/tests/core-components-it/Page.js",
        execBefore:c.tcExecuteBeforeTestSuite,
        execInNewWindow : false})

        .addTestCase(basicTitleAndTagsPageProperties)
        .addTestCase(basicTitlesAndDescriptionsPageProperties)
        .addTestCase(basicOnOffTimePageProperties)
        .addTestCase(basicVanityUrlPageProperties)
        .addTestCase(advancedSettingsPageProperties)
        .addTestCase(advancedTemplatesSettingsPageProperties)
        .addTestCase(advancedAuthenticationPageProperties)
        .addTestCase(advancedExportPageProperties)
        .addTestCase(thumbnailPageProperties)
        .addTestCase(socialMediaPageProperties)
        .addTestCase(cloudServicesPageProperties)
        .addTestCase(personalizationPageProperties)
        .addTestCase(permissionsPageProperties)
    ;

}(hobs, jQuery));

