angular.module('app').controller('thamsoCtrl',
['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
    $scope.loading = true;
    $scope.isDisabled = true;
    data.thamso().then(function (result) {
        $scope.loading = false;

        vm.thamso = result;
                console.log(result);
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

