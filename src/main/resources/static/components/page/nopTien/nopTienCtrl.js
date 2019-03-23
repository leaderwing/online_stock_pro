angular.module('app').controller('nopTienCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            var todos = {};
            //get ho va ten
            // vm.getHoten = function () {
            //     var todo = {
            //         account: $scope.formData.account,
            //     }
            //     console.log(todo)
            //     data.getHoten(todo.account).then(function (response) {
            //         vm.fullname = response;
            //     }, function (err) {
            //         console.log(err);
            //     })
            // }
            vm.noptien = function () {
               
                todos.account = $scope.formData.account;
                todos.amount = $scope.formData.amount;
                todos.txdesc = $scope.formData.txdesc;
                console.log(todos);
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.noptien(todos).then(function (response) {
                        alert(response);
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

