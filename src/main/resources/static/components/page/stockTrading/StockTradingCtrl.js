angular.module('app').controller('stockTradingCtrl',
    ['data', 'modal', '$window', '$rootScope', 'socket', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, socket, $state, $scope, dateFilter, ) {
            var todos = {};
            var vm = this;
            vm.hour = new Date().toString().substr(16, 8);
            console.log(vm.hour)
            //------hien thi chi tiet lich su--------
            // vm.showStockTrading = function (data) {
            //     console.log(data)
            //     modal.showStockTrading(data);
            // }

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
                    vm.floorNamess = result;

                })
                data.priceView(todo.symbol).then(function (result) {
                    vm.priceView = result;
                    todos.m1 = result.m1;

                });
            };
            //----------------get thong tin chung-----------------
            data.ttchung().then(function (result) {
                vm.ttchung = result;
                socket.on('change6', function (response) {

                    data.ttchung().then(function (result) {
                        vm.ttchung = result;
                    });
                    $scope.$apply();
                });
            }, function (err) {
                console.log(err);
            });
            //----------------get thong tin ty le-----------------
            data.tttyle().then(function (result) {
                vm.tttyle = result;
                socket.on('change8', function (response) {

                    data.tttyle().then(function (result) {
                        vm.tttyle = result;
                    });
                    $scope.$apply();
                });
            }, function (err) {
                console.log(err);
            });
            //----------------dat lenh mua-----------------------
            vm.createTodos = function () {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {

                    todos.command = $scope.formData.command,
                        todos.symbol = $scope.formData.symbol,
                        todos.quantity = $scope.formData.quantity,
                        todos.price = $scope.formData.price,
                        todos.orderType = $scope.formData.orderType,
                        todos.expiredDate = $scope.formData.expiredDate

                    console.log(todos)
                    if (todos.price === undefined) {
                        if (todos.orderType === 'PLO') {
                            todos.price = todos.m1;
                            data.createNormal(todos).then(function (result) {

                                if (result === 'Dat lenh thanh cong') {
                                    alert(result);
                                    $scope.formData.command = "";
                                    $scope.formData.symbol = "";
                                    $scope.formData.quantity = "";
                                    $scope.formData.price = "";
                                    $scope.formData.orderType = "";
                                    $scope.formData.expiredDate = "";
                                    //$window.location.href = '/back';
                                } else {
                                    alert(result);
                                    $scope.formData.command = "";
                                    $scope.formData.symbol = "";
                                    $scope.formData.quantity = "";
                                    $scope.formData.price = "";
                                    $scope.formData.orderType = "";
                                    $scope.formData.expiredDate = "";
                                }
                            }, function (err) {
                                console.log(err);
                            })
                        } else {
                            todos.price = todos.ceM;
                            data.createNormal(todos).then(function (result) {

                                if (result === 'Dat lenh thanh cong') {
                                    alert(result);
                                    $scope.formData.command = "";
                                    $scope.formData.symbol = "";
                                    $scope.formData.quantity = "";
                                    $scope.formData.price = "";
                                    $scope.formData.orderType = "";
                                    $scope.formData.expiredDate = "";
                                    //$window.location.href = '/back';
                                } else {
                                    alert(result);
                                    $scope.formData.command = "";
                                    $scope.formData.symbol = "";
                                    $scope.formData.quantity = "";
                                    $scope.formData.price = "";
                                    $scope.formData.orderType = "";
                                    $scope.formData.expiredDate = "";
                                }
                            }, function (err) {
                                console.log(err);
                            })
                        }
                    } else {
                        data.createNormal(todos).then(function (result) {

                            if (result === 'Dat lenh thanh cong') {
                                alert(result);
                                $scope.formData.command = "";
                                $scope.formData.symbol = "";
                                $scope.formData.quantity = "";
                                $scope.formData.price = "";
                                $scope.formData.orderType = "";
                                $scope.formData.expiredDate = "";
                                // $window.location.href = '/back';
                            } else {
                                alert(result);
                                $scope.formData.command = "";
                                $scope.formData.symbol = "";
                                $scope.formData.quantity = "";
                                $scope.formData.price = "";
                                $scope.formData.orderType = "";
                                $scope.formData.expiredDate = "";
                            }
                        }, function (err) {
                            console.log(err);
                        })
                    }
                } else {
                    alert('Lệnh đã được hủy');
                }
            };

            //-------hien thi lich su---------
            data.history().then(function (result) {
                // console.log(result)
                vm.history = result
                data.floorName(result.CODEID).then(function (resultfloor) {
                    // console.log(resultfloor)
                    vm.floorName = resultfloor;

                });
                socket.on('change2', function (response) {

                    data.history().then(function (result) {
                        // console.log(result)
                        vm.history = result
                        data.floorName(result.CODEID).then(function (resultfloor) {
                            // console.log(resultfloor)
                            vm.floorName = resultfloor;

                        });
                    });
                    $scope.$apply();
                });
            })
            //------hien thi ten san--------------
            // data.floorName(todo.symbol).then(function (result) {
            //     console.log(result)
            //     vm.floorName = result;

            // })
            //------hien thi account--------------
            data.getAcctno().then(function (result) {
                // console.log(result);
                vm.acctno = data;
            }, function (error) {
                console.log(error, 'can not get data.');
            });
            //----------------Dat lenh huy------------
            vm.deleteTodo = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.deletes(todo.ORDERID).then(function (result) {
                        alert('Bạn đã hủy thành công');
                        data.history().then(function (result) {

                            vm.history = result
                        })
                    }, function (err) {
                        console.log(err);
                    });
                } else {
                    alert('Lệnh đã được hủy');
                }
            }
            //-------Closed lenh-------------------
            vm.createNormalBan = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    var todo = {
                        execqtty: todo.EXECQTTY,
                        closedqtty: todo.CLOSEDQTTY,
                        oderid: todo.ORDERID,
                        price: 0,
                        symbol: todo.CODEID,
                        orderType: todo.PRICETYPE
                    }
                    console.log(todo)
                    data.createNormalBan(todo).then(function (result) {
                        alert(result);
                        data.history().then(function (result) {
                            // console.log(result)
                            vm.history = result
                        })
                    }, function (err) {
                        alert(err);
                    });
                } else {
                    alert('Lệnh đã được hủy');
                }
            }
            //----tim kiem--------------------------

            vm.seHistory = function () {
                $scope.date1 = new Date($scope.dateString);
                var THAMSO_NGAY2
                if (($scope.date1.getDate().toString()).length === 1) {
                    THAMSO_NGAY2 = "0" + $scope.date1.getDate().toString();
                } else {
                    THAMSO_NGAY2 = $scope.date1.getDate().toString();
                }
                var todo = {
                    ngay1: $scope.THAMSO_NGAY1,
                    ngay2: $scope.date1.getFullYear().toString() + ($scope.date1.getMonth() + 1).toString() + THAMSO_NGAY2,
                    exectype: $scope.THAMSO_EXECTYPE,
                    symbol: $scope.THAMSO_SYMBOL,

                }
                console.log(todo.ngay2)
                data.sehistory(todo).then(function (result) {
                    console.log(result)
                    vm.history = result
                })
            };

            $scope.date = new Date();
            $scope.$watch('date', function (date) {

                // console.log(dateFilter(date, 'yyyy/MM/dd'))
                // console.log(dateFilter(date, 'yyyy/MM/dd').substr(0, 4)+dateFilter(date, 'yyyy/MM/dd').substr(5, 2)+dateFilter(date, 'yyyy/MM/dd').substr(8, 2));
                $scope.THAMSO_NGAY1 = new Date(dateFilter("12/01/2018", 'yyyy/MM/dd'));
                $scope.dateString = new Date(dateFilter(date, 'yyyy/MM/dd'));
            });
            return;
        }
    ])

