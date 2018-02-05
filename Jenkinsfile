#!groovy
@Library(['com.adobe.qe.evergreen.sprout'])
import com.adobe.qe.evergreen.sprout.*
import com.adobe.qe.evergreen.sprout.config.*
import com.adobe.qe.evergreen.sprout.criteria.*
import com.adobe.qe.evergreen.sprout.model.*
import com.adobe.qe.evergreen.sprout.command.*

String MINION_HUB_URL = 'http://qa-bsl-minion-hub.corp.adobe.com:8811'
// String MINION_HUB_URL = 'http://or1010050212014.corp.adobe.com:8811'

TEST_GROUP_1 =  "aem.core-components.testsuite.formhidden," +
                "aem.core-components.testsuite.formoptions," +
                "aem.core-components.testsuite.formcomponents," +
                "aem.core-components.testsuite.breadcrumb," +
                "aem.core-components.testsuite.teaser"

TEST_GROUP_2 =  "aem.core-components.testsuite.formtext," +
                "aem.core-components.testsuite.languagenavigation," +
                "aem.core-components.testsuite.text," +
                "aem.core-components.testsuite.title," +
                "aem.core-components.testsuite.formbutton"

TEST_GROUP_3 =  "aem.core-components.testsuite.image," +
                "aem.core-components.testsuite.list," +
                "aem.core-components.testsuite.formcontainer"

TEST_GROUP_4 =  "aem.core-components.testsuite.navigation," +
                "aem.core-components.testsuite.page," +
                "aem.core-components.testsuite.search"

/* --------------------------------------------------------------------- */
/*                                MODULES V1 + Sandbox                   */
/* --------------------------------------------------------------------- */
Module componentsAll = new Module.Builder('main/all')
        .withArtifact('zip', 'main/all/target/core.wcm.components.all-*.zip', true)
        .build()
Module componentsContent = new Module.Builder('main/content')
        .withRelease()
        .withArtifact('zip', 'main/content/target/core.wcm.components.sandbox.content-*.zip', true)
        .withConsumer(componentsAll)
        .build()
Module componentsCore = new Module.Builder('main/bundles/core')
        .withUnitTests(true)
        .withCoverage(true)
        .withUnitTestCoverageCheck(80.0)
        .withRelease()
        .withArtifact('jar', 'main/bundles/core/target/core.wcm.components.sandbox.bundle-*.jar', true)
        .withConsumer(componentsAll)
        .withConsumer(componentsContent)
        .build()
Module componentsConfig = new Module.Builder('main/config')
        .withRelease()
        .withArtifact('zip', 'main/config/target/core.wcm.components.sandbox.config-*.zip', true)
        .withConsumer(componentsAll)
        .build()
Module componentsItUi = new Module.Builder('main/testing/it/ui-js')
        .withRelease()
        .withArtifact('zip', 'main/testing/it/ui-js/target/core.wcm.components.sandbox.it.ui-js-*.zip', true)
        .build()
Module componentsExtension = new Module.Builder('main/extension')
        .withArtifact('zip', 'main/extension/target/core.wcm.components.extension-*.zip', true)
        .build()
Module componentsExtCFContent = new Module.Builder('main/extension/contentfragment/content')
        .withRelease()
        .withArtifact('zip', 'main/extension/contentfragment/content/target/core.wcm.components.extension.contentfragment.content-*.zip', true)
        .withConsumer(componentsExtension)
        .build()
Module componentsExtCFBundle = new Module.Builder('main/extension/contentfragment/bundle')
        .withUnitTests(true)
        .withCoverage(true)
        .withUnitTestCoverageCommand(new MvnJacocoOfflineCommand())
        .withUnitTestCoverageCheck(80.0)
        .withRelease()
        .withArtifact('jar', 'main/extension/contentfragment/bundle/target/core.wcm.components.extension.contentfragment.bundle-*.jar', true)
        .withConsumer(componentsExtension)
        .withConsumer(componentsExtCFContent)
        .build()
