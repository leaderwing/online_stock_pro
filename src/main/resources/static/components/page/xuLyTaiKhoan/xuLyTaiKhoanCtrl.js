angular.module('app').controller('xuLyTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            $scope.loading = true;
            data.luongchungse().then(function (result) {
                $scope.loading = false;
                vm.luongchungse = result;
            }, function (err) {
                console.log(err);
            });

            vm.hisxulytaikhoan = function (id) {
                $scope.loading = false;
                $window.localStorage.setItem('idcusid', id);
                $state.go("root.his-xy-ly-tai-khoan");


            }

            return;
        }
    ])

