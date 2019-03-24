angular.module('app').controller('xuLyTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter', '$interval',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, $interval) {
            var vm = this;

            $interval( function(){
                vm.luongchung();}, 1000);

            vm.luongchung = function() { data.luongchungse().then(function (result) {

                vm.luongchungse = result;
            }, function (err) {
                console.log(err);
            })};

            vm.hisxulytaikhoan = function (id) {
                $window.localStorage.setItem('idcusid', id);
                $state.go("root.his-xy-ly-tai-khoan");


            }

            return;
        }
    ])

