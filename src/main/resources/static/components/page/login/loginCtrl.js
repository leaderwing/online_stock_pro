angular.module('app')
// Creating the Angular Controller
    .controller('LoginController',['$http', 'AuthService', 'data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
                                          function ($http, AuthService, data, modal, $window, $rootScope, $state, $scope, dateFilter) {
                                              var vm = this;
                                              var todos = {};

               vm.login = function () {
                todos.username = $scope.username;
                todos.password = $scope.password;
                               data.login(todos).then(function (res) {
                                   $scope.password = null;

                                                   // checking if the token is available in the response
                                                   if (res.token) {

                                                       $scope.message = '';
                                                       // setting the Authorization Bearer token with JWT token
                                                       $http.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;
                                                       // setting the user in AuthService
                                                       AuthService.setUser(res.user);
                                                       document.cookie = res.token + "$" + res.user.isStaft;
                                                       $rootScope.$broadcast('LoginSuccessful');
                                                       // going to the home page
                                                       $state.go('root.stock-trading');
                                                   } else {
                                                       // if the token is not present in the response then the
                                                       // authentication was not successful. Setting the error message.
                                                       $scope.message = 'Authetication Failed !';
                                                   }

                               }, function (err) {
                                   console.log(err);
                               })

                           };
    return;
            }
        ])