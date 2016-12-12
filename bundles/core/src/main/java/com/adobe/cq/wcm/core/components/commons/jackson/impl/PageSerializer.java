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
package com.adobe.cq.wcm.core.components.commons.jackson.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class PageSerializer extends StdSerializer<Page> {

    public PageSerializer() {
        this(null);
    }

    public PageSerializer(Class<Page> t) {
        super(t);
    }

    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        Map<String, String> pageProperties = getPageProperties(page);
        jsonGenerator.writeStartObject();
        for (String key : pageProperties.keySet()) {
            jsonGenerator.writeStringField(key, pageProperties.get(key));
        }
        jsonGenerator.writeEndObject();
    }

    private Map<String, String> getPageProperties(Page page) {
        Map<String, String> properties = new HashMap<>();
        properties.put("name", page.getName());
        properties.put("title", page.getTitle());
        properties.put("pageTitle", page.getPageTitle());
        properties.put("path", page.getPath());
        properties.put("description", page.getDescription());
        return properties;
    }
}
