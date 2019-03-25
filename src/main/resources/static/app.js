var sessionVal = "";
angular.module('app', ['ui.router', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'angularMoment', 'infinite-scroll', 'infinite-scroll'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('root', {
                abstract: true,
                templateUrl: "components/root/rootView.html",
                controller: 'rootCtrl as vm'
            }).state('login', {
            url: '/login',
            templateUrl: "components/page/login/login.html",
            controller: 'LoginController as vm',
        })
            .state('page-not-found', {
                parent: 'nav',
                url: '/page-not-found',
                views: {
                    'content@': {
                        templateUrl: 'components/page/page-not-found/page-not-found.html',
                        controller: 'PageNotFoundController'
                    }
                }
            })
            .state('access-denied', {
                parent: 'nav',
                url: '/access-denied',
                views: {
                    'content@': {
                        templateUrl: 'components/page/access-denied/access-denied.html',
                        controller: 'AccessDeniedController'
                    }
                }
            })
            .state('register', {
                url: '/register',
                templateUrl: 'components/page/register/register.html',
                controller: 'RegisterController as vm'
            }).state('reset-pass', {
            url: '/reset-pass',
            templateUrl: "components/page/login/resetPass.html",
            controller: 'ResetPasswordCtrl as vm'
            })
            .state('root.price-list', {
                url: '/price-list',
                templateUrl: "components/page/priceList/priceList.html",
                controller: 'priceListCtrl as vm',
            }).state('root.his-xy-ly-tai-khoan', {
            url: '/his-xy-ly-tai-khoan',
            data: {
                customData1: 44,
                customData2: "red"
            },
            templateUrl: "components/page/hisXuLyTaiKhoan/hisXuLyTaiKhoan.html",
            controller: 'hisXuLyTaiKhoan as vm',
        }).state('root.stock-trading', {
            url: '/stock-trading',
            templateUrl: "components/page/stockTrading/StockTradingView.html",
            controller: 'stockTradingCtrl as vm',
        }).state('root.aftype', {
            url: '/aftype',
            templateUrl: "components/page/afType/afType.html",
            controller: 'afTypeCtrl as vm',
        }).state('root.stock-tradinghits', {
            url: '/stock-tradinghits',
            templateUrl: "components/page/hits/hitsView.html",
            controller: 'hitsController as vm',
        }).state('root.chung-khoan', {
            url: '/chung-khoan',
            templateUrl: "components/page/thamso/chungkhoan.html",
            controller: 'chungkhoanCtrl as vm',
        }).state('root.users', {
            url: '/users',
            templateUrl: "components/page/users/users.html",
            controller: 'usersCtrl as vm',
        }).state('root.change-pass', {
            url: '/change-pass',
            templateUrl: "components/page/changePass/changePass.html",
            controller: 'changePassCtrl as vm',
        }).state('root.xu-ly-tai-khoan', {
            url: '/xu-ly-tai-khoan',
            templateUrl: "components/page/xuLyTaiKhoan/xuLyTaiKhoan.html",
            controller: 'xuLyTaiKhoanCtrl as vm',
        }).state('root.luong-chung', {
            url: '/luong-chung',
            templateUrl: "components/page/luongChung/luongChung.html",
            controller: 'luongChungCtrl as vm'
        }).state('root.cap-tai-khoan', {
            url: '/cap-tai-khoan',
            templateUrl: "components/page/capTaiKhoan/capTaiKhoan.html",
            controller: 'capTaiKhoanCtrl as vm'
        }).state('root.nop-tien', {
            url: '/nop-tien',
            templateUrl: "components/page/nopTien/nopTien.html",
            controller: 'nopTienCtrl as vm'
        }).state('root.duyet-nop', {
            url: '/duyet-nop',
            templateUrl: "components/page/duyetNop/duyetNop.html",
            controller: 'duyetNopCtrl as vm'
        }).state('root.tham-so', {
            url: '/tham-so',
            templateUrl: "components/page/thamso/thamso.html",
            controller: 'thamsoCtrl as vm'
        }).state('root.duyet-rut', {
            url: '/duyet-rut',
            templateUrl: "components/page/duyetRut/duyetRut.html",
            controller: 'duyetRutCtrl as vm'
        }).state('root.rut-tien', {
            url: '/rut-tien',
            templateUrl: "components/page/rutTien/rutTien.html",
            controller: 'nop1TienCtrl as vm'
        }).state('root.tham-so-he-thong', {
            url: '/tham-so-he-thong',
            templateUrl: "components/page/thamSoHeThong/thamSoHeThong.html",
            controller: 'thamSoHeThong as vm'
        }).state('root.export-report', {
            url: '/export',
            templateUrl: "components/page/baocao/exportView.html",
            controller: 'exportCtl as vm'
        })
        $urlRouterProvider.otherwise('/stock-trading')

    }).controller('appCtrl', ['$scope', '$http', function ($scope, $http) {
    var user = document.cookie.split("$")[0];
    if (user && user != '' && user != 'undefined') {
        $http.defaults.headers.common['Authorization'] = 'Bearer ' + user;
    }
}])
    .run(function (AuthService, $rootScope, $state) {
        // For implementing the authentication with ui-router we need to listen the
        // state change. For every state change the ui-router module will broadcast
        // the '$stateChangeStart'.
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            // checking the user is logged in or not
            //var user = $cookies.user;
            var user = document.cookie;
            if (!user || user == '' || user == 'undefined') {
                // if condition.
                if (toState.name != 'login' && toState.name != 'register') {
                    event.preventDefault();
                    $state.go('login');
                }
            } else {
                //$http.defaults.headers.common['Authorization'] = 'Bearer ' + user;
                // checking the user is authorized to view the states
                if (toState.data && toState.data.role) {
                    var hasAccess = false;
                    for (var i = 0; i < AuthService.user.roles.length; i++) {
                        var role = AuthService.user.roles[i];
                        if (toState.data.role == role) {
                            hasAccess = true;
                            break;
                        }
                    }
                    if (!hasAccess) {
                        event.preventDefault();
                        $state.go('access-denied');
                    }

                }
            }
        });
    })