/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
package com.adobe.cq.wcm.core.components.models.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.commons.RangeIterator;
import com.day.cq.search.Predicate;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = List.class,
       resourceType = ListImpl.RESOURCE_TYPE)
public class ListImpl implements List {

    protected static final String RESOURCE_TYPE = "core/wcm/components/list";
    private static final Logger LOGGER = LoggerFactory.getLogger(ListImpl.class);

    private static final String PN_SOURCE = "listFrom";
    private static final String PN_PAGES = "pages";
    private static final String PN_PARENT_PAGE = "parentPage";
    private static final String PN_TAGS = "tags";
    private static final String PN_TAGS_MATCH = "tagsMatch";
    private static final String PN_SAVED_QUERY = "savedquery";
    private static final int LIMIT_DEFAULT = 100;
    private static final String PN_LIMIT = "limit";
    private static final String PN_SHOW_THUMBNAIL = "showThumbnail";
    private static final boolean SHOW_THUMBNAIL_DEFAULT = false;
    private static final String PN_SHOW_DESCRIPTION = "showDescription";
    private static final boolean SHOW_DESCRIPTION_DEFAULT = false;
    private static final String PN_SHOW_MODIFICATION_DATE = "showModificationDate";
    private static final boolean SHOW_MODIFICATION_DATE_DEFAULT = false;
    private static final String PN_LINK_ITEM = "linkItem";
    private static final boolean LINK_ITEM_DEFAULT = false;
    private static final String PN_DEPTH = "childDepth";
    private static final int PN_DEPTH_DEFAULT = 1;
    private static final String PN_QUERY = "query";
    private static final String PN_SEARCH_IN = "searchIn";
    private static final String PN_SORT_ORDER = "sortOrder";
    private static final String PN_ORDER_BY = "orderBy";
    private static final String PN_DATA_FORMAT_DEFAULT = "yyyy-MM-dd";
    private static final String PN_DATA_FORMAT = "dateFormat";
    private static final java.lang.String PN_MAX_ITEMS = "maxItems";

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    private PageManager pageManager;
    private int limit;
    private java.util.List<ListItem> listItems;
    private boolean showThumbnail;
    private boolean showDescription;
    private boolean showModificationDate;
    private boolean linkItem;
    private int childDepth;
    private String query;
    private String startIn;
    private SortOrder sortOrder;
    private OrderBy orderBy;
    private DateFormat dateFormat;
    private int maxItems;

    @PostConstruct
    private void initModel() {
        pageManager = resourceResolver.adaptTo(PageManager.class);
        readProperties();
    }

    private void readProperties() {
        // read edit config properties
        limit = properties.get(PN_LIMIT, 0);
        if (limit == 0) {
            limit = LIMIT_DEFAULT;
        }
        childDepth = properties.get(PN_DEPTH, PN_DEPTH_DEFAULT);
        query = properties.get(PN_QUERY, StringUtils.EMPTY);
        startIn = properties.get(PN_SEARCH_IN, currentPage.getPath());
        sortOrder = SortOrder.fromString(properties.get(PN_SORT_ORDER, SortOrder.ASC.value));
        orderBy = OrderBy.fromString(properties.get(PN_ORDER_BY, StringUtils.EMPTY));
        maxItems = properties.get(PN_MAX_ITEMS, 0);

        // read design config properties
        showThumbnail = properties.get(PN_SHOW_THUMBNAIL, currentStyle.get(PN_SHOW_THUMBNAIL, SHOW_THUMBNAIL_DEFAULT));
        showDescription = properties.get(PN_SHOW_DESCRIPTION, currentStyle.get(PN_SHOW_DESCRIPTION, SHOW_DESCRIPTION_DEFAULT));
        showModificationDate = properties.get(
                PN_SHOW_MODIFICATION_DATE, currentStyle.get(PN_SHOW_MODIFICATION_DATE, SHOW_MODIFICATION_DATE_DEFAULT));
        linkItem = properties.get(PN_LINK_ITEM, currentStyle.get(PN_LINK_ITEM, LINK_ITEM_DEFAULT));
        try {
            dateFormat = new SimpleDateFormat(properties.get(PN_DATA_FORMAT, currentStyle.get(PN_DATA_FORMAT, PN_DATA_FORMAT_DEFAULT)));
        } catch (IllegalArgumentException e) {
            dateFormat = new SimpleDateFormat(PN_DATA_FORMAT_DEFAULT);
        }

    }

    @Override
    public Collection<ListItem> getListItems() {
        if (listItems == null) {
            Source listType = getListType();
            populateListItems(listType);
        }
        return listItems;
    }

    @Override
    public boolean linkItem() {
        return linkItem;
    }

    @Override
    public boolean showThumbnail() {
        return showThumbnail;
    }

    @Override
    public boolean showDescription() {
        return showDescription;
    }

    @Override
    public boolean showModificationDate() {
        return showModificationDate;
    }

    private Source getListType() {
        String listFromValue = properties.get(PN_SOURCE, currentStyle.get(PN_SOURCE, Source.CHILDREN.value));
        Source listType = Source.fromString(listFromValue);
        if (listType == null) {
            throw new IllegalArgumentException();
        }
        return listType;
    }

    private void populateListItems(Source listType) {
        switch (listType) {
            case STATIC:
                populateStaticListItems();
                break;
            case CHILDREN:
                populateChildListItems();
                break;
            case TAGS:
                populateTagListItems();
                break;
            case QUERY_BUILDER:
                populateQueryListItems();
                break;
            case SEARCH:
                populateSearchListItems();
                break;
            default:
                listItems = new ArrayList<>();
                break;
        }
        sortListItems();
        setMaxItems();
    }



