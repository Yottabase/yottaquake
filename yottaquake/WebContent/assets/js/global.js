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
	
	var fromDatepicker = $('#filters input.from').datepicker({
	});
	
	var toDatepicker = $('#filters input.to').datepicker({
	});
	
	$(document).on('yottaquake.bounding_box_update', function(e, newBB){
		console.log(newBB);
	});
	
});
		
	