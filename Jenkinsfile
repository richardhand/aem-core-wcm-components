#!groovy
@Library(['com.adobe.qe.evergreen.sprout'])
import com.adobe.qe.evergreen.sprout.Sprout
import com.adobe.qe.evergreen.sprout.Pipeline
import com.adobe.qe.evergreen.sprout.SproutConfig
import com.adobe.qe.evergreen.sprout.criteria.Branch
import com.adobe.qe.evergreen.sprout.criteria.Exclude
import com.adobe.qe.evergreen.sprout.criteria.GitCommitMessage
import com.adobe.qe.evergreen.sprout.model.BuildQuickstart
import com.adobe.qe.evergreen.sprout.model.CQInstance
import com.adobe.qe.evergreen.sprout.model.MavenDependency
import com.adobe.qe.evergreen.sprout.model.Module
import com.adobe.qe.evergreen.sprout.model.Quickstart
import com.adobe.qe.evergreen.sprout.model.UITestRun

/* --------------------------------------------------------------------- */
/*                                MODULES V1                               */
/* --------------------------------------------------------------------- */
Module componentsCore = new Module.Builder('main/bundles/core')
        .withUnitTests(true)
        .withCoverage()
        .withRelease()
        .withArtifact('jar', 'main/bundles/core/target/core.wcm.components.core-*.jar', true)
        .build()
Module componentsContent = new Module.Builder('main/content')
        .withRelease()
        .withArtifact('zip', 'main/content/target/core.wcm.components.content-*.zip', true)
        .build()
Module componentsConfig = new Module.Builder('main/config')
        .withRelease()
        .withArtifact('zip', 'main/config/target/core.wcm.components.config-*.zip', true)
        .build()
Module componentsItUi = new Module.Builder('main/testing/it/ui-js')
        .withRelease()
        .withArtifact('zip', 'main/testing/it/ui-js/target/core.wcm.components.it.ui-js-*.zip', true)
        .build()
Module componentsAll = new Module.Builder('main/all')
        .withRelease()
        .withArtifact('zip', 'main/all/target/core.wcm.components.all-*.zip', true)
        .build()

/* --------------------------------------------------------------------- */
/*                                MODULES Sandbox                        */
/* --------------------------------------------------------------------- */
Module componentsCoreSandbox = new Module.Builder('main/sandbox/bundle')
        .withUnitTests(true)
        .withCoverage()
        .withRelease()
        .withArtifact('jar', 'main/sandbox/bundle/target/core.wcm.components.sandbox.bundle-*.jar', true)
        .build()
Module componentsContentSandbox = new Module.Builder('main/sandbox/content')
        .withRelease()
        .withArtifact('zip', 'main/sandbox/content/target/core.wcm.components.sandbox.content-*.zip', true)
        .build()
Module componentsConfigSandbox = new Module.Builder('main/sandbox/config')
        .withRelease()
        .withArtifact('zip', 'main/sandbox/config/target/core.wcm.components.sandbox.config-*.zip', true)
        .build()
Module componentsItUiSandbox = new Module.Builder('main/sandbox/testing/it/ui-js')
        .withRelease()
        .withArtifact('zip', 'main/sandbox/testing/it/ui-js/target/core.wcm.components.sandbox.it.ui-js-*.zip', true)
        .build()

/* --------------------------------------------------------------------- */
/*                        EXTERNAL DEPENDENCIES                          */
/* --------------------------------------------------------------------- */
MavenDependency hobbesRewriterPackage = new MavenDependency.Builder()
        .withGroupId("com.adobe.granite")
        .withArtifactId("com.adobe.granite.testing.hobbes.rewriter")
        .withVersion("latest")
        .withExtension("jar").build()

MavenDependency uiTestingCommonsPackage = new MavenDependency.Builder()
        .withGroupId("com.adobe.qe")
        .withArtifactId("com.adobe.qe.ui-testing-commons")
        .withVersion("latest")
        .withExtension("zip").build()

/* --------------------------------------------------------------------- */
/*                       QUICKSTART CONFIGURATIONS                        */
/* --------------------------------------------------------------------- */
Quickstart quickstart = new BuildQuickstart.Builder('Quickstart 6.4')
        .withModule(componentsCore)
        .withModule(componentsContent)
        .withModule(componentsConfig).build()

// the quickstart to be build for the sandbox
Quickstart quickstartSandbox = new BuildQuickstart.Builder('Quickstart Sandbox')
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
        .withFileDependency(componentsItUi.getArtifact('zip')).build()

CQInstance authorSandbox = new CQInstance.Builder()
        .withQuickstart(quickstartSandbox)
        .withId('weretail-author-sandbox')
        .withPort(1235)
        .withRunmode("author")
        .withContextPath("/cp")
        .withMavenDependency(hobbesRewriterPackage)
        .withMavenDependency(uiTestingCommonsPackage)
        .withFileDependency(componentsCoreSandbox.getArtifact('jar'))
        .withFileDependency(componentsContentSandbox.getArtifact('zip'))
        .withFileDependency(componentsConfigSandbox.getArtifact('zip'))
        .withFileDependency(componentsItUiSandbox.getArtifact('zip')).build()

/* --------------------------------------------------------------------- */
/*                                UI TESTS                               */
/* --------------------------------------------------------------------- */
UITestRun coreCompUIChrome = new UITestRun.Builder()
        .withName('UI Tests Core Comp V1 / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.tests')
        .withHobbesHubUrl('http://or1010050212014.corp.adobe.com:8811')
        .withStopOnFail(false).build()

UITestRun coreCompUIChromeSandbox = new UITestRun.Builder()
        .withName('UI Tests Core Comp Sandbox / Chrome')
        .withInstance(authorSandbox)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.tests.v2')
        .withHobbesHubUrl('http://or1010050212014.corp.adobe.com:8811')
        .withStopOnFail(false).build()

/* --------------------------------------------------------------------- */
/*                       SPROUT CONFIGURATION                            */
/* --------------------------------------------------------------------- */
SproutConfig config = new SproutConfig()

config.setComputeCoverage(true)
config.setCoverageCriteria([new Branch(/^PRIVATE_master$/)])
config.setSonarSnapshotPrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-SNAPSHOT-')
config.setSonarReleasePrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-RELEASE-')

config.setModules([componentsCore, componentsContent, componentsConfig, componentsAll, componentsItUi,
                   componentsCoreSandbox,componentsContentSandbox,componentsConfigSandbox,componentsItUiSandbox])
config.setTestRuns([coreCompUIChrome, coreCompUIChromeSandbox])

// Releases
config.setReleaseCriteria([new Branch(/^dummy_branch$/)])
config.setQuickstartPRCriteria([new Branch(/^dummy_branch$/)])
config.setGithubAccessTokenId('740db810-2a69-4172-9973-6a9aa1b47624')
config.setIgnoreStatusAtPromotion(true)
config.setPromotionInputTimeout(60 * 24 * 3)
config.setQuickstartPRConfig(quickstart)

config.setEnableMailNotification(true)
config.setMailNotificationRecipients(['msagolj@adobe.com','adracea@adobe.com'])
config.setMailNotifyEveryUnstableBuild(false)

// Don't trigger sprout for release commits
config.setBuildCriteria([new Exclude(new GitCommitMessage(/^(.*)@releng \[maven\-scm\] :prepare(.*)$/))])

/* --------------------------------------------------------------------- */
/*                       SPROUT CUSTOMIZATION                            */
/* --------------------------------------------------------------------- */
Pipeline sprout = new Sprout.Builder()
        .withConfig(config)
        .withJenkins(this).build()

sprout.execute()
