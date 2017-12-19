/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.sandbox.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.osgi.annotation.versioning.ConsumerType;

import javax.annotation.Nonnull;

/**
 * Defines the {@code Image} Sling Model used for the {@code /apps/core/wcm/sandbox/components/image} component.
 */
@ConsumerType
public interface Image extends com.adobe.cq.wcm.core.components.models.Image, ComponentExporter {

    /**
     * Name of the configuration policy property that will indicate if the value of the {@code alt} attribute should be populated from
     * DAM if the component is configured with a file reference.
     *
     * @since com.adobe.cq.wcm.core.components.sandbox.models 4.2.0
     */
    String PN_ALT_VALUE_FROM_DAM = "altValueFromDAM";

    /**
     * Name of the configuration policy property that will indicate if the value of the {@code title} attribute should be populated from
     * DAM if the component is configured with a file reference.
     *
     * @since com.adobe.cq.wcm.core.components.sandbox.models 4.2.0
     */
    String PN_TITLE_VALUE_FROM_DAM = "titleValueFromDAM";

    @Override
    @JsonIgnore
    default String getJson() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the alternative image widths (in pixels), configured through the {@link #PN_DESIGN_ALLOWED_RENDITION_WIDTHS}
     * content policy. If no configuration is present, this method will return an empty array.
     *
     * @return the alternative image widths (in pixels)
     * @since com.adobe.cq.wcm.core.components.sandbox.models 9.0.0
     */
    @Nonnull
    default int[] getWidths() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a URI template representation of the image src attribute that can be variable expanded
     * to a URI reference. Useful for building an alternative image configuration from the original src.
     *
     * @return the image src URI template
     * @since com.adobe.cq.wcm.core.components.sandbox.models 9.0.0
     */
    default String getSrcUriTemplate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Indicates if the image should be rendered lazily or not.
     *
     * @return true if the image should be rendered lazily; false otherwise
     * @since com.adobe.cq.wcm.core.components.sandbox.models 2.3.0
     */
    default boolean isLazyEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    @JsonIgnore
    default String getFileReference() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    default String getExportedType() {
        throw new UnsupportedOperationException();
    }

}
