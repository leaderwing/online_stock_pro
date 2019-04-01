angular.module('app').controller('chungkhoanCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            $scope.loading = true;
            $scope.isDisabled = true;
            $scope.hidden = true;
            vm.chungkhoan = {};
            data.chungkhoan().then(function (result) {
                $scope.loading = false;
                vm.chungkhoan = result.data;
            }, function (err) {
                console.log(err);
            });

            vm.addSecurity = function() {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                var todo = {};
                todo.symbol = $scope.formData.symbol;
                todo.txdate  = $scope.formData.txdate;
                todo.txtime = $scope.formData.txtime;
                todo.basicprice = $scope.formData.basicprice;
                todo.ceilingprice = $scope.formData.ceilingprice;
                todo.floorprice = $scope.formData.floorprice;
                todo.bidprice1  = $scope.formData.bidprice1;
                todo.bqtty1 = $scope.formData.bqtty1;
                todo.askprice1 = $scope.formData.askprice1;
                todo.aqtty1 = $scope.formData.aqtty1;
                todo.lqtty = $scope.formData.lqtty;

                data.addSecurity(todo).then(function (response) {
                    data.chungkhoan().then(function (result) {
                        $scope.loading = false;

                        vm.chungkhoan = result.data;
                        $scope.formData.symbol = "";
                        $scope.formData.txdate = "";
                        $scope.formData.txtime = "";
                        $scope.formData.basicprice = "";
                        $scope.formData.ceilingprice = "";
                        $scope.formData.floorprice = "";
                        $scope.formData.bidprice1 = "";
                        $scope.formData.bqtty1 = "";
                        $scope.formData.askprice1 = "";
                        $scope.formData.aqtty1 = "";
                        $scope.formData.lqtty = "";
                        alert('Bạn đã thêm thành công')
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    alert(err);
                });} else {
                    alert ("Lệnh đã được hủy")
                }
            }

            vm.updateSecurity = function () {

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

            vm.saveUpdateSecurity = function () {
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
                data.saveUpdateSecurity(updatedItems[updatedItems.length-1]).then(function (res) {
                    alert("Cập nhật thành công!")
                    data.chungkhoan().then(function (result) {
                        $scope.loading = false;

                        vm.chungkhoan = result.data;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    alert(err);
                });} else {
                    alert("Lệnh đã được hủy")
                }
            }

            vm.deleteSecurity = function (chungkhoan) {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){

                var delVal = {
                    symbol : chungkhoan.symbol,

                }
                data.deleteSecurity(delVal).then(function (res) {
                    data.chungkhoan().then(function (result) {
                        $scope.loading = false;

                        vm.chungkhoan = result.data;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });
                    alert("Bạn đã xóa thành công")
                }, function (err) {
                    console.log(err)
                })} else {
                    alert("Lệnh đã được hủy")
                }
            }


        }
    ])

