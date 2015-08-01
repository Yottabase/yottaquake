jQuery(document).ready(function ($) {
	
	//init map
	var map = L.map('chart-events-map')
	.on('moveend', function() {
		$(document).trigger('yottaquake.bounding_box_update', 
			{
				'topLeft' :  this.getBounds().getNorthWest(),
				'bottomRight' : this.getBounds().getSouthEast(),
				'zoom'	: this.getZoom()
			}
		);
	})
	.setView([mapDefault.lat, mapDefault.lng], mapDefault.zoom);
	
	//tile
	var defaultTile = "Geografica";
	var url = mapTileTypes[defaultTile].url;
	var options = mapTileTypes[defaultTile].options;
	options.attribution = 'Dev by <strong>Yottabase</strong>';
	
	var tile = L.tileLayer(url, options).addTo(map);
	
	$(document).on('yottaquake.filters_update', function(e, filters){
		
		map.removeLayer(tile);
		
		var url = mapTileTypes[filters.mapTileType].url;
		var options = mapTileTypes[filters.mapTileType].options;
		options.attribution = 'Dev by <strong>Yottabase</strong>';
		
		tile = L.tileLayer(url, options).addTo(map);
		
	});
	
	setTimeout(function(){
		map.fire('moveend');
	}, 0);

	//popup
	var popup = null;
	$(document).on('yottaquake.filters_update', function(e, filters){
		if(popup !== null){
			map.removeLayer(popup);
		}
	});
	
	
	//********************* cluster *********************// 
	var markers = L.markerClusterGroup();
	
	var markersReq = null;
	$(document).on('yottaquake.filters_update', function(e, filters){
		if(markersReq != null) markersReq.abort();
		if(filters.showClusteredEvents){
			markersReq = $.getJSON(wsUrl + "api-events.do", filters, function(data){
				var coords = [];
				
				if(data.items.length > visualizationLimit.cluster){
					$(document).trigger('yottaquake.growl', "Impossibile visualizzare cluster di " + data.items.length + " punti");
					return;
				}
				
				for (var i = 0; i < data.items.length; i++) {
					var event = data.items[i];
					var marker = L.marker(new L.LatLng(event.properties.lat, event.properties.lon));
					coords.push(marker);
				}
		
				markers.clearLayers();
				markers.addLayers(coords);
				map.addLayer(markers);
			});
		}else{
			markers.clearLayers();
		}
	});
	
	
	//********************* heatmap *********************//
	var heat = L.heatLayer([], {
		//radius : 50, 
		//blur: 30, 
		gradient : {'0.4': 'blue', '0.65': 'lime', '1': 'red'}}
	).addTo(map);
	
	var heatReq = null;
	
	$(document).on('yottaquake.filters_update', function(e, filters){
		if(heatReq != null) heatReq.abort();
		if(filters.showHeatMap){
			heatReq = $.getJSON(wsUrl + "api-events.do", filters, function(data){
				
				if(data.items.length > visualizationLimit.heatMap){
					$(document).trigger('yottaquake.growl', "Impossibile visualizzare heatmap di " + data.items.length + " punti");
					return;
				}
				
				var coords = [];
				for (var i = 0; i < data.items.length; i++) {
					var event = data.items[i];
					switch(filters.heatType) {
					    case 'depth':
					    	coords.push(new L.LatLng(event.properties.lat, event.properties.lon, event.properties.depth));
					        break;
					    case 'magnitude':
					    	coords.push(new L.LatLng(event.properties.lat, event.properties.lon, event.properties.magnitude));
					        break;
					    default:
					    	coords.push(new L.LatLng(event.properties.lat, event.properties.lon));
					}
				}
				heat.setLatLngs(coords);
				
			});
		}else{
			heat.setLatLngs([]);
		}
	});
	
	
	//********************* markers *********************//
	var pointersReq = null;
	var pointers = [];
	var pointersIcon = [];
	
	magnitudeTypes.getList().forEach(function(magType){
		
		var icons = []
		
		for(i=Math.round(magType.min); i < Math.round(magType.max); i++){
			icons.push( L.MakiMarkers.icon({icon: "triangle", color: magType.mapColor(i), size: "s"}) );	
		}
		
		pointersIcon[magType._id] = icons;
		
	});
	
	$(document).on('yottaquake.filters_update', function(e, filters){
		if(pointersReq != null) pointersReq.abort();
		if(filters.showEvents){
			pointersReq = $.getJSON(wsUrl + "api-events.do", filters, function(data){
				
				pointers.forEach(function(p) {
				    map.removeLayer(p);
				    delete p;
				});
				
				if(data.items.length > visualizationLimit.markers){
					$(document).trigger('yottaquake.growl', "Impossibile visualizzare più di " + data.items.length + " punti");
					return;
				}
				
				var coords = [];
				for (var i = 0; i < data.items.length; i++) {
					var event = data.items[i];
					var icon = pointersIcon[event.properties.magtype.toUpperCase()][Math.round(event.properties.mag)];
					
					var d = new Date(Date.parse(event.properties.time));
					var label = 
						"Time: <strong>" + d.getDate() + "/" + (d.getMonth() +1) + "/" + d.getFullYear() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + "</strong><br/>" +
						"Latitude: <strong>" + event.properties.lat + "</strong><br/>" +
						"Longitude: <strong>" + event.properties.lon + "</strong><br/>" +
						"Depth: <strong>" + event.properties.depth + " km </strong><br/>" +
						"Magnitude: <strong>" + event.properties.mag + " ("+ event.properties.magtype.toUpperCase() +")</strong><br/>";
					
					var mapPointer = L.marker([event.properties.lat, event.properties.lon], {icon: icon})
						.bindPopup(label)
						.addTo(map);
					pointers.push(mapPointer);
				}
			});
		}else{
			pointers.forEach(function(p) {
			    map.removeLayer(p);
			    delete p;
			});
		}
	});
	
	
	//********************* geoForms *********************//
	var geoForms = {
		showCountries : {
			httpRequest : null,
			drawedLayer : null,
			api : "api-countries.do",
			httpRequestCallback : function(filters){
				if(filters.zoom <= 3){
					filters['levelQuality'] = 'LOW';	
				}else if(filters.zoom <= 5){
					filters['levelQuality'] = 'MEDIUM';
				}else{
					filters['levelQuality'] = 'HIGH';
				}
				return filters;
			},
		},
		showTectonicPlates : {
			httpRequest : null,
			drawedLayer : null,
			api : "api-tectonic-plates.do",
			httpRequestCallback : function(filters){
				
				if(filters.zoom <= 3){
					filters.topLeftLat = null;
					filters.topLeftLng = null;
					filters.bottomRightLat = null;
					filters.bottomRightLng = null;
				}
					
				return filters;
			},
		},
		showContinents : {
			httpRequest : null,
			drawedLayer : null,
			api : "api-continents.do",
			httpRequestCallback : null
		},
		showFlinnRegions : {
			httpRequest : null,
			drawedLayer : null,
			api : "api-flinn-regions.do",
			httpRequestCallback : function(filters){
				
				if(filters.zoom <= 3){
					filters.topLeftLat = null;
					filters.topLeftLng = null;
					filters.bottomRightLat = null;
					filters.bottomRightLng = null;
				}
					
				return filters;
			},
		},
	};
	
	$(document).on('yottaquake.filters_update', function(e, filters){
		
		for(var form in geoForms){
			
			var geoForm = geoForms[form];
			
			if(geoForm.httpRequest !== null) geoForm.httpRequest.abort();
		
			if(filters[form]){
				
				if(geoForm.httpRequestCallback != null){
					var customFilters = geoForm.httpRequestCallback(filters);
				}else{
					var customFilters = filters;
				}
				
				geoForm.httpRequest = $.getJSON(wsUrl + geoForm.api, customFilters, function(data){
					var mapColor = d3.scale.linear()
		    			.domain([data.minCount, data.maxCount])
		    			.range(['#fff7bc', '#d95f0e']);
					
					var geoData = 
						{
							"type": "FeatureCollection",
							"features": data.items 
						};
					
					if(geoForm.drawedLayer !== null) map.removeLayer(geoForm.drawedLayer);
					geoForm.drawedLayer = L.geoJson(geoData, {
						style: function (feature) {
							feature.color = mapColor(feature.count);
							return {
						        fillColor: feature.color,
						        weight: 1,
						        opacity: 1,
						        color: 'white',
						        dashArray: '3',
						        fillOpacity: 0.7
						    };
						},
						onEachFeature: function (feature, layer) {
						    layer.on({
						        click: function(e){
						        	if(popup != null) map.removeLayer(popup);
						        	popup = L
						        		.marker(e.latlng)
						        		.bindPopup(feature.name + ":" + " <strong>" + feature.count + "</strong>" + " eventi sismici")
						        		.addTo(map)
						        		.openPopup();
						        }
						    });
						}
					}).addTo(map);
				});
			}else{
				if(geoForm.drawedLayer !== null) map.removeLayer(geoForm.drawedLayer);
			}
		}
	});
	
});
		
	