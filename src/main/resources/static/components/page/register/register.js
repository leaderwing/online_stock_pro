angular.module('app')
// Creating the Angular Controller
    .controller('RegisterController', function ($http, data, $scope, AuthService) {
        var vm = this;
        $scope.account = "";
        data.accNames().then(function (res) {

            vm.acname = res.data;
        }, function (err) {
            console.log(err)
        })

        data.randomAccount().then(function (response) {

            $scope.account = response.data.result;


        }, function (err) {
            console.log(err);
        });

        vm.register = function () {
            var registerData = {
                acctno: $scope.acctno == undefined ? $scope.account : $scope.acctno,
                fullname: $scope.fullname,
                dateofbirth: $scope.dateofbirth,
                sex: $scope.sex,
                idcode: $scope.idcode,
                iddate: $scope.iddate,
                idplace: $scope.idplace,
                address: $scope.address,
                phone: $scope.phone,
                bankacctno: $scope.bankacctno,
                bankname: $scope.bankname,
                accType: $scope.accType,
                email: $scope.email
            }

            data.register(registerData).then(function (res) {
                if (res.status == 201) {
                    vm.messageStatus = 'Không gửi được mail';
                } else {
                    vm.status = res.status;
                    vm.messageStatus = res.data.result
                }

            }, function (err) {
                console.log(err);
            })
        }
    });