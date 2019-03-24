angular.module('app').controller('changePassCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter ) {
            var vm = this;
            var todos = {};
            vm.user = {};


            vm.changePass = function () {
                console.log("dsadas",$scope.newPassword)
                if($scope.newPassword == $scope.checkNewPassword){
                    var dataUser = {
                        oldPassword : $scope.oldPassword,
                        newPassword: $scope.newPassword

                    }

                    data.changePass(dataUser).then(function (res) {
                        $state.go("login");
                    },function (err) {
                        console.log(err);
                    })
                } else {
                    console.log("Không")
                }

            }


            return;
        }
    ])

