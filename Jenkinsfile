#!groovy
@Library(['com.adobe.qe.pipeline.cq@develop'])
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
/*                                MODULES                                */
/* --------------------------------------------------------------------- */
Module componentsCore = new Module.Builder('main/bundles/core')
        .withUnitTests(true)
        .withCoverage()
//        .withRelease()
        .withArtifact('jar', 'main/bundles/core/target/core.wcm.components.core-*.jar', true)
        .build()
Module componentsContent = new Module.Builder('main/content')
//        .withRelease()
        .withArtifact('zip', 'main/content/target/core.wcm.components.content-*.zip', true)
        .build()
Module componentsConfig = new Module.Builder('main/config')
//        .withRelease()
        .withArtifact('zip', 'main/config/target/core.wcm.components.config-*.zip', true)
        .build()
Module componentsItUi = new Module.Builder('main/testing/it/ui-js')
//        .withRelease()
        .withArtifact('zip', 'main/testing/it/ui-js/target/core.wcm.components.it.ui-js-*.zip', true)
        .build()
Module componentsAll = new Module.Builder('main/all')
//        .withRelease()
        .withArtifact('zip', 'main/all/target/core.wcm.components.all-*-SNAPSHOT.zip', true)
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
/*                       QUICKSTART CONFIGURATION                        */
/* --------------------------------------------------------------------- */
Quickstart quickstart = new BuildQuickstart.Builder('Quickstart 6.4')
        .withModule(componentsCore)
        .withModule(componentsContent)
        .withModule(componentsConfig).build()

Quickstart quickstart63 = new BuildQuickstart.Builder('Quickstart 6.3')
        .withBranch('release/630')
        .withModule(componentsCore)
        .withModule(componentsContent)
        .withModule(componentsConfig).build()


/* --------------------------------------------------------------------- */
/*                      CQ INSTANCE CONFIGURATION                        */
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

/* --------------------------------------------------------------------- */
/*                                UI TESTS                               */
/* --------------------------------------------------------------------- */
UITestRun coreCompUIChrome = new UITestRun.Builder()
        .withName('UI Tests Core Components / Chrome')
        .withInstance(author)
        .withBrowser('CHROME')
        .withFilter('aem.core-components.tests')
        .withHobbesHubUrl('http://or1010050212014.corp.adobe.com:8811')
        .withStopOnFail(true).build()

/* --------------------------------------------------------------------- */
/*                       SPROUT CONFIGURATION                            */
/* --------------------------------------------------------------------- */
SproutConfig config = new SproutConfig()

config.setComputeCoverage(true)
config.setCoverageCriteria([new Branch(/^PRIVATE_master$/)])
config.setSonarSnapshotPrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-SNAPSHOT-')
config.setSonarReleasePrefix('CORE-COMPONENT-SPROUT-PRIVATE_MASTER-RELEASE-')

config.setModules([componentsCore, componentsContent, componentsConfig, componentsAll, componentsItUi])
config.setTestRuns([coreCompUIChrome])

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
