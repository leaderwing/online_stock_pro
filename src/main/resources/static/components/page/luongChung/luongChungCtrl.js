angular.module('app').controller('luongChungCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, ) {
            var vm = this;
            $scope.loading = false;
            vm.cuoingay = function () {
                $scope.loading = true;
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.cuoingay().then(function (result) {
                        $scope.loading = false;
                        alert('Bạn đã chạy thành công');
                        $state.go("root.luong-chung");
                    }, function (err) {
                        console.log(err);
                    });
                } else {

                }
            }
            return;
        }
    ])

