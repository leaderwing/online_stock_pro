angular.module('app').controller('changePassCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            var todos = {};
            vm.user = {};


            vm.changePass = function () {
                if($scope.newPassword == $scope.checkNewPassword){
                    var dataUser = {
                        oldPassword : $scope.oldPassword,
                        newPassword: $scope.newPassword

                    }

                    data.changePass(dataUser).then(function (res) {
                        if(res.status == 404){
                            vm.messageChangePass = "Dữ liệu nhập chưa đúng"
                        }else {
                            vm.messageChangePass = "Mật khẩu đã được thay đổi"
                            $state.go("login");
                        }
                    },function (err) {
                        console.log(err);
                    })
                } else {

                }

            }


            return;
        }
    ])

