angular.module('app')
// Creating the Angular Controller
    .controller('resetCtrl', ['$http', 'AuthService', 'data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function ($http, AuthService, data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;




            vm.sendMail = function () {
                var datas = {
                    email : $scope.email
                }
                data.resetPass(datas).then(function (res) {
                    alert(res.result);
                },function (err) {
                    console.log(err);
                })
            }


            return;
        }
    ])