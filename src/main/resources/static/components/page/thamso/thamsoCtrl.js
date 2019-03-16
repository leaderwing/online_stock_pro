angular.module('app').controller('thamsoCtrl',
['data', 'modal', '$window', '$rootScope', 'socket', '$state', '$scope', 'dateFilter',
function (data, modal, $window, $rootScope, socket, $state, $scope, dateFilter, ) {
            var vm = this;
        
            data.thamso().then(function (result) {
                vm.thamso = result;
                console.log(result);
            }, function (err) {
                console.log(err);
            });
            return;
        }
    ])

