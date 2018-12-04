# README

## Content Fragment \(v1 - extension\)

Content Fragment component written in HTL that displays the elements of a Content Fragment or a selection thereof.

### Features

* Displays the elements of a Content Fragment as an HTML description list
* By default renders all elements of a Content Fragment
* Can be configured to render a subset of the elements in a specific order

#### Use Object

The Content Fragment component uses the `com.adobe.cq.wcm.core.components.extension.contentfragment.models.ContentFragment` Sling model as its Use-object.

#### Edit dialog properties

The following JCR properties are used:

1. `./fragmentPath` - defines the path to the Content Fragment to be rendered
2. `./variationName` - defines the variation to use to render the elements \(optional: if not present, the master variation is used\)
3. `./elementNames` - multi-valued property defining the elements to be rendered and in which order \(optional: if not present, all elements are rendered\)
4. `./paragraphScope` - defines if all or a range of paragraphs are to be rendered \(only used in paragraph mode\)
5. `./paragraphRange` - defines the range\(s\) of paragraphs to be rendered \(only used in paragraph mode and if paragraphs are restricted to ranges\)
6. `./paragraphHeadings` - defines if headings should count as paragraphs \(only used in paragraph mode and if paragraphs are restricted to ranges\)

### BEM description

```text
BLOCK cmp-contentfragment
    ELEMENT cmp-contentfragment__element
        MOD cmp-contentfragment__element--<name>
    ELEMENT cmp-contentfragment__element-label
    ELEMENT cmp-contentfragment__element-value
```

### Information

* **Vendor**: Adobe
* **Version**: v1 - extension
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_contentfragment\_v1](https://www.adobe.com/go/aem_cmp_contentfragment_v1)

