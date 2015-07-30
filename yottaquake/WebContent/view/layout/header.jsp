<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="Eureka Search Engine - Il motore di ricerca definitivo">
    <meta name="author" content="Yotabase">
    <link rel="icon" href="../../favicon.ico">

    <title>Yottaquake</title>

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->



	<script src="assets/js/lib/jquery-2.1.3.min.js"></script>
    
    <script src="assets/js/lib/d3.min.js"></script>
    
    <script src="assets/js/lib/bootstrap.js"></script>
    <link href="assets/css/lib/bootstrap.css" rel="stylesheet">
    <link href="assets/css/lib/bootstrap-theme.min.css" rel="stylesheet">
    
    <script src="assets/js/lib/leaflet.js"></script>
    <link href="assets/css/lib/leaflet.css" rel="stylesheet">
    
    <script src="assets/js/lib/Leaflet.MakiMarkers.js"></script>
    <link href="assets/css/lib/MarkerCluster.css" rel="stylesheet">
    
    <script src="assets/js/lib/leaflet.markercluster.js"></script>
    <link href="assets/css/lib/MarkerCluster.Default.css" rel="stylesheet">
    
    <script src="assets/js/lib/leaflet-heat.js"></script>
    
    <script src="assets/js/lib/bootstrap-slider.min.js"></script>
    <link href="assets/css/lib/bootstrap-slider.min.css" rel="stylesheet">
    
    <script src="assets/js/lib/bootstrap-datepicker.min.js"></script>
    <link href="assets/css/lib/bootstrap-datepicker3.min.css" rel="stylesheet">
    
    <script src="assets/js/lib/select2.min.js"></script>
    <link href="assets/css/lib/select2.min.css" rel="stylesheet">
    <link href="assets/css/lib/select2-bootstrap.min.css" rel="stylesheet">
    
    <!--  //TODO: remove this sample -->
    <script src="http://leaflet.github.io/Leaflet.markercluster/example/realworld.388.js"></script>
    
    <script type="text/javascript">
    	//js config
    	var wsUrl = "http://localhost:8080/yottaquake/";
    	L.Icon.Default.imagePath = 'assets/images/lib/leaflet';
    	var mapDefault = {
   			lat : 41.90278, 
   			lng : 12.49636,
   			zoom : 10
    	};
    	var visualizationLimit = {
    		cluster : 50000,
    		heatMap : 100000,
    		markers : 20000
    	};
    	var mapTileTypes = {
    		Default : {
    			url : "https://api.tiles.mapbox.com/v4/mapbox.outdoors/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ",
    			options : {
    				minZoom: 2,
    				maxZoom: 18,
    				attribution: 'Dev by <strong>Yottabase</strong>'
    			}
    		},
    		Satellite : {
    			url : "http://otile{s}.mqcdn.com/tiles/1.0.0/{type}/{z}/{x}/{y}.{ext}",
    			options : {
    				type: 'sat',
    				ext: 'jpg',
        			attribution: 'Tiles Courtesy of <a href="http://www.mapquest.com/">MapQuest</a> &mdash; Portions Courtesy NASA/JPL-Caltech and U.S. Depart. of Agriculture, Farm Service Agency',
        			subdomains: '1234'
    			}
    		}
    	};
    </script>
    
    <!-- app -->
	<link href="assets/css/events-by-month.css" rel="stylesheet">
    <script src="assets/js/events-by-month.js"></script>
    
    <link href="assets/css/events-by-year.css" rel="stylesheet">
    <script src="assets/js/events-by-year.js"></script>
    
    <link href="assets/css/events-map.css" rel="stylesheet">
    <script src="assets/js/events-map.js"></script>
    
    <link href="assets/css/style.css" rel="stylesheet">
    <script src="assets/js/filters.js"></script>
    <script src="assets/js/common.js"></script>
  </head>

  <body>
   	<div id="label-loading">Aggiornamento dati in corso...</div>
   	<div id="growl">Aggiornamento dati in corso...</div>