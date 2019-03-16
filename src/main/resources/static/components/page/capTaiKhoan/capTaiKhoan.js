angular.module('app').controller('capTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', 'socket', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, socket, $state, $scope, dateFilter, ) {
            var vm = this;

            //get thong tin tai khoan--------------------------
            data.getTK().then(function (response) {
                vm.taikhoan = response;
                
                socket.on('change', function(response) {
                    
                    vm.taikhoan = response;
                   console.log(vm.taikhoan)
                    $scope.$apply();
                });
            }, function (err) {
                console.log(err);
            });

            //--------------cap tai khoan--------------
            vm.captk = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                data.captksm(todo).then(function (response) {
                    alert(response);
                    data.getTK().then(function (response1) {
                        vm.taikhoan = response1;
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                })
            } else {
                alert('Lệnh đã được hủy');
            }
            };
            return;
        }
    ])

