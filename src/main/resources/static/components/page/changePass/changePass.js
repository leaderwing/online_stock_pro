angular.module('app').controller('changePassCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            var todos = {};
            vm.user = {};
            data.userInfo().then(function (res) {

                vm.user = res;
            }, function (err) {
                console.log(err)
            })
            data.accNames().then(function (res) {

                vm.acname = res;
            }, function (err) {
                console.log(err)
            })

            vm.updateUserInfo = function () {
                var datauser = {
                    acctno : $scope.acctno == undefined ? vm.user.acctno : $scope.acctno,
                    fullname: $scope.fullname == undefined ? vm.user.fullname : $scope.fullname,
                    dateofbirth : $scope.dateofbirth == undefined ? vm.user.dateofbirth : $scope.dateofbirth,
                    sex : $scope.sex == undefined ? vm.user.sex : $scope.sex ,
                    idcode : $scope.idcode == undefined ? vm.user.idcode : $scope.idcode ,
                    iddate : $scope.iddate == undefined ? vm.user.iddate : $scope.iddate ,
                    idplace : $scope.idplace == undefined ? vm.user.idplace : $scope.idplace ,
                    address : $scope.address == undefined ? vm.user.address : $scope.address ,
                    phone : $scope.phone == undefined ? vm.user.phone : $scope.phone ,
                    bankacctno : $scope.bankacctno == undefined ? vm.user.bankacctno : $scope.bankacctno ,
                    bankname : $scope.bankname == undefined ? vm.user.bankname : $scope.bankname ,
                    accType : $scope.accType == undefined ? vm.user.accType : $scope.accType ,
                    email : $scope.email == undefined ? vm.user.email : $scope.email ,
                    pin : $scope.pin == undefined ? vm.user.pin : $scope.pin ,
                }
                console.log("dsadad",datauser);
                data.userInfoUpdate(datauser).then(function (res) {
                    console.log("trreer",res);
                },function (err) {
                    console.log(err);
                })
            }


            return;
        }
    ])

