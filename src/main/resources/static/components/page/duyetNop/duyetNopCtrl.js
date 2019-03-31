angular.module('app').controller('duyetNopCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            $scope.loading = true;

            //---------------duyet nop-----------------
            vm.duyetNop = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                    var datas = {
                        acctno : todo.acctno,
                        amt    : todo.amt,
                        // approved_acctno : todo.approved_ACCTNO,
                        // approved_txtime : todo.approved_TXTIME,
                        created_acctno  : todo.created_acctno,
                        fullname        : todo.fullname,
                        id              : todo.id
                    }
                data.duyetNop(datas).then(function (response) {
                    alert("Duyệt thành công");
                    data.getDuyetNop().then(function (response1) {
                        vm.duyetnop = response1.data;
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
                $scope.loading = false;
                vm.duyetnop = response.data;
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

