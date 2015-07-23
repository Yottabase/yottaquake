jQuery(document).ready(function ($) {
   
	var map = L.map('chart-events-map').setView([51.505, -0.09], 4);

	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>, ' +
			'Dev by <strong>Yottabase</strong>',
		id: 'mapbox.streets'
	}).addTo(map);

	
	$.getJSON(wsUrl + "api-countries.do?levelQuality=low", function(data){
		
		var geoData = 
			{
				"type": "FeatureCollection",
				"features": data 
			};
		
		var geoLayer = L.geoJson(geoData, {
			style: function (feature) {
				return {
			        fillColor: '#' + Math.random().toString(16).substring(2, 8),
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
			        	console.log(feature.properties.name);
			        	
			        	geoLayer.eachLayer(function (_l) {
			        		
			        		if( _l._leaflet_id == layer._leaflet_id){
			        			
			        			//selezionato 
			        			_l.setStyle({
			        				weight: 3,
			        				fillColor: '#' + Math.random().toString(16).substring(2, 8),
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
	
	
});
		
	