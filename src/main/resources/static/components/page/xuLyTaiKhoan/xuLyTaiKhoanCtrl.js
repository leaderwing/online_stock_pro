angular.module('app').controller('xuLyTaiKhoanCtrl',
['data', 'modal', '$window', '$rootScope', 'socket', '$state', '$scope', 'dateFilter',
function (data, modal, $window, $rootScope, socket, $state, $scope, dateFilter, ) {
            var vm = this;
        
            data.luongchungse().then(function (result) {
                vm.luongchungse = result;
                socket.on('change1', function(response) {
                    
                    vm.luongchungse = response;
                   //console.log(vm.taikhoan)
                    $scope.$apply();
                });
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

