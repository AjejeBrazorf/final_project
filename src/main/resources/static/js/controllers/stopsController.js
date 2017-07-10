app.controller('StopsCtrl', ['$scope', 'DataProvider','$routeParams',
	function ($scope, DataProvider,$routeParams) {
	angular.extend($scope, {
		torino: {
			lat: 45.07,
			lng: 7.64,
			zoom: 11
		},
		defaults: {
			path: {
				weight: 10,
				color: '#800000',
				opacity: 1
			}
		},
		paths: {
			p1: {
				color: '#5581ab',
				weight: 8,
				latlngs: []
			}
		}
	});

	$scope.showPath= function (stops) {
		$scope.latlngs = [];
		$scope.stopsMarkes = {};
		$scope.center={};
		var maxLat,minLat,maxLng,minLng;
		maxLat=minLat=45.07;
		maxLng=minLng=7.64;
		angular.forEach(stops, function (stop, k) {
			var s = {
					lat: stop.lat,
					lng: stop.lng
			};
			$scope.latlngs.push(s);

			var s="<b style='text-align center'>"+stop.name+"</b><br><br>";
			if(stop.lines.length>0) s+="Lines: <br>";
			angular.forEach(stop.lines, function (line, k) {
				s+=line+"<br>";
			});
			$scope.stopsMarkes[stop.id]={
					lat: stop.lat,
					lng: stop.lng,
					focus: false,
					message: s,
					draggable: false
			};
			if(minLat>stop.lat){
				minLat=stop.lat;
			}
			if(minLng>stop.lng){
				minLng=stop.lng;
			}
			if(maxLat<stop.lat){
				maxLat=stop.lat;
			}
			if(maxLng<stop.lng){
				maxLng=stop.lng;
			}
		});


		$scope.center={
				lat: (minLat+maxLat)/2,
				lng: (minLng+maxLng)/2,
				zoom: 12
		};


		angular.extend($scope, {
			paths: {
				p1: {
					color: '#5581ab',
					weight: 8,
					latlngs: $scope.latlngs
				}
			},
			markers: $scope.stopsMarkes,
			torino: $scope.center
		});

	};
	$scope.percorsi=[];
	$scope.showSpinner=true;
	if($routeParams.lineToShow!=null) {

		$scope.lineToShow=$routeParams.lineToShow;
		$scope.lastFocused=-1;
		if( !DataProvider.lines || DataProvider.lines.indexOf($scope.lineToShow) != -1 ){

			$scope.lines= {
					"lines": DataProvider.getLine($routeParams.lineToShow)
			};

		}else{
			DataProvider.getLinePromise($routeParams.lineToShow).then( function(lines){
				$scope.lines= {
						"lines": lines
				}; 
			})
		}


	

		DataProvider.getPaths($scope.lineToShow).then(function(stops){
			$scope.percorsi = stops;
			$scope.showPath($scope.percorsi[0]);
			$scope.showSpinner=false;
		});
	}else {
		DataProvider.getLines().then(function(response){ 
			$scope.lines = response; 
			$scope.showSpinner=false;
		});
	}
	$scope.showPopUp = function(stopId){
		if($scope.lastFocused!==-1 && $scope.stopsMarkes[$scope.lastFocused]!=undefined)
			$scope.stopsMarkes[$scope.lastFocused].focus=false;
		$scope.lastFocused=stopId;         
		$scope.stopsMarkes[stopId].focus=true;
		angular.extend($scope, {
			markers: $scope.stopsMarkes
		});
	};



}
]);