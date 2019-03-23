var app = angular.module("app.todos")

app.factory("svTodos", ["$http",function($http){

    return {
       
        randomAccount: function(){
            return $http.get("/custid");
        },

        
    }

}]);