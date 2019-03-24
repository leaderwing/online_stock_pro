angular.module('app').controller('nop1TienCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            var todos = {};
            //get ho va ten
            vm.getHoten = function () {
                var todo = {
                    custid: $scope.formData.account
                }
                console.log(todo)
                data.getHoten(todo).then(function (response) {
                    console.log("sdsd"+response.name)
                    vm.fullname = response.name;
                }, function (err) {
                    console.log(err);
                })
            }
            vm.ruttien = function () {
               
                todos.account = $scope.formData.account;
                todos.amount = $scope.formData.amount;
                todos.txdesc = $scope.formData.txdesc;

                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.ruttien(todos).then(function (response) {
                        alert(response.result)
                        $scope.formData.account = "";
                        vm.fullname = "";
                        $scope.formData.amount = "";
                        $scope.formData.txdesc = "";
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

