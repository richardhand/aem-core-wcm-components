# Versioning

The AEM Core WCM Components employ three mechanisms for versioning. Since their logic is based on Sling Models [0], the components' Java API follows the semantic versioning guidelines [1][2]. However, since the markup of these components also represents an API, the components' scripts are also versioned, through their resource path and `sling:resourceType` values. For example, release [2.0.0](https://github.com/Adobe-Marketing-Cloud/aem-core-wcm-components/releases/tag/core.wcm.components.reactor-2.0.0) of the Core Components provides version 2 of the Image Component. Its resource type is therefore `core/wcm/components/image/v2/image`. This allows backwards compatible markup changes to be applied directly to this content structure, while incompatible changes would generate a new version of the component.

Component client library categories are also versioned, with the version relating directly to that of the component resource. For example, the `core.wcm.components.image.v2` category relates to the v2 Image Component. There are two possible types per component, “site” and “editor”, which can be found under folders of the same name in the component clientlib structure. Editor specific client libraries have an “.editor” postfix in their category name and are loaded on demand by the components' editing / design dialogs. For sites developers, it is necessary to load client libraries for the components in use, either through aggregation (one application client library that embeds the individual component client libraries) and / or through template or designer defined client libraries.

The following sections summarise all the possible changes and their impact on the components, allowing you to also understand what customisations are possible and guaranteed to still work if you upgrade your project components.

## HTL Scripts and Templates
**Backwards compatible changes:**
* adding new optional parameters to an HTL template, as long as the template's rendering does not require a change of the CSS selectors, or does not modify the markup's semantics

**Incompatible changes:**
* switching the used Sling model
* adding new HTL scripts to the component's structure
* removing HTL templates or removing previously used parameters;
* changing the rendering logic of a template, that would require either a CSS selectors change or that generates a semantic change of the markup


## HTML Markup
**Backwards compatible changes:**
* adding new markup/nodes that would not require a change of CSS selectors, or does not modify the markup's semantics
* adding new attributes

**Incompatible changes:**
* adding new markup that requires new CSS selectors in order for the previous functionality to work correctly
* removing existing markup/nodes
* removing existing attributes


## CSS Selectors
**Backwards compatible changes:**
* adding new classes
* adding IDs

**Incompatible changes:**
* removing previously used classes or IDs

## Sling Models
**Backwards compatible changes:**
* any change that would increase the `PATCH` or the `MINOR` version of the API is guaranteed to work with the older version of the components; such examples are adding new `default` [3] methods to the API interfaces or adding new constants; custom implementations of the API should not be affected by these additions to the API, due to the use of `default` methods; `default` methods can be added if and only if their usage in the HTL scripts does not generate incompatible changes

**Incompatible changes:**
* any change that would increase the `MAJOR` version of the API, such as: changing method signatures, adding non-`default` methods, changing constant values, removing methods, removing constants, removing interfaces or API classes; this would break all consumers

## JSON Representation
**Backwards compatible changes:**
* adding properties

**Incompatible changes:**
* removing properties
* changing the data type of existing properties

## Dialogs
**Backwards compatible changes:**
* adding nodes below tab item level
* removing nodes below tab item level
* reordering nodes below tab item level
* removing properties
* redefining properties

**Incompatible changes:**
* changing dialog structure down to tab item level
* adding a new dialog tab item
* removing a dialog tab item
* reordering tab items

## How to Perform Backwards Compatible Customisations at the Project Level
A project relying on the AEM Core WCM Components should never directly use these components, but rather rely on the [Proxy-Component Pattern](https://docs.adobe.com/docs/en/aem/6-3/develop/components/core-components/guidelines.html#Proxy%20Component%20Pattern). This means that each project should create a parallel and project-specific structure of the Core Components, by employing the `sling:resourceSuperType` property. The advantages are that customisations can then be performed directly on these proxy structures, if needed, and also that it would allow multiple projects hosted on the same AEM version to use their own specific version of the components.

HTL scripts can be overridden in these proxy locations, as long as the changes are backwards compatible - see the above sections.

The Core Components' logic is implemented with Sling Models, using the `adapter` [5] pattern. This allows defining an interface for providing the contract, while the implementation remains private.

Furthermore, the provided implementations are associated to a resource type [6], allowing the reuse of the model for inheriting resource types (via the `sling:resourceSuperType` property).

Custom implementations of the Sling Models are possible, and implementors should just bind the models' implementations to the proxy resource types [6]. In some cases, a custom implementation needs to partially change the behaviour of a model. For this scenario, the `delegate` pattern can be used, through Via Types [7].

----
[0] - https://sling.apache.org/documentation/bundles/models.html
[1] - http://semver.org/
[2] - https://www.osgi.org/wp-content/uploads/SemanticVersioning.pdf
[3] - https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html
[4] - https://osgi.org/javadoc/r6/annotation/org/osgi/annotation/versioning/ConsumerType.html
[5] - https://sling.apache.org/documentation/bundles/models.html#specifying-an-alternate-adapter-class-since-110
[6] - https://sling.apache.org/documentation/bundles/models.html#associating-a-model-class-with-a-resource-type-since-130
[7] - https://sling.apache.org/documentation/bundles/models.html#via-types-since-api-134-implementation-140
