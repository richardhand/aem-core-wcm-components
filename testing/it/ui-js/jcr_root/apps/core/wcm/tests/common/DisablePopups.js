/*...*/ ;
(function(h){  

    window.CQ.CoreComponentsIT.DisablePopups = function (h, $) {
    return new h.TestCase("Disable Popups") 
        .execSyncFct(commons.saveUserConfig) 
        .execFct(function(opts, done) {
            var preferences = {};
            preferences['winMode'] = 'single';
            preferences['granite.shell.showonboarding620'] = 'false';
            preferences['cq.authoring.editor.showTour'] = 'false';
            preferences['cq.authoring.editor.showTour62@Boolean'] = 'false';
            preferences['cq.authoring.editor.page.showTour@Boolean'] = 'false';
            preferences['cq.authoring.editor.page.showTour62'] = 'false';
            preferences['cq.authoring.editor.template.showTour'] = 'false';
            preferences['cq.authoring.cloudservices.optin'] = 'hide';
            preferences['cq.authoring.usagedata.optin'] = 'false';

            commons.setUserPreferences(opts, done, preferences);
        });
    }
})(hobs);

