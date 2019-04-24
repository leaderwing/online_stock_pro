angular.module('app').controller('stockTradingCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter', '$interval',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, $interval) {
            var todos = {};
            var todoBan = {};
            var vm = this;
            $scope.loading = false;
            var stompClient = null;
            //----------------get thong tin chung-----------------
            var getttchung = function () {
                data.ttchung().then(function (result) {

                    vm.ttchung = result.data;

                }, function (err) {
                    console.log(err);
                })
            };
            //----------------get thong tin ty le-----------------
            var getttTyle = function () {
                data.tttyle().then(function (result) {
                    vm.tttyle = result.data;

                }, function (err) {
                    console.log(err);
                })
            };
            getttchung();
            getttTyle();
            conn();
            data.getTime().then(function (res) {
                console.log(moment(res.data.time).format("HH:mm:ss"));
                vm.hour = moment(res.data.time).format("HH:mm:ss");

            })

            var fromDate = moment($scope.THAMSO_NGAY1).format("YYYYMMDD");
            var toDate = moment($scope.THAMSO_NGAY2).format("YYYYMMDD");
            var todo = {
                ngay1: fromDate,
                ngay2: toDate,
                exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

            };

            // // listen stomp api
            function conn() {
                console.log("start connect sockjs!");
                var socket = new SockJS('/gs-guide-websocket');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/trading', function (result) {
                        console.log("$$ update realtime trading");
                        var data = JSON.parse(result.body);
                        $scope.$apply(function () {
                            if ((moment($scope.THAMSO_NGAY1).format("YYYYMMDD") === moment(new Date()).format("YYYYMMDD")) &&
                            (moment($scope.dateString).format("YYYYMMDD") >= moment(new Date()).format("YYYYMMDD")))
                            {
                            vm.history = data.filter(isOwnerDataFilter);
                            }
                        })
                    });
                    stompClient.subscribe('/topic/ttchung', function (result) {
                        console.log("$$ update realtime sum info");
                        var data1 = JSON.parse(result.body);
                        $scope.$apply(function () {
                            vm.ttchung = data1.filter(isOwnerData);
                        })
                    });
                    stompClient.subscribe('/topic/tttyle', function (result) {
                        console.log("$$ update realtime rate info");
                        var data2 = JSON.parse(result.body);
                        $scope.$apply(function () {
                            vm.tttyle = data2.filter(isOwnerData);
                        })
                    });
                });
            }

            function isOwnerData(value) {
                return value.afacctno === document.cookie.split('$')[2] ;

            }
            function isOwnerDataFilter(value) {
                            return value.afacctno === document.cookie.split('$')[2] &&
                            (value.txdate >= moment($scope.THAMSO_NGAY1).format("YYYYMMDD")) && (value.txdate <= moment($scope.dateString).format("YYYYMMDD")) &&
                            ($scope.THAMSO_SYMBOL === "" || $scope.THAMSO_SYMBOL == undefined || value.codeid === $scope.THAMSO_SYMBOL) &&
                            ($scope.THAMSO_EXECTYPE === "" || $scope.THAMSO_EXECTYPE == undefined || value.exectype === $scope.THAMSO_EXECTYPE);
             }

            vm.conn = function () {
                stompClient.send('/app/db', {}, null);
            }

            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                if (stompClient !== null) {
                    stompClient.disconnect();
                }
            });

            data.history(todo).then(function (result) {
                $scope.loading = false;
                vm.history = result.data.rowList

            }, function (err) {
                console.log(err);
            });

            vm.seHistory = function () {
                var todoo = {
                    ngay1: moment($scope.THAMSO_NGAY1).format("YYYYMMDD"),
                    ngay2: moment($scope.dateString).format("YYYYMMDD"),
                    exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                    symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""
                }
                data.history(todoo).then(function (result) {
                    vm.history = result.data.rowList

                }, function (err) {
                    console.log(err);
                })
                getttchung();
                getttTyle();
            }
//            $interval(function () {
//                vm.seHistory();
//                vm.getttchung();
//                vm.getttTyle()
//            }, 3000);

