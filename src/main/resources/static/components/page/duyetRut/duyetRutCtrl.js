angular.module('app').controller('duyetRutCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, ) {
            var vm = this;

            //--------------duyet rut-------------------
            vm.duyetRut = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                data.duyetRut(todo).then(function (response) {
                    alert(response);
                    data.getDuyetRut().then(function (response1) {
                        vm.duyetrut = response1;
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

            //get thong tin duyet rut---------------------------
            // data.getDuyetRut().then(function (response) {
            //     vm.duyetrut = response;
            // }, function (err) {
            //     console.log(err);
            // });
            return;
        }
    ])

