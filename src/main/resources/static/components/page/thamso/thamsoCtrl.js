angular.module('app').controller('thamsoCtrl',
    ['data', 'modal', '$window', '$rootScope', '$state', '$scope', 'dateFilter',
        function (data, modal, $window, $rootScope, $state, $scope, dateFilter) {
            var vm = this;
            $scope.loading = true;
            $scope.isDisabled = true;
            $scope.hidden = true;
            vm.thamso = {};
            data.thamso().then(function (result) {
                $scope.loading = false;

                vm.thamso = result;
                console.log(result);
            }, function (err) {
                console.log(err);
            });

            vm.addSysVar = function() {
                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                var todo = {};

                todo.grname = $scope.formData.gname;
                todo.varname  = $scope.formData.name;
                todo.varvalue = $scope.formData.value;
                todo.vardesc = $scope.formData.v_note;
                todo.en_vardesc = $scope.formData.e_note;

                data.addSysVar(todo).then(function (response) {
                    data.thamso().then(function (result) {
                        $scope.loading = false;

                        vm.thamso = result;
                        $scope.formData.gname = ""
                        $scope.formData.name  = ""
                        $scope.formData.value = ""
                        $scope.formData.v_note = ""
                        $scope.formData.e_note = ""
                       alert('Bạn đã thêm thành công')
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                });} else {
                    alert("Lệnh đã được hủy")
                }
            }

            vm.updateSysVar = function () {

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

            vm.saveUpdateSysVar = function () {
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
                data.saveUpdateSysVar(updatedItems[updatedItems.length-1]).then(function (res) {
                    alert("Cập nhật thành công!")
                    data.thamso().then(function (result) {
                        $scope.loading = false;

                        vm.thamso = result;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });
                    alert("Thành công")
                }, function (err) {
                    console.log(err);
                });}else {
                    alert ("Lệnh đã được hủy")
                }
            }
            
            vm.deleteSysVar = function (thamso) {

                var t = confirm('Bạn có chắc chắn muốn thực hiện');
                if (t === true){
                var delVal = {
                    grname : thamso.grname,
                    varname : thamso.varname
                }
                data.deleteSysVar(delVal).then(function (res) {
                    data.thamso().then(function (result) {
                        $scope.loading = false;

                        vm.thamso = result;
                        console.log(result);
                    }, function (err) {
                        console.log(err);
                    });
                  alert("Thành công")
                }, function (err) {
                   console.log(err)
                })} else {
                    alert ("Lệnh đã được hủy")
                }
            }

            return;
        }
    ])

