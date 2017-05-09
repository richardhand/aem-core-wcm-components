#!groovy
@Library(['com.adobe.qe.pipeline.cq'])
import com.adobe.qe.pipeline.cq.CQPipeline
import com.adobe.qe.pipeline.cq.CQPipelineConfig
import com.adobe.qe.pipeline.cq.model.CQInstance
import com.adobe.qe.pipeline.cq.model.IntegrationTestRun
import com.adobe.qe.pipeline.cq.model.UITestRun
import com.adobe.qe.pipeline.cq.model.MavenDependency

//
// EXTERNAL DEPENDENCIES
//
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

//
// CQ INSTANCE CONFIGURATION
//
CQInstance author = new CQInstance.Builder()
    .withId('weretail-author')
    .withPort(1234)
    .withRunmode("author")
    .withContextPath("/cp")
    .withMavenDependency(hobbesRewriterPackage)
    .withMavenDependency(uiTestingCommonsPackage)
    .withArtifact('main/testing/it/ui-js/target/core.wcm.components.it.ui-js-*.zip').build()

//
// UI TESTS
//

UITestRun coreCompUIChrome = new UITestRun.Builder()
    .withName('UI Tests Core Components / Chrome')
    .withInstance(author)
    .withBrowser('CHROME')
    .withFilter('aem.core-components.tests')
    .withHobbesHubUrl('http://or1010050212014.corp.adobe.com:8811').build()
//
// PIPELINE CONFIG
//
CQPipelineConfig config = new CQPipelineConfig()

config.setComputeCoverage(true)
config.setCoverageBranch(/^PRIVATE_master$/)
config.setSonarPrefix('CORE-COMPONENT-PIPELINE-PRIVATE_MASTER')

config.setBundlesToTest([
    'main/bundles/core'
])
config.setBuildArtifacts([
    'main/bundles/core': ['main/bundles/core/target/core.wcm.components.core-*-SNAPSHOT.jar'],
    'main/content': ['main/content/target/core.wcm.components.content-*-SNAPSHOT.zip'],
    'main/config': ['main/config/target/core.wcm.components.config-*-SNAPSHOT.zip'],
    'main/testing/it/ui-js': ['main/testing/it/ui-js/target/core.wcm.components.it.ui-js-*.zip']
])
config.setArchiveBuildArtifacts(true)

config.setTestRuns([coreCompUIChrome])

config.setEnableMailNotification(true)
config.setMailNotificationRecipients(['msagolj@adobe.com'])
config.setMailNotifyEveryUnstableBuild(false)
// config.setQuickstartBranch('release/630') // default is master

// a release generates 2 commits, which will trigger the pipeline twice in parallel
// causing trouble for sonar as both job wil try to send analysis to sonar
config.setSkipReleasePrepareBuild(true)
config.setskipReleasePrepareCommitMessage("@releng [maven-scm] :prepare for next development iteration")

//
// PIPELINE CUSTOMIZATION
//
CQPipeline pipeline = new CQPipeline.Builder()
    .withConfig(config)
    .withJenkins(this).build()

//
// EXECUTE PIPELINE
//
pipeline.execute()