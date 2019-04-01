angular.module('app').controller('xuLyTaiKhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter', '$interval',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, $interval) {
            var vm = this;
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
                                     vm.luongchungse = data.filter(isOwnerData);
                                 })
                             });
                         });
                         }
            data.luongchungse().then(function (result) {
                            vm.luongchungse = result;
                        }, function (err) {
                            console.log(err);
                        });

            vm.luongchung = function () {
            data.luongchungse().then(function (result) {
                vm.luongchungse = result;
            }, function (err) {
                console.log(err);
            })};

            vm.hisxulytaikhoan = function (id) {
                $scope.loading = false;
                $window.localStorage.setItem('idcusid', id);
                $state.go("root.his-xy-ly-tai-khoan");
            }
            return;
        }
    ])

