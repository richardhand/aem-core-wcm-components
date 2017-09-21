#!groovy
@Library(['com.adobe.qe.evergreen.sprout'])
import com.adobe.qe.evergreen.sprout.Sprout
import com.adobe.qe.evergreen.sprout.Pipeline
import com.adobe.qe.evergreen.sprout.SproutConfig
import com.adobe.qe.evergreen.sprout.criteria.Branch
import com.adobe.qe.evergreen.sprout.criteria.Exclude
import com.adobe.qe.evergreen.sprout.criteria.GitCommitMessage
import com.adobe.qe.evergreen.sprout.criteria.OrCriteria
import com.adobe.qe.evergreen.sprout.model.BuildQuickstart
import com.adobe.qe.evergreen.sprout.model.CQInstance
import com.adobe.qe.evergreen.sprout.model.MavenDependency
import com.adobe.qe.evergreen.sprout.model.Module
import com.adobe.qe.evergreen.sprout.model.Quickstart
import com.adobe.qe.evergreen.sprout.model.UITestRun
import com.adobe.qe.evergreen.sprout.command.*

String MINION_HUB_URL = 'http://or1010050212014.corp.adobe.com:8811'

String GENERAL_METADATA_RUN_OPTIONS =
        '\\\"ignoreOn64\\\":{\\\"value\\\":true,\\\"type\\\":\\\"exclude\\\"}' +
        ',' +
        '\\\"flaky\\\":{\\\"value\\\":true,\\\"type\\\":\\\"exclude\\\"}' +
        ',' +
        '\\\"failing\\\":{\\\"value\\\":true,\\\"type\\\":\\\"exclude\\\"}'

String UI_TEST_OPTIONS = '{\\\"withMetadata\\\":{' + GENERAL_METADATA_RUN_OPTIONS + '}}'

String NUM_OF_RETRIES = '{\\\"global_maxretries_on_failed\\\":1}'

/* --------------------------------------------------------------------- */
/*                                MODULES V1 + Sandbox                   */
/* --------------------------------------------------------------------- */
Module componentsCore = new Module.Builder('main/bundles/core')
        .withUnitTests(true)
        .withCoverage(true)
        .withRelease()
        .withUnitTestCoverageCommand(new ShellCommand("mvn verify"))
        .withArtifact('jar', 'main/bundles/core/target/core.wcm.components.sandbox.bundle-*.jar', true)
        .build()
Module componentsContent = new Module.Builder('main/content')
        .withRelease()
        .withArtifact('zip', 'main/content/target/core.wcm.components.sandbox.content-*.zip', true)
        .build()
Module componentsConfig = new Module.Builder('main/config')
        .withRelease()
        .withArtifact('zip', 'main/config/target/core.wcm.components.sandbox.config-*.zip', true)
        .build()
Module componentsItUi = new Module.Builder('main/testing/it/ui-js')
        .withRelease()
        .withArtifact('zip', 'main/testing/it/ui-js/target/core.wcm.components.sandbox.it.ui-js-*.zip', true)
        .build()
Module componentsAll = new Module.Builder('main/all')
        .withArtifact('zip', 'main/all/target/core.wcm.components.all-*.zip', true)
        .build()
Module componentsJUnitCore = new Module.Builder('main/testing/junit/core')
        .withRelease()
        .withArtifact('jar', 'main/testing/junit/core/target/core.wcm.components.junit.core-*.jar', true)
        .build()
Module componentsParent = new Module.Builder('main/parent')
        .withRelease()
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
        .withContextPath("/cp")
        .withMavenDependency(hobbesRewriterPackage)
        .withMavenDependency(uiTestingCommonsPackage)
        .withFileDependency(componentsItUi.getArtifact('zip'))
        .build()

/* --------------------------------------------------------------------- */
/*                                UI TESTS                               */
/* --------------------------------------------------------------------- */

