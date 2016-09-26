/*
 Copyright 2016 Adobe Systems Incorporated

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and limitations under the License.
 */

package com.adobe.cq.wcm.core.components.commons;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertEquals;

public class ComponentUtilsTest {

    @Rule
    public final AemContext context = new AemContext();

    @Before
    public void setUp() throws Exception {
        context.load().json("/test-content.json", "/content/list");
        context.load().json("/test-apps.json", "/apps/core/wcm/components/list");
    }

    @Test
    public void testGetComponentTitle() throws Exception {
        Resource resource = context.currentResource("/content/list/jcr:content/sidebar/list-children");
        assertEquals("default", ComponentUtils.getComponentTitle(resource, "default"));
        resource = context.currentResource("/content/list/jcr:content/sidebar/list-static");
        assertEquals("List Component", ComponentUtils.getComponentTitle(resource, "default"));
    }
}