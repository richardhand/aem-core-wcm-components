# README

## Title \(v1\)

Title component written in HTL, allowing to define a section heading.

### Features

* In-place editing
* HTML element configuration \(`h1` - `h6`\)
* Styles

#### Use Object

The Title component uses the `com.adobe.cq.wcm.core.components.models.Title` Sling model as its Use-object.

#### Component Policy Configuration Properties

The following configuration properties are used:

1. `./type` - defines the default HTML heading element type \(`h1` - `h6`\) this component will use for its rendering

#### Edit Dialog Properties

The following properties are written to JCR for this Title component and are expected to be available as `Resource` properties:

1. `./jcr:title` - will store the text of the title to be rendered
2. `./type` - will store the HTML heading element type which will be used for rendering; if no value is defined, the component will fallback

   to the value defined by the component's policy

### Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_title\_v1](https://www.adobe.com/go/aem_cmp_title_v1)

