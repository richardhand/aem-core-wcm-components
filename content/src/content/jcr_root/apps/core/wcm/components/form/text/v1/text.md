# README

## Form Text \(v1\)

Text form field component written in HTL.

### Features

* Provides the following type of input:
  * text
  * textarea
  * email
  * telephone
  * date
  * number
* Custom constraint messages for the above types

#### Use Object

The Form Text component uses the `com.adobe.cq.wcm.core.components.models.form.Text` Sling Model for its Use-object.

#### Edit Dialog Properties

The following properties are written to JCR for this Form Text component and are expected to be available as `Resource` properties:

1. `./type` - defines the type of text this field provides; possible values: `text`, `textarea`, `email`, `tel`, `date`, `number`,

   `password`

2. `./rows` - defines the number of text lines available in this input field
3. `./jcr:title` - defines the label to use for this field
4. `./hideTitle` - if set to `true`, the label of this field will be hidden
5. `./name` - defines the name of the field, which will be submitted with the form data
6. `./value` - defines the default value of the field
7. `./helpMessage` - defines a help message that can be rendered in the field as a hint for the user
8. `./usePlaceholder` - if set to `true`, the help message will be displayed inside the form input if the field is empty and not focused
9. `./constraintMessage` - defines the message displayed as tooltip when submitting the form if the value does not validate the chosen type
10. `./required` - if set to `true`, this field will be marked as required, not allowing the form to be submitted until the field has a value
11. `./requiredMessage` - defines the message displayed as tooltip when submitting the form if the value is left empty
12. `./readOnly` - if set to `true`, the filed will be read only

### Client Libraries

The component provides a `core.wcm.components.form.text.v1` client library category that contains a JavaScript component. It should be added to a relevant site client library using the `embed` property.

It also provides a `core.wcm.components.form.text.v1.editor` editor client library category that includes JavaScript handling for dialog interaction. It is already included by its edit dialog.

### Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_form\_text\_v1](https://www.adobe.com/go/aem_cmp_form_text_v1)

