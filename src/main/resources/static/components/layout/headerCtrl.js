angular.module('app').controller('headerCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope',
        function (data, modal, $window, $rootScope, $state, $scope, ) {
        var vm = this;
        vm.logout = function() {
                    document.cookie = '';
                    $rootScope.$broadcast('LogoutSuccessful');
                    $state.go('login');
        }
//            var todos = {};
//            var vm = this;
//            data.getIsstaft().then(function (result) {
//                console.log(result);
//                vm.isstaft = result;
//                console.log(vm.isstaft)
//            });
//            // data.getAcctno().then(function (result) {
//            //     console.log(result);
//            //     vm.acctno = result;
//            //     console.log(vm.acctno)
//            // });
//            //---get so tai khoan------------------
//            data.getAcctno().then(function (result) {
//                // console.log(result);
//                vm.acctno = result;
//                // console.log(vm.acctno)
//            });
//
//            //----------------dat lenh ban-----------------------
//            vm.createTodosBanUser = function () {
//
//                todos.command = $scope.formData.command,
//                    todos.symbol = $scope.formData.symbolB,
//                    todos.quantity = $scope.formData.quantityB,
//                    todos.price = $scope.formData.priceB,
//                    todos.orderType = $scope.formData.orderTypeB,
//                    todos.expiredDate = $scope.formData.expiredDate
//
//                console.log(todos)
//                if (todos.price === undefined) {
//                    todos.price = todos.ceM;
//                    data.createNormalBanUser(todos).then(function (result) {
//
//                        if (result === 'Dat lenh thanh cong') {
//                            alert(result);
//                            $window.location.href = '/back';
//                        } else {
//                            alert(result);
//                        }
//                    }, function (err) {
//                        console.log(err);
//                    })
//                } else {
//                    data.createNormalBanUser(todos).then(function (result) {
//
//                        if (result === 'Dat lenh thanh cong') {
//                            alert(result);
//                            $window.location.href = '/back';
//                        } else {
//                            alert(result);
//                        }
//                    }, function (err) {
//                        console.log(err);
//                    })
//                }
//            };

$scope.$on('LoginSuccessful', function () {
            $scope.user = AuthService.user;
        });
        $scope.$on('LogoutSuccessful', function () {
            $scope.user = null;
        });
        $scope.logout = function () {
            AuthService.user = null;
            $rootScope.$broadcast('LogoutSuccessful');
            $state.go('login');
        };
            return;
        }
    ])
