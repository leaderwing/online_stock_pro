angular.module('app').controller('afTypeCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            $scope.loading = true;
            $scope.isDisabled = true;
            $scope.hidden = true;
            vm.chungkhoan = {};
            data.afType().then(function (result) {
                $scope.loading = false;
                vm.chungkhoan = result.data;
                console.log(result);
            }, function (err) {
                console.log(err);
            });

            vm.addAfType = function() {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true) {
                    var todo = {};
                    todo.acType = $scope.formData.acType;
                    todo.typeName = $scope.formData.typeName;
                    todo.afType = $scope.formData.afType;
                    todo.tradeRate = $scope.formData.tradeRate;
                    todo.margRate = $scope.formData.margRate;
                    todo.WarningRate = $scope.formData.WarningRate;
                    todo.processRate = $scope.formData.processRate;
                    todo.depoRate = $scope.formData.depoRate;
                    todo.MiscRate = $scope.formData.MiscRate;


                    data.addAfType(todo).then(function (response) {
                        data.afType().then(function (result) {
                            $scope.loading = false;

                            vm.chungkhoan = result.data;
                            $scope.formData.acType = "";
                            $scope.formData.typeName = "";
                            $scope.formData.afType = "";
                            $scope.formData.tradeRate = "";
                            $scope.formData.margRate = "";
                            $scope.formData.WarningRate = "";
                            $scope.formData.processRate = "";
                            $scope.formData.depoRate = "";
                            $scope.formData.MiscRate = "";

                            alert('Bạn đã thêm thành công')
                        }, function (err) {
                            console.log(err);
                        });
                    }, function (err) {
                        alert(err);
                    });
                } else {
                    alert("Lệnh đã được hủy")
                }
            }

            vm.updateAfType = function () {

                if($scope.isDisabled == false){
                    $scope.isDisabled = true;
                    $scope.hidden = true;
                } else {
                    $scope.isDisabled = false;
                    $scope.hidden = false;
                }

            }
            var listOfChangedRows = [];
            $scope.markAsChanged = function(index){
                if(listOfChangedRows.indexOf(index)=== -1)
                {
                    listOfChangedRows.push(index);
                }
            }

            vm.saveUpdateAfType = function () {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                var updatedItems = [];
                for(var i=0;i<listOfChangedRows.length;i++)
                {
                    updatedItems.push(listOfChangedRows[i]);
                }
                // todo.grname = vm.thamso[a].grname;
                // todo.varname  =  vm.thamso[a].varname;
                // todo.varvalue =  vm.thamso[a].varvalue;
                // todo.vardesc =  vm.thamso[a].vardesc;
                // todo.en_vardesc =  vm.thamso[a].en_vardesc;
                data.saveUpdateAfType(updatedItems[updatedItems.length-1]).then(function (res) {
                    alert("Cập nhật thành công!")
                    data.afType().then(function (result) {
                        $scope.loading = false;

                        vm.chungkhoan = result.data;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    alert(err);
                });} else {
                    alert("Lệnh đã được hủy");
                }
            }

            vm.deleteAfType = function (chungkhoan) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                var delVal = {
                    acType : chungkhoan.acType,

                }
                data.deleteAfType(delVal).then(function (res) {
                    alert("Bạn đã xóa thành công")
                    data.afType().then(function (result) {
                        $scope.loading = false;

                        vm.chungkhoan = result.data;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });

                }, function (err) {
                    console.log(err)
                })} else {
                    alert("Lệnh đã được hủy")
                }
            }


        }
    ])

