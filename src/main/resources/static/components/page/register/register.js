angular.module('app')
// Creating the Angular Controller
    .controller('RegisterController', function ($http, data, $scope, AuthService) {
        data.randomAccount().then(function (response) {
            data = response.data;
            $scope.account = data;
            console.log(data)

        }, function (err) {
            console.log(err);
        });
    });