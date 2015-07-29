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
});