//----------------------------get view data--------------------------------------

            vm.getViewData = function () {
                var todo = {
                    symbol: $scope.formData.symbol,
                }
                console.log(todo)
                data.floorName(todo.symbol).then(function (result) {
                    console.log(result)

                    todos.floor = result.data.floorCode;
                    todos.ceM = result.data.ceil / 1000;

                    vm.floorNamess = result.data;

                }).catch(function (err) {
                    console.log(err);
                    console.log(err);
                })
                data.priceView(todo.symbol).then(function (result) {

                    vm.priceView = result.data;
                    todos.m1 = result.data.m1;

                });
            };




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
                    $scope.loading = true;

                    if ($scope.formData.price === undefined || $scope.formData.price == '0') {
                        if (todos.orderType === 'PLO') {
                            todos.price = todos.m1 * 1000;
                            data.createNormal(todos).then(function (result) {
                                $scope.loading = false;
                                if (result.data.result == "Order successfully") {
                                    alert("Đặt lệnh thành công");
                                    $scope.formData.command = ""
                                    $scope.formData.symbol = ""
                                    $scope.formData.quantity = ""
                                    $scope.formData.price = 0
                                    $scope.formData.orderType = ""
                                    $scope.formData.expiredDate = ""
                                    $state.go('root.stock-trading');
                                } else {
                                    if (result.data.result == undefined) {
                                        alert("Đặt lệnh không thành công")
                                        $state.go('root.stock-trading');
                                    } else {
                                        alert(result.data.result);
                                        $state.go('root.stock-trading');
                                    }
                                }
                            }, function (err) {
                                alert("Đặt lệnh thất bại, vui lòng thử lại!");
                                console.log(err);
                            })
                        } else {
                            todos.price = todos.ceM * 1000;
                            data.createNormal(todos).then(function (result) {
                                $scope.loading = false;
                                if (result.data.result == "Order successfully") {
                                    alert("Đặt lệnh thành công");
                                    $scope.formData.command = ""
                                    $scope.formData.symbol = ""
                                    $scope.formData.quantity = ""
                                    $scope.formData.price = 0
                                    $scope.formData.orderType = ""
                                    $scope.formData.expiredDate = ""
                                    $state.go('root.stock-trading');
                                } else {
                                    if (result.data.result == undefined) {
                                        alert("Đặt lệnh không thành công")
                                        $state.go('root.stock-trading');
                                    } else {
                                        alert(result.data.result);
                                        $state.go('root.stock-trading');
                                    }
                                }
                            }, function (err) {
                                alert("Đặt lệnh thất bại, vui lòng thử lại!");
                                console.log(err);
                            })
                        }
                    } else {

                        data.createNormal(todos).then(function (result) {

                            $scope.loading = false;
                            if (result.data.result == "Order successfully") {
                                alert("Đặt lệnh thành công");
                                $scope.formData.command = ""
                                $scope.formData.symbol = ""
                                $scope.formData.quantity = ""
                                $scope.formData.price = 0
                                $scope.formData.orderType = ""
                                $scope.formData.expiredDate = ""
                                $state.go('root.stock-trading');
                            } else {
                                if (result.data.result == undefined) {
                                    alert("Đặt lệnh không thành công")
                                    $state.go('root.stock-trading');
                                } else {
                                    alert(result.data.result);
                                    $state.go('root.stock-trading');
                                }
                            }
                        }, function (err) {
                            alert("Đặt lệnh thất bại, vui lòng thử lại!");
                            console.log(err);
                        })
                    }
                } else {
                    //alert('Lệnh đã được hủy');
                }
            };

            vm.deleteTodo = function (todo) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.deletes(todo.orderid).then(function (result) {
                        alert('Bạn đã hủy thành công! Vui lòng đợi hệ thống xác nhận');
                        vm.seHistory();
                    }, function (err) {
                        console.log(err);
                    });
                } else {
                    //alert('Lệnh đã được hủy');
                }
            }

            //--------------------Before close lenh----------
            vm.createNormalBanBefore = function (todo) {
             vm.symbolBan = todo.codeid;

             todos.symbolBan = todo.codeid;
             todos.txdate    = todo.txdate;
             todoBan.oderid = todo.orderid;

             var todo = {
                    symbol: todos.symbolBan
                   }
                             console.log(todo)
                             data.floorName(todo.symbol).then(function (result) {


                                 todoBan.floor = result.data.floorCode;
                                 todos.ceMBan = result.data.ceil / 1000;

                                 vm.floorNamessBan = result.data;

                             }).catch(function (err) {

                             })
                             data.priceView(todo.symbol).then(function (result) {

                                 vm.priceViewBan = result.data;
                                 todos.m1Ban = result.data.m1;

                             });
            }


            //-------Closed lenh-------------------
            vm.createNormalBan = function () {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                                if (t === true) {

                                    todoBan.command = $scope.formData.commandBan,
                                    todoBan.symbol = todos.symbolBan,
                                        todoBan.quantity = $scope.formData.quantityBan,
                                        todoBan.price = $scope.formData.priceBan * 1000,
                                        todoBan.orderType = $scope.formData.orderTypeBan,
                                        todoBan.expiredDate = $scope.formData.expiredDateBan,
                                        todoBan.buyDate = todos.txdate
                                    $scope.loading = true;

                                    if ($scope.formData.priceBan === undefined || $scope.formData.priceBan == '0') {
                                        if (todoBan.orderType === 'PLO') {
                                            todoBan.price = todos.m1Ban * 1000;
                                            data.createNormalBan(todoBan).then(function (result) {
                                                $scope.loading = false;
                                                if (result.data.result == "Order successfully") {
                                                    alert("Đặt lệnh thành công");
                                                    $scope.formData.commandBan = ""

                                                    $scope.formData.quantityBan = ""
                                                    $scope.formData.priceBan = 0
                                                    $scope.formData.orderTypeBan = ""
                                                    $scope.formData.expiredDateBan = ""
                                                    $state.go('root.stock-trading');
                                                } else {
                                                    if (result.data.result == undefined) {
                                                        alert("Đặt lệnh không thành công")
                                                        $state.go('root.stock-trading');
                                                    } else {
                                                        alert(result.data.result);
                                                        $state.go('root.stock-trading');
                                                    }
                                                }
                                            }, function (err) {
                                                alert("Đặt lệnh thất bại, vui lòng thử lại!");
                                                console.log(err);
                                            })
                                        } else {

                                            todoBan.price = todos.ceMBan * 1000;
                                            data.createNormalBan(todoBan).then(function (result) {
                                                $scope.loading = false;
                                                if (result.data.result == "Order successfully") {
                                                    alert("Đặt lệnh thành công");
                                                    $scope.formData.commandBan = ""

                                                    $scope.formData.quantityBan = ""
                                                    $scope.formData.priceBan = 0
                                                    $scope.formData.orderTypeBan = ""
                                                    $scope.formData.expiredDateBan = ""
                                                    $state.go('root.stock-trading');
                                                } else {
                                                    if (result.data.result == undefined) {
                                                        alert("Đặt lệnh không thành công")
                                                        $state.go('root.stock-trading');
                                                    } else {
                                                        alert(result.data.result);
                                                        $state.go('root.stock-trading');
                                                    }
                                                }
                                            }, function (err) {
                                                alert("Đặt lệnh thất bại, vui lòng thử lại!");
                                                console.log(err);
                                            })
                                        }
                                    } else {

                                        data.createNormalBan(todoBan).then(function (result) {

                                            $scope.loading = false;
                                            if (result.data.result == "Order successfully") {
                                                alert("Đặt lệnh thành công");
                                                $scope.formData.commandBan = ""

                                                $scope.formData.quantityBan = ""
                                                $scope.formData.priceBan = 0
                                                $scope.formData.orderTypeBan = ""
                                                $scope.formData.expiredDateBan = ""
                                                $state.go('root.stock-trading');
                                            } else {
                                                if (result.data.result == undefined) {
                                                    alert("Đặt lệnh không thành công")
                                                    $state.go('root.stock-trading');
                                                } else {
                                                    alert(result.data.result);
                                                    $state.go('root.stock-trading');
                                                }
                                            }
                                        }, function (err) {
                                            alert("Đặt lệnh thất bại, vui lòng thử lại!");
                                            console.log(err);
                                        })
                                    }
                                } else {
                                    //alert('Lệnh đã được hủy');
                                }
            }

            $scope.date = new Date();
            $scope.$watch('date', function (date) {


                $scope.THAMSO_NGAY1 = new Date(dateFilter(moment(new Date(moment().toDate())).format('MM/DD/YYYY'), 'yyyy/MM/dd'));
                $scope.dateString = new Date(dateFilter(date, 'yyyy/MM/dd'));
            });
        }
    ])

