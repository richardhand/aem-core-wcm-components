# README

## Form Button \(v1\)

Button component written in HTL.

### Features

* Provides support for regular and submit buttons

#### Use Object

The Form Button component uses the `com.adobe.cq.wcm.core.components.models.form.Button` Sling Model for its Use-object.

#### Edit Dialog Properties

The following properties are written to JCR for this Form Button component and are expected to be available as `Resource` properties:

1. `./jcr:title` - defines the text displayed on the button; if none is provided, the text will default to the button type
2. `./name` - defines the name of the button, which will be submitted with the form data
3. `./value` - defines the value of the button, which will be submitted with the form data

### Client Libraries

The component provides a `core.wcm.components.form.button.v1.editor` editor client library category that includes JavaScript handling for dialog interaction. It is already included by its edit dialog.

### Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_form\_button\_v1](https://www.adobe.com/go/aem_cmp_form_button_v1)

