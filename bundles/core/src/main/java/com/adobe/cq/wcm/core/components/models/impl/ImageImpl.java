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

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Constants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.wcm.api.designer.Style;
import com.day.jcr.vault.util.JcrConstants;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Image.class, resourceType = "wcm/core/components/image")
public class ImageImpl implements Image {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Resource resource;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private SlingScriptHelper sling;

    @Inject @Source("osgi-services")
    private MimeTypeService mimeTypeService;

    private String fileReference;
    private String fileName;
    private String extension;
    private String src;
    private String[] smartImages;
    private Integer[] smartSizes;

    private boolean isDecorative;
    private boolean displayCaptionPopup;
    private String alt;
    private String title;
    private String linkURL;

    // content policy settings
    private Set<Integer> allowedRenditionWidths;
    private boolean disableLazyLoading;

    @PostConstruct
    private void postConstruct() {
        fileReference = properties.get(Constants.IMAGE_FILE_REFERENCE, "");
        if (StringUtils.isNotEmpty(fileReference)) {
            fileName = fileReference.substring(fileReference.lastIndexOf("/") + 1);
            int dotIndex;
            if ((dotIndex = fileReference.lastIndexOf(".")) != -1) {
                extension = fileReference.substring(dotIndex + 1);
            }
        } else {
            Resource file = resource.getChild(Constants.IMAGE_CHILD_NODE_IMAGE_FILE);
            if (file != null) {
                fileName = resource.getName();
                extension = mimeTypeService
                        .getExtension(PropertiesUtil.toString(file.getResourceMetadata().get(ResourceMetadata.CONTENT_TYPE), "image/jpeg"));
            }
        }
        if (StringUtils.isNotEmpty(fileName)) {
            Set<Integer> supportedRenditionWidths = getSupportedRenditionWidths();
            smartImages = new String[supportedRenditionWidths.size()];
            int index = 0;
            for (Integer width : supportedRenditionWidths) {
                smartImages[index++] = "\"" + request.getContextPath() + resource.getPath() + ".img." + width + "." + extension + "\"";
            }
            smartSizes = supportedRenditionWidths.toArray(new Integer[0]);
            if (smartSizes.length == 0 || smartSizes.length >= 2) {
                src = request.getContextPath() + resource.getPath() + ".img." + extension;
            } else if (smartSizes.length == 1) {
                src = request.getContextPath() + resource.getPath() + ".img." + smartSizes[0] + "." + extension;
            }
            disableLazyLoading = currentStyle.get(Constants.IMAGE_LAZY_LOADING_ENABLED, false);
            isDecorative = properties.get(Constants.IMAGE_IS_DECORATIVE, false);
            displayCaptionPopup = properties.get(Constants.IMAGE_DISPLAY_CAPTION_POPUP, false);
            if (!isDecorative) {
                alt = properties.get(Constants.IMAGE_ALT, String.class);
                linkURL = properties.get(Constants.IMAGE_LINK, String.class);
            }
            title = properties.get(JcrConstants.JCR_TITLE, String.class);
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
}
