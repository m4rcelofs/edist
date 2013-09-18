<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/interface/RequestService.js'></script>
		<script type='text/javascript' src="scriptform.js"></script>
		<link href="styleform.css" rel="stylesheet">
    </jsp:attribute>

	<jsp:body>
	<div class="row new">
		<div class="col-8">
				<img class="supimage" src="../img/formtitle.cache.png"></img>
				<form action="" id="contact-form" class="form-horizontal">
					<fieldset>
						<div class="input-group">
							<span class="input-group-addon input-large">Id:</span> 
							<input type="text" class="form-control input-large" name="id"
								id="id" placeholder="Nick, id ou profile">
						</div>
						
						<div class="input-group">
						<span class="input-group-addon">FF:</span> 
							<input type="number" disabled class="form-control" name="quant"
									id="quant" min="10" max="100" value="10" step="10">
						</div>
						
						
						<div class="input-group">
							<select class="selectpicker" disabled data-style="btn-inverse"
									name="type" id="type">
								<option value="WEAPONS">Armas</option>
								<option value="FOOD">Comida</option>
								<option value="WEAPONS_FOOD">Armas + Comida</option>
								<option data-divider="true"></option>
								<option value="ROCKET">Rocket</option>
							</select>
						</div>
						
						
						
						<div class="form-actions">
							<button type="submit" class="btn btn-primary btn-large">Enviar</button>
							<button type="reset" class="btn btn-default">Cancelar</button>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="col-4">
				<img class="citizen_avatar"
					src="http://s1.www.erepublik.net/images/modules/citizenprofile/profile_avatar.png"></img>
					<div class="list-group second">
					<a class="list-group-item active">  </a> 
					<a class="list-group-item"> </a> 
					<a class="list-group-item"><DIV class="counter" id="counter"></DIV> </a> 
				</div>
				
			</div>
	</div>
	</jsp:body>
</t:wrapper>

