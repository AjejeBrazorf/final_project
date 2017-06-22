app.controller('RouteCtrl', ['$scope', 'DataProvider','$routeParams','$timeout','$q','leafletData',
	function ($scope, DataProvider,$routeParams, $timeout, $q, leafletData) {

	angular.extend($scope, {
		centerLocation: {
			lat: 45.07,
			lng: 7.64,
			zoom: 11
		},
		paths: {
			p1: {
			}
		},
		defaults: {
			path: {
				weight: 8,
				color: '#5581ab',
				opacity: 1
			}
		}
	});


	$scope.errorDest="";
	$scope.errorSrc="";
	$scope.markers=[];

	//START code for md-autocomplete

	var self = this;

	$scope.simulateQuery = false;
	$scope.noCache    = false;

	// list of `state` value/display objects
	$scope.hints=[];
	//$scope.hints        	= loadAll();
	$scope.querySearch   = querySearch;
	$scope.selectedItemChange = selectedItemChange;
	$scope.searchTextChange   = searchTextChange;

	// ******************************
	// Internal methods
	// ******************************

	/**
	 * Search for stops... use $timeout to simulate
	 * remote dataservice call.
	 */
	function querySearch (query) {
		var results = query ? $scope.hints.filter( createFilterFor(query) ) : $scope.hints,
				deferred;
		if ($scope.simulateQuery) {
			deferred = $q.defer();
			$timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
			return deferred.promise;
		} else {

			return results;
		}
	}


	function searchTextChange(text) {
		console.log('Text changed to ' + text);
		DataProvider.getPositionFromString($scope.from, onPositionAddress, onErrorPositionAddress);
	}

	function selectedItemChange(item) {
		console.log('Item changed to ' + JSON.stringify(item));
	}

	/**
	 * Build `stops` list of key/value pairs
	 */
	function loadAll() {
		var stops = DataProvider.getNameStops();
		console.log(stops.length);

		return  stops.map( function (stop) {
			return {
				value: stop.toLowerCase(),
				display: stop
			};
		});
	}

	/**
	 * Create filter function for a query string
	 */
	function createFilterFor(query) {
		var lowercaseQuery = angular.lowercase(query);

		return function filterFn(state) {
			return (state.value.indexOf(lowercaseQuery) === 0);
		};

	}

	function createFilterExistance(query) {
		var lowercaseQuery = angular.lowercase(query);

		return function filterFn(state) {
			return (state.value === lowercaseQuery);
		};

	}
	//END code for md-autocomplete

	$scope.from = "";
	$scope.to = "";
	/*$scope.from = DataProvider.points.from;
        $scope.to =  DataProvider.points.to;*/

	$scope.findPath = function(){
		var flagFrom = false, flagTo = false;
		console.log("entro in funzione");
		var lat1, lng1, lat2, lng2;
		DataProvider.getPositionFromString($scope.from, function(positions){
			flagFrom = true;
			lat1=positions[0].geometry.location.lat();
			lng1=positions[0].geometry.location.lng();
			console.log(lat1);
			console.log(lng1);
			
			if(flagTo = true){
				// fai richiesta al server
			}
			
		}, onErrorPositionAddress);
		
		DataProvider.getPositionFromString($scope.to, function(positions){
			flagTo = true;
			lat2=positions[0].geometry.location.lat();
			lng2=positions[0].geometry.location.lng();
			console.log(lat2);
			console.log(lng2);
			
			if(flagFrom = true){
				// fai richiesta al server
			}
			
		}, onErrorPositionAddress);
	}

	function findPathToServer(lat1, lng1, lat2, lng2){
		DataProvider.findPath(lat1, lng1, lat2, lng2).then(function(edges){
			console.log(edges);
			
			//da visualizzare a schermo
		});
	}

	//--------geolocation google
	$scope.map;
	$scope.infoWindow;

	function onCurrentPosition(position) {
		var pos = {
				lat: position.coords.latitude,
				lng: position.coords.longitude
		};  
		console.log("posizione presa");
		console.log(pos.lat);
		$scope.centerLocation.lat=pos.lat;
		$scope.centerLocation.lng=pos.lng;
		$scope.centerLocation.zoom=16;
		$scope.markers.push({
			lat:  $scope.centerLocation.lat,
			lng:  $scope.centerLocation.lng,
			message: "I'm here!"
		});

		//updateMap
		leafletData.getMap("myMap").then(function(map) {});
	}

	var localPosition=DataProvider.getCurrentPosition(onCurrentPosition);

	function onPositionAddress(positions) {
		console.log(positions);
		var lat=positions[0].geometry.location.lat();
		var lng=positions[0].geometry.location.lng();
		console.log(lat);
		console.log(lng);
		$scope.centerLocation.lat=lat;
		$scope.centerLocation.lng=lng;
		leafletData.getMap().then(function(map) {
			L.GeoIP.centerMapOnPosition(map, 16);
		});

		angular.forEach(positions, function(value, key) {
			$scope.hints.push(value.formatted_address);
		});

//		map.setCenter(positions[0].geometry.location);
//		var marker = new google.maps.Marker({
//		map: map,
//		position: positions[0].geometry.location
//		});
	}

	function onErrorPositionAddress(status) {
		alert('Geocode was not successful for the following reason: ' + status);
	}

}
]);
