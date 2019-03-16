// Creating angular OnlineStockApp with module name "OnlineStockApp"
angular.module('app', ['ui.router'])
// the following method will run at the time of initializing the module. That
// means it will run only one time.
    .controller('appCtrl', ['$scope', function ($scope) {
        $scope
            .$on('$stateChangeSuccess', function(event, toState){
                if (angular.isDefined(toState.data.pageTitle)) {
                    $scope.pageTitle = toState.data.pageTitle;
                }
            });
    }])
    .run(function (AuthService, $rootScope, $state) {
        // For implementing the authentication with ui-router we need to listen the
        // state change. For every state change the ui-router module will broadcast
        // the '$stateChangeStart'.

    });