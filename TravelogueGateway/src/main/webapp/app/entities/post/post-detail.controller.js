(function() {
    'use strict';

    angular
        .module('travelogueGatewayApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Post'];

    function PostDetailController($scope, $rootScope, $stateParams, entity, Post) {
        var vm = this;
        vm.post = entity;
        
        var unsubscribe = $rootScope.$on('travelogueGatewayApp:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
