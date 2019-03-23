angular.module('app').controller('chungkhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            $scope.loading = true;
            $scope.isDisabled = true;
            $scope.hidden = true;
            vm.chungkhoan = {};
            data.chungkhoan().then(function (result) {
                vm.chungkhoan = result;
                console.log(result);
            }, function (err) {
                console.log(err);
            });

            vm.addSecurity = function() {
                var todo = {};

                todo.symbol = $scope.formData.gname;
                todo.txdate  = $scope.formData.name;
                todo.txtime = $scope.formData.value;
                todo.basicprice = $scope.formData.v_note;
                todo.ceilingprice = $scope.formData.e_note;
                todo.floorprice = $scope.formData.gname;
                todo.bidprice1  = $scope.formData.name;
                todo.bqtty1 = $scope.formData.value;
                todo.askprice1 = $scope.formData.v_note;
                todo.aqtty1 = $scope.formData.e_note;
                todo.lqtty = $scope.formData.e_note;

                data.addSecurity(todo).then(function (response) {
                    data.thamso().then(function (result) {
                        $scope.loading = false;

                        vm.thamso = result;
                        $scope.formData.gname = ""
                        $scope.formData.name  = ""
                        $scope.formData.value = ""
                        $scope.formData.v_note = ""
                        $scope.formData.e_note = ""
                        alert('Bạn đã thêm thành công')
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    alert(err);
                });
            }


            return;
        }
    ])

