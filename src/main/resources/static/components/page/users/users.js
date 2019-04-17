angular.module('app').controller('usersCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            var todos = {};
            vm.user = {};
            data.userInfo().then(function (res) {

                vm.user = res.data;

            }, function (err) {
                console.log(err)
            })
            data.accNames().then(function (res) {

                vm.acname = res.data;
            }, function (err) {
                console.log(err)
            })

            vm.updateUserInfo = function () {

                var datauser = {
                    acctno: $scope.acctno == undefined || $scope.acctno == null ? vm.user.acctno : $scope.acctno,
                    fullname: $scope.fullname == undefined || $scope.fullname == null ? vm.user.fullname : $scope.fullname,
                    dateofbirth: $scope.dateofbirth == undefined || $scope.dateofbirth == null ? moment(vm.user.dateofbirth).format("DDMMYYYY") : moment($scope.dateofbirth).format("DDMMYYYY"),
                    sex: $scope.sex == undefined || $scope.sex == null ? vm.user.sex : $scope.sex,
                    idcode: $scope.idcode == undefined || $scope.idcode == null ? vm.user.idcode : $scope.idcode,
                    iddate: $scope.iddate == undefined || $scope.iddate == null ? moment(vm.user.iddate).format("DDMMYYYY") : moment($scope.iddate).format("DDMMYYYY"),
                    idplace: $scope.idplace == undefined || $scope.idplace == null ? vm.user.idplace : $scope.idplace,
                    address: $scope.address == undefined || $scope.address == null ? vm.user.address : $scope.address,
                    phone: $scope.phone == undefined || $scope.phone == null ? vm.user.phone : $scope.phone,
                    bankacctno: $scope.bankacctno == undefined || $scope.bankacctno == null ? vm.user.bankacctno : $scope.bankacctno,
                    bankname: $scope.bankname == undefined || $scope.bankname == null ? vm.user.bankname : $scope.bankname,
                    accType: $scope.accType == undefined || $scope.accType == null ? vm.user.accType : $scope.accType,
                    email: $scope.email == undefined || $scope.email == null ? vm.user.email : $scope.email,
                    pin: $scope.pin == undefined || $scope.pin == null ? vm.user.pin : $scope.pin,
                }
                console.log(datauser)
                data.userInfoUpdate(datauser).then(function (res) {
                    if (res.status == 500) {
                        vm.messageUpdate = "Dữ liệu truyền vào sai định dạng"
                    } else {
                        if (res.result == '030002') {
                            vm.messageUpdate = "Số tài khoản không hợp lệ"
                        } else if (res.result == '030003') {
                            vm.messageUpdate = "Số chứng minh nhân dân đã tồn tại"
                        } else if (res.result == '030004') {
                            vm.messageUpdate = "Email đã được đăng ký "
                        } else if (res.result == '000000') {
                            vm.messageUpdate = "Dữ liệu đã được cập nhật"
                            data.userInfo().then(function (res) {
                                vm.user = res.data;
                            }, function (err) {
                                console.log(err)
                            })
                            data.accNames().then(function (res) {
                                vm.acname = res.data;
                            }, function (err) {
                                console.log(err)
                            })
                        }
                    }
                }, function (err) {
                    alert("Cập nhật không thành công");
                })
            }
            return;
        }
    ])

