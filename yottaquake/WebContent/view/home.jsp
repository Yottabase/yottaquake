<%@ include file="layout/header.jsp"%>

<div class="container">

	

			
			<h3>Filtri</h3>

			<form>
				<div class="form-group">
					<label for="exampleInputPassword1">Magnitudo</label>
					
					<div class="row">
						<div class="col-md-5">
							<select
								class="form-control">
								<option>1</option>
								<option>2</option>
								<option>3</option>
								<option>4</option>
								<option>5</option>
								<option>6</option>
							</select>
						</div>
						<div class="col-md-1">-</div>
						<div class="col-md-5">
							<select
								class="form-control">
								<option>1</option>
								<option>2</option>
								<option>3</option>
								<option>4</option>
								<option>5</option>
								<option>6</option>
							</select>
						</div>
					</div> 
				</div>
				
				<button type="submit" class="btn btn-primary">Aggiorna</button>
			</form>


		
			<div id="chart-events-by-month"></div>
		
	

</div>
<!-- /.container -->

<%@ include file="layout/footer.jsp"%>