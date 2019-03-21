angular.module('app').controller('xuLyTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;

            data.luongchungse().then(function (result) {

                vm.luongchungse = result;
            }, function (err) {
                console.log(err);
            });

            vm.hisxulytaikhoan = function (id) {
                $state.go("/trading/{id}",id);
                data.hisxulytaikhoan(id).then(function (result) {

                }, function (err) {
                    alert(err);
                });

            }

            return;
        }
    ])

