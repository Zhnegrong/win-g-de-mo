sap.ui.define( [ "sap/ui/core/mvc/Controller" ], function ( Controller) {
    "use strict";
    return Controller.extend( "sap.wing.ui.controller.app", {

        onInit : function () {
           
        },

        onAfterRendering : function () {

        },
   

        _initHashChangedListener : function () {
            if(!this.getView().byId( "siderNavigation" )){
                return;
            }
            var items = this.getView().byId( "siderNavigation" ).getItems();
            var oHashChanger = sap.ui.core.routing.HashChanger.getInstance();
            oHashChanger.init();
            var _this = this;
            oHashChanger.attachEvent( "hashChanged", function ( oEvent ) {
                _this._activeSiderMenuItemSelected( oEvent.getParameter( "newHash" ).toUpperCase().split( "/" ), items, 0, null );
            } );

            // first come to app page or refresh page don't trigger hash change event,
            // So, need to addtionally set sider menus seleted by hash in the url
            this._activeSiderMenuItemSelected( window.location.hash.replace( /^#\//, "" ).toUpperCase().split( "/" ), items, 0,
                null );
        },

        _activeSiderMenuItemSelected : function ( paths, items, level, parent ) {
            if ( parent ) {
                parent.setExpanded( true );
            }
            var path = paths[level];
            if ( level === 0 ) {
                this.getView().byId( "siderNavigation" ).setSelectedItem( items[0], true );
            }
            if ( ! path || ! items ) {
                return;
            }
            for ( var i = 0; i < items.length; i++ ) {
                if ( items[i].getKey() === path ) {
                    this.getView().byId( "siderNavigation" ).setSelectedItem( items[i], true );
                    return this._activeSiderMenuItemSelected( paths, items[i].getItems(), ++level, items[i] );
                }
            }
        }
    } );
} );