Module componentsJUnitCore = new Module.Builder('main/testing/junit/core')
        .withRelease()
        .withArtifact('jar', 'main/testing/junit/core/target/core.wcm.components.sandbox.junit.core-*.jar', true)
        .withConsumer(componentsCore)
        .withConsumer(componentsExtCFBundle)
        .build()
Module componentsParent = new Module.Builder('main/parent')
        .withRelease()
        .withConsumer(componentsAll)
        .withConsumer(componentsConfig)
        .withConsumer(componentsContent)
        .withConsumer(componentsCore)
        .withConsumer(componentsExtCFBundle)
        .withConsumer(componentsExtCFContent)
        .withConsumer(componentsExtension)
        .withConsumer(componentsItUi)
        .withConsumer(componentsJUnitCore)
        .withBuild()
        .build()

/* --------------------------------------------------------------------- */
/*                        EXTERNAL DEPENDENCIES                          */
/* --------------------------------------------------------------------- */
MavenDependency hobbesRewriterPackage = new MavenDependency.Builder()
        .withGroupId("com.adobe.granite")
        .withArtifactId("com.adobe.granite.testing.hobbes.rewriter")
        .withVersion("latest")
        .withExtension("jar")
        .build()

MavenDependency uiTestingCommonsPackage = new MavenDependency.Builder()
        .withGroupId("com.adobe.qe")
        .withArtifactId("com.adobe.qe.ui-testing-commons")
        .withVersion("latest")
        .withExtension("zip")
        .build()

/* --------------------------------------------------------------------- */
/*                       QUICKSTART CONFIGURATIONS                        */
/* --------------------------------------------------------------------- */
Quickstart quickstart = new BuildQuickstart.Builder('Quickstart 6.4')
        .build()

Quickstart qsWithSampleContentProvisioning = new BuildQuickstart.Builder('Quickstart 6.4')
        .withModule(componentsItUi)
        .withModule(componentsCore)
        .withModule(componentsContent)
        .withModule(componentsConfig)
        .build()

/* --------------------------------------------------------------------- */
/*                      CQ INSTANCE CONFIGURATIONS                        */
/* --------------------------------------------------------------------- */
CQInstance author = new CQInstance.Builder()
        .withQuickstart(quickstart)
        .withId('weretail-author')
        .withPort(1234)
        .withRunmode("author")
        .withRunmode("nosamplecontent")
        .withContextPath("/cp")
        .withMavenDependency(hobbesRewriterPackage)
        .withMavenDependency(uiTestingCommonsPackage)
        .withFileDependency(componentsItUi.getArtifact('zip'))
        .withFileDependency(componentsCore.getArtifact('jar'))
        .withFileDependency(componentsContent.getArtifact('zip'))
        .withFileDependency(componentsConfig.getArtifact('zip'))
        .build()

/* --------------------------------------------------------------------- */
/*                                UI TESTS                               */
/* --------------------------------------------------------------------- */

// Run against Edge
UITestRun coreCompUIEdgePart1 = new UITestRun.Builder()
        .withName('Test Group 1 / Edge')
        .withInstance(author)
        .withBrowser('EDGE')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_1)
        .build()

UITestRun coreCompUIEdgePart2 = new UITestRun.Builder()
        .withName('Test Group 2 / Edge')
        .withInstance(author)
        .withBrowser('EDGE')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_2)
        .build()

UITestRun coreCompUIEdgePart3 = new UITestRun.Builder()
        .withName('Test Group 3 / Edge')
        .withInstance(author)
        .withBrowser('EDGE')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_3)
        .build()

UITestRun coreCompUIEdgePart4 = new UITestRun.Builder()
        .withName('Test Group 4 / Edge')
        .withInstance(author)
        .withBrowser('EDGE')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_4)
        .build()