// Run tests against chrome
UITestRun coreCompUIChromePart1 = new UITestRun.Builder()
        .withName('UI Tests: Breadcrumb, FormButton, FormContainer, Title / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.testsuite.breadcrumb,aem.core-components.testsuite.formbutton,' +
        'aem.core-components.testsuite.formcontainer,aem.core-components.testsuite.title')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIChromePart2 = new UITestRun.Builder()
        .withName('UI Tests: FormHidden, FormOptions, FormComponents / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.testsuite.formhidden,aem.core-components.testsuite.formoptions,' +
        'aem.core-components.testsuite.formcomponents')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIChromePart3 = new UITestRun.Builder()
        .withName('UI Tests: FormText, LanguageNavigation, Text / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.testsuite.formtext,aem.core-components.testsuite.languagenavigation,' +
        'aem.core-components.testsuite.text')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIChromePart4 = new UITestRun.Builder()
        .withName('UI Tests: Image, List / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.testsuite.image,aem.core-components.testsuite.list')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIChromePart5 = new UITestRun.Builder()
        .withName('UI Tests: Navigation, Page, Search / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.testsuite.navigation,aem.core-components.testsuite.page,' +
        'aem.core-components.testsuite.search')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

// Run tests against firefox
UITestRun coreCompUIFirefoxPart1 = new UITestRun.Builder()
        .withName('UI Tests: Breadcrumb, FormButton, FormContainer, Title / Chrome')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withFilter('aem.core-components.testsuite.breadcrumb,aem.core-components.testsuite.formbutton,' +
        'aem.core-components.testsuite.formcontainer,aem.core-components.testsuite.title')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIFirefoxPart2 = new UITestRun.Builder()
        .withName('UI Tests: FormHidden, FormOptions, FormComponents / Chrome')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withFilter('aem.core-components.testsuite.formhidden,aem.core-components.testsuite.formoptions,' +
        'aem.core-components.testsuite.formcomponents')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIFirefoxPart3 = new UITestRun.Builder()
        .withName('UI Tests: FormText, LanguageNavigation, Text / Chrome')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withFilter('aem.core-components.testsuite.formtext,aem.core-components.testsuite.languagenavigation,' +
        'aem.core-components.testsuite.text')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIFirefoxPart4 = new UITestRun.Builder()
        .withName('UI Tests: Image, List / Chrome')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withFilter('aem.core-components.testsuite.image,aem.core-components.testsuite.list')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
        .build()

UITestRun coreCompUIFirefoxPart5 = new UITestRun.Builder()
        .withName('UI Tests: Navigation, Page, Search / Chrome')
        .withInstance(author)
        .withBrowser('FIREFOX')
        .withFilter('aem.core-components.testsuite.navigation,aem.core-components.testsuite.page,' +
        'aem.core-components.testsuite.search')
        .withRunOptions(UI_TEST_OPTIONS)
        .withHobbesHubUrl(MINION_HUB_URL)
        .withHobbesConfig(NUM_OF_RETRIES)
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
                    componentsJUnitCore,componentsParent])
// the tests to execute
config.setTestRuns([coreCompUIChromePart1,coreCompUIChromePart2,coreCompUIChromePart3,coreCompUIChromePart4,
                    coreCompUIChromePart5,coreCompUIFirefoxPart1,coreCompUIFirefoxPart2,coreCompUIFirefoxPart3,
                    coreCompUIFirefoxPart4,coreCompUIFirefoxPart5])

// Releases
config.setReleaseCriteria([new Branch(/^PRIVATE_master$/)])
config.setQuickstartPRCriteria([new Branch(/^PRIVATE_master$/)])

// don't ask for release at the end
config.setEnableBuildPromotion(false)
// use parameterized build on this branch when manual triggering to set release info
config.setParameterDefinitionCriteria([ new Branch(/^PRIVATE_master$/)])

config.setGithubAccessTokenId('bf3be1a6-ad0a-43d9-86e2-93b30279060f')
config.setQuickstartPRConfig(quickstart)

config.setEnableMailNotification(false)

// Don't trigger sprout for release commits
config.setBuildCriteria([new Exclude(new GitCommitMessage(/^(.*)@releng \[maven\-scm\] :prepare(.*)$/))])

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

sprout.execute()
