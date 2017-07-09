app.controller('userCtrl', ['$scope', '$rootScope', 'DataProvider','$routeParams','$http','$sce',
	function ($scope, $rootScope, DataProvider,$routeParams, $http,$sce) {
	$scope.img={
			compressed:{
				dataURL:""
			}
	};
	$scope.nickname={};
	$scope.user={};

	$scope.newpswError=false;
	$scope.oldpswError=false;

	$scope.saveImgOnServer=function(){
		DataProvider.changeImageUser($scope.img.compressed.dataURL).then( function(response){
			console.log("foto aggiunta:"+response);
		});
	};

	$scope.setuserImage=function(){
		$scope.img.compressed.dataURL=$('#imgsrc').val();
	};

	$scope.openFileReader=function(){
		$(this).find('#inp:hidden').click();
		$('input:file')[0].click();

	}

	document.getElementById("inp").addEventListener("change", $scope.readFile);
	$('#img').click(function(e) {
		$(this).find('#inp:hidden').click();
		$('input:file')[0].click();
	});




	$scope.getUserInfo=function(){
		$scope.showSpinner=true;
		$rootScope.nickname=$('#user').text();
		DataProvider.getMyInfo($rootScope.nickname).then(function(response){
			$scope.user=response;
			$scope.showSpinner=false;
			$scope.$apply();
		});
	}

	$scope.checkIfValid=function(isValid,$event){
		var data={'oldpsw':$scope.oldpsw,'newpsw':$scope.newpsw};
		$scope.newpswError=false;
		$scope.oldpswError=false;

		if(!isValid){
			$event.preventDefault();
			$scope.error="";
			if($scope.oldpsw==undefined){
				$scope.error+="The old password can not be empty <br />";
				$scope.oldpswError=true;
			}
			if($scope.newpsw==undefined){
				$scope.error+="The new password can not be empty  or less than 8 characters <br />";
				$scope.newpswError=true;
			}
			$scope.error=$sce.trustAsHtml($scope.error);
		}
	}
	
	$scope.update=function(){
		$scope.$apply();
	}



}]);


