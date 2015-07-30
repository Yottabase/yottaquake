<%@ include file="layout/header.jsp"%>

<div class="container-fluid">

	<div class="row">
		<div class="col-md-3" id="filters">

			<h3>Filtri</h3>
			<form>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label>Tipologia magnitudo</label>

							<div class="row">
								<div class="col-md-12">
									<select class="form-control magnitudeType">
										<option value=""></option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label>Mappa</label>

							<div class="row">
								<div class="col-md-12">
									<select class="form-control mapTileType">
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label>Magnitudo</label>

					<div class="row">
						<div class="col-md-12">
							<input type="text" class="magnitude" />
						</div>
					</div>
				</div>

				<div class="form-group">
					<label>Periodo</label>

					<div class="row">
						<div class="col-md-6">
							Dal <input type="text" class="form-control input-block from">

						</div>
						<div class="col-md-6">
							Al <input type="text" class="form-control input-block to">
						</div>
					</div>
				</div>

				<div class="form-group">
					<label>Profondita</label>

					<div class="row">
						<div class="col-md-12">
							<input type="text" class="depth" />
						</div>
					</div>
				</div>

				<div class="form-group layers">
					<label>Layers</label>
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showCountries">Mappa politica</label>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showTectonicPlates">Placche tettoniche</label>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showFlinnRegions">Regioni di Flinn</label>
					</div>
				</div>				
				
				<div class="form-group layers">
					<label>Eventi</label>					
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showClusteredEvents">Eventi clusterizzati</label>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showHeatMap">Heat map</label>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" data-layer-filter="showEvents">Eventi</label>
					</div>
				</div>				
			</form>
		</div>

		<div class="col-md-9" id="graph">
			<div id="chart-events-map"></div>
		</div>
	</div>









	<br />
</div>
<!-- /.container -->


<!-- <div id="chart-events-by-month"></div>
			
			<div id="chart-events-by-year"></div>-->

<%@ include file="layout/footer.jsp"%>