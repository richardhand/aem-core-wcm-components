
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
(function ($,channel) {
    'use strict';

    var ADD_ITEM = "[core-form-textField-addItem]";
    var REMOVE_ITEM = "[core-form-textField-item-remove]";
    var TEMPLATE = "[core-form-textField-template]";
    var CONTENT="[core-form-textField-content]";
    var CONTAINER ="[core-form-textField-container]";
    var INPUT_GROUP="[core-form-input-subgroup]";
    
    channel.on("click",ADD_ITEM,function(event) {
		console.log("Add item clicked");
		var container=$(event.target).parent(CONTAINER)[0];
		var template = $(container).find(TEMPLATE)[0];
		var newElement = $(template).clone(true)[0];
		newElement.removeAttribute("core-form-textField-template");
		$(container).find(CONTENT)[0].appendChild(newElement);
    });

    channel.on("click",REMOVE_ITEM,function(event) {
        console.log("remove item clicked");
        var content = $(event.target).parent(INPUT_GROUP)[0];
        content.parentElement.removeChild(content);
    });

})(jQuery,jQuery(document));
