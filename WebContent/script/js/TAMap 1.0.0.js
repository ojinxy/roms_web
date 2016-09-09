
function TAMap(){
		var poly;
		var geodesic;
		var map;
		var clickcount 			= 0;
		var country 			= "Jamaica";
		var dragcolor 			= "e9ef2e";
		var orgincolor 			="";
		var startMarkercolor 	= "6fa428";
		var endMarkercolor 		= "eb4037";
		var polystrokecolor 	= colorGenerator(); //"#f6df51";
		var defaultZoom 		= 14;
		var markersArray 		= [];
		var latLngArray 		= [] ;	
		var geocoder;
		var directionData 		= null;
		var directionURL 		= "https://maps.googleapis.com/maps/api/directions/json?";
		var resultArray			= [];
		var infowindow 		;//	= new google.maps.InfoWindow({ content:""  });
		var directionalPath		= {
			name			:"",
			address			:"",
			color			:"",
			start_location	: {lat : 0,lng : 0},
			end_location	: {lat : 0,lng : 0},
			points			:"",
			distance		:0
		};
		var drawPaths			= new Array();
			
		var originalAddress="";
		

		
		function initialize() {
		
			try{
				geocoder = new google.maps.Geocoder();
				  var jamaica = new google.maps.LatLng(18.01076160531344,-76.79747815723874);
				  var mapOptions = {
					zoom: 7,
					center: jamaica,
					mapTypeId: google.maps.MapTypeId.ROADMAP,
					streetViewControl: false
				  };

				  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
					
				  infowindow 			= new google.maps.InfoWindow({ content:""  });
			}catch(e){
				console.log(e);
			}
		  

		}
		
		initialize();
		
		function displayPath(pathOptions, map)
		{
			try{
				var index = pathExist(pathOptions.name);
				var color;
				
				if( index == -1)
				{
					 color = "#FD3814";
				}else
				{
					color = drawPaths[index].color;
				
				}
				
					
					var polyOptions = {
						strokeColor: color, 
						strokeOpacity: 0.5, 
						strokeWeight:5
					};
					pathOptions.color = color;
					var newPath = {
						name:pathOptions.name,
						codedPath:pathOptions.points,
						color: color,
						polyOptions: polyOptions,
						poly: new google.maps.Polyline(polyOptions),
						pathOptions:pathOptions,
						listernerID:""
					};
					newPath.poly.setMap(map);
					drawPaths.push(newPath);
					map.setCenter(pathOptions.start_location);
					map.setZoom(defaultZoom);
					
					newPath.listernerID = google.maps.event.addListener(newPath.poly, 'click', function(e) {
						try{
							var location =e.latLng;
							var content ="<strong>Location:</strong>   "+  pathOptions.name+"<br/><strong>Distance:</strong> "+ (pathOptions.distance/1000).toFixed(2)+"km  ";
							var map = this.getMap();
							
							infowindow.setContent(content );
							
							 infowindow.setPosition(location);
							 infowindow.open(map);
						 }
						 catch(e)
						 {
							alert(e);
						 }
				   })
				   
					index = drawPaths.length - 1;
					
				var points = decode(drawPaths[index].codedPath);
				addPath(drawPaths[index].poly,points )

			
			}catch(e)
			{
				alert(e);
			
			}
		
		}
		  
		function drawingPath(pathOptions, map)
		{
			try
			{
				var index = pathExist(pathOptions.name);
				if( index == -1)
				{
					var  color = "#0A8207";
				
					var polyOptions = {
						strokeColor: color, 
						strokeOpacity: 0.5, 
						strokeWeight:5
					};
					pathOptions.color = color;
					var newPath = {
						name:pathOptions.name,
						codedPath:pathOptions.points,
						color: color,
						polyOptions: polyOptions,
						poly: new google.maps.Polyline(polyOptions),
						pathOptions:pathOptions,
						listernerID:""
					};
					newPath.poly.setMap(map);
					drawPaths.push(newPath);
					
					newPath.listernerID = google.maps.event.addListener(newPath.poly, 'click', function(e) {
						try{
							var location =e.latLng;
							var content ="<strong>Location:</strong>   "+  pathOptions.name+"<br/><strong>Distance:</strong> "+ (pathOptions.distance/1000).toFixed(2)+"km  ";
							var map = this.getMap();
							
							infowindow.setContent(content );
							
							 infowindow.setPosition(location);
							 infowindow.open(map);
						 }
						 catch(e)
						 {
							alert(e);
						 }
				   })
				   
					index = drawPaths.length - 1;
					
					
				}else //replace path
				{
					clearPaths(drawPaths[index].poly);
					drawPaths[index].codedPath = pathOptions.points;
					drawPaths[index].pathOptions = pathOptions;
					
					google.maps.event.removeListener(drawPaths[index].listernerID);
					
					drawPaths[index].listernerID= google.maps.event.addListener(drawPaths[index].poly, 'click', function(e) {
						try{
							var location =e.latLng;
							var content ="<strong>Artery:</strong>   "+ pathOptions.name+"<br/><strong>Distance:</strong> "+ (pathOptions.distance/1000).toFixed(2)+"km  ";
							var map = this.getMap();
							
							infowindow.setContent(content );
							
							 infowindow.setPosition(location);
							 infowindow.open(map);
						 }
						 catch(e)
						 {
							alert(e);
						 }
				   });
			
			}
				
			var points = decode(drawPaths[index].codedPath);
			addPath(drawPaths[index].poly,points )
			
			
			
			
			}catch(e)
			{
				alert("Can not add new path: "+e);
			
			}
		
		}
		function pathExist(name)
		{
			try
			{
				if( drawPaths == null || drawPaths.length == 0)
					return -1;
				
				for( i in drawPaths)
				{
					
					if( $.trim(drawPaths[i].name) == $.trim(name))
						return i;
				}
			
			}catch(e)
			{
				alert("Can not determine path: "+e);
			
			}
			return -1;
		
		}
		
		function pathAddressExist(address)
		{
			try
			{
				if( drawPaths == null || drawPaths.length == 0)
					return -1;
				
				for( i in drawPaths)
				{
					
					
					if( $.trim(drawPaths[i].pathOptions.address) == $.trim(address))
						return i;
				}
			
			}catch(e)
			{
				alert("Can not determine path: "+e);
			
			}
			return -1;
		
		}
		
		
		function colorGenerator()
		{		
			return "#"+Math.random().toString(16).slice(2, 8);
		}


		  
		function clearPaths(poly) {
		  try
		  {
			  if(poly == null)
				return;
			  var path = poly.getPath();
			  while (path.getLength()) {
				path.pop();
			  }
		  }catch(e)
		  {
			alert(" Can clear path"+e);
		  }
		}	

		function decode(encoded){

				// array that holds the points

				var points=[ ]
				var index = 0, len = encoded.length;
				var lat = 0, lng = 0;
				while (index < len) {
					var b, shift = 0, result = 0;
					do {

				b = encoded.charAt(index++).charCodeAt(0) - 63;//finds ascii                                                                                    //and substract it by 63
						  result |= (b & 0x1f) << shift;
						  shift += 5;
						 } while (b >= 0x20);


				   var dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				   lat += dlat;
				  shift = 0;
				  result = 0;
				 do {
					b = encoded.charAt(index++).charCodeAt(0) - 63;
					result |= (b & 0x1f) << shift;
				   shift += 5;
					 } while (b >= 0x20);
				 var dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				 lng += dlng;
			 
			   points.push({latitude:( lat / 1E5),longitude:( lng / 1E5)})  
			 
			  }
			  return points
			}
		

		function removePath(poly)
		{
			try{
				poly.setMap(null);
				
			 }
			 catch(e)
			 {
				alert( "Can not display path: "+ e);
			 }
		
		}
		
		
		function addPath(poly,points )
		{
			try{
				var path = poly.getPath();
				 for(ipoint =0; ipoint < points.length; ipoint++)
				 {
					path.push(new google.maps.LatLng(points[ipoint].latitude, points[ipoint].longitude));
				 }
			 }
			 catch(e)
			 {
				alert( "Can not display path: "+ e);
			 }
		
		}
		
		function positionToJSON(position)
		{
			try
			{
				var strPositionJSON;
				strPositionJSON ={ lat: position.lat(),lng:position.lng()}
				return strPositionJSON;
		
			 }
			 catch(e)
			 {
				alert( "Can not convert to JSON format : "+ e);
			 }
			 return null;
		
		}
		
		
		function loadDirectionPath( )
		{
			try
			{
			
				
				var summary ="";
				var codedPath="";
				var strdistance="";
				var distance = 0.0;
				
				if(markersArray && markersArray.length == 2)
				{
					var origin = new google.maps.LatLng(markersArray[0].getPosition().lat(),markersArray[0].getPosition().lng());
					var destination = new google.maps.LatLng(markersArray[markersArray.length-1].getPosition().lat(),markersArray[markersArray.length-1].getPosition().lng());
					
				
					var directionRequest = {
						origin:origin,
						destination:destination,
						travelMode: google.maps.TravelMode.DRIVING
					  };
					
					var directionalService = new google.maps.DirectionsService();
					
					directionalService.route( directionRequest,function( result, status){ 
					
						if(result != null && status == google.maps.DirectionsStatus.OK)
						{
							var routes = result.routes;
						   
						   
						   
							for( var i in routes){
								// codedPath 		+= routes[i].overview_polyline.points;
								if(typeof routes[i].overview_polyline.points == "undefined")
								{
									codedPath 		+= routes[i].overview_polyline;
								}else
								{
									codedPath 		+= routes[i].overview_polyline.points;
								}
								 summary 		+= routes[i].summary;
								 for ( var x in routes[i].legs)
								 {
									strdistance += "<br\>Distance: "+routes[i].legs[x].distance.text;
									distance 	+= parseInt(routes[i].legs[x].distance.value);
								 }
								 
							 }
							 
							 
							 var pathOptions ={
								name:			originalAddress,
								address: 		summary,
								start_location:	positionToJSON(markersArray[0].getPosition()),
								end_location: 	positionToJSON(markersArray[markersArray.length-1].getPosition()),
								points: 		codedPath,
								distance: 		distance							
							};
							
							drawingPath(pathOptions, map);
							
							
							 $("#direction").append("<br\>No. of routes: "+routes.length);
							 $("#direction").append("<br\>location: "+summary);
							 $("#direction").append("<br/>distance: "+ (distance/ 1000).toFixed(2)+"km ");
							 $("#direction").append("<br/>coded paths: "+codedPath);
							 $("#direction").append("<br/>no. of characters: "+codedPath.length);
							 directionalPath.name 				= originalAddress;
							 directionalPath.address 			= summary;
							 directionalPath.start_location 	= positionToJSON(markersArray[0].getPosition()); 
							 directionalPath.end_location 		= positionToJSON(markersArray[markersArray.length-1].getPosition());							 
							 directionalPath.points 			= codedPath;
							 directionalPath.distance 			= distance;
							 
							$("#arteryMaintenanceDetails\\:dlgDistanceBx").val((distance/ 1000).toFixed(2));
							$("#arteryMaintenanceDetails\\:dlgDistanceBx2").val((distance/ 1000).toFixed(2));
							//$("#arteryMaintenanceDetails\\:dlgDistanceBx").prop('readonly','true');
							$("#arteryMaintenanceDetails\\:dlgStartLat").val(directionalPath.start_location.lat);
							$("#arteryMaintenanceDetails\\:dlgStartLong").val(directionalPath.start_location.lng);
							$("#arteryMaintenanceDetails\\:dlgEndLat").val(directionalPath.end_location.lat);
							$("#arteryMaintenanceDetails\\:dlgEndLong").val(directionalPath.end_location.lng);
							$("#arteryMaintenanceDetails\\:dlgPoints").val(directionalPath.points);
							 
							 
							 
							 resultArray[0] = distance;
							 
							  $("#direction").append("<br/>directionalPath: "+JSON.stringify(directionalPath)+"");
							  
							  $("#direction").append("<br/>directionalPath: <pre>"+JSON.stringify(directionalPath, null,4)+"</pre>");
							  
							  $("#direction").append("<br/>directionalPath: <pre>"+JSON.stringify(routes, null, 4)+"</pre>");						
							
							
						}
					
					});
				
				}
			}
			catch(e)
			{
				alert(e);
			}
		
		}
		


		
		function getParish()
		{
			return $("#Parish").find("option:selected").text();
		
		}
		
		function getAddress()
		{
			var address = getParish()+", "+ country;
			
			return address;
		
		}
		
		function removerMarkers()
		{
			if(markersArray)
			{
			
				for (i in markersArray) 
				{
					markersArray[i].setMap(null);
				}
				markersArray.length = 0;
				clearPaths();
			
			}
		}
		
		function addMarker(AddMarkerOptions)
		{
				var marker = new StyledMarker({
						styleIcon:new StyledIcon(StyledIconTypes.MARKER,{color:AddMarkerOptions.color,text:AddMarkerOptions.text}),
						position:AddMarkerOptions.location,
						draggable:true,
						title:"Drag to position",
						map:map
						});
				if(markersArray)
					markersArray.push(marker);
				// add click event function	
				google.maps.event.addListener(marker, 'click', function(e) {
						try{
							var location =e.latLng;
							var content = "<strong>Artery:</strong>   "+ originalAddress+"<br/>Position "+ location.lng()+","+location.lat()+"  ";
							var map = this.getMap();
							
							infowindow.setContent(content );
							
							 infowindow.setPosition(location);
							 infowindow.open(map);
						 }
						 catch(e)
						 {
							alert(e);
						 }
				   })
				   
				   google.maps.event.addListener(marker, 'mouseover', function(e) {
						try{
							/*var location =e.latLng;
							var content = "<strong>Artery:</strong>   "+ originalAddress+"<br/>Position "+ location.lng()+","+location.lat()+"  ";
							var map = this.getMap();
							
							infowindow.setContent(content );
							
							 infowindow.setPosition(location);
							 infowindow.open(map);*/
							
							//alert("hovet");
						 }
						 catch(e)
						 {
							alert(e);
						 }
				   });
				
				 /* var tooltip = new Tooltip({map: map}, marker);
			        tooltip.bindTo("text", marker, "tooltip");
			        google.maps.event.addListener(marker, 'mouseover', function() {
			            tooltip.addTip();
			            tooltip.getPos2(marker.getPosition());
			        });*/
				  
				// add drag event function
				 google.maps.event.addListener(marker,"drag" , function(e){
					   try{
					   
							var location =e.latLng;
							var content = "<br/>Position "+ location.lng()+","+location.lat()+"  ";
							$("#direction").html(content);
						}
						 catch(e)
						 {
							alert(e);
						 }
				   });
				   
				 // start drag event function - change marker color to drag color
				google.maps.event.addListener(marker,"dragstart" , function(e){
					   try{
							origincolor = this.styleIcon.get("color");
							this.styleIcon.set("color",dragcolor);
										
						}
						 catch(e)
						 {
							alert(e);
						 }
				   });				   
				   
				 // end drag event function - change marker color to original color
				   google.maps.event.addListener(marker,"dragend" , function(e){
					   try{
							this.styleIcon.set("color",origincolor);
							
							loadDirectionPath();
							
							
										
						}
						 catch(e)
						 {
							alert(e);
						 }
				   });		
		
		
		}
		

		
		this.codeAddress = function codeAddress() {
			  var address = getAddress();
			  
			   
			 try{
			   
			  geocoder.geocode( { 'address': address}, function(results, status) {
					
					if (status == google.maps.GeocoderStatus.OK) {
					
					
					
					var location = results[0].geometry.location;
					
					map.setCenter(location);
					map.setZoom(defaultZoom);
					removerMarkers();
					
					var color = startMarkercolor; //"6fa428";
					
					
					
					var addMarkerOptions = {
						location:location,
						color:color , //,
						text:"S",
						address:address
					};
					
					addMarker(addMarkerOptions);// add start marker
					
					addMarkerOptions = {
							location:location,
							color:endMarkercolor,
							text:"E",
							address:address
							
						};
						addMarker(addMarkerOptions);// add end marker					   

					} else {
					  alert('Geocode was not successful for the following reason: ' + status);
					}
				});
				
			}catch(e)
			{
				alert("Can not retrieve address location: "+e);
			}
		}
		 
		
		function checkLocationBoundary(location)
		{
			try
			{
				var upperLat = 18.5494;
				var upperLng = -78.4500;
				var lowerLat = 17.650;
				var lowerLng = -75.9451;
				
				
				return ( location.lat()> lowerLat && location.lat() < upperLat ) && 
				(location.lng() > upperLng && location.lng() < lowerLng   ) ;
				
			
			
			
			}catch(e)
			{
				alert("Can not check location: "+e);
			
			}
		
		
		
		}

		
		function stripAddress(address)
		{
			try
			{
				var comma =",";
				var index = address.indexOf(comma);
				index++;
				return $.trim( address.substring(index));
			
			}
			catch(e)
			{
				alert("Can format address: "+e);
			
			}
			return null;
		
		}
		

		function validatePathCoordinates(pathOptions)
		{
			try
			{
			
			 	var start_location = new google.maps.LatLng(pathOptions.start_location.lat, pathOptions.start_location.lng);
				var end_location = new google.maps.LatLng(pathOptions.end_location.lat, pathOptions.end_location.lng);
				return checkLocationBoundary( start_location) &&
					checkLocationBoundary(end_location) && $.trim( pathOptions.points).length > 0;
			
			
			}catch(e)
			{
				//alert("validatePathCoordinates: "+e);
			
			}
		
		return false;
		
		
		}
		
		this.getDistance = function getDistance()
		{
			return resultArray;
		}
		
		
		
		this.getPaths = function getPaths()
		{
		
			try{
			
				var pathOptions = new Array();
			
				for( index in drawPaths)
				{
					var pathOption =
					{
						"ID":drawPaths[index].pathOptions.ID,
						"name": drawPaths[index].name,
						"address": drawPaths[index].pathOptions.address,
						"color": drawPaths[index].color,
						"start_location":drawPaths[index].pathOptions.start_location,
						"end_location":drawPaths[index].pathOptions.end_location,
						"points": drawPaths[index].pathOptions.points,
						"distance": drawPaths[index].pathOptions.distance
					
					}
					pathOptions.push(pathOption);
					
				
				}
			
				return pathOptions;
			
			}catch(e)
			{
			
				alert(e);
			}
			
			return null;
			
		
		}
		
		
		this.modifyPaths = function modifyPaths(pathOptions)
		{	
			try{
			
				if(validatePathCoordinates(pathOptions))
				{
					displayPath(pathOptions, map);
					
					
						//add markers
					removerMarkers();
	
					var color = startMarkercolor; //"6fa428";
					
					addMarkerOptions = {
						location:pathOptions.end_location,
						color:endMarkercolor,
						text:"E",
						address:pathOptions.address
						
					};
					addMarker(addMarkerOptions);// add end marker
					
					var addMarkerOptions = {
						location:pathOptions.start_location,
						color:color , //,
						text:"S",
						address:pathOptions.address
					};
					
					addMarker(addMarkerOptions);// add start marker
				
					
				}else
				{
					this.showLocation(pathOptions.address);
				
				}
			
				originalAddress = pathOptions.address;
			
										

				
				 $("#direction").append("<br/>pathOptions: <pre>"+JSON.stringify(pathOptions)+"</pre>");

			
			}catch(e)
			{
				//alert(e);
			
			}
		
		
		}
		this.showPaths = function showPaths(arr_pathOptions)
		{
			try{
			
					for(index in arr_pathOptions)
					{
						if(validatePathCoordinates(arr_pathOptions[index]))
						{
							displayPath(arr_pathOptions[index], map);
							
						}
						else
						{
							showLocation(arr_pathOptions[index].address);
						}
					}
					
					
					
			
			}catch(e)
			{
			
				//alert(e);
			}
		
		
		}
		
		this.addPaths = function addPaths(arr_pathOptions)
		{
			try
			{
				for(index in arr_pathOptions)
				{
					if(validatePathCoordinates(arr_pathOptions[index]))
						displayPath(arr_pathOptions[index], map);
				
				}
			
			
			}catch(e)
			{
				alert(e);
			}
		
		}
		
		
		
		this.removePaths = function removePaths(arr_pathOptions)
		{
			try
			{
				for(index in arr_pathOptions)
				{
					var pathIndex = pathAddressExist(arr_pathOptions[index].address);
					
					if(pathIndex > -1)
					{
					//removePath(poly,points )
						removePath(drawPaths[pathIndex].poly);
						drawPaths.splice(pathIndex,1);
					
					}
				
				}
			
			
			}catch(e)
			{
				alert(e);
			}
		
		}
		
		
		this.showLocation= function showLocation(strLocation) {
				  //alert("showlocation called");
			try{
				var comma =",";
				var didItfail = true;
				if( strLocation.indexOf(country) == -1)
					strLocation += ",  "+country
					
				var stripped = stripAddress(strLocation);	
			   
				geocoder.geocode( { 'address': strLocation}, function(results, status) {
					
					if (status == google.maps.GeocoderStatus.OK) {

						var location = results[0].geometry.location;
						
						//alert("StrLocation" + stripped);
						if(checkLocationBoundary(location) && strLocation != "Jamaica") // The location is within Jamaica
						{
							map.setCenter(location);
							map.setZoom(defaultZoom);
							removerMarkers();
							
							addMarkerOptions = {
								location:location,
								color:endMarkercolor,
								text:"E",
								address:strLocation
								
							};
							addMarker(addMarkerOptions);// add end marker
							
							var addMarkerOptions = {
								location:location,
								color:startMarkercolor,
								text:"S",
								address:strLocation
							};
							
							addMarker(addMarkerOptions);// add start marker
							
							//alert("Within Jamaica "+JSON.stringify(addMarkerOptions));
						 }else
						{
							 //alert("Outside Jamaica " +  stripAddress(strLocation));
							//if( strLocation.indexOf(comma) > -1)
								//showLocation(stripAddress(strLocation));
							didItfail = true;
						}

					} else {
					  //alert("else");
					  if( strLocation.indexOf(comma) > -1)
					  {
						  //initialize();
						//showLocation(stripAddress(strLocation));
					  }
					  didItfail = true;
					}
				});
				return didItfail;
			}catch(e)
			{
				//alert(e);
			}
		}
		
	};