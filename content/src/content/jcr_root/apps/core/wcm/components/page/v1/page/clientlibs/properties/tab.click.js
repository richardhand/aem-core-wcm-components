/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2016 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
(function(window, document, $, Granite) {
    "use strict";

    /**
     * Handler to show/hide the MSM action buttons according to the selected tab.
     */
    $(document).on("coral-panelstack:change", ".cq-siteadmin-admin-properties-tabs", function(e) {
        var $target = $(e.target);

        var $actionBar = $("coral-actionbar");

        if ($target.find(".cq-siteadmin-admin-properties-blueprint")) {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-blueprint").removeClass("hide");
        } else {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-blueprint").addClass("hide");
        }

        if ($target.find(".cq-siteadmin-admin-properties-livecopy")) {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-livecopy").removeClass("hide");
        } else {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-livecopy").addClass("hide");
        }
    });

})(window, document, Granite.$, Granite);