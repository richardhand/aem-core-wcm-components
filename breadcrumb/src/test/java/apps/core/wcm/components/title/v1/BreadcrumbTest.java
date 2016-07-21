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
package apps.core.wcm.components.title.v1;

import javax.script.Bindings;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import apps.core.wcm.components.breadcrumb.v1.breadcrumb.Breadcrumb;
import com.adobe.cq.wcm.core.components.commons.AuthoringUtils;
import com.adobe.cq.wcm.core.components.testing.WCMUsePojoBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@PrepareForTest({Breadcrumb.class, AuthoringUtils.class})
public class BreadcrumbTest extends WCMUsePojoBaseTest<Breadcrumb> {

    static {
        TEST_ROOT = "/content/breadcrumb/women";
    }

    public static final String RESOURCE_DEVI_SLEEVELESS_SHIRT = TEST_ROOT + "/shirts/devi-sleeveless-shirt";
    public static final String RESOURCE_ABSOLUTE_PARENT = RESOURCE_DEVI_SLEEVELESS_SHIRT + "/jcr:content/header/breadcrumb-abs-parent";
    public static final String RESOURCE_RELATIVE_PARENT = RESOURCE_DEVI_SLEEVELESS_SHIRT + "/jcr:content/header/breadcrumb-rel-parent";
    public static final String RESOURCE_UNLINK_CURRENT_PAGE = RESOURCE_DEVI_SLEEVELESS_SHIRT +
            "/jcr:content/header/breadcrumb-unlink-current-item";

    @Test
    public void testBreadcrumbConsistency() {
        Breadcrumb breadcrumb = getSpiedObject(RESOURCE_DEVI_SLEEVELESS_SHIRT);
        checkBreadcrumbConsistency(breadcrumb, new String[]{"Women", "Shirts", "Devi Sleeveless Shirt"});
    }

    @Test
    public void testBreadcrumbConsistencyWithAbsoluteParent() {
        final Resource resource = context.resourceResolver().getResource(RESOURCE_ABSOLUTE_PARENT);
        Bindings bindings = getResourceBackedBindings(RESOURCE_ABSOLUTE_PARENT);
        Resource resourceSpy = PowerMockito.spy(resource);
        Breadcrumb breadcrumb = getSpiedObject();
        doReturn(resourceSpy).when(breadcrumb).getResource();
        breadcrumb.init(bindings);
        checkBreadcrumbConsistency(breadcrumb, new String[]{"Shirts", "Devi Sleeveless Shirt"});
    }

    @Test
    public void testBreadcrumbConsistencyWithRelativeStopLevel() {
        final Resource resource = context.resourceResolver().getResource(RESOURCE_RELATIVE_PARENT);
        Bindings bindings = getResourceBackedBindings(RESOURCE_RELATIVE_PARENT);
        Resource resourceSpy = PowerMockito.spy(resource);
        Breadcrumb breadcrumb = getSpiedObject();
        doReturn(resourceSpy).when(breadcrumb).getResource();
        breadcrumb.init(bindings);
        checkBreadcrumbConsistency(breadcrumb, new String[]{"Women", "Shirts"});
    }

    @Test
    public void testBreadcrumbConsistencyWithUnlinkCurrentItem() {
        final Resource resource = context.resourceResolver().getResource(RESOURCE_UNLINK_CURRENT_PAGE);
        Bindings bindings = getResourceBackedBindings(RESOURCE_UNLINK_CURRENT_PAGE);
        Resource resourceSpy = PowerMockito.spy(resource);
        Breadcrumb breadcrumb = getSpiedObject();
        doReturn(resourceSpy).when(breadcrumb).getResource();
        breadcrumb.init(bindings);
        checkBreadcrumbConsistency(breadcrumb, new String[]{"Women", "Shirts", "Devi Sleeveless Shirt"});
        assertEquals(StringUtils.EMPTY, breadcrumb.getItems().get(2).getPath());
    }

    private void checkBreadcrumbConsistency(Breadcrumb breadcrumb, String[] expectedPages) {
        assertTrue("Expected that the returned breadcrumb will contain " + expectedPages.length + " items",
                breadcrumb.getItems().size() == expectedPages.length);
        int index = 0;
        for (Breadcrumb.BreadcrumbItem item : breadcrumb.getItems()) {
            assertEquals(expectedPages[index++], item.getTitle());
        }
    }

}
