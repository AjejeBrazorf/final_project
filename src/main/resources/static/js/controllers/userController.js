app.controller('userCtrl', ['$scope', '$rootScope', 'DataProvider','$routeParams',
	function ($scope, $rootScope, DataProvider,$routeParams) {
	$scope.img={
			compressed:{
				dataURL:""
			}
	};
	$scope.nickname={};
	$scope.user={};
	


	$scope.saveImgOnServer=function(){
		console.log("salvo img su server");
		console.log($scope.img);	
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
		console.log(" nickname"+$rootScope.nickname);
		console.log(" auth"+$rootScope.auth);
		
	}

	
	$scope.update=function(){
		$scope.$apply();
	}



}]);


