var sessionVal = "";
angular.module('app', ['ui.router', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'angularMoment', 'infinite-scroll', 'infinite-scroll'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider.state('root', {
            abstract: true,
            templateUrl: "components/root/rootView.html",
            controller: 'rootCtrl as vm'
        }).state('login', {
                      url: '/login',
                      templateUrl: "components/page/login/login.html",
                      controller: 'LoginController as vm',
                  }).state('users', {
                  parent: 'nav',
                  url: '/users',
                  data: {
                      role: 'ADMIN'
                  },
                  views: {
                      'content@': {
                          templateUrl: 'components/page/users/users.html',
                          controller: 'UsersController',
                      }
                  }
              }).state('home', {
                  parent: 'nav',
                  url: '/',
                  views: {
                      'content@': {
                          templateUrl: 'components/page/home/home.html',
                          controller: 'HomeController'
                      }
                  }
              }).state('page-not-found', {
                  parent: 'nav',
                  url: '/page-not-found',
                  views: {
                      'content@': {
                          templateUrl: 'components/page/page-not-found/page-not-found.html',
                          controller: 'PageNotFoundController'
                      }
                  }
              }).state('access-denied', {
                  parent: 'nav',
                  url: '/access-denied',
                  views: {
                      'content@': {
                          templateUrl: 'components/page/access-denied/access-denied.html',
                          controller: 'AccessDeniedController'
                      }
                  }
              }).state('register', {
                  parent: 'nav',
                  url: '/register',
                  views: {
                      'content@': {
                          templateUrl: 'components/page/register/register.html',
                          controller: 'RegisterController'
                      }
                  }
              }).state('root.price-list', {
            url: '/price-list',
            templateUrl: "components/page/priceList/priceList.html",
            controller: 'priceListCtrl as vm',
        }).state('root.stock-trading', {
            url: '/stock-trading',
            templateUrl: "components/page/stockTrading/StockTradingView.html",
            controller: 'stockTradingCtrl as vm',
        }).state('root.stock-tradinghits', {
            url: '/stock-tradinghits',
            templateUrl: "components/page/hits/hitsView.html",
            controller: 'hitsController as vm',
        }).state('root.chung-khoan', {
            url: '/chung-khoan',
            templateUrl: "components/page/thamso/chungkhoan.html",
            controller: 'chungkhoanCtrl as vm',
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
        })
        $urlRouterProvider.otherwise('/xu-ly-tai-khoan')

    }).run(function (AuthService, $rootScope, $state) {
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
    .controller('appCtrl', ['$scope', function ($scope) {
          }])