angular.module('app').controller('hisXuLyTaiKhoan',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var todos = {};
             var todoBan = {};
            var vm = this;
            $scope.loading = true;
            var fromDate = moment($scope.THAMSO_NGAY1).format("YYYYMMDD");
            var toDate = moment($scope.THAMSO_NGAY2).format("YYYYMMDD");


            var todo = {
                ngay1: fromDate,
                ngay2: toDate,
                exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

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
                                                    todoBan.expiredDate = $scope.formData.expiredDateBan
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
                                                                $state.go('root.his-xy-ly-tai-khoan');
                                                            } else {
                                                                if (result.data.result == undefined) {
                                                                    alert("Đặt lệnh không thành công")
                                                                    $state.go('root.his-xy-ly-tai-khoan');
                                                                } else {
                                                                    alert(result.data.result);
                                                                    $state.go('root.his-xy-ly-tai-khoan');
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
                                                                $state.go('root.his-xy-ly-tai-khoan');
                                                            } else {
                                                                if (result.data.result == undefined) {
                                                                    alert("Đặt lệnh không thành công")
                                                                    $state.go('root.his-xy-ly-tai-khoan');
                                                                } else {
                                                                    alert(result.data.result);
                                                                    $state.go('root.his-xy-ly-tai-khoan');
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
                                                            $state.go('root.his-xy-ly-tai-khoan');
                                                        } else {
                                                            if (result.data.result == undefined) {
                                                                alert("Đặt lệnh không thành công")
                                                                $state.go('root.his-xy-ly-tai-khoan');
                                                            } else {
                                                                alert(result.data.result);
                                                                $state.go('root.his-xy-ly-tai-khoan');
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

                        //--------------------Before close lenh----------
                                    vm.createNormalBanBefore = function (todo) {
                                     vm.symbolBan = todo.codeid;
                                     todos.symbolBan = todo.codeid;
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

//            //-------Closed lenh-------------------
//            vm.createNormalBan = function (todo) {
//                var t = confirm('Bạn có chắc chắn muốn thực hiện');
//                if (t === true) {
//                    var todo = {
//                        execqtty: todo.execqtty,
//                        closedqtty: todo.closedqtty,
//                        oderid: todo.orderid,
//                        price: 0,
//                        symbol: todo.codeid,
//                        orderType: todo.pricetype
//                    }
//
//                    data.createNormalBan(todo).then(function (result) {
//                        alert(result);
//                        data.hisxulytaikhoan($window.localStorage.getItem('idcusid')).then(function (result) {
//                            if (result1.data.result == "Order successfully") {
//                                alert("Đặt lệnh thành công");
//                            } else {
//                                alert(result1.data.result);
//                            }
//                            vm.history = result.data.rowList
//                        })
//                    }, function (err) {
//                        console.log(err);
//                    });
//                } else {
//                    alert('Lệnh đã được hủy');
//                }
//            }

            //---------------------------
            data.hisxulytaikhoan($window.localStorage.getItem('idcusid')).then(function (result) {
                $scope.loading = false;

                vm.history = result.data.rowList
            })


            //------hien thi account--------------
            // data.getAcctno().then(function (result) {
            //     // console.log(result);
            //     vm.acctno = data;
            // }, function (error) {
            //     console.log(error, 'can not get data.');
            // });
            //----tim kiem--------------------------
            vm.seHistory = function () {
                var todoo = {
                    ngay1: moment($scope.THAMSO_NGAY1).format("YYYYMMDD"),
                    ngay2: moment($scope.dateString).format("YYYYMMDD"),
                    exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                    symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

                }
                data.hisxulytaikhoan($window.localStorage.getItem('idcusid')).then(function (result) {

                    vm.history = result.data.rowList
                })
            }
            $scope.date = new Date();
            $scope.$watch('date', function (date) {


                $scope.THAMSO_NGAY1 = new Date(dateFilter(moment(new Date(moment().toDate())).format('MM/DD/YYYY'), 'yyyy/MM/dd'));
                $scope.dateString = new Date(dateFilter(date, 'yyyy/MM/dd'));
            });
            return;
        }
    ])

