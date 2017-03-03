# AEM Core WCM Components

A set of standardized components that can be used to speed up development of web sites.

## Available Components

* Page authoring components:
  * [Page component](content/src/content/jcr_root/apps/core/wcm/components/page/v1/page)
  * [Breadcrumb component](content/src/content/jcr_root/apps/core/wcm/components/breadcrumb/v1/breadcrumb)
  * [Title component](content/src/content/jcr_root/apps/core/wcm/components/title/v1/title)
  * [Text component](content/src/content/jcr_root/apps/core/wcm/components/text/v1/text)
  * [Image component](content/src/content/jcr_root/apps/core/wcm/components/image/v1/image)
  * [List component](content/src/content/jcr_root/apps/core/wcm/components/list/v1/list)
  * [Sharing component](content/src/content/jcr_root/apps/core/wcm/components/sharing/v1/sharing)
* Form components:
  * [Form container](content/src/content/jcr_root/apps/core/wcm/components/form/container/v1/container)
  * [Form text field](content/src/content/jcr_root/apps/core/wcm/components/form/text/v1/text)
  * [Form options field](content/src/content/jcr_root/apps/core/wcm/components/form/options/v1/options)
  * [Form hidden field](content/src/content/jcr_root/apps/core/wcm/components/form/hidden/v1/hidden)
  * [Form button](content/src/content/jcr_root/apps/core/wcm/components/form/button/v1/button)

## Installation

For ease of installation the following profiles are provided:

 * ``autoInstallSinglePackage`` - install everything to an existing AEM author instance, as specified by ``http://${aem.host}:${aem.port}``
 * ``autoInstallSinglePackagePublish`` - install everything to an existing AEM publish instance, as specified by ``http://${aem.publish.host}:${aem.publish.port}``
 * ``autoInstallPackage`` - installs the package/bundle to an existing AEM author instance, as specified by ``http://${aem.host}:${aem.port}``
 * ``autoInstallPackagePublish`` - installs the package/bundle to an existing AEM publish instance, as specified by ``http://${aem.publish.host}:${aem.publish.port}``

### UberJar

This project relies on the unobfuscated AEM 6.2 cq-quickstart. This is not publicly available from http://repo.adobe.com and must be 
manually downloaded from https://daycare.day.com/home/products/uberjar.html. After downloading the file (_cq-quickstart-6.2.0-apis.jar_), you must install it into your local Maven repository with this command:

    mvn install:install-file -Dfile=cq-quickstart-6.2.0-apis.jar -DgroupId=com.day.cq -DartifactId=cq-quickstart -Dversion=6.2.0 -Dclassifier=apis -Dpackaging=jar

For more details about the UberJar please head over to the
[How to Build AEM Projects using Apache Maven](https://docs.adobe.com/docs/en/aem/6-2/develop/dev-tools/ht-projects-maven.html#What%20is%20the%20UberJar?)
documentation page.

### Install everything

You can install everything needed to use the components on your running AEM instance by issuing the following command in the top level folder of the project:

    mvn clean install -PautoInstallSinglePackage

### Individual packages/bundles

You can install individual packages/bundles by issuing the following command in the top level folder of the project:

    mvn clean install -PautoInstallPackage -pl <project_name(s)> -am

Please note that

 * ``-pl/-projects`` option specifies the list of projects that you want to install
 * ``-am/-also-make`` options specifies that dependencies should also be built
