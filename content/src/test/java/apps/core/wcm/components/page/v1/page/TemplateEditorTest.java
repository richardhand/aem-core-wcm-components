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

import javax.script.Bindings;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.adobe.cq.wcm.core.components.testing.WCMUsePojoBaseTest;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@PrepareForTest(TemplateEditor.class)
public class TemplateEditorTest extends WCMUsePojoBaseTest<TemplateEditor> {

    static {
        TEST_CONTENT_ROOT = "/content/mysite";
        TEST_BASE = "/page";
    }

    public static final String TEMPLATED_PAGE = TEST_CONTENT_ROOT + "/templated-page";

    @Test
    public void testGetTemplateCategories() throws Exception {
        context.load().json(TEST_BASE + "/test-conf.json", "/conf/we-retail/settings");
        Resource templateResource = context.resourceResolver().getResource("/conf/we-retail/settings/wcm/templates/product-page");
        Bindings bindings = getResourceBackedBindings(TEMPLATED_PAGE);
        Page currentPageSpy = PowerMockito.spy(context.currentPage());
        Template template = mock(Template.class);
        doReturn(true).when(template).hasStructureSupport();
        doReturn(templateResource).when(template).adaptTo(Resource.class);
        doReturn(template).when(currentPageSpy).getTemplate();
        TemplateEditor templateEditor = getSpiedObject();
        doReturn(currentPageSpy).when(templateEditor).getCurrentPage();
        templateEditor.init(bindings);
        assertEquals("we-retail.product-page", templateEditor.getTemplateCategories()[0]);
    }


}