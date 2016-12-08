/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.adobe.cq.wcm.core.components.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyMapping;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = Page.class,
        resourceType = PageImpl.RESOURCE_TYPE
)
public class PageImpl implements Page {

    public static final String RESOURCE_TYPE = "core/wcm/components/page";

    @ScriptVariable
    private com.day.cq.wcm.api.Page currentPage;

    @ScriptVariable
    private ValueMap pageProperties;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Design currentDesign;

    @ScriptVariable
    private ResourceResolver resolver;

    private String[] keywords = new String[0];
    private String designPathCSS;
    private String staticDesignPath;
    private String title;
    private String[] templateCategories = new String[0];

    private static final String FN_ICO_FAVICON = "favicon.ico";
    private static final String FN_PNG_FAVICON = "favicon_32.png";
    private static final String FN_TOUCH_ICON_60 = "touch-icon_60.png";
    private static final String FN_TOUCH_ICON_76 = "touch-icon_76.png";
    private static final String FN_TOUCH_ICON_120 = "touch-icon_120.png";
    private static final String FN_TOUCH_ICON_152 = "touch-icon_152.png";
    private static final String DEFAULT_TEMPLATE_EDITOR_CLIENT_LIB = "wcm.foundation.components.parsys.allowedcomponents";
    private static final String POLICIES_MAPPING_PATH = "policies/jcr:content";
    private static final String PN_CLIENTLIBS = "clientlibs";

    private String icoFavicon;
    private String pngFavicon;
    private String touchIcon60;
    private String touchIcon76;
    private String touchIcon120;
    private String touchIcon152;

    @PostConstruct
    private void postConstruct() {
        title = currentPage.getTitle();
        if (StringUtils.isEmpty(title)) {
            title = currentPage.getName();
        }
        Tag[] tags = currentPage.getTags();
        keywords = new String[tags.length];
        int index = 0;
        for (Tag tag : tags) {
            keywords[index++] = tag.getTitle();
        }
        if (currentDesign != null) {
            String designPath = currentDesign.getPath();
            if (StringUtils.isNotEmpty(designPath)) {
                designPathCSS = designPath + ".css";
            }
            if (resolver.getResource(designPath + "/static.css") != null) {
                staticDesignPath = designPath + "/static.css";
            }
            loadFavicons(designPath);
        }
        populateTemplateCategories();
    }


    @Override
    public String getLanguage() {
        return currentPage == null ? Locale.getDefault().toLanguageTag() : currentPage.getLanguage(false).toLanguageTag();
    }

    @Override
    public String getLastModifiedDate() {
        return pageProperties.get(NameConstants.PN_PAGE_LAST_MOD, String.class);
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public String getDesignPath() {
        return designPathCSS;
    }

    @Override
    public String getStaticDesignPath() {
        return staticDesignPath;
    }

    @Override
    public String getICOFavicon() {
        return icoFavicon;
    }

    @Override
    public String getPNGFavicon() {
        return pngFavicon;
    }

    @Override
    public String getTouchIcon60() {
        return touchIcon60;
    }

    @Override
    public String getTouchIcon76() {
        return touchIcon76;
    }

    @Override
    public String getTouchIcon120() {
        return touchIcon120;
    }

    @Override
    public String getTouchIcon152() {
        return touchIcon152;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getTemplateCategories() {
        return templateCategories;
    }

    private void loadFavicons(String designPath) {
        icoFavicon = getFaviconPath(designPath, FN_ICO_FAVICON);
        pngFavicon = getFaviconPath(designPath, FN_PNG_FAVICON);
        touchIcon60 = getFaviconPath(designPath, FN_TOUCH_ICON_60);
        touchIcon76 = getFaviconPath(designPath, FN_TOUCH_ICON_76);
        touchIcon120 = getFaviconPath(designPath, FN_TOUCH_ICON_120);
        touchIcon152 = getFaviconPath(designPath, FN_TOUCH_ICON_152);
    }

    private String getFaviconPath(String designPath, String faviconName) {
        String path = designPath + "/"+ faviconName;
        if (resolver.getResource(path) == null) {
            return null;
        }
        return path;
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
