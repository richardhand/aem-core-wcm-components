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

import java.util.Map;

/**
 * Helper for page functionality related to page sharing by user on social media platforms.
 */
public interface SocialMediaHelper {
    /**
     * Returns {@code true} if Facebook sharing is enabled in page configuration, {@code false} otherwise.
     */
    public boolean isFacebookEnabled();

    /**
     * Returns {@code true} if Pinterest sharing is enabled in page configuration, {@code false} otherwise.
     */
    public boolean isPinterestEnabled();
    /**
     * Returns {@code true} if a supported social media sharing is enabled in page configuration, {@code false} otherwise.
     */
    public boolean isSocialMediaEnabled();

    /**
     * Returns {@code true} if Facebook sharing is enabled in page configuration
     * and the page contains the sharing component, {@code false} otherwise.
     */
    public boolean hasFacebookSharing();

    /**
     * Returns {@code true} if Pinterest sharing is enabled in page configuration
     * and the page contains the sharing component, {@code false} otherwise.
     */
    public boolean hasPinterestSharing();

    /**
     * Returns the social media metadata for the current page.
     */
    public Map<String, String> getMetadata();
}
