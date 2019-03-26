angular.module('app').factory('config',
    [
        function () {
            var config = {
                HOST: 'http://locahost:8080',
            }
            return config;
        }
    ])