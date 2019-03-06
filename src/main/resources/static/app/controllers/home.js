angular.module('OnlineStockApp')
// Creating the Angular Controller
    .controller('HomeController', function ($http, $scope, AuthService) {
        $scope.user = AuthService.user;
    });
