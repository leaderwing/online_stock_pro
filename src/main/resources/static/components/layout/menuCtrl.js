angular.module('app').controller('menuCtrl',
    ['data','modal', '$rootScope', '$state', '$scope',
        function (data, modal, $rootScope, $state, $scope, ) {
            var vm = this;
            data.getIsstaft().then(function (result) {
                console.log(result);
                vm.isstaft = result;
                console.log(vm.isstaft)
            });
            return;
        }
    ])
