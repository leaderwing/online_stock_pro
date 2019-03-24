angular.module('app').controller('hitsController',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var todos = {};
            var vm = this;
            $scope.loading = true;
            var fromDate = moment($scope.THAMSO_NGAY1).format("YYYYMMDD");
            var toDate = moment($scope.THAMSO_NGAY2).format("YYYYMMDD");


            var todo = {
                ngay1: fromDate,
                ngay2: toDate,
                exectype: "a",
                symbol: "a"

            }


            data.historyhits(todo).then(function (result) {
                $scope.loading = false;
                vm.history = result.rowList
                // data.floorName(result.CODEID).then(function (resultfloor) {
                //     // console.log(resultfloor)
                //     vm.floorName = resultfloor;
                //
                // });

            }, function (err) {
                alert(err);
            })

            // vm.seHistory = function () {
            //     var todoo = {
            //         ngay1: moment($scope.THAMSO_NGAY1).format("YYYYMMDD"),
            //         ngay2: moment($scope.dateString).format("YYYYMMDD"),
            //         exectype: ($scope.THAMSO_EXECTYPE) ? $scope.THAMSO_EXECTYPE : "",
            //         symbol: ($scope.THAMSO_SYMBOL) ? $scope.THAMSO_SYMBOL : ""
            //
            //     }
            //     data.historyhits(todoo).then(function (result) {
            //         vm.history = result.rowList
            //         // data.floorName(result.CODEID).then(function (resultfloor) {
            //         //     // console.log(resultfloor)
            //         //     vm.floorName = resultfloor;
            //         // });
            //     }, function (err) {
            //         alert(err);
            //     })
            // }



            $scope.date = new Date();
            $scope.$watch('date', function (date) {


                $scope.THAMSO_NGAY1 = new Date(dateFilter(moment(new Date(moment().toDate())).format('MM/DD/YYYY'), 'yyyy/MM/dd'));
                $scope.dateString = new Date(dateFilter(date, 'yyyy/MM/dd'));
            });
            return;
        }
    ])

