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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyMapping;

public class TemplateEditor extends WCMUsePojo {

    private static final String DEFAULT_TEMPLATE_EDITOR_CLIENT_LIB = "wcm.foundation.components.parsys.allowedcomponents";
    private static final String POLICIES_MAPPING_PATH = "policies/jcr:content";
    private static final String PN_CLIENTLIBS = "clientlibs";

    private String[] templateCategories = ArrayUtils.EMPTY_STRING_ARRAY;
    private Page currentPage;

    @Override
    public void activate() throws Exception {
        currentPage = getCurrentPage();
        populateTemplateCategories();
    }

    public String[] getTemplateCategories() {
        return templateCategories;
    }

    private void populateTemplateCategories() {
        List<String> categories = new ArrayList<>();
        Template template = currentPage.getTemplate();
        if(template != null && template.hasStructureSupport()) {
            Resource templateResource = template.adaptTo(Resource.class);
            if(templateResource != null) {
                addDefaultTemplateEditorClientLib(templateResource, categories);
                addPolicyClientLibs(templateResource, categories);
            }
        }
        templateCategories = categories.toArray(new String[categories.size()]);
    }

    private void addDefaultTemplateEditorClientLib(Resource templateResource, List<String> categories) {
        if(currentPage.getPath().startsWith(templateResource.getPath())) {
            categories.add(DEFAULT_TEMPLATE_EDITOR_CLIENT_LIB);
        }
    }

    private void addPolicyClientLibs(Resource templateResource, List<String> categories) {
        Resource pageContentPolicyMapping = templateResource.getChild(POLICIES_MAPPING_PATH);
        ContentPolicyMapping contentPolicyMapping = pageContentPolicyMapping.adaptTo(ContentPolicyMapping.class);
        if(contentPolicyMapping != null) {
            ContentPolicy policy = contentPolicyMapping.getPolicy();
            if(policy != null) {
                String[] clientLibs = policy.getProperties().get(PN_CLIENTLIBS, ArrayUtils.EMPTY_STRING_ARRAY);
                Collections.addAll(categories, clientLibs);
            }
        }
    }
}
