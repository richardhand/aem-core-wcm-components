/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2016 Adobe Systems Incorporated
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
package apps.core.wcm.components.page.v1.page;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;

public class Head extends WCMUsePojo {

    public String[] keywords;
    public String designPathCSS;
    public String title;
    public String staticDesignPath;

    private static final String FN_ICO_FAVICON = "favicon.ico";
    private static final String FN_PNG_FAVICON = "favicon_32.png";
    private static final String FN_TOUCH_ICON_60 = "touch-icon_60.png";
    private static final String FN_TOUCH_ICON_76 = "touch-icon_76.png";
    private static final String FN_TOUCH_ICON_120 = "touch-icon_120.png";
    private static final String FN_TOUCH_ICON_152 = "touch-icon_152.png";

    private String icoFavicon;
    private String pngFavicon;
    private String touchIcon60;
    private String touchIcon76;
    private String touchIcon120;
    private String touchIcon152;

    @Override
    public void activate() throws Exception {
        Page currentPage = getCurrentPage();
        Tag[] tags = currentPage.getTags();
        ResourceResolver resourceResolver = getResourceResolver();
        keywords = new String[tags.length];
        int index = 0;
        for (Tag tag : tags) {
            keywords[index++] = tag.getTitle();
        }
        Design design = getCurrentDesign();
        if (design != null) {
            String designPath = design.getPath();
            if (StringUtils.isNotEmpty(designPath)) {
                designPathCSS = designPath + ".css";
            }
            if (resourceResolver.getResource(designPath + "/static.css") != null) {
                staticDesignPath = designPath + "/static.css";
            }
            loadFavicons(resourceResolver, designPath);
        }
        title = currentPage.getTitle();
        if (StringUtils.isEmpty(title)) {
            title = currentPage.getName();
        }
    }

    /**
     * Loads the favicon paths for the various favicons into their respective variables.
     * If the favicon exists at its path, then the path is stored in its variable, 
     * otherwise a null value is stored in the respective variable. 
     * @param resourceResolver The {@link ResourceResolver} to use for checking if the favicon exists under the specified {@code designPath}
     * @param designPath The design path under which the favicon should be present
     */
    private void loadFavicons(ResourceResolver resourceResolver, String designPath) {
        icoFavicon = getFaviconPath(resourceResolver, designPath, FN_ICO_FAVICON);
        pngFavicon = getFaviconPath(resourceResolver, designPath, FN_PNG_FAVICON);
        touchIcon60 = getFaviconPath(resourceResolver, designPath, FN_TOUCH_ICON_60);
        touchIcon76 = getFaviconPath(resourceResolver, designPath, FN_TOUCH_ICON_76);
        touchIcon120 = getFaviconPath(resourceResolver, designPath, FN_TOUCH_ICON_120);
        touchIcon152 = getFaviconPath(resourceResolver, designPath, FN_TOUCH_ICON_152);
    }

    /**
     * Generates the path for the given {@code faviconName}
     * @param resourceResolver The {@link ResourceResolver} to use for checking if the favicon exists under the specified {@code designPath}
     * @param designPath The design path under which the favicon with name {@code faviconName} should be present
     * @param faviconName The name of the favicon for which the path is to be generated
     * @return The path of the favicon ( which is path concatenation of {@code designPath} and {@code faviconName} if
     * the favicon with {@code faviconName} exists under the given {@code designPath}, otherwise <br>
     *     {@code null} if the favicon with given {@code faviconName} does not exist
     */
    private String getFaviconPath(ResourceResolver resourceResolver, String designPath, String faviconName) {
        String path = designPath + "/"+ faviconName;
        if (resourceResolver.getResource(path) == null) {
            path = null;
        }
        return path;
    }

    /**
     * Returns an array with the page's keywords.
     *
     * @return an array of keywords represented as {@link String}s; the array can be empty if no keywords have been defined for the page
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * Retrieves the page's design path.
     *
     * @return the design path as a {@link String}
     */
    public String getDesignPath() {
        return designPathCSS;
    }

    /**
     * Retrieves the static design path if {@code static.css} exists in the design path.
     *
     * @return the static design path if it exists, {@code null} otherwise
     */
    public String getStaticDesignPath() {
        return staticDesignPath;
    }

    /**
     * Retrieves the ICO favicon's path ({@code favicon.ico} file), relative to the page's design path.
     *
     * @return the path to the {@code favicon.ico} file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    public String getICOFavicon() {
        return icoFavicon;
    }

    /**
     * Retrieves the PNG favicon's path ({@code favicon.png} file), relative to the page's design path.
     *
     * @return the path to the {@code favicon.png} file relative to the page's design path, if the file exists; {@code null} otherwise
     */
    public String getPNGFavicon() {
        return pngFavicon;
    }

    public String getTouchIcon60() {
        return touchIcon60;
    }

    public String getTouchIcon76() {
        return touchIcon76;
    }

    public String getTouchIcon120() {
        return touchIcon120;
    }

    public String getTouchIcon152() {
        return touchIcon152;
    }

    /**
     * Retrieves the page's title.
     *
     * @return the page title
     */
    public String getTitle() {
        return title;
    }
    
    
}
