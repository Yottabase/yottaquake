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