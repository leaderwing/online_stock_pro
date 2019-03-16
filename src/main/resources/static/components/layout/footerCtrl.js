angular.module('app').controller('footerCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope',
        function (data, modal, $window, $rootScope, $state, $scope, ) {
            var todos = {};
            var vm = this;
            //---get so tai khoan------------------
            data.getAcctno().then(function (result) {
                // console.log(result);
                vm.acctno = result;
                // console.log(vm.acctno)
            });
            //---------------get du lieu gia va ten san----------------
            vm.getViewData = function () {
                var todo = {
                    symbol: $scope.formData.symbol,
                }
                console.log(todo)
                data.floorName(todo.symbol).then(function (result) {
                    console.log(result)

                    todos.floor = result.floorCode;
                    todos.ceM = result.ceil / 1000;
                    vm.floorName = result;

                })
                data.priceView(todo.symbol).then(function (result) {
                    vm.priceView = result;

                });
            };
            //----------------get thong tin chung-----------------
            data.ttchung().then(function (result) {
                vm.ttchung = result;
            }, function (err) {
                console.log(err);
            });
            //----------------get thong tin ty le-----------------
            data.tttyle().then(function (result) {
                vm.tttyle = result;
            }, function (err) {
                console.log(err);
            });
            //----------------dat lenh mua-----------------------
            vm.createTodos = function () {

                todos.command = $scope.formData.command,
                    todos.symbol = $scope.formData.symbol,
                    todos.quantity = $scope.formData.quantity,
                    todos.price = $scope.formData.price,
                    todos.orderType = $scope.formData.orderType,
                    todos.expiredDate = $scope.formData.expiredDate

                console.log(todos)
                if (todos.price === undefined) {
                    todos.price = todos.ceM;
                    data.createNormal(todos).then(function (result) {

                        if (result === 'Dat lenh thanh cong') {
                            alert(result);
                            $window.location.href = '/back';
                        } else {
                            alert(result);
                        }
                    }, function (err) {
                        console.log(err);
                    })
                } else {
                    data.createNormal(todos).then(function (result) {

                        if (result === 'Dat lenh thanh cong') {
                            alert(result);
                            $window.location.href = '/back';
                        } else {
                            alert(result);
                        }
                    }, function (err) {
                        console.log(err);
                    })
                }
            };
            //----------------dat lenh ban-----------------------
            vm.createTodosBanUser = function () {

                todos.command = $scope.formData.command,
                    todos.symbol = $scope.formData.symbolB,
                    todos.quantity = $scope.formData.quantityB,
                    todos.price = $scope.formData.priceB,
                    todos.orderType = $scope.formData.orderTypeB,
                    todos.expiredDate = $scope.formData.expiredDate

                console.log(todos)
                if (todos.price === undefined) {
                    todos.price = todos.ceM;
                    data.createNormalBanUser(todos).then(function (result) {

                        if (result === 'Dat lenh thanh cong') {
                            alert(result);
                            $window.location.href = '/back';
                        } else {
                            alert(result);
                        }
                    }, function (err) {
                        console.log(err);
                    })
                } else {
                    data.createNormalBanUser(todos).then(function (result) {

                        if (result === 'Dat lenh thanh cong') {
                            alert(result);
                            $window.location.href = '/back';
                        } else {
                            alert(result);
                        }
                    }, function (err) {
                        console.log(err);
                    })
                }
            };
            return;
        }
    ])
