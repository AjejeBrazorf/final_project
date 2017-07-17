app.controller('navCtrl', ['$scope', '$rootScope',
	function ($scope, $rootScope) {
	$scope.nickname={};
	
	//set nickname user on the rootscope
	$scope.setNickname=function(){
		$rootScope.nickname=$scope.nickname;
	}
	 
	//set the color of the navbar tab depending on the page you are 
	$rootScope.$on("$routeChangeSuccess", function (event, current, previous, rejection) {
		$scope.userImage=$rootScope.rootUserImage;
		console.log("success on "+current.$$route.originalPath);
		//set the color of the navbar tab depending on the page i am
		$("li>a").each(function () {
			var href=this.href.split("/")[this.href.split("/").length-1];
			var last=document.URL.split("/")[document.URL.split("/").length-1];
			last=last.split("?")[0];
			console.log(last);
			if(href == last){
				$(this).addClass('active');

				switch (href){
				case "stops":
					$(this).css( "background-color" ,"#4285f4");
					break;
				case "route":
					$(this).css( "background-color" ,"#ea4335");
					break;
				case "signals":
					$(this).css( "background-color" ,"#fbbc05");
					break;
				case "chat":
					$(this).css( "background-color" ,"#34a853");
					break;
				case "userpage":
					$(this).css( "background-color" ,"#2f9be0");
					break;
				};
			}else{

				$(this).css( "background-color" ,"rgba(0,0,0,0)");
				$(this).removeClass('active');
			}
		});
	});


}]);