<!--
Copyright 2017 Adobe Systems Incorporated

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
Image (v2)
====
Image component written in HTL that renders an adaptive image.

## Features
* Smart loading of optimal rendition
* In-place editing, cropping, rotating, and resizing
* Image title, description, accessibility text and link
* Styles

### Use Object
The Image component uses the `com.adobe.cq.wcm.core.components.models.Image` Sling Model as its Use-object.

### Component Policy Configuration Properties
The following configuration properties are used:

1. `./allowedRenditionWidths` - defines the allowed renditions (as an integer array) that will be generated for the images rendered by this
component; the actual size will be requested by the client device;
2. `./disableLazyLoading` - if `true`, the lazy loading of images (loading only when the image is visible on the client
device) is disabled.

### Edit Dialog Properties
The following properties are written to JCR for this Image component and are expected to be available as `Resource` properties:

1. `./fileReference` property or `file` child node - will store either a reference to the image file, or the image file
2. `./isDecorative` - if set to `true`, then the image will be ignored by assistive technology
3. `./alt` - defines the value of the HTML `alt` attribute (not needed if `./isDecorative` is set to `true`)
4. `./linkURL` - allows defining a URL to which the image will link to
5. `./jcr:title` - defines the value of the HTML `title` attribute or the value of the caption, depending on the value of
`./displayPopupTitle`
6. `./displayPopupTitle` - if set to `true` it will render the value of the `./jcr:title` property through the HTML `title` attribute,
otherwise a caption will be rendered

## Extending from This Component
1. In case you overwrite the image's HTL script, make sure the necessary attributes for the JavaScript loading script are contained in the markup at the right position (see section below).
2. In case your own component does not only render an image but does also renders something else, use the following approach:
  1. `resourceSuperType` should be set to `core/wcm/components/image/v1/image` (to make sure the image rendering servlet is being used)
  2. Your HTL script should include the image markup via `<div class="cmp-image" data-sly-include="image.html"></div>`
  3. You derived component should reset `cq:htmlTags`
  4. You component's dialog should overwrite the dialog fully from the image component via `sling:hideResource="true"` on the node `cq:dialog/content/items/image`

## URL Formats
The images are loaded through the `com.adobe.cq.wcm.core.components.internal.servlets.AdaptiveImageServlet`, therefore their URLs have the following patterns:

```
Author:
/content/<project_path>/<page_path>/<component_path>/<component_name>.coreimg.<width>.<extension>/<timestamp>.<extension>

Publish:
/content/<project_path>/<page_path>/<component_path>/<component_name>.coreimg.<width>.<extension>
```

## Client Libraries
The component provides a `core.wcm.components.image.v2` client library category that contains a recommended base
CSS styling and JavaScript component. It should be added to a relevant site client library using the `embed` property.

It also provides a `core.wcm.components.image.v2.editor` editor client library category that includes JavaScript
handling for dialog interaction. It is already included by its edit dialog.

## BEM Description
```
BLOCK cmp-image
    ELEMENT cmp-image__link
    ELEMENT cmp-image__image
    ELEMENT cmp-image__title
```

## JavaScript Data Attribute Bindings
Apply a `data-cmp-is="image"` attribute to the wrapper block to enable initialization of the JavaScript component.

The following attributes can be added to the same element to provide options:

1. `data-cmp-lazy` - if not `false`, indicates that the image should be rendered lazily.
2. `data-cmp-src` - the image source. Can be a simple image source, or a URI template representation that can be variable expanded -
useful for building an image configuration with an alternative width. Should contain a `{.width}` variable.
e.g. '/path/to/image.coreimg{.width}.jpeg'
3. `data-cmp-widths` - a comma-separated string of alternative image widths (in pixels).
Populated with `allowedRenditionWidths` from the component's edit dialog.

A hook attribute from the following should be added to the corresponding element so that the JavaScript is able to target it:

```
 data-cmp-hook-image="image"
 data-cmp-hook-image="link"
 data-cmp-hook-image="noscript"
```

The `img` should be placed inside a `noscript` element with the `data-cmp-hook-image="noscript"` attribute.
It will be inserted into the DOM by the JavaScript component.

To allow lazy loading it is expected that the `data-cmp-lazy` and `data-cmp-src` options are supplied.

It is possible to configure the JavaScript component such that the most appropriate image url is built and applied to the `img`.
The most appropriate width being the one which is at least as wide as the image's container.
The `data-cmp-widths` option must be provided with more than one width, as well as the `data-cmp-src` option,
with a URI template representation of the source.

## Information
* **Vendor**: Adobe
* **Version**: v2
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_image\_v2](https://www.adobe.com/go/aem_cmp_image_v2)

