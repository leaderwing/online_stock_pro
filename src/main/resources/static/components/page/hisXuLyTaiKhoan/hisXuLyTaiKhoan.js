angular.module('app').controller('hisXuLyTaiKhoan',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var todos = {};
             var todoBan = {};
             var todoMua = {};
            var vm = this;
            $scope.loading = true;
            var fromDate = moment($scope.THAMSO_NGAY1).format("YYYYMMDD");
            var toDate = moment($scope.THAMSO_NGAY2).format("YYYYMMDD");
            var arr = [];

            var todo = {
                ngay1: fromDate,
                ngay2: toDate,
                exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
                symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""

            }
            data.getTime().then(function (res) {
                            console.log(moment(res.data.time).format("HH:mm:ss"));
                            vm.hour = moment(res.data.time).format("HH:mm:ss");

                        })

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

            //-------Closed lenh-------------------
                        vm.createNormalBan = function () {
                            var t = confirm('Bạn có chắc chắn muốn thực hiện');
                                            if (t === true) {

                                                todoBan.command = $scope.formData.commandBan,
                                                todoBan.symbol = todos.symbolBan,
                                                todoBan.idcusid = $window.localStorage.getItem('idcusid')
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
                                     todoBan.execqtty = todo.execqtty;
                                     todoBan.orderid = todo.orderid;
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

             vm.createNormalMuaBefore = function (todo) {
                                                 vm.symbolMua = todo.codeid;
                                                 todos.symbolMua = todo.codeid;
                                                 todoMua.oderid = todo.orderid;
                                                 todoMua.execqtty = todo.execqtty;
                                                 todoMua.orderid = todo.orderid;
                                                 var todo = {
                                                        symbol: todos.symbolMua
                                                       }
                                                                 console.log(todo)
                                                                 data.floorName(todo.symbol).then(function (result) {


                                                                     todoMua.floor = result.data.floorCode;
                                                                     todos.ceMMua = result.data.ceil / 1000;

                                                                     vm.floorNamessMua = result.data;

                                                                 }).catch(function (err) {

                                                                 })
                                                                 data.priceView(todo.symbol).then(function (result) {

                                                                     vm.priceViewMua = result.data;
                                                                     todos.m1Mua = result.data.m1;

                                                                 });
                                                }

       vm.createNormalMua = function () {
                                   var t = confirm('Bạn có chắc chắn muốn thực hiện');
                                                   if (t === true) {

                                                       todoMua.command = $scope.formData.commandMua,
                                                       todoMua.symbol = todos.symbolMua,
                                                           todoMua.quantity = $scope.formData.quantityMua,
                                                           todoMua.price = $scope.formData.priceMua * 1000,
                                                           todoMua.idcusid = $window.localStorage.getItem('idcusid')
                                                           todoMua.orderType = $scope.formData.orderTypeMua ? $scope.formData.orderTypeMua : 'LO',
                                                           todoMua.expiredDate = $scope.formData.expiredDateMua,
                                                           todoMua.buyDate = todos.txdate
                                                       $scope.loading = true;

                                                       if ($scope.formData.priceMua === undefined || $scope.formData.priceMua == '0') {
                                                           if (todoMua.orderType === 'PLO') {
                                                               todoMua.price = todos.m1Mua * 1000;
                                                               data.createNormal(todoMua).then(function (result) {
                                                                   $scope.loading = false;
                                                                   if (result.data.result == "Order successfully") {
                                                                       alert("Đặt lệnh thành công");
                                                                       $scope.formData.commandMua = ""

                                                                       $scope.formData.quantityMua = ""
                                                                       $scope.formData.priceMua = 0
                                                                       $scope.formData.orderTypeMua = ""
                                                                       $scope.formData.expiredDateMua = ""
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

                                                               todoMua.price = todos.ceMMua * 1000;
                                                               data.createNormal(todoBan).then(function (result) {
                                                                   $scope.loading = false;
                                                                   if (result.data.result == "Order successfully") {
                                                                       alert("Đặt lệnh thành công");
                                                                       $scope.formData.commandMua = ""

                                                                       $scope.formData.quantityMua = ""
                                                                       $scope.formData.priceMua = 0
                                                                       $scope.formData.orderTypeMua = ""
                                                                       $scope.formData.expiredDateMua = ""
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

                                                           data.createNormal(todoMua).then(function (result) {

                                                               $scope.loading = false;
                                                               if (result.data.result == "Order successfully") {
                                                                   alert("Đặt lệnh thành công");
                                                                   $scope.formData.commandMua = ""

                                                                   $scope.formData.quantityMua = ""
                                                                   $scope.formData.priceMua = 0
                                                                   $scope.formData.orderTypeMua = ""
                                                                   $scope.formData.expiredDateMua = ""
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
                arr = result.data.rowList;
                var arrres = [];
                for(i=0 ; i < arr.length; i++) {
                    if(arr[i].execqtty > 0 || arr[i].cancelqtty == 0 ){
                        arrres.push(arr[i]);
                    }
                }
                console.log(arrres)
                vm.history = arrres;
            })

            vm.closedAll = function () {
            var t = confirm('Bạn có chắc chắn muốn thực hiện');
              if (t === true) {
            var arrreq = [];
                for(i=0; i<arr.length; i++){
                    if(arr[i].clearday <= 0 && arr[i].clearday != null){
                        arrreq.push(arr[i].orderid);
                    }
                }
            var closeAllReq = {
            "custId" : $window.localStorage.getItem('idcusid'),
            "orderList" : arrreq
            };
            console.log('dđ',closeAllReq)
            data.closedAll(closeAllReq).then(function (result) {
//            console.log("ok",JSON.stringify(result.result));
                if (result.status == 500) {
                 alert("Chạy không thành công");
                } else if (result.status == 404) {
                 alert("Không có lệnh nào để đóng");
                }else{
                    alert(JSON.stringify(result.result))
                }
            }, function(err){
                alert("Chạy không thành công");
            })
            } else {
            }
            }
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

