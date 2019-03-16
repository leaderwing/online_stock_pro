angular.module('app').factory('request',
    [
        '$http', '$q', '$timeout','$window',
        function ($http, $q, $timeout, $window) {
            var methods = {
                get: get,
                post: post,
                deletes: deletes
            };
            function post(url, data, header) {
                var _defer = $q.defer();
                var req = {
                    method: 'POST',
                    url: url,
                    headers: {
                    },
                    data: data,
                    timeout: 30000
                };
                if (header != null) {
                    req.headers.Accept = 'application/json';
                }
                $http(req).then(function (data) {
                    
                    _defer.resolve(data.data);
                }, function (err) {
                    _defer.resolve({
                        msg: err
                    });
                });
                return _defer.promise;
            }
            function get(url) {
                var defer = $q.defer();
                var config = {
                    url: url,
                    method: 'GET',
                    timeout: 20000
                }
                $http(config).then(function (data) {
                    console.log(data)
                    defer.resolve(data.data)
                    // if (data.data.error == 0) {
                    //     defer.resolve(data.data);
                    // } else {
                    // }

                }, function (err) {
                    console.log(err)
                    $window.location.href = '/';
                });
                return defer.promise;
            }
            function deletes(url) {
                var defer = $q.defer();
                var config = {
                    url: url,
                    method: 'DELETE',
                    timeout: 20000
                }
                $http(config).then(function (data) {
                    console.log(data)
                    defer.resolve(data.data)
                    // if (data.data.error == 0) {
                    //     defer.resolve(data.data);
                    // } else {
                    // }

                }, function (err) {
                    defer.resolve({
                        msg: err
                    });
                });
                return defer.promise;
            }
            return methods;
        }
    ]
);
