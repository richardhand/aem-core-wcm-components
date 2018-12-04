# Carousel \(v1\)

Carousel component written in HTL.

## Features

* Allows addition of Carousel item components of varying resource type.
* Allowed components can be configured through policy configuration.
* Carousel navigation via next/previous and position indicators.
* Carousel autoplay with configurable delay.
* Editing features for items \(adding, removing, editing, re-ordering\).

### Use Object

The Carousel component uses the `com.adobe.cq.wcm.core.components.models.Carousel` Sling model as its Use-object.

### Component Policy Configuration Properties

The following configuration properties are used:

1. `./autoplay` - defines whether or not the carousel should automatically transition between slides.
2. `./delay` - defines the delay \(in milliseconds\) when automatically transitioning between slides.

It is also possible to define the allowed components for the Carousel.

### Edit Dialog Properties

The following properties are written to JCR for this Carousel component and are expected to be available as `Resource` properties:

1. `./autoplay` - defines whether or not the carousel should automatically transition between slides.
2. `./delay` - defines the delay \(in milliseconds\) when automatically transitioning between slides.

The edit dialog also allows editing of Carousel items \(adding, removing, naming, re-ordering\).

## Client Libraries

The component provides a `core.wcm.components.carousel.v1` client library category that contains a recommended base CSS styling and JavaScript component. It should be added to a relevant site client library using the `embed` property.

## BEM Description

```text
BLOCK cmp-carousel
    ELEMENT cmp-carousel__content
    ELEMENT cmp-carousel__item
    ELEMENT cmp-carousel__action
        MOD cmp-carousel__action--previous
        MOD cmp-carousel__action--next
    ELEMENT cmp-carousel__action-icon
    ELEMENT cmp-carousel__action-text
    ELEMENT cmp-carousel__indicators
    ELEMENT cmp-carousel__indicator
```

## JavaScript Data Attribute Bindings

Apply a `data-cmp-is="carousel"` attribute to the wrapper block to enable initialization of the JavaScript component.

The following attributes can be added to the same element to provide options:

1. `data-cmp-autoplay` - if the attribute is present, indicates that the carousel should automatically transition between slides.
2. `data-cmp-delay` - the delay \(in milliseconds\) when automatically transitioning between slides.

A hook attribute from the following should be added to the corresponding element so that the JavaScript is able to target it:

```text
data-cmp-hook-carousel="item"
data-cmp-hook-carousel="previous"
data-cmp-hook-carousel="next"
data-cmp-hook-carousel="indicators"
data-cmp-hook-carousel="indicator"
```

### Enabling Carousel Editing Functionality

The following properties and child nodes are required in the proxy component to enable full editing functionality for the Carousel:

1. `./cq:isContainer` - set to `true`, marks the Carousel as a container component
2. `./cq:editConfig` - `afterchilddelete`, `afterchildinsert` and `afterchildmove` listeners should be provided via

   the edit configuration of the proxy. `_cq_editConfig.xml` contains the recommended actions and can be copied to the proxy component.

The default Carousel site Client Library provides a handler for message requests between the editor and the Carousel. If the built-in Client Library is not used, a message request handler should be registered:

```text
new Granite.author.MessageChannel("cqauthor", window).subscribeRequestMessage("cmp.panelcontainer", function(message) {
    if (message.data && message.data.type === "cmp-carousel" && message.data.id === myCarouselHTMLElement.dataset["cmpPanelcontainerId"]) {
        if (message.data.operation === "navigate") {
            // handle navigation
        }
    }
});
```

The handler should subscribe to a `cmp.panelcontainer` message that allows routing of a `navigate` operation to ensure that the UI component is updated when the active item is switched in the editor layer.

## Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_carousel\_v1](https://www.adobe.com/go/aem_cmp_carousel_v1)

