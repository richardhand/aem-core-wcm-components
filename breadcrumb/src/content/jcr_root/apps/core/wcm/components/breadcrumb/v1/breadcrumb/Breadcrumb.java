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
package apps.core.wcm.components.breadcrumb.v1.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.cq.wcm.core.components.commons.AuthoringUtils;
import com.day.cq.wcm.api.Page;

public class Breadcrumb extends WCMUsePojo {

    private static final String PROP_PARENT_LEVEL = "absParent";
    private static final int PROP_PARENT_LEVEL_DEFAULT = 2;
    private static final String PROP_RELATIVE_STOP_LEVEL = "relParent";
    private static final int PROP_RELATIVE_STOP_LEVEL_DEFAULT = 0;
    private static final String PROP_DELIMITER = "delim";
    private static final String PROP_DELIMITER_DEFAULT = "&nbsp;&gt;&nbsp;";
    private static final String PROP_TRAIL_DELIMITER = "trail";
    private static final String PROP_TRAIL_DELIMITER_DEFAULT = "";
    private static final String PROP_UNLINK_CURRENT_ITEM = "unlinkCurrentItem";
    private static final boolean PROP_UNLINK_CURRENT_ITEM_DEFAULT = false;

    private List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
    private ValueMap properties;
    private Integer level;
    private Integer endLevel;
    private String delimiter;
    private String trailingDelimiter;
    private Boolean unlinkCurrentItem;
    private Page currentPage;
    private SlingHttpServletRequest request;

    @Override
    public void activate() throws Exception {
        properties = getProperties();
        currentPage = getCurrentPage();
        request = getRequest();
        readBreadcrumbConfiguration();
        populateItems();
    }

    private void populateItems() {
        int currentLevel = currentPage.getDepth();
        while (level < currentLevel - endLevel) {
            Page page = currentPage.getAbsoluteParent(level);
            if (page != null) {
                BreadcrumbItem breadcrumbItem = new BreadcrumbItem(getTitle(page), getPath(page));
                breadcrumbItems.add(breadcrumbItem);
                level++;
            } else {
                break;
            }
        }
    }

    private String getPath(Page page) {
        if(unlinkCurrentItem && page.equals(currentPage)) {
            return StringUtils.EMPTY;
        } else {
            return page.getPath();
        }
    }

    private String getTitle(Page page) {
        String title = page.getNavigationTitle();
        if (StringUtils.isEmpty(title)) {
            title = page.getTitle();
            if (StringUtils.isEmpty(title)) {
                title = page.getName();
            }
        }
        return title;
    }


    private void readBreadcrumbConfiguration() {
        level = properties.get(PROP_PARENT_LEVEL, PROP_PARENT_LEVEL_DEFAULT);
        endLevel = properties.get(PROP_RELATIVE_STOP_LEVEL, PROP_RELATIVE_STOP_LEVEL_DEFAULT);
        delimiter = properties.get(PROP_DELIMITER, PROP_DELIMITER_DEFAULT);
        trailingDelimiter = properties.get(PROP_TRAIL_DELIMITER, PROP_TRAIL_DELIMITER_DEFAULT);
        unlinkCurrentItem = properties.get(PROP_UNLINK_CURRENT_ITEM, PROP_UNLINK_CURRENT_ITEM_DEFAULT);
    }

    public List<BreadcrumbItem> getItems() {
        return breadcrumbItems;
    }

    public boolean isTouch() {
        return AuthoringUtils.isTouch(request);
    }

    public boolean isEmpty() {
        return breadcrumbItems.isEmpty();
    }

    public String getDelimiter() {
        return delimiter;
    }

    public boolean hasDelimiter() {
        return StringUtils.isNotEmpty(delimiter);
    }

    public String getTrailingDelimiter() {
        return trailingDelimiter;
    }

    public boolean hasTrailingDelimiter() {
        return StringUtils.isNotEmpty(delimiter);
    }

    public class BreadcrumbItem {
        private String title;
        private String path;

        /**
         * Create a {@code BreadcrumbItem}
         *
         * @param title the item's title
         * @param path  the item's path
         */
        public BreadcrumbItem(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }
    }

}
