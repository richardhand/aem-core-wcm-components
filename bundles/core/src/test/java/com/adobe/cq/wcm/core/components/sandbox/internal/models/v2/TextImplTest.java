/*******************************************************************************
 * Copyright 2017 Adobe Systems Incorporated
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.adobe.cq.wcm.core.components.sandbox.internal.models.v2;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Test;

import com.adobe.cq.wcm.core.components.sandbox.models.Text;

import static org.junit.Assert.assertEquals;

public class TextImplTest extends com.adobe.cq.wcm.core.components.internal.models.v1.TextImplTest {

    @Test
    public void testExportedType() {
        Resource resource = context.currentResource("/content/text/rich-text");
        MockSlingHttpServletRequest request = context.request();
        SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
        bindings.put(SlingBindings.RESOURCE, resource);
        Text text = request.adaptTo(Text.class);

        assertEquals("core/wcm/sandbox/components/text/v2/text", text.getExportedType());
    }
}
