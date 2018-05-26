/**
 * Created by YJL on 2017/12/20.
 */
/**
 * Created by YJL on 2017/4/17.
 */
(function(){
    'use strict';
    var teacherApp = angular.module('teacherApp',[]);
    teacherApp.controller('showListCtrl',['$http','$scope',ShowListCtrl]);
    function ShowListCtrl($http,$scope) {
        var vm = this;
        vm.showSingleTeacher = showSingleTeacher;

        init();
        function init() {
            $http({
                url:"http://localhost:8080/",
                method:'GET',
            }).success(function(res) {
                $scope.teachers = res;
            });
        }

        function showSingleTeacher(id){
            $http({
                url:"http://localhost:8080/",
                method:'GET',
                params:{
                    'id':id,
                }
            }).success(function(res) {
                window.location()
            });
        }
    }

})();