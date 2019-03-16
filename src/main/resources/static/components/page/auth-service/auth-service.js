angular.module('app')
// Creating the Angular Service for storing logged user details
    .service('AuthService', function () {
         var user;

                return {
                    getUser: getUser,
                    setUser: setUser
                };

                // .................

                function getUser() {
                    return user;
                }

                function setUser(value) {
                    user = value;
                }
    });