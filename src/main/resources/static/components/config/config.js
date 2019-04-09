angular.module('app').factory('config',
    [
        function () {
            var config = {
                HOST: 'http://stock88.com.vn',
            }
            return config;
        }
    ])