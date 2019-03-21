angular.module('app').controller('hisXuLyTaiKhoan',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, ) {
            var vm = this;
            // console.log('showStockTrading')
            //------hien thi chi tiet lich su--------
            vm.showStockTrading = function () {
                console.log('modal')
                modal.showStockTrading();
            }

            //-------hien thi lich su---------
            // data.history().then(function (result) {
            //     // console.log(result)
            //     vm.history = result
            // })
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
                        data.historyAdmin().then(function (result) {
                            // console.log(result)
                            vm.history = result
                        })
                    }, function (err) {
                        console.log(err);
                    });
                } else {
                    alert('Lệnh đã được hủy');
                }
            }

            //---------------------------
            data.historyAdmin().then(function (result) {
                // console.log(result)
                vm.history = result
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

