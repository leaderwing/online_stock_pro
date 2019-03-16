angular.module('app').controller('chungkhoanCtrl',
['data', 'modal', '$window', '$rootScope', 'socket', '$state', '$scope', 'dateFilter',
function (data, modal, $window, $rootScope, socket, $state, $scope, dateFilter, ) {
            var vm = this;
        
            data.chungkhoan().then(function (result) {
                vm.chungkhoan = result;
                console.log(result);
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

