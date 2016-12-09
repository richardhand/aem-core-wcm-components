/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
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
package com.adobe.cq.wcm.core.components.models.impl.v1;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Constants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = Image.class,
       resourceType = ImageImpl.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME,
          extensions = Constants.EXPORTER_EXTENSION)
public class ImageImpl implements Image {

    protected static final String RESOURCE_TYPE = "wcm/core/components/image/v1/image";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Resource resource;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private PageManager pageManager;

    @Inject
    @Source("osgi-services")
    private MimeTypeService mimeTypeService;

    @ValueMapValue(name = Constants.IMAGE_FILE_REFERENCE,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    private String fileReference;

    @ValueMapValue(name = Constants.IMAGE_IS_DECORATIVE,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean isDecorative;

    @ValueMapValue(name = Constants.IMAGE_DISPLAY_CAPTION_POPUP,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean displayCaptionPopup;

    @ValueMapValue(name = Constants.IMAGE_ALT,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    private String alt;

    @ValueMapValue(name = JcrConstants.JCR_TITLE,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    private String title;

    @ValueMapValue(name = Constants.IMAGE_LINK,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    private String linkURL;

    @ValueMapValue(name = Constants.IMAGE_FILE_NAME,
                   injectionStrategy = InjectionStrategy.OPTIONAL)
    private String fileName;

    private String extension;
    private String src;
    private String[] smartImages;
    private Integer[] smartSizes;

    // content policy settings
    private Set<Integer> allowedRenditionWidths;
    private boolean disableLazyLoading;

    @PostConstruct
    private void postConstruct() {
        if (StringUtils.isNotEmpty(fileReference)) {
            fileName = fileReference.substring(fileReference.lastIndexOf("/") + 1);
            int dotIndex;
            if ((dotIndex = fileReference.lastIndexOf(".")) != -1) {
                extension = fileReference.substring(dotIndex + 1);
            }
        } else {
            Resource file = resource.getChild(Constants.IMAGE_CHILD_NODE_IMAGE_FILE);
            if (file != null) {
                if (StringUtils.isEmpty(fileName)) {
                    fileName = resource.getName();
                }
                extension = mimeTypeService
                        .getExtension(PropertiesUtil.toString(file.getResourceMetadata().get(ResourceMetadata.CONTENT_TYPE), "image/jpeg"));
            }
        }
        if (StringUtils.isNotEmpty(fileName)) {
            Set<Integer> supportedRenditionWidths = getSupportedRenditionWidths();
            smartImages = new String[supportedRenditionWidths.size()];
            int index = 0;
            String escapedResourcePath = Text.escapePath(resource.getPath());
            for (Integer width : supportedRenditionWidths) {
                smartImages[index++] = "\"" + request.getContextPath() + escapedResourcePath + ".img." + width + "." + extension + "\"";
            }
            smartSizes = supportedRenditionWidths.toArray(new Integer[supportedRenditionWidths.size()]);
            if (smartSizes.length == 0 || smartSizes.length >= 2) {
                src = request.getContextPath() + escapedResourcePath + ".img." + extension;
            } else if (smartSizes.length == 1) {
                src = request.getContextPath() + escapedResourcePath + ".img." + smartSizes[0] + "." + extension;
            }
            disableLazyLoading = currentStyle.get(Constants.IMAGE_LAZY_LOADING_ENABLED, false);
            Page page = pageManager.getPage(linkURL);
            if (page != null) {
                String vanityURL = page.getVanityUrl();
                linkURL = (vanityURL == null ? linkURL + ".html" : vanityURL);
            }
        }

    }

    @Override
    public Set<Integer> getSupportedRenditionWidths() {
        if (allowedRenditionWidths == null) {
            allowedRenditionWidths = new LinkedHashSet<>();
            String[] supportedWidthsConfig = currentStyle.get(Constants.IMAGE_DESIGN_ALLOWED_RENDITION_WIDTHS, new String[0]);
            for (String width : supportedWidthsConfig) {
                try {
                    allowedRenditionWidths.add(Integer.parseInt(width));
                } catch (NumberFormatException e) {
                    LOGGER.error(String.format("Invalid width detected (%s) for content policy configuration.", width), e);
                }
            }
        }
        return allowedRenditionWidths;
    }


    @Override
    public String getSrc() {
        return src;
    }

    @Override
    public Integer[] getSmartSizes() {
        return smartSizes;
    }

    @Override
    public String[] getSmartImages() {
        return smartImages;
    }

    @Override
    public boolean isLazyLoadingEnabled() {
        return !disableLazyLoading;
    }

    @Override
    public String getAlt() {
        return alt;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getLink() {
        return linkURL;
    }

    @Override
    public boolean shouldDisplayCaptionPopup() {
        return displayCaptionPopup;
    }

    @Override
    public String getFileReference() {
        return fileReference;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isDecorative() {
        return isDecorative;
    }
}
