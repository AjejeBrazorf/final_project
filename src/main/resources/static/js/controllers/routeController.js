app.controller('RouteCtrl', ['$scope', 'DataProvider','$routeParams','$timeout','$q','leafletData','$timeout',
	function ($scope, DataProvider,$routeParams, $timeout, $q, leafletData, $timeout) {



	angular.extend($scope, {
		centerLocation: {
			lat: 45.07,
			lng: 7.64,
			zoom: 11
		},
		paths: {
			p1: {
				color: 'red',
				weight: 8,
				latlngs: [

					]            },
		},
		defaults: {
			path: {
				weight: 8,
				color: '#5581ab',
				opacity: 1
			}
		},
		markers:{
			pathPoints:{
				
			}
		},
		decorations: {
			byFoot: {
				coordinates: [],
				patterns:  [
					{
						offset: 0,
						repeat: 10,
						symbol: L.Symbol.dash({pixelSize: 0, color: '#3287d2',weight: 4, opacity: 1})
					}
					]
			},
			byBus: {
				coordinates: [],
				patterns:[

					{
						offset: 0,
						repeat: 25,
						symbol: L.Symbol.arrowHead({pixelSize: 10, polygon: false, pathOptions: {color: '#ea4335',stroke: true, opacity: 1}})
					}
					]
			}
		}
	});

	$scope.yourPosition="Your position";
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
		console.log("quesry searc -> "+query+"\n hints");
		console.log($scope.hints);
		var results = $scope.hints;
		return results;
	}


	function searchTextChange(text) {
		console.log('Text changed to ' + text);

		if(text.length>0){
			console.log("hints");
			console.log($scope.hints);
			DataProvider.getPositionFromString(text, $scope.onPositionAddress, onErrorPositionAddress);
		}else{
			$scope.hints=[];
		}
	}

	function selectedItemChange(item) {
		if(item==$scope.yourPosition){
			var localPosition=DataProvider.getCurrentPosition(onCurrentPosition);
		}
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
		console.log("createefilterfor");
		return function filterFn(lowercaseQuery) {
			return ($scope.hints.indexOf(lowercaseQuery) != -1);
		};

	}

	function createFilterExistance(query) {
		var lowercaseQuery = angular.lowercase(query);
		console.log("createefilterexistance");

		return function filterFn(lowercaseQuery) {
			return ($scope.hints.value === lowercaseQuery);
		};

	}
	//END code for md-autocomplete

	$scope.from = "";
	$scope.to = "";
	console.log($routeParams.from+" - "+ $routeParams.to);
	if($routeParams.from!=null && $routeParams.to!=null ){
		$scope.from=$routeParams.from;
		$scope.to= $routeParams.to;
	}
	/*$scope.from = DataProvider.points.from;
        $scope.to =  DataProvider.points.to;*/

	$scope.findPath = function(){
		var flagFrom = false, flagTo = false;
		console.log("entro in funzione");
		console.log($scope.from+" == "+ $scope.yourPosition);
		if($scope.from==$scope.yourPosition)
			$scope.from=$scope.myLat+","+$scope.myLng;
		console.log($scope.from+" == "+ $scope.yourPosition);

		var lat1, lng1, lat2, lng2;
		DataProvider.getPositionFromString($scope.from, function(positions){
			flagFrom = true;
			lat1=positions[0].geometry.location.lat();
			lng1=positions[0].geometry.location.lng();
			console.log(lat1);
			console.log(lng1);

			if(flagTo == true){
				// fai richiesta al server
				$scope.makePathRequest(lat1, lng1, lat2, lng2);
			}

		}, onErrorPositionAddress);

		console.log($scope.to+" == "+ $scope.yourPosition);
		if($scope.to==$scope.yourPosition)
			$scope.to=$scope.myLat+","+$scope.myLng;
		console.log($scope.to+" == "+ $scope.yourPosition);

		DataProvider.getPositionFromString($scope.to, function(positions){
			flagTo = true;
			lat2=positions[0].geometry.location.lat();
			lng2=positions[0].geometry.location.lng();
			console.log(lat2);
			console.log(lng2);

			if(flagFrom == true){
				// fai richiesta al server
				$scope.makePathRequest(lat1, lng1, lat2, lng2);
			}

		}, onErrorPositionAddress);
	}

	$scope.getPointNameStart= function(text,iPath){
		$showSpinner=true;
		console.log("waiting for nr "+iPath);
		DataProvider.getPositionFromString(text, function(position){
			console.log(position[0].formatted_address);
			$scope.fullPath[iPath].start=position[0].formatted_address;
			$scope.checkSpinner();
		}, onErrorPositionAddress);
	}

	$scope.getPointNameEnd= function(text,iPath){
		$showSpinner=true;
		console.log("waiting for nr "+iPath);
		DataProvider.getPositionFromString(text, function(position){
			console.log(position[0].formatted_address);
			$scope.fullPath[iPath].end=position[0].formatted_address;
			$scope.checkSpinner();
		}, onErrorPositionAddress);
	}

	$scope.checkSpinner= function(){
		$scope.countStartEndPointReceived++;
		if($scope.countStartEndPointReceived==$scope.fullPath.length-1){
			$scope.showSpinner=false;
			console.log($scope.fullPath);
		}
	}

	$scope.makePathRequest = function(lat1, lng1, lat2, lng2){
		$scope.error="";
		$scope.showSpinner=true;
		$scope.$apply();
		DataProvider.findPath(lat1, lng1, lat2, lng2).then(function(positions){	
			if(positions.length==0){
				$scope.error="No route found";
				$scope.showSpinner=false;
				return;
			}
			$scope.countStartEndPointReceived=0;
			$scope.byFoot={};
			let countFoot=0;
			$scope.byFoot[countFoot]={
					patterns:[],
					coordinates:[]
			};
			$scope.byBus={};
			let countBus=0;
			$scope.byBus[countBus]={
					patterns:[],
					coordinates:[]
			};
			let path=[];
			let lastMode=positions[0].mode;
			var lastNameFrom=positions[0].nameFrom;
			var nameTo= positions[0].nameTo;
			var lineId= positions[0].lineId;
			var startPath=[positions[0].lat, positions[0].lng];

			$scope.fullPath={};
			var countFullPath=0;
			var countAllPasses=0;

			var maxLat,minLat;
			maxLat=minLat=positions[0].lat;
			var maxLng,minLng;
			maxLng=minLng=positions[0].lng;
			$scope.markersPath={};

			angular.forEach(positions, function(value, key) {	
				let start = [value.lat, value.lng];
				let end = [value.lat2, value.lng2];

				if(value.lat > maxLat) maxLat=value.lat;
				if(value.lat < minLat) minLat=value.lat;
				if(value.lng > maxLng) maxLng=value.lng;
				if(value.lng < minLng) minLng=value.lng;

				if(value.lat2 > maxLat) maxLat=value.lat2;
				if(value.lat2 < minLat) minLat=value.lat2;
				if(value.lng2 > maxLng) maxLng=value.lng2;
				if(value.lng2 < minLng) minLng=value.lng2;

				countAllPasses++;


				if(value.mode==true){
					$scope.byFoot[countFoot]={
							patterns:[],
							coordinates:[]
					};
					$scope.byFoot[countFoot].patterns=$scope.decorations.byFoot.patterns;
					$scope.byFoot[countFoot].coordinates.push(start);
					$scope.byFoot[countFoot].coordinates.push(end);
					countFoot++;
				}else{
					$scope.byBus[countBus]={
							patterns:[],
							coordinates:[]
					};
					$scope.byBus[countBus].patterns=$scope.decorations.byBus.patterns;
					$scope.byBus[countBus].coordinates.push(start);
					$scope.byBus[countBus].coordinates.push(end);
					countBus++;
				}
				
				if(value.mode!=lastMode || lineId != value.lineId){

					$scope.fullPath[countFullPath]={
							start:"",
							end:"",
							type:""
					};
					
					nameTo=value.nameFrom;
					startPath=start;
					
					console.log(countFullPath+"-"+positions.length+"-"+	countAllPasses);
					if(countFullPath!=0 && countAllPasses!=positions.length){
						$scope.fullPath[countFullPath].start=lastNameFrom;
						$scope.fullPath[countFullPath].end=nameTo;
					}else{
						if(countFullPath==0) {
							$scope.fullPath[countFullPath].start=$scope.from;
							$scope.fullPath[countFullPath].end=nameTo;

						}else{
							$scope.fullPath[countFullPath].start=lastNameFrom;
							$scope.fullPath[countFullPath].end=$scope.to;							
						}
					}
					if(lastMode==false){
						$scope.fullPath[countFullPath].type="bus";
						$scope.fullPath[countFullPath].lineId=lineId;
					}else{
						$scope.fullPath[countFullPath].type="foot";
					}
					$scope.markersPath["Marker"+countFullPath]={
						lat: startPath[0], 
						lng: startPath[1],
						message: lastNameFrom,
						focus: false,
						draggable: false,
						icon: ""
					};
					console.log($scope.markers);
					console.log(value);
					console.log($scope.fullPath[countFullPath]);
					lastNameFrom=value.nameFrom;
					startPath=end;
					lineId=value.lineId;
					//$scope.getPointNameStart(path[0][0]+","+path[0][1],countFullPath);
					//$scope.getPointNameEnd(lastPoint[0]+","+lastPoint[1],countFullPath);
					countFullPath++;
				}
				lastMode=value.mode;

			});


			
			$scope.centerLocation={
					lat: (minLat+maxLat)/2,
					lng: (minLng+maxLng)/2,
					zoom: 20
			};
			console.log($scope.centerLocation);

			angular.forEach($scope.byFoot, function (value,key){
				$scope.decorations["foot"+key]=value;
			});
			angular.forEach($scope.byBus, function (value,key){
				$scope.decorations["bus"+key]=value;
			});

			console.log($scope.decorations);


			leafletData.getMap("idRoute").then(function(map) {});

			$timeout(()=>{
				$scope.centerLocation.zoom=15;
				leafletData.getMap("idRoute").then(function(map) {});
			},0.5);
			
			$scope.showSpinner=false;
		});
	};

	function findPathToServer(lat1, lng1, lat2, lng2){
		DataProvider.findPath(lat1, lng1, lat2, lng2).then(function(edges){
			console.log(edges);

			//da visualizzare a schermo
		});
	}

	$scope.lastFocused=-1;
	$scope.showPopUp = function(markerId){
		if($scope.lastFocused!==-1 && $scope.markersPath[$scope.lastFocused]!=undefined)
			$scope.markersPath[$scope.lastFocused].focus=false;
		$scope.lastFocused=markerId;
		console.log("center on "+markerId);
		$scope.markersPath[markerId].focus=true;
		
		$scope.centerLocation={
				lat: $scope.markersPath[markerId].lat,
				lng: $scope.markersPath[markerId].lng,
				zoom: 20
		};
		
		/*angular.extend($scope, {
			markers: $scope.markers
		});*/
	};
	
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

		$scope.myLat=pos.lat;
		$scope.myLng=pos.lng;

		$scope.centerLocation.zoom=10;
		/*
		$scope.markers.push({
			lat:  $scope.centerLocation.lat,
			lng:  $scope.centerLocation.lng,
			message: "I'm here!"
		});
		 */

		$scope.centerLocation.zoom=16;
		//updateMap
		leafletData.getMap("idRoute").then(function(map) {});
	}

	//var localPosition=DataProvider.getCurrentPosition(onCurrentPosition);

	$scope.onPositionAddress=function (positions) {
		console.log(positions);
		var lat=positions[0].geometry.location.lat();
		var lng=positions[0].geometry.location.lng();
		console.log(lat);
		console.log(lng);
		$scope.centerLocation.lat=lat;
		$scope.centerLocation.lng=lng;
		$scope.centerLocation.zoom=16;
		leafletData.getMap("idRoute").then(function(map) {});

		console.log("in position address hints");
		console.log($scope.hints);
		$scope.hints=[];
		$scope.hints.push($scope.yourPosition);
		angular.forEach(positions, function(value, key) {
			$scope.hints.push(value.formatted_address);
		});

		console.log($scope.hints);

//		map.setCenter(positions[0].geometry.location);
//		var marker = new google.maps.Marker({
//		map: map,
//		position: positions[0].geometry.location
//		});
	}

	function onErrorPositionAddress(status) {
		//alert('Geocode was not successful for the following reason: ' + status);
	}


}
]);
