angular.module('app').controller('ResetPasswordCtrl', ['$http', 'data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function ($http, data, modal, $window, $rootScope, $state, $scope, dateFilter) {
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