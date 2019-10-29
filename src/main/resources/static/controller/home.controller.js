sap.ui.define(["sap/ui/core/mvc/Controller", "sap/m/MessageToast","sap/ui/model/json/JSONModel","sap/wing/ui/common/http"


], function (Controller, MessageToast,JSONModel, Http) {
    "use strict";
    return Controller.extend("sap.wing.ui.controller.home", {
    	oModel:null,
        onInit : function () {
           this.oModel = new sap.ui.model.json.JSONModel();
           this.getView().setModel(this.oModel,'data');

           Http({
        	   url:'../mock_data.json'
           }).done(function(data){
        	   this.oModel.setProperty('/',data);
           }.bind(this)).fail(function(error){}).complete(function(){})
        },
        
        
        onAfterRendering: function () {

        },

        onRefresh: function () {
            Http({
                url:'../mock_data.json'
            }).done(function(data){
                this.getView().getModel().setProperty('/',data);
            }.bind(this)).fail(function(error){}).complete(function(){})
        }

    });
});
