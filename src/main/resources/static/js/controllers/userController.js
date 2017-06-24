app.controller('userCtrl', ['$scope', '$rootScope', 'DataProvider','$routeParams',
	function ($scope, $rootScope, DataProvider,$routeParams) {
    $scope.img={};
	$scope.readFile=function() {  
		if (this.files && this.files[0]) {
			var FR= new FileReader();
			FR.addEventListener("load", function(e) {
				document.getElementById("img").src= e.target.result;
				$rootScope.userImagesrc=e.target.result;	
			}); 

			FR.readAsDataURL( this.files[0] );
			console.log("imagine 64" + $rootScope.userImagesrc);	
		};	  
	};

	
	$scope.saveImgOnServer=function(){
		console.log("salvo img su server");
		console.log($scope.img.compressed.dataURL);	
		DataProvider.changeImageUser($scope.img.compressed.dataURL).then( function(response){
			console.log("foto aggiunta:"+response);
		});
	};

	$scope.setuserImage=function(){
		$rootScope.userImagesrc=$('#imgsrc').val();
		console.log($rootScope.userImagesrc);	
	}


	document.getElementById("inp").addEventListener("change", $scope.readFile);
	$('#img').click(function(e) {
		$(this).find('#inp:hidden').click();
		$('input:file')[0].click();
		console.log(" clicco clicco");
	});

}]);


