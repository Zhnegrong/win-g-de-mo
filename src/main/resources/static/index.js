sap.ui.define([

], function () {
	"use strict";
	  sap.ui.getCore().attachInit(function() {
		    sap.ui.require([ "sap/ui/core/ComponentContainer" ], function(ComponentContainer) {
		      "use strict";
		      new ComponentContainer({
		        height : "100%",
		        name : "sap.wing.ui"
		      }).placeAt("content");
		    });
		  });
});