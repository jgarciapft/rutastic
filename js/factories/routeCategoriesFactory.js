angular.module('Rutastic')
    .factory('routeCategoriesFactory', ['$http', function ($http) {

        let restBaseUrl = 'https://nx4zpjerx5.execute-api.us-east-1.amazonaws.com/v1/categoriasruta';

        return {
            /**
             * Retrieve all the registered route categories
             *
             * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the response object,
             * which contains the collection of route categories
             */
            getAllRouteCategories: function () {
                return $http
                    .get(restBaseUrl)
                    .then(response => response);
            }
        }
    }])