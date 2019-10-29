/**
 * add page loading when send ajax request, and after ajax request completing, hide page loading
 *
 * @param BusyIndicator
 * @returns
 */
sap.ui.define( [ "sap/ui/core/BusyIndicator" ], function ( BusyIndicator ) {
    var ciAjax = function ( params, finallyCallback, noLoading, noDelay ) {
        if ( ! noLoading ) {
        	BusyIndicator.show();
        }
        if ( ! params.type ) {
            params.type = "GET";
        }
        var options = {
            success : function ( data, textStatus, request ) {
//                var csrfToken = request.getResponseHeader( "X-Csrf-Token" );
//                if ( csrfToken ) {
//                    Utils.setCSRFToken( csrfToken );
//                }
            },
            complete : function () {
                if ( ! noLoading && noDelay ) {
                	BusyIndicator.hide();
                }
                if ( ! noLoading && ! noDelay ) {
                	BusyIndicator.hide();
                }
                typeof finallyCallback === "function" && finallyCallback();
            },
            beforeSend : function ( request ) {
//                var csrftoken = Utils.getCSRFToken();
//                if ( ! csrftoken ) {
//                    csrftoken = "Fetch";
//                }
//                request.setRequestHeader( "X-CSRF-Token", csrftoken );
            },
        };

        $.extend( true, options, params );
        return $.ajax( options );
    };
    return ciAjax;
} );