    private void populateStaticListItems() {
        listItems = new ArrayList<>();
        String[] pagesPaths = properties.get(PN_PAGES, new String[0]);
        for (String path : pagesPaths) {
            Page page = pageManager.getContainingPage(path);
            if (page != null) {
                listItems.add(new ListItemImpl(page, dateFormat));
            }
        }
    }

    private void populateChildListItems() {
        listItems = new ArrayList<>();
        Page rootPage = getRootPage();
        if (rootPage != null) {
            collectChildren(rootPage.getDepth(), rootPage);
        }
    }

    private void collectChildren(int startLevel, Page parent) {
        Iterator<Page> childIterator = parent.listChildren();
        while (childIterator.hasNext()) {
            Page child = childIterator.next();
            listItems.add(new ListItemImpl(child, dateFormat));
            if (child.getDepth() - startLevel < childDepth) {
                collectChildren(startLevel, child);
            }
        }
    }

    private void populateTagListItems() {
        listItems = new ArrayList<>();
        String[] tags = properties.get(PN_TAGS, new String[0]);
        boolean matchAny = properties.get(PN_TAGS_MATCH, "any").equals("any");
        if (ArrayUtils.isNotEmpty(tags)) {
            Page rootPage = getRootPage();
            if (rootPage != null) {
                TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                RangeIterator<Resource> resourceRangeIterator = tagManager.find(rootPage.getPath(), tags, matchAny);
                if (resourceRangeIterator != null) {
                    while (resourceRangeIterator.hasNext()) {
                        Page containingPage = pageManager.getContainingPage(resourceRangeIterator.next());
                        if (containingPage != null) {
                            listItems.add(new ListItemImpl(containingPage, dateFormat));
                        }
                    }
                }
            }
        }
    }

    private void populateQueryListItems() {
        listItems = new ArrayList<>();
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        if (queryBuilder != null && session != null) {
            try {
                Query query = queryBuilder.loadQuery(resource.getPath() + "/" + PN_SAVED_QUERY, session);
                if (query != null) {
                    query.setHitsPerPage(limit);
                    SearchResult result = query.getResult();
                    collectSearchResults(result);
                }
            } catch (RepositoryException | IOException e) {
                LOGGER.error("Unable to load stored query for " + resource.getPath(), e);
            }
        } else {
            LOGGER.error("Error loading query builder.");
        }
    }

    private void populateSearchListItems() {
        listItems = new ArrayList<>();
        if (!StringUtils.isEmpty(query)) {
            SimpleSearch search = resource.adaptTo(SimpleSearch.class);
            if (search != null) {
                search.setQuery(query);
                search.setSearchIn(startIn);
                search.addPredicate(new Predicate("type", "type").set("type", NameConstants.NT_PAGE));
                search.setHitsPerPage(limit);
                try {
                    collectSearchResults(search.getResult());
                } catch (RepositoryException e) {
                    LOGGER.error("Unable to retrieve search results for query.", e);
                }
            }
        }
    }

    private void collectSearchResults(SearchResult result) throws RepositoryException {
        for (Hit hit : result.getHits()) {
            Page containingPage = pageManager.getContainingPage(hit.getResource());
            if (containingPage != null) {
                listItems.add(new ListItemImpl(containingPage, dateFormat));
            }
        }
    }

    private void sortListItems() {
        if (orderBy != null) {
            Collections.sort(listItems, new ListSort(orderBy, sortOrder));
        }
    }

    private void setMaxItems() {
        if (maxItems != 0) {
            java.util.List<ListItem> tmpListItems = new ArrayList<>();
            for (ListItem listItem : listItems) {
                if (tmpListItems.size() < maxItems) {
                    tmpListItems.add(listItem);
                } else {
                    break;
                }
            }
            listItems = tmpListItems;
        }
    }

    private Page getRootPage() {
        String parentPath = properties.get(PN_PARENT_PAGE, currentPage.getPath());
        return pageManager.getContainingPage(resourceResolver.getResource(parentPath));
    }


    private enum Source {
        CHILDREN("children"),
        STATIC("static"),
        SEARCH("search"),
        QUERY_BUILDER("querybuilder"),
        TAGS("tags");

        private String value;

        Source(String value) {
            this.value = value;
        }

        public static Source fromString(String value) {
            for (Source s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }

    private enum SortOrder {
        ASC("asc"),
        DESC("desc");

        private String value;

        SortOrder(String value) {
            this.value = value;
        }

        public static SortOrder fromString(String value) {
            for (SortOrder s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return ASC;
        }
    }

    private enum OrderBy {
        TITLE("title"),
        MODIFIED("modified");

        private String value;

        OrderBy(String value) {
            this.value = value;
        }

        public static OrderBy fromString(String value) {
            for (OrderBy s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }

    private class ListSort implements Comparator<ListItem> {

        private SortOrder sortOrder;
        private OrderBy orderBy;

        public ListSort(OrderBy orderBy, SortOrder sortOrder) {
            this.orderBy = orderBy;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(ListItem listItem1, ListItem listItem2) {
            int i = 0;
            if (orderBy == OrderBy.MODIFIED) {
                i = listItem1.getModificationDate().compareTo(listItem2.getModificationDate());
            } else {
                i = listItem1.getTitle().compareTo(listItem2.getTitle());
            }

            if (sortOrder == SortOrder.DESC) {
                i = i * -1;
            }
            return i;
        }
    }
}
