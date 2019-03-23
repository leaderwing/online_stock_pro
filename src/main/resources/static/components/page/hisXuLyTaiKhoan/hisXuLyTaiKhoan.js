angular.module('app').controller('hisXuLyTaiKhoan',
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
                        data.hisxulytaikhoan($window.localStorage.getItem('idcusid')).then(function (result) {
                            console.log("ddds", result)
                            vm.history = result.rowList
                        })
                    }, function (err) {
                        console.log(err);
                    });
                } else {
                    alert('Lệnh đã được hủy');
                }
            }

            //---------------------------
            data.hisxulytaikhoan($window.localStorage.getItem('idcusid')).then(function (result) {
                 console.log("ddds", result)
                vm.history = result.rowList
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
                    console.log("ddds", result)
                    vm.history = result.rowList
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

