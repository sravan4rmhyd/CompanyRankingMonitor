angular.module('companyPortfolio.controllers', ['ui.bootstrap'])
    .constant("buy", "Buy")
    .constant("sell", "Sell")
    .controller('companyController',
    ['$scope', '$uibModal', 'TradeService',
    function ($scope, $uibModal, tradeService) {
        $scope.notifications = [];
        $scope.positions = {};

        var processQuote = function(quote) {
            var existing = $scope.positions[quote.companyCode];
            if(existing) {
                existing.change = quote.price - existing.price;
                existing.price = quote.price;
            }
        };
        var udpatePosition = function(position) {
            var existing = $scope.positions[position.companyCode];
            if(existing) {
                existing.points = position.points;
            }
        };
        var pushNotification = function(message) {
            $scope.notifications.unshift(message);
        };

        var validateTrade = function(trade) {
            if (isNaN(trade.points) || (trade.points < 1)) {
                $scope.notifications.push("Trade Error: Invalid number of points");
                return false;
            }
            if ((trade.action === "Sell") && (trade.points > $scope.positions[trade.companyCode].points)) {
                $scope.notifications.push("Trade Error: Not enough points");
                return false;
            }
            return true;
        }

        $scope.openTradeModal = function (action, position) {
            var modalInstance = $uibModal.open({
                templateUrl: 'tradeModal.html',
                controller: 'TradeModalController',
                size: "sm",
                resolve: {
                    action: action,
                    position: position
                }
            });
            modalInstance.result.then(function (result) {
                var trade = {
                    "action" : result.action,
                    "companyCode" : result.position.companyCode,
                    "points" : result.numberOfPoints
                };
                if(validateTrade(trade)) {
                    tradeService.sendTradeOrder(trade);
                }
            });
        };

        $scope.logout = function() {
            tradeService.disconnect();
        };

        tradeService.connect("/CompanyRankingMonitor/watchlist")
            .then(function (username) {                    
                    return tradeService.loadPositions();
                },
                function (error) {
                    pushNotification(error);
                })
            .then(function (positions) {
                positions.forEach(function(pos) {
                    $scope.positions[pos.companyCode] = pos;
                });
                tradeService.fetchQuoteStream().then(null, null,
                    function(quote) {
                        processQuote(quote);
                    }
                );
                tradeService.fetchPositionUpdateStream().then(null, null,
                    function(position) {
                        udpatePosition(position);
                    }
                );
                tradeService.fetchErrorStream().then(null, null,
                    function (error) {
                        pushNotification(error);
                    }
                );
            });

    }])
    .controller('TradeModalController',
            ["$scope", "$uibModalInstance", "TradeService", "action", "position",
            function ($scope, $uibModalInstance, tradeService, action, position) {

        $scope.action = action;
        $scope.position = position;
        $scope.numberOfPoints = 0;
        $scope.trade = function () {
            $uibModalInstance.close({action: action, position: position, numberOfPoints: $scope.numberOfPoints});
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }])
    .filter('percent', ['$filter', function ($filter) {
        return function (input, total) {
            return $filter('number')(input / total * 100, 1) + '%';
        };
    }])
    .filter('totalPortfolioShares', [function () {
        return function (positions) {
            var total = 0;
            for(var companyCode in positions) {
                total += positions[companyCode].points;
            }
            return total;
        };
    }])
    .filter('totalPortfolioValue', [function () {
        return function (positions) {
            var total = 0;
            for(var ticker in positions) {
                total += positions[companyCode].price * positions[companyCode].points;
            }
            return total;
        };
    }]);