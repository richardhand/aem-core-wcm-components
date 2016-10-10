/**
 ...
 */
;(function(h,$){

    var page = window.CQ.CoreComponentsIT.Pages;

    page.pageRoot = "/content/core-components/core-components-page"

    //Data for the sample page
    page.samplePage = {
        title: "CoreComponent TestPage",
        template: "/conf/core-components/settings/wcm/templates/core-components",
        primaryType: "cq:PageContent",
        resourceType: "wcm/foundation/components/page",
        name: "core-components-page"

    }

    page.createPage = function(pageNumber){
        return $.ajax({
            url: page.pageRoot,
            method: "POST",
            data: {
                ":name": page.name + pageNumber,
                "./jcr:primaryType": "cq:Page"
            }
        })
    }

    page.createPageContent = function(pageNumber) {
        return $.ajax({
            url: page.pageRoot,
            method: "POST",
            data: {
                "./cq:template": page.template,
                "./jcr:primaryType": page.primaryType,
                "./jcr:title": page.title,
                ":name": page.name + pageNumber,
                "_charset_": "UTF-8"
            }
        });
    };



})(hobs, jQuery);