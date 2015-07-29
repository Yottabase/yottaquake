jQuery(document).ready(function ($) {
	
	var eventTrigger = 'yottaquake.filters_update';
	var filters = {
		minMagnitude : 3,
		maxMagnitude : 10,
		
		minDepth : 10,
		maxDepth : 750,
	
		fromDate : null,
		toDate : null
	};
	
	$(document).on('yottaquake.bounding_box_update', function(e, newBB){
		filters.zoom = newBB.zoom;
		filters.topLeftLat = newBB.topLeft.lat;
		filters.topLeftLng = newBB.topLeft.lng;
		filters.bottomRightLat = newBB.bottomRight.lat;
		filters.bottomRightLng = newBB.bottomRight.lng;
		
		$(document).trigger(eventTrigger, filters);
	});
	
	$("#filters input.magnitude").slider({
		min: 0, 
		max: 10, 
		value: [filters.minMagnitude, filters.maxMagnitude],
		range: true
	}).on('slideStop', function(e){
		filters.minMagnitude = e.value[0];
		filters.maxMagnitude = e.value[1];
		$(document).trigger(eventTrigger, filters);
	});
	
	var depthSlider = $("#filters input.depth").slider({
		min: 0, 
		max: 750, 
		value: [filters.minDepth, filters.maxDepth],
		range: true
	}).on('slideStop', function(e){
		filters.minDepth = e.value[0];
		filters.maxDepth = e.value[1];
		$(document).trigger(eventTrigger, filters);
	});
	
	var date = $('#filters input.from').datepicker({
		format: 'dd/mm/yyyy',
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('change', function(e){
		
		if( $(this).val() == '' ) {
			filters.fromDate = null;
		}else{
			var date = new Date(e.timeStamp);
			filters.fromDate = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDay();
		}
		
		$(document).trigger(eventTrigger, filters);
	}).datepicker('setDate', filters.fromDate);
	
	$('#filters input.to').datepicker({
		format: 'dd/mm/yyyy',
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('change', function(e){
		
		if( $(this).val() == '' ) {
			filters.toDate = null;
		}else{
			var date = new Date(e.timeStamp);
			filters.toDate = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDay();
		}
		
		$(document).trigger(eventTrigger, filters);
	}).datepicker('setDate', filters.toDate);
	
	$('#filters .layers input[type=checkbox]')
		.attr('checked', false)
		.on('change', function() {
			var filterName = $(this).attr('data-layer-filter');
			var status = $(this).is(":checked");
			filters[filterName] = status; 
			$(document).trigger(eventTrigger, filters);
	    });
	
	
});
		
	