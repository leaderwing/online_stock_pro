angular.module('app').factory('modal', ['$uibModal', 'data',
    function ($uibModal, data) {
        methods = {}
        methods.showStockTrading = function (message) {
            return modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'components/modal/tockTrading/tockTrading.html',
                controller: 'tockTradingCtrl',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    args: function () {
                        return message ? message : '';
                    }
                }
            });
        };
        return methods
    }
])