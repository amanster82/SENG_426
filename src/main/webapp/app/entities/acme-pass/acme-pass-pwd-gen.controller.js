(function () {
    'use strict';

    angular
        .module('acmeApp')
        .controller('ACMEPassPwdGenController', ACMEPassPwdGenController);

    ACMEPassPwdGenController.$inject = ['$timeout', '$scope', '$uibModalInstance'];

    function ACMEPassPwdGenController($timeout, $scope, $uibModalInstance) {
        var vm = this;

        vm.clear = clear;
        vm.generate = generate;
        vm.save = save;
        vm.checkSufficient = checkSufficient;

        vm.genOptions = {
            length: 8,
            lower: true,
            upper: true,
            digits: true,
            special: true,
            repetition: false
        };

        vm.chars = {
            lower: "qwertyuiopasdfghjklzxcvbnm",
            upper: "QWERTYUIOPASDFGHJKLZXCVBNM",
            digits: "0123456789",
            special: "!@#$%-_"
        };

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function generate() {
            var chars = "";
            vm.password = "";

            if (vm.genOptions.lower) {
                chars += vm.chars.lower;
            }

            if (vm.genOptions.upper) {
                chars += vm.chars.upper;
            }

            if (vm.genOptions.digits) {
                chars += vm.chars.digits;
            }

            if (vm.genOptions.special) {
                chars += vm.chars.special;
            }

            for (var i = 0; i < vm.genOptions.length; i++) {
                var position = Math.round(Math.random() * (chars.length - 1));
                if (vm.genOptions.repetition) {
                    if (vm.password.indexOf(chars[position]) === -1) {
                        vm.password += chars[position];
                    } else {
                    	i--;
                    }
                } else {
                    vm.password += chars[position];
                }
            }
        }

        function save() {
            $scope.$emit('acmeApp:ACMEPassPwdGen', vm.password);
            $uibModalInstance.close(vm.password);
        }
        
        // Checks to make sure that the user has some characters selected in the generation modal
        // If no possible characters selected, the 'Generate' button is disabled
        function checkSufficient(){
        	var selected = angular.element('.character-option:checked').length;
        	if(selected == 0){
        		angular.element('#generateSubmit').prop('disabled', true);
        	} else {
        		angular.element('#generateSubmit').prop('disabled', false);
        	}
        }
    }
})();