// Run tests against chrome
UITestRun coreCompUIChromePart1 = new UITestRun.Builder()
        .withName('Test Group 1 / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_1)
        .build()

UITestRun coreCompUIChromePart2 = new UITestRun.Builder()
        .withName('Test Group 2 / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_2)
        .build()

UITestRun coreCompUIChromePart3 = new UITestRun.Builder()
        .withName('Test Group 3 / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_3)
        .build()

UITestRun coreCompUIChromePart4 = new UITestRun.Builder()
        .withName('Test Group 4 / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_4)
        .build()

// Run tests against firefox
UITestRun coreCompUIFirefoxPart1 = new UITestRun.Builder()
        .withName('Test Group 1 / Firefox')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_1)
        .build()

UITestRun coreCompUIFirefoxPart2 = new UITestRun.Builder()
        .withName('Test Group 2 / Firefox')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_2)
        .build()

UITestRun coreCompUIFirefoxPart3 = new UITestRun.Builder()
        .withName('Test Group 3 / Firefox')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_3)
        .build()

UITestRun coreCompUIFirefoxPart4 = new UITestRun.Builder()
        .withName('Test Group 4 / Firefox')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withHobbesHubUrl(MINION_HUB_URL)
        .withRunInstructions('main/jenkinsfiles/UITestRunOptions.json')
        .withWaitForMinionMinutes(10)
        .withFilter(TEST_GROUP_4)
        .build()

/* --------------------------------------------------------------------- */
/*                       SPROUT CONFIGURATION                            */
/* --------------------------------------------------------------------- */
SproutConfig config = new SproutConfig()

// calculate code coverage
config.setComputeCoverage(true)
config.setComputeReleaseCoverage(true)

// only for the PRIVATE_master branch
config.setCoverageCriteria([new Branch(/^PRIVATE_master$/)])
config.setReleaseCoverageCriteria([new Branch(/^PRIVATE_master$/)])

// the prefix for the sonar dashboards
config.setSonarSnapshotPrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-')
config.setSonarReleasePrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-')

// Report Sprout stats to elasticsearch
config.getElasticsearchReporting().setEnable(true)

// the modules to build
config.setModules([componentsCore, componentsContent, componentsConfig, componentsAll, componentsItUi,
                   componentsJUnitCore,componentsParent,
                   componentsExtension, componentsExtCFBundle, componentsExtCFContent])
// the tests to execute
config.setTestRuns([coreCompUIChromePart1,coreCompUIChromePart2,coreCompUIChromePart3,coreCompUIChromePart4,
                    coreCompUIFirefoxPart1,coreCompUIFirefoxPart2,coreCompUIFirefoxPart3,coreCompUIFirefoxPart4,
                    coreCompUIEdgePart1,coreCompUIEdgePart2,coreCompUIEdgePart3,coreCompUIEdgePart4])

// Releases
config.setReleaseCriteria([new Branch(/^PRIVATE_master$/)])
config.setQuickstartPRCriteria([new Branch(/^PRIVATE_master$/)])

// don't ask for release at the end
config.setEnableBuildPromotion(false)
// use parameterized build on this branch when manual triggering to set release info
config.setParameterDefinitionCriteria([ new Branch(/^PRIVATE_master$/)])

config.setGithubAccessTokenId('bf3be1a6-ad0a-43d9-86e2-93b30279060f')
config.setQuickstartPRConfig(qsWithSampleContentProvisioning)

config.setEnableMailNotification(false)

// Don't trigger sprout for release commits
config.setBuildCriteria([new Exclude(
        new AndCriteria()
                .withCriteria(new GitCommitMessage(/^(.*)(@releng|NPR-84|@docs)(.*)$/))
                .withCriteria(new Exclude(new ManuallyTriggered())))])

// Slack notification
config.setEnableSlackNotifications(true)
config.setSlackChannel('#refsquad-sprouts')
config.setSlackTeamDomain('cq-dev')
config.setSlackIntegrationToken('TPhlDoZqT0DvKyVC1RnCvzfj')

/* --------------------------------------------------------------------- */
/*                       SPROUT CUSTOMIZATION                            */
/* --------------------------------------------------------------------- */
Pipeline sprout = new Sprout.Builder()
        .withConfig(config)
        .withJenkins(this).build()

withEnv(['USE_SPROUT_BUILD_PROFILE=true']) {
    sprout.execute()
}
