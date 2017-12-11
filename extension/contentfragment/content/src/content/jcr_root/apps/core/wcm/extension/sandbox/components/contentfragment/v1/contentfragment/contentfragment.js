/*******************************************************************************
 * Copyright 2017 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
use(function () {

    /**
     * Transforms Java strings and arrays into their native JavaScript counterparts, proceeding recursively
     * through the supplied object.
     */
    function transform (object) {
        for (var key in object) {
            var value = object[key];
            if (!value) {
                // skip undefined / empty values
                continue;
            }
            if (value instanceof Packages.java.lang.String) {
                // wrap Java strings in JavaScript strings
                object[key] = new String(value);
            } else if (value.getClass && value.getClass().isArray && value.getClass().isArray()) {
                // use 'map' to get entries into a JavaScript array
                var array = value.map(function (item) { return item; });
                // recurse
                object[key] = transform(array);
            } else {
                try {
                    if (typeof value === "object") {
                        // recurse
                        transform(value);
                    }
                } catch (error) {
                    // 'typeof' throws an error if the type is not a JavaScript type:
                    // 'Invalid JavaScript value of type ...'
                }
            }
        }
        return object;
    }

    /* parameters */

    var fragment = this.fragment;
    var properties = this.properties;

    /* assemble JSON object */

    var json = {
        title: fragment.title,
        path: properties.fragmentPath,
        variation: properties.variationName,
        elements: properties.elementNames
    };

    var associatedContent = fragment.associatedContent && fragment.associatedContent.toArray();
    if (associatedContent && associatedContent.length > 0) {
        json.associatedContent = associatedContent.map(function (resource) {
            // get title and path from collection resource
            var properties = resource.adaptTo(Packages.org.apache.sling.api.resource.ValueMap);
            return {
                title: properties.get("jcr:title") || undefined,
                path: resource.path
            }
        });
    }

    return {
        // transform the JSON object to prevent 'JSON.stringify' from throwing an error:
        // 'no public instance field or method named "toJSON"'
        string: JSON.stringify(transform(json))
    };

});
