angular.module('app').factory('config',
    [
        function () {
            var config = {
                HOST: 'http://10.176.28.15',
            }
            return config;
        }
    ])