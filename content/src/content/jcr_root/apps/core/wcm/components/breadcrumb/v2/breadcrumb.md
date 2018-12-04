# README

## Breadcrumb \(v2\)

Breadcrumb component written in HTL.

### Features

* Start level
* Option to show hidden navigation items
* Exclude the current page from the breadcrumb

#### Use Object

The Breadcrumb component uses the `com.adobe.cq.wcm.core.components.models.Breadcrumb` Sling model as its Use-object.

#### Component Policy Configuration Properties

The following configuration properties are used:

1. `./startLevel` - the level at which to start the breadcrumb: 0 = /content, 1 = /content/site, etc.
2. `./showHidden` - if `true`, show navigation items hidden via a ./hideInNav property in the breadcrumb.
3. `./hideCurrent` - if `true`, don't display the current page in the breadcrumb.

#### Edit Dialog Properties

The following properties are written to JCR for this Breadcrumb component and are expected to be available as `Resource` properties:

1. `./startLevel` - the level at which to start the breadcrumb: 0 = /content, 1 = /content/site, etc.
2. `./showHidden` - if `true`, show navigation items hidden via a ./hideInNav property in the breadcrumb.
3. `./hideCurrent` - if `true`, don't display the current page in the breadcrumb.

### Client Libraries

The component provides a `core.wcm.components.breadcrumb.v2` client library category that contains a recommended base CSS styling. It should be added to a relevant site client library using the `embed` property.

### BEM description

```text
BLOCK cmp-breadcrumb
    ELEMENT cmp-breadcrumb__list
    ELEMENT cmp-breadcrumb__item
    ELEMENT cmp-breadcrumb__item-link
```

### Information

* **Vendor**: Adobe
* **Version**: v2
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_breadcrumb\_v2](https://www.adobe.com/go/aem_cmp_breadcrumb_v2)

