angular.module('app').controller('exportCtl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;

            var buyfrom = moment($scope.startbuydate).format("YYYYMMDD");
            var buyto = moment($scope.endbuydate).format("YYYYMMDD");
            var sellfrom = moment($scope.startselldate).format("YYYYMMDD");
            var sellto = moment($scope.endselldate).format("YYYYMMDD");
            var symbol = $scope.symbol;
            var account = $scope.account;
            vm.export= function () {
                $window.location.href = '/export/excel/' + buyfrom + '/' + buyto + '/' + sellfrom + '/' + sellto + '/' + symbol + '/' + account;
            }
        }
    ])

