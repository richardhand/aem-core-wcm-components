# Tabs \(v1\)

Tabs component written in HTL.

## Features

* Allows addition of tab panel components of varying resource type.
* Allowed components can be configured through policy configuration.
* Navigation to tab panels via tabs.
* Editing features for tab panels \(adding, removing, editing, re-ordering\).

### Use Object

The Tabs component uses the `com.adobe.cq.wcm.core.components.models.Tabs` Sling model as its Use-object.

### Component Policy Configuration Properties

The component policy dialog allows definition of allowed components for the Tabs.

### Edit Dialog Properties

The following properties are written to JCR for this Tabs component and are expected to be available as `Resource` properties:

1. `./activeItem` - defines the item that is active by default.

The edit dialog also allows editing of Tabs items \(adding, removing, naming, re-ordering\).

## Client Libraries

The component provides a `core.wcm.components.tabs.v1` client library category that contains a recommended base CSS styling and JavaScript component. It should be added to a relevant site client library using the `embed` property.

## BEM Description

```text
BLOCK cmp-tabs
    ELEMENT cmp-tabs__tablist
    ELEMENT cmp-tabs__tab
        MOD cmp-tabs__tab--active
    ELEMENT cmp-tabs__tabpanel
        MOD cmp-tabs__tabpanel--active
```

## JavaScript Data Attribute Bindings

Apply a `data-cmp-is="tabs"` attribute to the wrapper block to enable initialization of the JavaScript component.

A hook attribute from the following should be added to the corresponding element so that the JavaScript is able to target it:

```text
data-cmp-hook-tabs="tab"
data-cmp-hook-tabs="tabpanel"
```

### Enabling Tabs Editing Functionality

The following properties and child nodes are required in the proxy component to enable full editing functionality for the Tabs:

1. `./cq:isContainer` - set to `true`, marks the Tabs as a container component
2. `./cq:editConfig` - `afterchilddelete`, `afterchildinsert` and `afterchildmove` listeners should be provided via

   the edit configuration of the proxy. `_cq_editConfig.xml` contains the recommended actions and can be copied to the proxy component.

The default Tabs site Client Library provides a handler for message requests between the editor and the Tabs. If the built-in Client Library is not used, a message request handler should be registered:

```text
new Granite.author.MessageChannel("cqauthor", window).subscribeRequestMessage("cmp.panelcontainer", function(message) {
    if (message.data && message.data.type === "cmp-tabs" && message.data.id === myTabsHTMLElement.dataset["cmpPanelcontainerId"]) {
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
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_tabs\_v1](https://www.adobe.com/go/aem_cmp_tabs_v1)

