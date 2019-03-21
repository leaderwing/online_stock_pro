angular.module('app').controller('stockTradingCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var todos = {};
            var vm = this;
            var fromDate = moment($scope.THAMSO_NGAY1).format("YYYYMMDD");
            var toDate = moment($scope.THAMSO_NGAY2).format("YYYYMMDD");


            var todo = {
                ngay1: fromDate,
                ngay2: toDate,
                exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

            }


            data.history(todo).then(function (result) {
                vm.history = result.rowList

            }, function (err) {
                alert(err);
            })

            vm.seHistory = function () {
                var todoo = {
                    ngay1: moment($scope.THAMSO_NGAY1).format("YYYYMMDD"),
                    ngay2: moment($scope.dateString).format("YYYYMMDD"),
                    exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                    symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

                }
                data.history(todoo).then(function (result) {
                    vm.history = result.rowList

                }, function (err) {
                    alert(err);
                })
            }

//----------------------------get view data--------------------------------------

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

                }).catch(function(err){
                    console.log(err);
                    alert(err);
                })
                data.priceView(todo.symbol).then(function (result) {

                    vm.priceView = result;
                    todos.m1 = result.m1;

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

                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {

                    todos.command = $scope.formData.command,
                        todos.symbol = $scope.formData.symbol,
                        todos.quantity = $scope.formData.quantity,
                        todos.price = $scope.formData.price * 1000,
                        todos.orderType = $scope.formData.orderType,
                        todos.expiredDate = $scope.formData.expiredDate


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
                        alert(todos.symbol)
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

            $scope.date = new Date();
            $scope.$watch('date', function (date) {


                $scope.THAMSO_NGAY1 = new Date(dateFilter(moment(new Date(moment().toDate())).format('MM/DD/YYYY'), 'yyyy/MM/dd'));
                $scope.dateString = new Date(dateFilter(date, 'yyyy/MM/dd'));
            });
            return;
        }
    ])

