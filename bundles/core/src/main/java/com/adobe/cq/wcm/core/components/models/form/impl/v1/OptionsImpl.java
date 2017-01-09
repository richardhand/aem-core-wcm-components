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
package com.adobe.cq.wcm.core.components.models.form.impl.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;
import javax.servlet.RequestDispatcher;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.cq.wcm.core.components.models.Constants;
import com.adobe.cq.wcm.core.components.models.form.OptionItem;
import com.adobe.cq.wcm.core.components.models.form.Options;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = Options.class,
       resourceType = OptionsImpl.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME,
          extensions = Constants.EXPORTER_EXTENSION)
public class OptionsImpl implements Options {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionsImpl.class);

    protected static final String RESOURCE_TYPE = "core/wcm/components/form/options/v1/options";
    protected static final String PN_TYPE = "type";

    private static final String OPTION_ITEMS_PATH = "optionitems";

    private static final String ID_PREFIX = "form-options";

    @ChildResource(optional = true)
    @Named(OPTION_ITEMS_PATH)
    private List<Resource> itemResources;

    @ValueMapValue(optional = true)
    private String name;

    @ValueMapValue(optional = true)
    private String helpMessage;

    @ValueMapValue(optional = true)
    private String caption;

    @ValueMapValue(name = OptionsImpl.PN_TYPE,
                   optional = true)
    private String typeString;
    private String typeValue;

    @SlingObject
    private Resource resource;

    private List<OptionItem> optionItems;
    private String id;

    @ValueMapValue(optional = true)
    private String source;

    @ValueMapValue(optional = true)
    private String fromList;

    @ValueMapValue(optional = true)
    private String fromDatasource;

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private SlingHttpServletResponse response;

    @SlingObject
    private ResourceResolver resolver;

    @Override
    public List<OptionItem> getOptionItems() {
        if (optionItems == null) {
            populateOptionItems();
        }
        return optionItems;
    }

    @Override
    public String getId() {
        if (id == null) {
            populateId();
        }
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelpMessage() {
        return helpMessage;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getType() {
        if (typeValue == null) {
            typeValue = Type.fromString(typeString).getValue();
        }
        return typeValue;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public String getSource() {
        Source from = Source.getSource(source);
        if (from != null) {
            return from.getElement();
        }
        return null;
    }

    @Override
    public String getFromList() {
        return fromList;
    }

    @Override
    public String getFromDatasource() {
        return fromDatasource;
    }


    /* ------------------------ Internal stuff -------------------------------------------- */

    private void populateOptionItems() {
        this.optionItems = new ArrayList<>();
        if (StringUtils.equals(source, Source.DATASOURCE.getElement()) && StringUtils.isNotEmpty(fromDatasource)) {
            populateOptionItemsFromDatasource();
        } else if (StringUtils.equals(source, Source.LIST.getElement()) && StringUtils.isNotEmpty(fromList)) {
            populateOptionItemsFromList();
        } else {
            populateOptionItemsFromLocal();
        }
    }

    private void populateOptionItemsFromLocal() {
        if (itemResources != null) {
            for (Resource itemResource : itemResources) {
                OptionItem optionItem = itemResource.adaptTo(OptionItem.class);
                if (optionItem != null && (optionItem.isDisabled() || StringUtils.isNotBlank(optionItem.getValue()))) {
                    optionItems.add(optionItem);
                }
            }
        }
    }

    private void populateOptionItemsFromList() {
        Resource parent = resolver.getResource(fromList);
        if (parent != null) {
            for(Resource itemResource: parent.getChildren()) {
                OptionItem optionItem = itemResource.adaptTo(OptionItem.class);
                if (optionItem != null && (optionItem.isDisabled() || StringUtils.isNotBlank(optionItem.getValue()))) {
                    optionItems.add(optionItem);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void populateOptionItemsFromDatasource() {

        // build the options by running the datasource code (the list is set as a request attribute)
        RequestDispatcherOptions opts = new RequestDispatcherOptions();
        opts.setForceResourceType(fromDatasource);
        RequestDispatcher dispatcher = request.getRequestDispatcher(resource, opts);
        try {
            if (dispatcher != null) {
                dispatcher.include(request, response);
            } else {
                LOGGER.error("Failed to include the datasource at " + fromDatasource);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to include the datasource at " + fromDatasource, e);
        }

        // retrieve the datasource from the request and adapt it to form options
        SimpleDataSource dataSource = (SimpleDataSource) request.getAttribute(DataSource.class.getName());
        if (dataSource != null) {
            Iterator<Resource> itemIterator = dataSource.iterator();
            if (itemIterator != null) {
                while (itemIterator.hasNext()) {
                    Resource item = itemIterator.next();
                    OptionItem optionItem = item.adaptTo(OptionItem.class);
                    if (optionItem != null && (optionItem.isDisabled() || StringUtils.isNotBlank(optionItem.getValue()))) {
                        optionItems.add(optionItem);
                    }
                }
            }
        }
    }

    private void populateId() {
        id = ID_PREFIX + "-" + String.valueOf(Math.abs(resource.getPath().hashCode()));
    }

    public enum Type {
        CHECKBOX("checkbox"),
        RADIO("radio"),
        DROP_DOWN("drop-down"),
        MULTI_DROP_DOWN("multi-drop-down");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromString(String value) {
            for (Type type : Type.values()) {
                if (StringUtils.equals(value, type.value)) {
                    return type;
                }
            }
            return CHECKBOX;
        }

        public String getValue() {
            return value;
        }
    }

    private enum Source {

        LOCAL("local"),
        LIST("list"),
        DATASOURCE("datasource");

        private String element;

        Source(String element) {
            this.element = element;
        }

        private static Source getSource(String value) {
            for (Source source : values()) {
                if (StringUtils.equalsIgnoreCase(source.element, value)) {
                    return source;
                }
            }
            return null;
        }

        public String getElement() {
            return element;
        }
    }

}
