angular.module('app').controller('luongChungCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter, ) {
            var vm = this;
            vm.cuoingay = function () {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    data.cuoingay().then(function (result) {
                        alert('Bạn đã chạy thành công');
                        $state.go("root.luong-chung");
                    }, function (err) {
                        console.log(err);
                    });
                } else {
                    alert('Lệnh đã được hủy');
                }
            }
            return;
        }
    ])

