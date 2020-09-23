/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var webApp = angular.module("webApp", ['ngSanitize']);

webApp.factory('$apply', ['$rootScope', function ($rootScope) {
        return function (fn) {
            setTimeout(function () {
                $rootScope.$apply(fn);
            });
        };
    }
]);

webApp.factory('$storage', function () {
    return {
        set: function (key, value) {
            localStorage[key] = JSON.stringify(value);
        },
        get: function (key) {
            if (!localStorage[key])
                return undefined;
            try {
                return $.parseJSON(localStorage[key]);
            } catch (ex) {
                return undefined;
            }
        },
        removeItem: function (key) {
            localStorage.removeItem(key);
        }
    };
});