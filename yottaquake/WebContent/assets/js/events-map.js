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
	
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ', {
		minZoom: 2,
		maxZoom: 18,
		attribution: 
			/*'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>, ' +*/
			'Dev by <strong>Yottabase</strong>',
		id: 'mapbox.outdoors'
	}).addTo(map);
	
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
					coords.push(new L.LatLng(event.properties.lat, event.properties.lon));
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
	
	var mapColor = d3.scale.linear()
		.domain([1, 10])
		.range(['yellow', 'red']);
	
	for(i=1; i < 10; i++){
		pointersIcon[i] = L.MakiMarkers.icon({icon: "triangle", color: mapColor(i), size: "s"});	
	}
	
	
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
					var icon = pointersIcon[Math.round(event.properties.mag)];
					var mapPointer = L.marker([event.properties.lat, event.properties.lon], {icon: icon})
						.bindPopup("Magnitudo: <strong>" + event.properties.mag + "</strong><br/>Profondita: <strong>" + event.properties.depth + "km </strong>")
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
			httpRequestCallback : null,
		}
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
		    			.domain([data.minCount, (data.maxCount-data.minCount)/2,  data.maxCount])
		    			.range(['#fee0d2', '#fc9272', '#de2d26']);
					
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
						        		.bindPopup(feature.properties.name + ":" + "<strong>" + feature.count + "</strong>" + "earthquakes")
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
	
	
	
	
	
	
	
    
	
	//var api = wsUrl + "api-countries.do?levelQuality=low";
	var api = wsUrl + "api-flinn-regions.do";
	/*
	$.getJSON(api, function(data){
		
		var mapColor = d3.scale.linear()
    		.domain([0, 1])
    		.range(['#fcbba1', '#99000d']);
		
		var geoData = 
			{
				"type": "FeatureCollection",
				"features": data 
			};
		
		var geoLayer = L.geoJson(geoData, {
			style: function (feature) {
				feature.color = mapColor(Math.random());
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
			        	//map.fitBounds(e.target.getBounds());
			        	//console.log(feature.properties.name);
			        	
			        	geoLayer.eachLayer(function (_l) {
			        		
			        		if( _l._leaflet_id == layer._leaflet_id){
			        			
			        			//selezionato 
			        			_l.setStyle({
			        				weight: 3,
			        				fillColor: feature.color,
			        			});
			        		}else{
			        			//non selezionato 
			        			_l.setStyle({
			        				weight: 1,
			        				fillColor: "transparent",
			        			});
			        		}
			        	    
			        	});
			        	
			        }
			    });
			}
		}).addTo(map);
		
	});
		*/
	
	
	
});
		
	