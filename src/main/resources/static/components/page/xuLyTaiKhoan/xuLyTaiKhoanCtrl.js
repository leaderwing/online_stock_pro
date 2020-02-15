angular.module('app').controller('xuLyTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter', '$interval', 'orderByFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, $interval, orderBy) {
            var vm = this;
             $scope.propertyName = 'real_MARGRATE';
             $scope.reverse = true;
            conn();
            function conn() {
                         console.log ("start connect sockjs!");
                         var socket = new SockJS('/gs-guide-websocket');
                         stompClient = Stomp.over(socket);
                         stompClient.connect({}, function (frame) {
                             console.log('Connected: ' + frame);
                             stompClient.subscribe('/topic/handleAcc', function (result) {
                                 var data = JSON.parse(result.body);
                                 $scope.$apply(function () {
                                    /* vm.luongchungse = data.filter(isOwnerData);*/
                                     vm.luongchungse = orderBy(data.filter(isOwnerData), $scope.propertyName, $scope.reverse);
                                 })
                             });
                         });
                         }
            data.luongchungse().then(function (result) {
//                           /* vm.luongchungse = result.data;*/
                            vm.luongchungse = orderBy(result.data, $scope.propertyName, $scope.reverse);
                        }, function (err) {
                            console.log(err);
                        });

             $scope.sortBy = function(propertyName) {
                    data.luongchungse().then(function (result) {
                           /*vm.luongchungse = result.data;*/
                              $scope.reverse = (propertyName !== null && $scope.propertyName === propertyName)
                                    ? !$scope.reverse : false;
                                  $scope.propertyName = propertyName;
                                  vm.luongchungse = orderBy(result.data, $scope.propertyName, $scope.reverse);
                    }, function (err) {
                        console.log(err);
                    });

             };

            vm.luongchung = function () {
            data.luongchungse().then(function (result) {
                vm.luongchungse = result.data;
            }, function (err) {
                console.log(err);
            })};

            vm.hisxulytaikhoan = function (id) {
                $scope.loading = false;
                $window.localStorage.setItem('idcusid', id);
                $state.go("root.his-xy-ly-tai-khoan");
            };
            vm.closedAccount = function (account) {
            var t = confirm('Bạn có chắc chắn muốn thực hiện');
                   if (t === true) {
                data.closedAccount(account).then(function (result) {
                if(result.status == 500) {
                    alert("Chạy không thành công");
                } else {
                    alert(result.data.result);
                }
            }, function (err) {
                console.log(err);
            })}else {}};
            return;
        }
    ])

