<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript'
			src='dwr/interface/RequestService.js'></script>
    </jsp:attribute>

	<jsp:body>
	<div class="col-8">
		<h3>Form</h3>
		<form action="" id="contact-form" class="form-horizontal">
			<fieldset>
				<div class="input-group">
					<span class="input-group-addon input-large">Id:</span> <input
							type="text" class="form-control input-large" name="id" id="id"
							placeholder="Nick, id ou profile">
				</div>

				<div class="input-group-btn">
					<select class="selectpicker" data-style="btn-inverse">
						<option value="WEAPONS">Armas</option>
						<option value="FOOD">Comida</option>
						<option value="WEAPONS_FOOD">Armas + Comida</option>
						<option data-divider="true"></option>
						<option value="ROCKET">Rocket</option>
					</select>
				</div>
				<div class="form-actions">
					<button type="submit" class="btn btn-primary btn-large">Submit</button>
					<button type="reset" class="btn">Cancel</button>
				</div>
			</fieldset>
		</form>
	</div>
	<div class="col-4">
		<img class="citizen_avatar"
				style="background-image: url(http://static.erepublik.net/uploads/avatars/Citizens/2010/09/13/5711b24ddebb1a4e8faaae65c6250b9c_142x142.jpg);"
				src="/img/profile_avatar.cache.png"></img>
	</div>
	</jsp:body>
</t:wrapper>

