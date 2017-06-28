app.controller('userCtrl', ['$scope', '$rootScope', 'DataProvider','$routeParams','$http',
	function ($scope, $rootScope, DataProvider,$routeParams, $http) {
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
		$scope.showSpinner=true;
		$rootScope.nickname=$('#user').text();
		console.log("username :" + $rootScope.nickname);
		DataProvider.getMyInfo($rootScope.nickname).then(function(response){
			$scope.user=response;
			console.log(response);
			$scope.showSpinner=false;
			$scope.$apply();
		});
	}

	$scope.checkIfValid=function(isValid,$event){
		console.log(isValid);
		var data={'oldpsw':$scope.oldpsw,'newpsw':$scope.newpsw};
		console.log(data);
		if(!isValid){
			$event.preventDefault();
			/*
			 $http
			.post("/changePassword",data)
			.then(()=>{
				console.log("uttapposto");
			})
			*/
		//	/changePassword
		}
	}
	
	$scope.update=function(){
		$scope.$apply();
	}



}]);


