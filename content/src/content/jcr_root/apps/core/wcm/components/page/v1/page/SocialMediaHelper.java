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

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.CommerceHelper;
import com.adobe.cq.commerce.common.PriceFilter;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for page functionality related to page sharing by user on social media platforms.
 */
public class SocialMediaHelper extends WCMUsePojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocialMediaHelper.class);
    private static final String EXPERIENCE_FRAGMENT_CONTAINER = "cq/experience-fragments/editor/components/experiencefragment";
    private static final String FACEBOOK_VARIATION = "cq/experience-fragments/components/xffacebook";
    private static final String PINTEREST_VARIATION = "cq/experience-fragments/components/xfpinterest";
    private static final String SHARING_COMPONENT = "core/wcm/components/sharing";
    private static final String PN_SOCIAL_MEDIA = "socialMedia";
    private static final String PN_FRAGMENT_PATH = "fragmentPath";
    private static final String PN_JCR_CONTENT = "jcr:content";
    private static final String PV_FACEBOOK = "facebook";
    private static final String PV_PINTEREST = "pinterest";
    //Open Graph metadata property names
    private static final String OG_TITLE = "og:title";
    private static final String OG_URL = "og:url";
    private static final String OG_TYPE = "og:type";
    private static final String OG_SITE_NAME = "og:site_name";
    private static final String OG_IMAGE = "og:image";
    private static final String OG_DESCRIPTION = "og:description";
    private static final String OG_PRODUCT_PRICE_AMOUNT = "product:price:amount";
    private static final String OG_PRODUCT_PRICE_CURRENCY = "product:price:currency";
    //lazy variable, use hasSharingComponent() method for accessing it
    private Boolean hasSharingComponent;
    private boolean facebookEnabled;
    private boolean pinterestEnabled;
    private boolean socialMediaEnabled;
    /**
     * Holds the metadata for a page.
     */
    private Map<String, String> metadata;

    //*************** WEB INTERFACE METHODS *******************
    /**
     * Returns {@code true} if Facebook sharing is enabled in page configuration, {@code false} otherwise.
     */
    public boolean isFacebookEnabled() {
        return facebookEnabled;
    }

    /**
     * Returns {@code true} if Pinterest sharing is enabled in page configuration, {@code false} otherwise.
     */
    public boolean isPinterestEnabled() {
        return facebookEnabled;
    }

    /**
     * Returns true if a supported social media sharing is enabled in page configuration, false otherwise.
     */
    public boolean isSocialMediaEnabled() {
        return socialMediaEnabled;
    }

    /**
     * Returns {@code true} if Facebook sharing is enabled in page configuration
     * and the page contains the sharing component, {@code false} otherwise.
     */
    public boolean hasFacebookSharing() {
        return facebookEnabled && hasSharingComponent();
    }

    /**
     * Returns {@code true} if Pinterest sharing is enabled in page configuration
     * and the page contains the sharing component, {@code false} otherwise.
     */
    public boolean hasPinterestSharing() {
        return pinterestEnabled && hasSharingComponent();
    }

    /**
     * Returns the metadata.
     */
    public Map<String, String> getMetadata() {
        if (metadata == null) {
            initMetadata();
        }
        return metadata;
    }

    //*************** IMPLEMENTATION *******************
    @Override
    public void activate() throws Exception {
        Page currentPage = getCurrentPage();
        String[] socialMedia = currentPage.getProperties().get(PN_SOCIAL_MEDIA, String[].class);
        facebookEnabled = ArrayUtils.contains(socialMedia, PV_FACEBOOK);
        pinterestEnabled = ArrayUtils.contains(socialMedia, PV_PINTEREST);
        socialMediaEnabled = facebookEnabled || pinterestEnabled;
    }

    //Private accessor for hasSharingComponent field providing lazy initialization.
    private boolean hasSharingComponent() {
        if (hasSharingComponent == null) {
            hasSharingComponent = hasSharingComponent(getCurrentPage().getContentResource());
        }
        return hasSharingComponent;
    }

    /**
     * Search a resource tree for sharing component starting from a given resource.
     *
     * @param resource the resource
     *
     * @return {@code true} if the sharing vomponent was found, {@code false} otherwise
     */
    private boolean hasSharingComponent(final Resource resource) {
        if (resource.isResourceType(SHARING_COMPONENT))
            return true;

        for (Resource child : resource.getChildren())
            if (hasSharingComponent(child))
                return true;

        return false;
    }

    /**
     * Prepares Open Graph metadata for a page to be shared on social media services.
     */
    private void initMetadata() {
        metadata = new LinkedHashMap<>();
        if (socialMediaEnabled) {
            WebsiteMetadata websiteMetadata = createMetadataProvider();
            put(OG_TITLE, websiteMetadata.getTitle());
            put(OG_URL, websiteMetadata.getURL());
            put(OG_TYPE, websiteMetadata.getTypeName());
            put(OG_SITE_NAME, websiteMetadata.getSiteName());
            put(OG_IMAGE, websiteMetadata.getImage());
            put(OG_DESCRIPTION, websiteMetadata.getDescription());

            if (pinterestEnabled && websiteMetadata instanceof ProductMetadata) {
                ProductMetadata productMetadata = (ProductMetadata) websiteMetadata;
                put(OG_PRODUCT_PRICE_AMOUNT, productMetadata.getProductPriceAmount());
                put(OG_PRODUCT_PRICE_CURRENCY, productMetadata.getProductPriceCurrency());
            }
        }
    }

    /**
     * Put non-blank named values in metadata map.
     */
    private void put(String name, String value) {
        if (StringUtils.isNotBlank(value)) {
            metadata.put(name, value);
        }
    }

    /**
     * Instantiates the suitable metadata provider based on the contents of the current page.
     */
    private WebsiteMetadata createMetadataProvider() {
        Page currentPage = getCurrentPage();
        Product product = CommerceHelper.findCurrentProduct(currentPage);
        Set<Resource> eFragments = new LinkedHashSet<>();
        collectExperienceFragments(currentPage.getContentResource(), eFragments);
        if (product == null) {
            if (eFragments.isEmpty()) {
                return new WebsiteMetadataProvider();
            } else {
                return new ExperienceFragmentWebsiteMetadataProvider(eFragments);
            }
        } else {
            if (eFragments.isEmpty()) {
                return new ProductMetadataProvider(product);
            } else {
                return new ExperienceFragmentProductMetadataProvider(product, eFragments);
            }
        }
    }

    private void collectExperienceFragments(final Resource resource, Set<Resource> eFragments) {
        if (resource.isResourceType(EXPERIENCE_FRAGMENT_CONTAINER)) {
            String fragmentPath = resource.getValueMap().get(PN_FRAGMENT_PATH, String.class);
            if (StringUtils.isNotBlank(fragmentPath)) {
                Resource fragmentResource = getResourceResolver().getResource(fragmentPath);
                if (fragmentResource != null) {
                    fragmentResource = fragmentResource.getParent();
                    for (Resource res : fragmentResource.getChildren()) {
                        Resource contentRes = res.getChild(PN_JCR_CONTENT);
                        if (contentRes != null) {
                            if (contentRes.isResourceType(FACEBOOK_VARIATION)) {
                                if (facebookEnabled) {
                                    eFragments.add(contentRes);
                                }
                            } else if (contentRes.isResourceType(PINTEREST_VARIATION)) {
                                if (pinterestEnabled) {
                                    eFragments.add(contentRes);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (Iterator<Resource> iter = resource.listChildren(); iter.hasNext();) {
                collectExperienceFragments(iter.next(), eFragments);
            }
        }
    }

    /**
     * Provides metadata based on the content of a generic webpage.
     */
    private interface WebsiteMetadata {
        enum Type {website, product}
        String getTitle();
        String getURL();
        Type getType();
        String getTypeName();
        String getImage();
        String getDescription();
        String getSiteName();
    }

    /**
     * Provides metadata based on the content of a product page.
     */
    private interface ProductMetadata extends WebsiteMetadata {
        String getProductPriceAmount();
        String getProductPriceCurrency();
    }

    private class WebsiteMetadataProvider implements WebsiteMetadata {
        private static final String PN_IMAGE_FILE_JCR_CONTENT = "image/file/jcr:content";
        private static final String PN_JCR_LAST_MODIFIED = "jcr:lastModified";
        private static final String PN_JCR_TITLE = "jcr:title";
        private Page currentPage;

        public WebsiteMetadataProvider() {
            this.currentPage = getCurrentPage();
        }

        @Override
        public String getTitle() {
            String title = currentPage.getTitle();
            if (StringUtils.isEmpty(title)) {
                title = currentPage.getName();
            }
            return title;
        }

        @Override
        public String getURL() {
            Externalizer externalizer = getSlingScriptHelper().getService(Externalizer.class);
            String pagePath = Text.escapePath(getCurrentPage().getPath());
            String extension = getRequest().getRequestPathInfo().getExtension();
            String url = externalizer.publishLink(getResourceResolver(), pagePath) + "." + extension;
            return url;
        }

        @Override
        public Type getType() {
            return Type.website;
        }

        @Override
        public String getTypeName() {
            return getType().name();
        }

        @Override
        public String getImage() {
            String image = getThumbnailUrl(currentPage, 800, 480);
            Externalizer externalizer = getSlingScriptHelper().getService(Externalizer.class);
            image = externalizer.publishLink(getResourceResolver(), image);
            return image;
        }

        private String getThumbnailUrl(Page page, int width, int height) {
            String ck = "";

            ValueMap metadata = page.getProperties(PN_IMAGE_FILE_JCR_CONTENT);
            if (metadata != null) {
                Calendar cal = metadata.get(PN_JCR_LAST_MODIFIED, Calendar.class);
                if (cal != null) {
                    ck = "" + (cal.getTimeInMillis() / 1000);
                }
            }

            return Text.escapePath(page.getPath()) + ".thumb." + width + "." + height + ".png?ck=" + ck;
        }


        @Override
        public String getDescription() {
            return currentPage.getDescription();
        }

        @Override
        public String getSiteName() {
            Page page = findRootPage();

            if (page == null)
                return null;

            String pageTitle = page.getPageTitle();
            if (StringUtils.isNotBlank(pageTitle))
                return pageTitle;

            Resource content = page.getContentResource();
            if (content == null)
                return null;

            String title = content.getValueMap().get(PN_JCR_TITLE, String.class);
            if (StringUtils.isBlank(title))
                return null;

            return title;
        }

        private Page findRootPage() {
            Page page = currentPage;
            while (true) {
                Page parent = page.getParent();
                if (parent == null) {
                    return page;
                } else {
                    page = parent;
                }
            }
        }
    }

    private class ProductMetadataProvider extends WebsiteMetadataProvider implements ProductMetadata {
        private Product product;
        private PriceInfo priceInfo;

        public ProductMetadataProvider(Product product) {
            this.product = product;
        }

        @Override
        public String getTitle() {
            String title = product.getTitle();
            if (StringUtils.isBlank(title)) {
                title = super.getTitle();
            }
            return title;
        }

        @Override
        public Type getType() {
            return Type.product;
        }

        @Override
        public String getImage() {
            String image = product.getImage().getFileReference();
            if (StringUtils.isBlank(image)) {
                image = super.getImage();
            } else {
                Externalizer externalizer = getSlingScriptHelper().getService(Externalizer.class);
                image= Text.escapePath(image);
                image = externalizer.publishLink(getResourceResolver(), image);
            }
            return image;
        }

        @Override
        public String getDescription() {
            String description = product.getDescription();
            if (StringUtils.isBlank(description)) {
                description = super.getDescription();
            }
            return description;
        }

        @Override
        public String getProductPriceAmount() {
            String amount = null;
            try {
                initPriceInfo();
                if (priceInfo != null) {
                    amount = String.valueOf(priceInfo.getAmount());
                }
            } catch (CommerceException x) {
                LOGGER.error("Error getting product price amount", x);
            }
            return amount;
        }

        @Override
        public String getProductPriceCurrency() {
            String currency = null;
            try {
                initPriceInfo();
                if (priceInfo != null) {
                    currency = priceInfo.getCurrency().getCurrencyCode();
                }
            } catch (CommerceException x) {
                LOGGER.error("Error getting product price currency", x);
            }
            return currency;
        }

        private void initPriceInfo() throws CommerceException {
            CommerceService commerceService = getResourceResolver().getResource(product.getPath()).adaptTo(CommerceService.class);
            CommerceSession commerceSession = commerceService.login(getRequest(), getResponse());
            List<PriceInfo> priceInfoList = commerceSession.getProductPriceInfo(product, new PriceFilter("UNIT"));
            if (!priceInfoList.isEmpty()) {
                priceInfo = priceInfoList.get(0);
            }
        }
    }

    private class ExperienceFragmentMetadataProvider {
        private Set<Resource> fragmentResources;

        public ExperienceFragmentMetadataProvider(Set<Resource> fragmentResources) {
            this.fragmentResources = fragmentResources;
        }
        //todo extract info from EF
    }

    private class ExperienceFragmentWebsiteMetadataProvider extends WebsiteMetadataProvider {
        private final ExperienceFragmentMetadataProvider efMetadata;

        public ExperienceFragmentWebsiteMetadataProvider(Set<Resource> eFragments) {
            efMetadata = new ExperienceFragmentMetadataProvider(eFragments);
        }
        //todo provide info from EF
    }

    private class ExperienceFragmentProductMetadataProvider extends ProductMetadataProvider {
        private final ExperienceFragmentMetadataProvider efMetadata;

        public ExperienceFragmentProductMetadataProvider(Product product, Set<Resource> eFragments) {
            super(product);
            efMetadata = new ExperienceFragmentMetadataProvider(eFragments);
        }
        //todo provide info from EF
    }
}
