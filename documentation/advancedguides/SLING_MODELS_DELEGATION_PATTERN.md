# Sling Models Delegation Pattern

The Business logic for the core-components is implemented in Sling Models. In case it is necessary to customize the business logic to fulfill project specific requirements the delegation Pattern for Sling Models can be used, which was added in [SLING-6778](https://issues.apache.org/jira/browse/SLING-6778).

The following example demonstrates a custom Sling Model for the Title component. The title core-component uses the jcr:title property of the requested resource to provide the title text. If no jcr:title property is defined a fallback to the current page title is implemented. In the following example the behavior should be changed, so that always the title of the current page is displayed.

## Example:
### Step 1: Create a proxy component for the title component
As described in [Using Core Components](https://helpx.adobe.com/experience-manager/core-components/using/using.html) the core components should always be used in a project by a proxy component. This is also mandatory for the delegation pattern and to not break the default behavior of the title component. The new component will be created in ```/apps/myproject/components/pageHeadline```.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="cq:Component"
    jcr:title="Page Headline"
    jcr:description="Display Page Heading"
    sling:resourceSuperType="core/wcm/components/title/v2/title"
    componentGroup="My Project"/>
```

### Step 2: Create a custom Sling Model for the page headline component

```java
@Model(adaptables = SlingHttpServletRequest.class, adapters = Title.class, resourceType = "myproject/components/pageHeadline")
public class PageHeadline implements Title {

    @ScriptVariable
    private Page currentPage;

    @Self @Via(type = ResourceSuperType.class)
    private Title title;

    @Override
    public String getText() {
        return currentPage.getTitle();
    }

    @Override
    public String getType() {
        return title.getType();
    }
}
```
For the bean property ```title``` the @Via annotation creates a wrapped resource and request with the Sling Model of the resource super type of the page headline component, which is in this example the title core-component. After that the custom sling model can reuse all the public methods of the Sling Model from the resource super type and can partial customize the behavior according the projects requirements. In this example the getText() method was customized and returns always the title of the current page instead of the jcr:title property of the resource.

