# README

## Form Container \(v1\)

Form container written in HTL.

### Features

* Form submit actions like sending emails, storing content
* Configurable list of allowed components
* Thank you page

#### Use Object

The Form Container component uses the `com.adobe.cq.wcm.core.components.models.form.Container` Sling Model for its Use-object.

#### Component Policy Configuration Properties

The following configuration properties are used:

1. `./components` - defines the allowed components that can be dropped onto a Form Container associated to this component policy
2. `./columns` - defines the number of columns for the container's grid for a Form Container associated to this component policy

#### Edit Dialog Properties

The following properties are written to JCR for this Form Container component and are expected to be available as `Resource` properties:

1. `./actionType` - defines the action that will be performed by the form
2. `./workflowModel` - defines the workflow which should be started with the stored content as payload
3. `./workflowTitle` - defines the workflow's title
4. `./redirect` - if left empty the form will be rendered after submission, otherwise the user will be redirected to the page stored by this

   property

### Client Libraries

The component provides a `core.wcm.components.form.container.v1` client library category that contains a recommended base CSS styling. It should be added to a relevant site client library using the `embed` property.

It also provides a `core.wcm.components.form.container.v1.editor` editor client library category that includes JavaScript handling for dialog interaction. It is already included by its edit dialog.

### Information

* **Vendor**: Adobe
* **Version**: v1
* **Compatibility**: AEM 6.3
* **Status**: production-ready
* **Documentation**: [https://www.adobe.com/go/aem\_cmp\_form\_container\_v1](https://www.adobe.com/go/aem_cmp_form_container_v1)

