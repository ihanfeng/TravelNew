(function() {
    'use strict';
    angular
        .module('travelogueGatewayApp')
        .factory('Post', Post);

    Post.$inject = ['$resource'];

    function Post ($resource) {
        var resourceUrl =  'traveloguepost/' + 'api/posts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
