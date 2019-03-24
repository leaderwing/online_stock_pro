angular.module('app').controller('capTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;


            //get thong tin tai khoan--------------------------
            data.getTK().then(function (response) {

                vm.taikhoan = response;
                

            }, function (err) {
                console.log(err);
            });

            //--------------cap tai khoan--------------
            vm.captk = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                data.captksm(todo).then(function (response) {

                    alert("thanh cong");
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

