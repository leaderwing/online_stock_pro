angular.module('app').controller('duyetRutCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            $scope.loading = true;

            //--------------duyet rut-------------------
            vm.duyetRut = function (todo) {
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
                    console.log("dsadas",datas)
                data.duyetRut(datas).then(function (response) {
                    alert("Duyệt thành công")
                    data.getDuyetRut().then(function (response1) {
                        vm.duyetrut = response1.data;
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
            data.getDuyetRut().then(function (response) {
                $scope.loading = false;
              // console.log(response)
                vm.duyetrut = response.data;
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

