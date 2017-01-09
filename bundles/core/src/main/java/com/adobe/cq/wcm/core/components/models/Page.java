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

import java.util.Calendar;

/**
 * Defines the {@code Page} Sling Model used for the {@code /apps/core/wcm/components/page} component.
 */
public interface Page {

    /**
     * Returns the language of this page, if one has been defined. Otherwise the default {@link java.util.Locale} will be used.
     *
     * @return the language code (IETF BCP 47) for this page
     */
    String getLanguage();

    /**
     * Returns the last modified date of this page.
     *
     * @return {@link Calendar} representing the last modified date
     */
    Calendar getLastModifiedDate();

    /**
     * Returns an array with the page's keywords.
     *
     * @return an array of keywords represented as {@link String}s; the array can be empty if no keywords have been defined for the page
     */
    String[] getKeywords();

    /**
     * Retrieves the page's design path.
     *
     * @return the design path as a {@link String}
     */
    String getDesignPath();

    /**
     * Retrieves the static design path if {@code static.css} exists in the design path.
     *
     * @return the static design path if it exists, {@code null} otherwise
     */
    String getStaticDesignPath();

    /**
     * Retrieves the ICO favicon's path ({@code favicon.ico} file), relative to the page's design path.
     *
     * @return the path to the {@code favicon.ico} file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getICOFavicon();

    /**
     * Retrieves the PNG favicon's path ({@code favicon.png} file), relative to the page's design path.
     *
     * @return the path to the {@code favicon.png} file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getPNGFavicon();

    /**
     * Retrieves the path of the 60px wide touch icon that will be used as a Webpage Icon for Web Clip on iOS devices, relative to the
     * page's design path.
     *
     * @return the path to the icon file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getTouchIcon60();

    /**
     * Retrieves the path of the 76px wide touch icon that will be used as a Webpage Icon for Web Clip on iOS devices, relative to the
     * page's design path.
     *
     * @return the path to the icon file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getTouchIcon76();

    /**
     * Retrieves the path of the 120px wide touch icon that will be used as a Webpage Icon for Web Clip on iOS devices, relative to the
     * page's design path.
     *
     * @return the path to the icon file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getTouchIcon120();

    /**
     * Retrieves the path of the 152px wide touch icon that will be used as a Webpage Icon for Web Clip on iOS devices, relative to the
     * page's design path.
     *
     * @return the path to the icon file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    String getTouchIcon152();

    /**
     * Retrieves the page's title.
     *
     * @return the page title
     */
    String getTitle();

    /**
     * If this page is associated with a Template, then this method will return the Template's client libraries categories.
     *
     * @return an array of client libraries categories; the array can be empty if the page doesn't have an associated template or if the
     * template has no client libraries
     */
    String[] getClientLibCategories();

}
