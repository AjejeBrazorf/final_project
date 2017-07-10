app.controller('SignalsCtrl', ['$scope', 'DataProvider','$routeParams','$timeout','$q','leafletData', "leafletMarkerEvents", "leafletLogger",'$filter','$compile','$rootScope', '$routeParams',
	function ($scope, DataProvider,$routeParams, $timeout, $q, leafletData, leafletMarkerEvents, leafletLogger,$filter, $compile,$rootScope,$routeParams) {
    //initialize map
	angular.extend($scope, {
		centerLocation: {
			lat: 45.07,
			lng: 7.64,
			zoom: 11
		},
		icons: {
			carJam: {
				type: 'awesomeMarker',
				icon: 'car',
				prefix: 'fa',
				markerColor:'red'
			},
			speedCamera: {
				type: 'awesomeMarker',
				prefix: 'fa',
				icon: 'camera',
				markerColor:'blue'
			},
			inProgress: {
				type: 'awesomeMarker',
				prefix: 'fa',
				icon: 'wrench',
				markerColor:'green'
			},
			carCrash: {
				type: 'awesomeMarker',
				prefix: 'fa',
				icon: 'ambulance',
				markerColor:'orange'
			}
		}
	});


	$scope.yourPositionString="Your position";
	$scope.signal={};
	$scope.signal.type="inProgress";
	$scope.signal.currentIcon=$scope.inProgressIcon;
	$scope.signal.message="";
	$scope.inProgressList={};
	$scope.speedCameraList={};
	$scope.carJamList={};
	$scope.carCrashList={};
	$scope.errorAddress="";
	$scope.markerAddress="";
	$scope.signalMarkers={};
	$scope.tagPressed=false;
	if($rootScope.dbMarkers!=null)
		$scope.signalMarkers=$rootScope.dbMarkers;


	$scope.signalToShow = {
			inProgress : true,
			speedCamera: true,
			carJam: true,
			carCrash: true
	};


	//markers EventListener
	$scope.events = {
			signalMarkers: {
				enable: leafletMarkerEvents.getAvailableEvents(),
			}
	};

	$scope.eventDetected = "No events yet...";
	var markerEvents = leafletMarkerEvents.getAvailableEvents();
	for (var k in markerEvents){
		var eventName = 'leafletDirectiveMarker.myMap.' + markerEvents[k];
		$scope.$on(eventName, function(event, args){            	
			if(event.name=="leafletDirectiveMarker.myMap.click"){
				$scope.signalMarkers[args.model.id].rate=$scope.signalMarkers[args.model.id].rate;
				var markerDiv = (angular.element(document.getElementById('Marker'+args.model.id)));
				$compile(markerDiv)($scope);

			}
		});
	}

	//START code for md-autocomplete

	var self = this;

	$scope.simulateQuery = false;
	$scope.noCache    = false;

	// list of `state` value/display objects
	$scope.hints=[];
	//$scope.hints        	= loadAll();
	$scope.querySearch   = querySearch;
	$scope.selectedItemChange = selectedItemChange;
	$scope.searchTextChange = searchTextChange;

	
	
	//get User Info 

	$scope.getUserInfo=function(){
		$rootScope.nickname=$('#user').text();
		DataProvider.getMyInfo($rootScope.nickname).then(function(response){
			$scope.user=response;
		});
	}
	
	$scope.getUserInfo();

	// ******************************
	// Internal methods
	// ******************
	
	


	$scope.signalsType = {
			"inprogress" : "inProgress",
			"speedcamera": "speedCamera",
			"carjam": "carJam",
			"carcrash": "carCrash",
			"lavori in corso" : "inProgress",
			"autovelox": "speedCamera",
			"ingorgo": "carJam",
			"incidente": "carCrash",
	};



	function setSelectionRange(input, selectionStart, selectionEnd) {
		if (input.setSelectionRange) {
			input.focus();
			input.setSelectionRange(selectionStart, selectionEnd);
		} else if (input.createTextRange) {
			var range = input.createTextRange();
			range.collapse(true);
			range.moveEnd('character', selectionEnd);
			range.moveStart('character', selectionStart);
			range.select();
		}
	}

	function setCaretToPos(input, pos) {
		setSelectionRange(input, pos, pos);
	}


	$scope.addSignalsFromChat = function(keyCode) {
		$scope.showSpinner=true;

		//se ho schiacciato invio vado avanti
		$scope.chatSignalTypeTemp="";
		var textIfSent="";
		if ($scope.inputMess.length==0 || $scope.inputMess=="Send message...") {
			$scope.signalshints={};
			$scope.addresshints={};
			$scope.tagPressed=false;
			$scope.signalshints["show"]=false;
			$scope.addresshints["show"]=false;
			return;	
		}
		if(keyCode == 222){ 
			$scope.tagPressed=true;
			$scope.inputMess+="#";
			var pos=$scope.inputMess.length-1;
			document.getElementById("btn-input").selectionStart = pos;
			document.getElementById("btn-input").selectionEnd = pos;
		}

		$scope.chatSignalType=null;



		var n = $scope.inputMess.split("#");
		var index=$scope.inputMess.search("#");
		var i=1;
		while(i<n.length){
			n[i]=n[i].toLowerCase();
			//setto $scope.signalType
			if($scope.signalsType[n[i]]!=null){
				//ho trovato il tipo
				$scope.chatSignalType=$scope.signalsType[n[i]];
			}else{
				//ho trovato address
				$scope.address=n[i];
				$scope.chatSignalTypeTemp=n[i];

			}
			textIfSent+=n[i-1]+"<b>"+n[i]+"</b>";
			i+=2;
			if(i>n.length) textIfSent+=n[i-2];
			if(i==n.length) textIfSent+=n[i-1];
		}

		DataProvider.getPositionFromString($scope.address, onPositionHintsAddress, onErrorPositionAddress);
		//invio
		if(keyCode != 13){
			$scope.signalshints={};
			if($scope.chatSignalType==null&&$scope.chatSignalTypeTemp!=null ){
				$scope.signalshints["show"]=false;
				if($scope.tagPressed){
					angular.forEach($scope.signalsType, function(value, key) {
						var k=key.toLowerCase();
						var indexOfType=k.indexOf($scope.chatSignalTypeTemp);
						if(indexOfType!=-1){
							$scope.signalshints[key]=k;
							$scope.signalshints["show"]=true;
						}		
					});
				}
			}else{
				$scope.signalshints["show"]=false;
				$scope.addresshints["show"]=false;	
				$scope.tagPressed=false;
			}
			return;
		} 
		$scope.markerAddress=$scope.address;
		$scope.signal.type=$scope.chatSignalType;	


		if($scope.address!=null && $scope.chatSignalType!=null){
			//invia api a google e aggiungi eventualmente il marker;
			$scope.inputMess=textIfSent;
			$scope.message= $scope.inputMess;
			DataProvider.getPositionFromString($scope.address, $scope.onPositionReadyForChat, onError)
			$scope.signalshints={};
			$scope.addresshints={};	
			$scope.signalshints["show"]=false;
			$scope.addresshints["show"]=false;	
			

		}else{
			sendMessage($scope.inputMess);
		}


		$scope.address = "";
		$scope.chatSignalType="";
		$scope.inputMess="";

	}

	//add signal to map

	$scope.addSignals = function() {
		if($scope.address.legnth==0) return;
		$scope.showSpinner=true;

		if(!stompClients[0])
		{
			connectToAllTopics();
		}else{
			if(stompClients[0].connected==false)
				connectToAllTopics();
		}
		if($scope.address==$scope.yourPositionString){
			//modifica address con lat  e lng della tua posizione
			$scope.address=$scope.myLat+","+$scope.myLng;
		}
		//DataProvider.getPositionFromString($scope.address, $scope.onPositionReady, onError);
		$scope.inputMess="E' stato inserito un #"+$scope.signal.type+"# nella posizione "+"#"+$scope.address+"# da: "+$("#user")[0].innerText;
		$scope.addSignalsFromChat(13);
		//$scope.markerAddress=$scope.address;
		$scope.address = "";
	}

	//per Chat
	$scope.onHintClick = function(hint,hinttype){
		var n = $scope.inputMess.split("#");
		var index=$scope.inputMess.search("#");
		var i=1;
		var finalText="";
		while(i<n.length){
			n[i]=n[i].toLowerCase();
			if(hinttype=="address"){
				if(n[i]==$scope.address){
					n[i]=hint;
				}
			}
			else{
				if(n[i]==$scope.chatSignalTypeTemp){
					n[i]=hint;
				}
			}			

			finalText+=n[i-1]+"#"+n[i]+"#";
			i+=2;
		}
		$scope.addresshints={};	
		$scope.signalshints={};
		$scope.addresshints["show"]=false;	
		$scope.signalshints["show"]=false;
		$scope.inputMess=finalText;
	}


	$scope.filterSignals=function(signaltype){
		switch(signaltype){
		case  "inProgress"  : 
			if($scope.signalToShow.inProgress==false)  $scope.removeMarkerOnMap( $scope.inProgressList );
			else $scope.addMarkerOnMap( $scope.inProgressList );break;
		case  "speedCamera" :  
			if($scope.signalToShow.speedCamera==false) $scope.removeMarkerOnMap( $scope.speedCameraList );
			else $scope.addMarkerOnMap( $scope.speedCameraList );break;
		case  "carJam"	    :
			if($scope.signalToShow.carJam==false) $scope.removeMarkerOnMap( $scope.carJamList );
			else $scope.addMarkerOnMap( $scope.carJamList );break;
		case  "carCrash"    :	
			if($scope.signalToShow.carCrash==false) $scope.removeMarkerOnMap( $scope.carCrashList );
			else $scope.addMarkerOnMap( $scope.carCrashList);break
		}

	}


	//only ON MAP not from db
	$scope.removeMarkerOnMap=function (markers){
		angular.forEach(markers, function(value, key) {
			delete $scope.signalMarkers[key];  
		});
	}


	//only ON MAP not from db
	$scope.addMarkerOnMap=function (marker){
		angular.forEach(marker, function(value, key) {
			$scope.signalMarkers[key]=value;
		});
	}


	$scope.onPositionReadyForChat=function(positions) {
		$scope.centerLocation.lat=positions[0].geometry.location.lat();
		$scope.centerLocation.lng=positions[0].geometry.location.lng();

		//create signal on server
		var segnalation = {};
		segnalation.lat = $scope.centerLocation.lat;
		segnalation.lng = $scope.centerLocation.lng;
		segnalation.type = $scope.signal.type;
		segnalation.address = positions[0].formatted_address;
		$scope.addresshints={};	
		$scope.signalshints={};
		$scope.addresshints["show"]=false;	
		$scope.signalshints["show"]=false;
		return DataProvider.addSignalToServer(segnalation,$rootScope.nickname).then( function(signal){		
			var item={
					segnalazione:signal.data,
			}
			$scope.onSignalsFromServer(item);	
			var s="../../images/map.jpg";
			$scope.message='<a class="amap" href="../#/signals?lat='+signal.data.lat+'&lng='+signal.data.lng+'"><img src="'+s+'" class="mapimg"  />'+$scope.message+"</a>";
			sendMessage($scope.message);
			$scope.showSpinner=false;
			$scope.addresshints={};	
			$scope.signalshints={};
			$scope.addresshints["show"]=false;	
			$scope.signalshints["show"]=false;
			segnalation.address = "";
		});
	};
	
	
	//send new rate to server
	$scope.updateSignalRate=function(id){
		//update Rate
		//mode : 0  new vote
		//mode : 1  update of old vote
		let action=$scope.signalMarkers[id].action;
			
		let item={
				"action":'rate',
				"rate": $scope.signalMarkers[marker.id].usersvote
		};
		DataProvider.updateSignalRate(item,id,$rootScope.nickname).then(function(item){
			//update signal's rate average
			let rate=item.data;
			$scope.signalMarkers[id].rate=rate;
		});
	}
	
	
	//delete signal
	$scope.deleteSignal=function(id){
		let action=$scope.signalMarkers[id].action;	
		let item={
				"action":'cancel'
		};
		DataProvider.deleteSignal(item,id);
		delete $scope.signalMarkers[id]
	};
	
	

	$scope.onPositionReady=function(positions) {
		$scope.centerLocation.lat=positions[0].geometry.location.lat();
		$scope.centerLocation.lng=positions[0].geometry.location.lng();

		//create signal on server
		var segnalation = {};
		segnalation.lat = $scope.centerLocation.lat;
		segnalation.lng = $scope.centerLocation.lng;
		segnalation.type = $scope.signal.type;
		segnalation.address = positions[0].formatted_address;
		DataProvider.addSignalToServer(segnalation).then( function(signal){
			$scope.onSignalsFromServer(signal.data);
			$scope.showSpinner=false;

		});

	};

	$scope.onSignalsFromServer=function(item) {
		item.segnalazione.action=item.voto;
		marker=item.segnalazione;
		marker.icon = $scope.icons[marker.tipo];
		marker.dataInizio = $filter('date')(marker.dataInizio, "MM/dd/yyyy  h:mma");
		var s='<h1 class="typesignal">'+marker.tipo+'</h1>'+
		'<div><h2 class="time">{{signalMarkers['+ marker.id +'].address}}</h2></div>'+
		'<div><h2 class="time"> from '+marker.dataInizio+'</h2></div>'+
		'<div ng-cloak="" class="sliderdemoBasicUsage">'+
		'<md-content style="margin: 16px; padding:16px">'+
		'<h5 ng-show="signalMarkers['+ marker.id+'].nickname!=nickname && authenticated" style="text-align: center;">Your rate</h5><br>'+
		'<div ng-show="signalMarkers['+ marker.id+'].nickname!=nickname && authenticated" layout="">'+
			'<md-slider flex="" class="md-warn" md-discrete="" ng-mouseup="updateSignalRate('+ marker.id+')" ng-disabled="'+!$rootScope.authenticated+'" ng-model="signalMarkers['+ marker.id+'].usersvote" step="1" min="1" max="5" aria-label="rating"></md-slider><br>'+
			'<h3 style="padding-left: 25px; margin-top: 10px;">{{signalMarkers['+ marker.id+'].usersvote}}</h3><br>'+
		'</div>'+
		'<h5 style="text-align: center;">Average rate</h5><br>'+
		'<div layout="">'+
			'<md-slider flex="" class="md-primary" md-discrete="" ng-disabled="true"  ng-model="signalMarkers['+ marker.id+'].rate" step="1" min="1" max="5" aria-label="rating"></md-slider><br>'+
			'<h3 style="padding-left: 25px; margin-top: 10px;">{{signalMarkers['+ marker.id+'].rate}}</h3><br>'+
		'</div>'+
		'</md-content>'+
		'<div style="text"><h2 class="time">{{signalMarkers['+ marker.id+'].nickname}}</h2> <i ng-show="signalMarkers['+ marker.id+'].nickname==nickname" ng-click="deleteSignal('+marker.id+')"  style="float: right; margin-top: -30px;" class="fa fa-2x fa-trash-o" aria-hidden="true"></i></div>'+
		'</div>';

		$scope.ppContainer='<div id="Marker'+ marker.id+'">'+s+'</div>';
		switch($scope.signal.type){
		case "inProgress": 
			$scope.inProgressList[ marker.id]=marker;
			if($scope.signalToShow.inProgress==true){
				$scope.signalMarkers[ marker.id]=marker;
			}
			break;
		case "speedCamera": 
			$scope.speedCameraList[ marker.id]=marker;
			if($scope.signalToShow.speedCamera==true){
				$scope.signalMarkers[ marker.id]=marker;

			}
			break;
		case "carJam": 
			$scope.carJamList[ marker.id]=marker;
			if($scope.signalToShow.carJam==true){
				$scope.signalMarkers[ marker.id]=marker;

			}
			break;
		case "carCrash":
			$scope.carCrashList[ marker.id]=marker;
			if($scope.signalToShow.carCrash==true){
				$scope.signalMarkers[ marker.id]=marker;	
			}
			break;
		}	
		$scope.signalMarkers[ marker.id].message=$scope.ppContainer;
		$scope.signalMarkers[marker.id].usersvote=item.voto;

		//updateMap
		leafletData.getMap("myMap").then(function(map) {});
	};


	//request to server for filling marker arrays
	DataProvider.getSignals().then(function(signals){
		angular.forEach(signals, function(value, key) {
			$scope.onSignalsFromServer(value);
		});

		if($routeParams.lat!=null && $routeParams.lng!=null ){
			$scope.centerLocation.lat= Number($routeParams.lat);
			$scope.centerLocation.lng= Number($routeParams.lng);
			leafletData.getMap("myMap").then(function(map) {});
		}

	});


	function onError(status) {
	}


	/**
	 * Search for stops... use $timeout to simulate
	 * remote dataservice call.
	 */
	function querySearch (query) {
		var results = $scope.hints,
		deferred;
		if ($scope.simulateQuery) {
			deferred = $q.defer();
			deferred.resolve( results );
			return deferred.promise;
		} else {
			return results;
		}
	}


	function searchTextChange(text) {
		$scope.showSpinner=true;
		if(text.length>0 || text)
			DataProvider.getPositionFromString($scope.address, onPositionAddress, onErrorPositionAddress);
	}

	function selectedItemChange(item) {
		if(item=="Your position"){
			var localPosition=DataProvider.getCurrentPosition(onCurrentPosition);
		}
	}


	/**
	 * Create filter function for a query string
	 */
	function createFilterFor(query) {
		var lowercaseQuery = angular.lowercase(query);

		return function filterFn(state) {
			return (state.indexOf(lowercaseQuery) === 0);
		};

	}

	function createFilterExistance(query) {
		var lowercaseQuery = angular.lowercase(query);

		return function filterFn(state) {
			return (state === lowercaseQuery);
		};

	}
	//END code for md-autocomplete

	$scope.address = "";



	//--------geolocation google
	$scope.map;
	$scope.infoWindow;

	function onCurrentPosition(position) {
		var pos = {
				lat: position.coords.latitude,
				lng: position.coords.longitude
		};  
		$scope.centerLocation.lat=pos.lat;
		$scope.centerLocation.lng=pos.lng;
		$scope.myLat=pos.lat;
		$scope.myLng=pos.lng;
		$scope.centerLocation.zoom=16;
		$scope.centerLocation.zoom=16;
		//updateMap
		leafletData.getMap("myMap").then(function(map) {});
	}

//	var localPosition=DataProvider.getCurrentPosition(onCurrentPosition);

	function onPositionAddress(positions) {
		var lat=positions[0].geometry.location.lat();
		var lng=positions[0].geometry.location.lng();
		$scope.centerLocation.lat=lat;
		$scope.centerLocation.lng=lng;
		$scope.hints.length=0;
		$scope.hints.push($scope.yourPositionString);
		angular.forEach(positions, function(value, key) {
			$scope.hints.push(value.formatted_address);
		});
		$scope.showSpinner=false;
	}

	//for chat hints
	function onPositionHintsAddress(positions) {
		
		$scope.addresshints={};
		$scope.addresshints["show"]=false;
		angular.forEach(positions, function(value, key) {
			$scope.addresshints[value.formatted_address]=value.formatted_address;
			$scope.addresshints["show"]=true;
		});
	}

	function onErrorPositionAddress(status) {

	}

}
]);

