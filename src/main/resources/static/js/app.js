var app = angular.module('App', ['ngRoute', 'ngResource','ui-leaflet', 'ngMaterial','ngImageCompress'])

app.config(function ($routeProvider, $locationProvider,$httpProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'main.html',
	})
	.when('/route', {
		templateUrl: 'route.html',
		controller: 'RouteCtrl',
		controllerAs: 'ctrl'
	})
	.when('/stops', {
		templateUrl: 'stops.html',
		controller: 'StopsCtrl',
		controllerAs: 'ctrl'
	})
	.when('/signals', {
		templateUrl: 'signals.html',
		controller: 'SignalsCtrl',
		controllerAs: 'ctrl'
	})
	.when('/chat', {
		templateUrl: 'chat.html',
	})
	.otherwise({ redirectTo: "/" });
	$locationProvider.hashPrefix('');
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.controller('AuthCtrl', ['$scope', '$rootScope',
	function ($scope, $rootScope) {
	$rootScope.img={
			compressed:{
				dataURL:""
			}
	};
	$rootScope.authenticated=false;
	$scope.setAuthenticated=function(auth){
		console.log(auth);
		$rootScope.authenticated=auth;
		console.log($rootScope.authenticated);
		//setto immagine se autenticato
		if(auth){
			$rootScope.img.compressed.dataURL=$('#imgsrc').val();
			$rootScope.nickname=$('#user').text();
			console.log("username :" + $rootScope.nickname);
		}
	}
	
	$scope.setuserImage=function(){
		$scope.img.compressed.dataURL=$('#imgsrc').val();
	}

}]);

app.factory('DataProvider', 
		function($http, $rootScope){
	var DataProvider = {};
	var lines = [];
	var stops = [];

	DataProvider.getLines= function() {
		return readJson('../lines').then(function (response){
			DataProvider.lines=response.lines;
			return response.lines;
		});
	};

	DataProvider.getLine= function(line) {
		var array = DataProvider.extractLine();
		return array;
	};

	DataProvider.getLinePromise= function(line) {
		var array = DataProvider.getLines().then( function(){return DataProvider.extractLine();});
		console.log("getLinePromise modafocka");
		return array;
	};

	DataProvider.extractLine = function(){
		var array;
		angular.forEach(lines, function(value, key) {
			if(value.line==line){
				array = value;
			}
		});
		return array;
	};

	DataProvider.getPaths= function(line) {
		return readJson('../'+line+'/stops').then( function (response){ 
			var paths = [];
			var stops = [];
			var stopKs= [];
			angular.forEach(response.stops, function (stop, stop_key) {
				if(stopKs.indexOf(stop.id)==-1 ) {
					stops.push(stop);
					stopKs.push(stop.id);
				}else{
					paths.push(stops);
					stops=[];
					stopKs=[];
					stops.push(stop);
					stopKs.push(stop.id);
				}
			});
			paths.push(stops);
			return paths;
		});
	};

	DataProvider.getSignals= function() {
		console.log($rootScope.nickname);
		if($rootScope.nickname==undefined || $rootScope.nickname==null || $rootScope.nickname.length==0){
			return readJson('../segnalations').then( function (response){ 
				console.log(response);
				return response;
			});
		}else{
			return readJson('../segnalations?name='+$rootScope.nickname).then( function (response){ 
				console.log(response);
				return response;
			});
		}
	};
	
	DataProvider.getMyInfo= function(nickname) {
		return readJson('../'+nickname+'/myInfo').then( function (response){ 
			console.log(response);
			return response;
		});
	};
	
	
	
	 
	DataProvider.addSignalToServer= function(item,nickname) {
		var values= {'lat': item.lat, 'lng': item.lng, 'type': item.type, 'address':item.address};
		var promise = $http({
			url: "../"+nickname+"/segnalations",
			method: "POST",
			params: values
		});
		promise.then( function(item) {
			return item.data;
		} , 
		function error() { }
		);
		return promise;
	};

	DataProvider.updateSignalRate= function(item,id,nickname) {
		var values={
				'action': item.action,
				'rate':item.rate
		}
		var promise = $http({
			url: "../"+nickname+"/segnalations/"+id,
			method: "PUT",
			params: values
		});
		promise.then( function(item) {
			//return the average
			return item;	
		} , 
		function error() { }
		);
		return promise;
	};

	
	DataProvider.deleteSignal= function(item,id) {
		var values={ 'action': item.action};
		var promise = $http({
			url: "../segnalations/"+id,
			method: "PUT",
			params: values
		});
		promise.then( function(item) {
			return item.data;
		} , 
		function error() { }
		);
		return promise;
	};
	
	



	function status(response) {  
		console.log(response.status);
	  if (response.status >= 200 && response.status < 300) {  
		    return Promise.resolve(response)
		  } else {  
			    return Promise.reject(new Error(response.statusText) )  
			  }  
	}

	function json(response) {
		console.log(response);

		  return response.json()
	}

	function readJson(url){
		return fetch(url)  
		  .then(status)  
		  .then(json)  
		  .then(function(data) {  
			    console.log('Request success with JSON', data); 
		return data; 
		  }).catch(function(error) {  
			    console.log('Request failed', error);  
			  });
	}

	//get the local position 
	DataProvider.getCurrentPosition = function(onSuccess){

		infoWindow = new google.maps.InfoWindow;

		// Try HTML5 geolocation.
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function(position) {
				onSuccess(position);
			}, function() {
				handleLocationError(true, infoWindow, map.getCenter());
			});
		} else {
			// Browser doesn't support Geolocation
			console.log("error");
		}

		return 

	}

	//get lat&long for a given name
	DataProvider.getPositionFromString = function(name, onSuccess, onError){
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode( { 'address': name}, function(results, status) {
			if (status == 'OK') {
				onSuccess(results);
			} else {
				
				if(status=='ZERO_RESULTS'){
					if(name.length>7)
					name=name.substr(0,name.length - 7);
					DataProvider.getPositionFromString(name, onSuccess, onError);
				}
				
				onError(status);
			}
		});
	}

	DataProvider.findPath = function(lat1, lng1, lat2, lng2){
		return readJson('/findPath?lat1='+lat1+'&lng1='+lng1+'&lat2='+lat2+'&lng2='+lng2)
		.then( function (response){ 
			return response;
		});

	}

	DataProvider.changeImageUser= function(foto) {
		var values= {'image': foto};
		var promise = $http({
			url: "../image",
			method: "PUT",
			params: values
		});
		promise.then( function(item) {
			console.log("Foto salvata");
		} , 
		function error() { 
			console.log("Errore in salvataggio foto");
		}
		);
		return promise;
	};

	return DataProvider;
}
);






app.directive('myDirective', function () {
	/*
     return {

     };
	 */
});

