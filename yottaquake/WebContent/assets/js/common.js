jQuery(document).ready(function ($) {

	$body = $("body");

	$(document).on({
	    ajaxStart: function() { $body.addClass("loading");    },
	     ajaxStop: function() { $body.removeClass("loading"); }    
	});
	
	$(document).on('yottaquake.growl', function(e, message){
		
		var growl = $('#growl')
			.text(message)
			.fadeIn({
				duration: 600,
				complete : function(){
					setTimeout(function(){
						growl.fadeOut();
					}, 5000);
				}
			});
	});
	
	
	magnitudeTypes = (function(){
		 
	    var cache;
	    
	    return {
	        init: function() {
	            if (!cache) {
	            	
	            	cache = $.parseJSON(
            		    $.ajax(
            		        {
            		           url: wsUrl + "api-magnitude-types.do", 
            		           async: false, 
            		           dataType: 'json'
            		        }
            		    ).responseText
            		);
	            	
	            	var types = [];
	            	cache.forEach(function(type){
	            		types.push(type._id);
	            	});
	            	
	            	var color = d3.scale.category10().domain(types);
	            	
	            	for(i=0; i<cache.length; i++){
	            		var type = cache[i];
	            		var darkColor = cache[i].darkColor = d3.rgb(color(type._id)).darker(1.5).toString();
	            		var lightColor = cache[i].lightColor = d3.rgb(color(type._id)).brighter(1.5).toString();
	            		cache[i].baseColor = color(type._id);
	            		
	            		cache[i].mapColor = d3.scale.linear()
	        				.domain([type.min, type.max])
	        				.range([lightColor, darkColor]);
	            	}

	            } 
	            return cache; 
	        },
	        getList : function() {
	        	this.init();
	        	return cache;
	        },
	        getColorMapper : function(magnitudeType) {
	        	this.init();
	        	var selected = null;
	        	cache.forEach(function(type){
            		if( type._id == magnitudeType){
            			selected = type;
            		}
            	});
	        	return selected.mapColor;
	        },
	        getColor : function(magnitudeType) {
	        	this.init();
	        	var selected = null;
	        	cache.forEach(function(type){
            		if( type._id == magnitudeType){
            			selected = type;
            		}
            	});
	        	return selected.baseColor;
	        },
	        getLabel : function(magnitudeType) {
	        	switch(magnitudeType) {
	        	case "MD" : return "Duration: based on the duration of shaking as measured by the time decay of the amplitude of the seismogram. Often used to compute magnitude from seismograms with “clipped” waveforms due to limited dynamic recording range of analog instrumentation, which makes it impossible to measure peak amplitudes.";
	            case "ML": return "Local: the original magnitude relationship defined by Richter and Gutenberg for local earthquakes in 1935. It is based on the maximum amplitude of a seismogram recorded on a Wood-Anderson torsion seismograph. Although these instruments are no longer widely in use, ML values are calculated using modern instrumentation with appropriate adjustments.";
	            case "MS": return "Surface wave: a magnitude for distant earthquakes based on the amplitude of Rayleigh surface waves measured at a period near 20 sec.";
	            case "MW": return "Moment: based on the moment of the earthquake, which is equal to the rigidity of the earth times the average amount of slip on the fault times the amount of fault area that slipped.";
	            case "ME" : return "Energy: based on the amount of recorded seismic energy radiated by the earthquake.";
	            case "MI" : return "Moment: based on the integral of the first few seconds of P wave on broadband instruments (Tsuboi method).";
	            case "MB" : return "Body: based on the amplitude of P body-waves. This scale is most appropriate for deep-focus earthquakes.";
	            default: return "";
	        	}
	        }
	    };
	})();
	
	
});