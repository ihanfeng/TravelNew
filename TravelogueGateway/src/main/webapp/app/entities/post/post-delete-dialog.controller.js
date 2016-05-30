(function() {
    'use strict';

    angular
        .module('travelogueGatewayApp')
        .controller('PostDeleteController',PostDeleteController);

    PostDeleteController.$inject = ['$uibModalInstance', 'entity', 'Post'];

    function PostDeleteController($uibModalInstance, entity, Post) {
        var vm = this;
        vm.post = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Post.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
