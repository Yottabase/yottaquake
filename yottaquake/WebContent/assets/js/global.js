jQuery(document).ready(function ($) {
	
	var magnitudeSlider = $("#filters input.magnitude").slider({
		min: 0, 
		max: 7, 
		range: true
	});
	
	var depthSlider = $("#filters input.depth").slider({
		min: 0, 
		max: 100, 
		range: true
	});
	
	
});
		
	