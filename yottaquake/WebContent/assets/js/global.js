jQuery(document).ready(function ($) {
	
	var eventTrigger = 'yottaquake.filters_update';
	var filters = {};
	
	$(document).on('yottaquake.bounding_box_update', function(e, newBB){
		
		filters.zoom = newBB.zoom;
		filters.topLeft = newBB.topLeft;
		filters.bottomRight = newBB.bottomRight;
		
		$(document).trigger(eventTrigger, filters);
	});
	
	$("#filters input.magnitude").slider({
		min: 0, 
		max: 7, 
		range: true
	}).on('slideStop', function(e){
		filters.minMagnitude = e.value[0];
		filters.maxMagnitude = e.value[1];
		$(document).trigger(eventTrigger, filters);
	});
	
	var depthSlider = $("#filters input.depth").slider({
		min: 0, 
		max: 100, 
		range: true
	}).on('slideStop', function(e){
		filters.minDepth = e.value[0];
		filters.maxDepth = e.value[1];
		$(document).trigger(eventTrigger, filters);
	});
	
	$('#filters input.from').datepicker({
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('changeDate', function(e, a){
		filters.fromDate = e.date;
		$(document).trigger(eventTrigger, filters);
	});
	
	$('#filters input.to').datepicker({
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('changeDate', function(e, a){
		filters.toDate = e.date;
		$(document).trigger(eventTrigger, filters);
	});
	
});
		
	