# Adaptive Image Servlet

The [`AdaptiveImageServlet`](https://github.com/Adobe-Marketing-Cloud/aem-core-wcm-components/blob/master/bundles/core/src/main/java/com/adobe/cq/wcm/core/components/internal/servlets/AdaptiveImageServlet.java)
is responsible for image processing and streaming.

## What is an image
An image is a `Resource` with the `sling:resourceType` property set to `core/wcm/components/image` or to any other resource type inheriting
from it <sup>[1](#core-image-component)</sup>, with either a `fileReference` property, pointing to a DAM asset, or a `file` `nt:file`
subnode, that was uploaded directly to the component node.

## Servlet properties
Out of the box, the `AdaptiveImageServlet` comes with the following configurations:

* `sling.servlet.resourceTypes=["core/wcm/components/image"]`, `sling.servlet.selectors=["img"]`, `sling.servlet.extenions=["jpg","jpeg","png","gif"]` - for the Core Image v1 URLs
* `sling.servlet.resourceTypes=["core/wcm/components/image", "cq/Page"]`, `sling.servlet.selectors=["coreimg"]`, `sling.servlet.extenions=["jpg","jpeg","png","gif"]` - for the Core Image v2 URLs

For more details about the servlet's configurations, check the `AdaptiveImageServletMappingConfigurationFactory` OSGi configuration
factory in the configuration console (`/system/console/configMgr`).

## URL formats
The URLs to which the `AdaptiveImageServlet` is bound have the following pattern:

```
/content/<project_path>/<page_path>/<component_path>/<component_name>.<configured selectors>.<width>.<configured extension>/<timestamp>.<configured extension>
```

  * `<width>` defines the client requested width, which is compared against the widths defined by an image's content policy configuration;
  when this selector is not present, the servlet will resize images to the default resize width, without overscaling;
  * `<timestamp>` defines the last modification date of the currently rendered Image Component; when this suffix is missing or not the
    expected value, the servlet will redirect the request to the correct URL

## Operations
The following operations, listed in the order in which they're actually applied, can be executed on an image:

1. cropping - based on the value of the `com.day.cq.commons.ImageResource#PN_IMAGE_CROP` property on the image resource; the value of
this property is a CSV list, defining two sets of (x, y) coordinates for the cropping rectangle;
2. rotation - based on the value of the `com.day.cq.commons.ImageResource#PN_IMAGE_ROTATE` property on the image resource; the value of
this property is an Integer number, defining the rotation angle;
3. horizontal flipping - based on the value of the `com.adobe.cq.wcm.core.components.models.Image#PN_FLIP_HORIZONTAL` property on the
image resource; the value is a Boolean;
4. vertical flipping - based on the value of the `com.adobe.cq.wcm.core.components.models.Image#PN_FLIP_VERTICAL` property on the
image resource; the value is a Boolean;
5. resizing - based on the `width` request selector defined above


----
<a name="core-image-component">1</a> - all Core Image Component's resource types (`core/wcm/components/image/v1/image`,
`core/wcm/components/image/v2/image`) inherit from this resource type

