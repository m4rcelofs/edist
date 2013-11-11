<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/interface/RequestService.js'></script>
		<script type='text/javascript' src="scriptform.cache.js"></script>
		<link href="styleform.cache.css" rel="stylesheet">
    </jsp:attribute>

	<jsp:body>
		<div class="jumbotron">
			<div class="container">
				<div class="col-md-8">
					<img class="img-responsive" src="../img/formtitle.cache.png"></img>
					<div class="form-group"></div>
					<form onsubmit="return false;" id="infoform"
						class="form-horizontal" role="form">
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon input-large">Id:</span> 
									<input type="text" class="form-control input-large" name="id"
									id="id" placeholder="Nick, id ou profile">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">FF:</span> 
									<input type="number" disabled class="form-control" name="quant"
									id="quant" min="10" max="100" value="10" step="10">
								</div>
							</div>
							<div class="form-group">
								<div class="row">
									<div class="col-md-offset-2">
										<div class="col-md-8">
											<select class="selectpicker" name="type" id="type"
											disabled="disabled">
												<option value="WEAPONS">Armas</option>
												<option value="FOOD">Comida</option>
												<option value="WEAPONS_FOOD">Armas + Comida</option>
												<option data-divider="true"></option>
												<option value="ROCKET">Rocket</option>
											</select>
										</div>
									</div>
								</div>
							</div>
					</form>
				</div>
				<div class="col-md-4">
					<img id="citizen-avatar" class="img-thumbnail img-responsive"
						src="../img/profile_avatar.cache.png"></img>
					<div class="form-group"></div>
					<ul class="list-group" id="user-info" style="display: none;">
						<li class="list-group-item active">  </li> 
						<li class="list-group-item"> </li> 
						<li class="list-group-item">
							<div class="counter" id="counter"></div>
						</li> 
					</ul>
				</div>
				<div class="col-md-4 col-md-offset-4">
					<button type="submit" onclick="request();"
						class="btn btn-primary btn-large">Enviar</button>
					<button type="reset" class="btn btn-default">Cancelar</button>
				</div>
			</div>
		</div>
	</jsp:body>
</t:wrapper>

