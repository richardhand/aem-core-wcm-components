/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
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
package com.adobe.cq.wcm.core.components.models;

import java.util.Set;

/**
 * Defines the {@code Image} Sling Model used for the {@code /apps/core/wcm/components/image} component.
 */
public interface Image {

    /**
     * Returns the set of supported width for adaptive images.
     *
     * @return the set of supported widths for adaptive images
     */
    Set<Integer> getSupportedRenditionWidths();

    /**
     * Returns the value for the {@code src} attribute of the image.
     *
     * @return the image's URL
     */
    String getSrc();

    /**
     * Returns the array of allowed rendition widths, if one was defined for the component's design.
     *
     * @return the array of allowed rendition widths, or an empty array if the allowed renditions widths have not been configured for the
     * component's design
     */
    Integer[] getSmartSizes();

    /**
     * Returns an array of URLs for smart images, if the component's design provides an array of allowed rendition widths.
     *
     * @return the array of URLs for smart images, or an empty array if the component's design doesn't provide an array of allowed
     * rendition widths
     */
    String[] getSmartImages();

    /**
     * Returns {@code true} if the image should be loaded lazily, {@false otherwise}.
     *
     * @return {@code true} if the image should be loaded lazily, {@false otherwise}
     */
    boolean isLazyLoadingEnabled();

    /**
     * Returns the value for the image's {@code alt} attribute, if one was set.
     *
     * @return the value, if one was set, or {@code null}
     */
    String getAlt();

    /**
     * Returns the value for the image's {@code title} attribute, if one was set.
     *
     * @return the value, if one was set, or {@code null}
     */
    String getTitle();

    /**
     * Returns the image's link URL, if one was set.
     *
     * @return the image's link URL, if one was set, or {@code null}
     */
    String getLink();

    /**
     * Returns {@code true} if the image should display its caption as a popup (through the <code>&lt;img&gt;</code> {@code title}
     * attribute).
     *
     * @return {@code true} if the caption should be displayed as a popup
     */
    boolean shouldDisplayCaptionPopup();

}
