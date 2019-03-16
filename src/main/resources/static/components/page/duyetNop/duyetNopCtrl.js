angular.module('app').controller('duyetNopCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, ) {
            var vm = this;

            //---------------duyet nop-----------------
            vm.duyetNop = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                data.duyetNop(todo).then(function (response) {
                    alert(response);
                    data.getDuyetNop().then(function (response1) {
                        vm.duyetnop = response1;
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                })
            } else {
                alert('Lệnh đã được hủy')
            }
            };

            //------------get thong tin duyet nop---------------
            data.getDuyetNop().then(function (response) {
                vm.duyetnop = response;
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

