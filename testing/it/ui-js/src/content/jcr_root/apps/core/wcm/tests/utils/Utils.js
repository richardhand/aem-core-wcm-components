/*
 *  Copyright 2016 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

hobs.config.pacing_delay = 300

window.CQ.CoreComponentsIT.pageRoot = "/content/core-components/core-components-page";
//window.CQ.CoreComponentsIT.pageFinalUrl = window.CQ.CoreComponentsIT.pageRoot

window.CQ.CoreComponentsIT.checkNumberOfItems = function (hobs, selector, numberOfItems) {
    return hobs.find(selector).length == numberOfItems;
};

window.CQ.CoreComponentsIT.checkNumberOfItemsFromIFrame = function (hobs, frameSelector, selector, numberOfItems) {
    return hobs.find(selector).length == numberOfItems;

};

window.CQ.CoreComponentsIT.checkContentFromIFrame = function (hobs, frameSelector, selector, value) {
    return hobs.find(selector,frameSelector).text().trim() == value;

};

window.CQ.CoreComponentsIT.checkInputValue = function (hobs, selector, value) {
    return hobs.find(selector).val() == value;

};


