jQuery(document).ready(function ($) {
	
	//init map
	var map = L.map('chart-events-map')
	.on('moveend load', function() {
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
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>, ' +
			'Dev by <strong>Yottabase</strong>',
		id: 'mapbox.outdoors'
	}).addTo(map);
	
	
	
	//markers
	var markers = L.markerClusterGroup();
	
	var markersReq = null;
	$(document).on('yottaquake.filters_update', function(e, filters){
		if(markersReq != null) markersReq.abort();
		if(filters.showEvents){
			markersReq = $.getJSON(wsUrl + "api-events.do", filters, function(data){
				var coords = [];
				
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
	
	//heatmap
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
				
				var coords = [];
				for (var i = 0; i < data.items.length; i++) {
					var event = data.items[i];
					coords.push(new L.LatLng(event.properties.lat, event.properties.lon));
				}
				heat.setLatLngs(coords);
				
			});
			console.log(heatReq);
		}else{
			heat.setLatLngs([]);
		}
	});
	
	
	map.on('click', function(e){
		console.log(e.latlng);
	});
	
	
	
	var bounds = [[53.912257, 27.581640], [53.902257, 27.561640]];
	
	
	var markerIconM = L.MakiMarkers.icon({icon: "triangle", color: "#b0b", size: "m"});
	var markerIconL = L.MakiMarkers.icon({icon: "triangle", color: "#f00", size: "l"});
	var marker1 = L.marker([51.5, -0.09], {icon: markerIconM}).addTo(map);
	var marker2 = L.marker([40, -0.09], {icon: markerIconL}).addTo(map);
	
	
	
	
	
    
	
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
		
	