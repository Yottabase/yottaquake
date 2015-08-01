jQuery(document).ready(function ($) {
	
	var eventTrigger = 'yottaquake.filters_update';
	var filters = {
		magnitudeType : null,
		mapTileType : "Geografica",
		heatType : "count",
			
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
	
	
	//mapTypeSel
	var magTypeSel = $('#filters select.magnitudeType');
	magnitudeTypes.getList().forEach(function(magType){
		magTypeSel.append($('<option>', {
		    value: magType._id,
		    text: magType._id,
		})
		);
	});
	magTypeSel
	.select2({
		theme : 'bootstrap',
		allowClear: true,
	    placeholder: "Tipo magnitudo"
	})
	.on('change', function(e){
		filters.magnitudeType = $(this).val();
		$(document).trigger(eventTrigger, filters);
	});
	
	
	//mapTileType
	var mapTileTypeSel = $('#filters select.mapTileType');
	for(tileName in mapTileTypes){
		mapTileTypeSel.append($('<option>', {
		    value: tileName,
		    text: tileName
		}));
	};
	mapTileTypeSel
		.select2({
			theme : 'bootstrap',
			minimumResultsForSearch: Infinity
		})
		.on('change', function(e){
			filters.mapTileType = $(this).val();
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
	
	var fromDate = $('#filters input.from').datepicker({
		format: 'dd-mm-yyyy',
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('change', function(e){
		
		if( $(this).val() == '' ) {
			filters.fromDate = null;
		}else{
			filters.fromDate = $(this).val();
		}
		
		$(document).trigger(eventTrigger, filters);
	});
	
	$('#filters input.to').datepicker({
		format: 'dd-mm-yyyy',
		startDate: '01-01-2000',
        endDate: new Date()
	}).on('change', function(e){
		
		if( $(this).val() == '' ) {
			filters.toDate = null;
		}else{
			filters.toDate = $(this).val();
		}
		
		$(document).trigger(eventTrigger, filters);
	});
	
	$('#filters .layers input[type=checkbox]')
		.attr('checked', false)
		.on('change', function() {
			var filterName = $(this).attr('data-layer-filter');
			var status = $(this).is(":checked");
			filters[filterName] = status; 
			$(document).trigger(eventTrigger, filters);
	    });
	
	//heatTypeSel
	var heatTypeSel = $('#filters select.heatType')
	.select2({
		theme : 'bootstrap',
		minimumResultsForSearch: Infinity,
		enable : false,
	})
	.val(filters.heatType)
	.change()
	.on('change', function(e){
		filters.heatType = $(this).val();
		$(document).trigger(eventTrigger, filters);
	});
	
	$(document).on('yottaquake.filters_update', function(e, filters){
		heatTypeSel.select2("enable", filters.showHeatMap);
	});
	
	
});
		
	