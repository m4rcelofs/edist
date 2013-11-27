<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript'
			src="${baseUrl}/form/form.all.closure.js"></script>
		<link href="${baseUrl}/form/styleform.cache.css" rel="stylesheet">
    </jsp:attribute>

	<jsp:body>
		<div class="jumbotron">
			<div class="row">
				<img class="img-responsive" src="../img/formtitle.cache.png"
					alt="Suprimentos"></img>
			</div>
			<div class="form-group"></div>
			<div id="playerForm" class="row">
				<div class="col-md-10">
					<form onsubmit="return false;" id="infoform"
						class="form-horizontal" role="form">
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon input-large">Id:</span> 
									<input type="text" class="form-control input-large" name="id"
									id="id" placeholder="Nick, id ou link do perfil"
									data-bind="value : selectedName, valueUpdate: 'keyup'">
								</div>
							</div>
							<div class="form-group text-center"
							data-bind="visible: player().groups() && player().groups().length > 1">
								<div data-toggle="buttons" data-bind="foreach: player().groups">
									 <label class="btn btn-default"
									data-bind="click: $root.selectedGroup, css: { active: $root.selectedGroup().groupName == $data.groupName() }">
									 	<input type="radio" name="groupBtn">
										<span data-bind="text: $data.groupName()"></span>
									 </label>
								 </div>
							</div>
							<div class="row">
								<div class="col-md-7">
									<div class="form-group">
										<div class="input-group">
											<span class="input-group-addon">FF:</span> 
											<input type="number" class="form-control" id="quant" min="0"
											step="10"
											data-bind="enable: requestAttempt().availableFF() > 0, 
														value: selectedFFs, 
														attr: {max: requestAttempt().availableFF()}">
										</div>
										<p class="help-block">Disponíveis: <span
											data-bind="text: requestAttempt().availableFF()"></span> <abbr
											title="Food Fights">FF</abbr>
									</p>
									</div>
								</div>
								<div class="col-md-offset-1 col-md-4">
									<div class="form-group">
										<select class="selectpicker show-menu-arrow show-tick"
										name="type" id="type"
										data-bind="enable: requestAttempt().availableFF() > 0 
														|| requestAttempt().rocketAllowed() == true, 
													updateSelectPicker: $root.requestAttempt,
													value: selectedType"
										data-width="100%">
											<option value="WEAPONS"
											data-bind="enable: (requestAttempt().allowedType() == 'WEAPONS_FOOD' 
															|| requestAttempt().allowedType() == 'WEAPONS') 
															&& requestAttempt().availableFF() > 0">Armas</option>
											<option value="FOOD"
											data-bind="enable: (requestAttempt().allowedType() == 'WEAPONS_FOOD' 
																|| requestAttempt().allowedType() == 'FOOD') 
																&& requestAttempt().availableFF() > 0">Comida</option>
											<option value="WEAPONS_FOOD"
											data-bind="enable: (requestAttempt().allowedType() == 'WEAPONS_FOOD') 
														&& requestAttempt().availableFF() > 0">Armas + Comida</option>
											<option data-divider="true"></option>
											<option value="ROCKET" data-icon="glyphicon-fire"
											data-bind="enable: requestAttempt().rocketAllowed() == true">Foguete</option>
										</select>
									</div>
								</div>
							</div>
					</form>
					<div class="row">
						<div class="col-md-12">
							<button type="submit" onclick="request();"
								class="btn btn-primary btn-lg btn-block">Requisitar Suprimentos</button>
						</div>					
					</div>
				</div>
				<div class="col-md-2">
					<img id="citizen-avatar"
						class="img-thumbnail img-responsive citizen-avatar"
						src="../img/profile_avatar.cache.png"
						data-bind="style: {'background': avatarImage}"></img>
					<div class="form-group"></div>
					<div class="panel panel-default">
						<div class="panel-body text-center">
							Virada do Dia
						    <span class="badge"
								data-bind="visible: refreshCountdown() !== null, text: refreshCountdown"></span>	
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</t:wrapper>

