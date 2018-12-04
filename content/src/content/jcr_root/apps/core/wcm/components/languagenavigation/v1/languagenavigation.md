# README

## Language Navigation \(v1\)

Language Navigation component written in HTL that renders a global language structure navigation.

### Features

#### Use Object

The Language Navigation component uses the `com.adobe.cq.wcm.core.components.models.LanguageNavigation` Sling model as its Use-object.

#### Component Policy Configuration Properties

The following configuration properties are used:

1. `./navigationRoot` - the root page of the global language structure.
2. `./structureDepth` - the depth of the global language structure relative to the navigation root.

#### Edit Dialog Properties

The following properties are written to JCR for the Language Navigation component and are expected to be available as `Resource` properties:

1. `./navigationRoot` - the root path of the global language structure.
2. `./structureDepth` - the depth of the global language structure relative to the navigation root.

### BEM Description

```text
BLOCK cmp-languagenavigation
    ELEMENT cmp-languagenavigation__group
    ELEMENT cmp-languagenavigation__item
        MOD cmp-languagenavigation__item--active
        MOD cmp-languagenavigation__item--countrycode-*
        MOD cmp-languagenavigation__item--langcode-*
        MOD cmp-languagenavigation__item--level-*
    ELEMENT cmp-languagenavigation__item-link
    ELEMENT cmp-languagenavigation__item-title
```

### Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_languagenavigation\_v1](https://www.adobe.com/go/aem_cmp_languagenavigation_v1)

