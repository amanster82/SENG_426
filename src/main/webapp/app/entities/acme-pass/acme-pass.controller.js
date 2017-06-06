(function () {
    'use strict';

    angular
        .module('acmeApp')
        .controller('ACMEPassController', ACMEPassController);

    ACMEPassController.$inject = ['$scope', '$state', 'ACMEPass', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ACMEPassController($scope, $state, ACMEPass, ParseLinks, AlertService, paginationConstants, pagingParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.toggleVisible = toggleVisible;

        loadAll();

        function loadAll() {
            ACMEPass.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.acmePasses = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function toggleVisible(id) {
        	
        	// Change inputs type from 'password' to 'text'
        	var fieldType = angular.element('#passField'+id).attr('type');
        	if(fieldType == 'password'){
        		angular.element('#passField'+id).attr('type', 'text');
        	} else {
        		angular.element('#passField'+id).attr('type', 'password');
        	}

        	// Toggle eye icon
        	if(fieldType == 'password'){
        		angular.element('#passField'+id).parent().find('span').addClass('glyphicon-eye-close');
        		angular.element('#passField'+id).parent().find('span').removeClass('glyphicon-eye-open');
        	} else {
        		angular.element('#passField'+id).parent().find('span').addClass('glyphicon-eye-open');
        		angular.element('#passField'+id).parent().find('span').removeClass('glyphicon-eye-close');
        	}
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
