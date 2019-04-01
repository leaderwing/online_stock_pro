angular.module('app').factory('config',
    [
        function () {
            var config = {
                HOST: 'http://localhost:8080',
            }
            return config;
        }
    ])