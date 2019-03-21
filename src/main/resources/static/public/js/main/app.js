var app = angular.module("app.todos", []);

app.controller("todoController", ['$scope', '$window', 'svTodos', function ($scope, $window, svTodos) {

    $scope.formData = {};
    $scope.loading = true;

    $scope.todos = [];
    $scope.count = 0;
    
    svTodos.randomAccount().then(function (response) {
        data = response.data;
        $scope.account = data;
        console.log(data)
        $window.localStorage.setItem('tokensss', response.data);
    }, function (err) {
        console.log(err);
    });
    
}]);