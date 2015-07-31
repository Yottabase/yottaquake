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
	            	
	            	var color = d3.scale.category20b().domain(types);
	            	
	            	for(i=0; i<cache.length; i++){
	            		var type = cache[i];
	            		var darkColor = cache[i].darkColor = color(type._id);
	            		var lightColor = cache[i].lightColor = d3.rgb(color(type._id)).brighter(1.5).toString();
	            		
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
	        	return selected.darkColor;
	        }
	    };
	})();
	
	
});