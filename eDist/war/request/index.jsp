<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.erep.dist.i18n.edist" />

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript'
			src="${baseUrl}/request/form.all.closure.js"></script>
		<script type="text/javascript"
			src="//www.google.com/jsapi?autoload=%7B%22modules%22%3A%5B%7B%22name%22%3A%22feeds%22%2C%22version%22%3A%221%22%7D%5D%7D"></script>
    </jsp:attribute>

	<jsp:body>
		
		<div id="feeds" class="panel panel-default" data-bind="visible: feed() && currentResult()">
  			<div class="panel-body" data-bind="if: currentResult()">
  				<img data-bind="attr:{src: $root.getLocalIcon(currentResult(), '${baseUrl}')}" height="20px">
  				<span data-bind="text: currentResult().author ? (currentResult().author.replace(/-/g, ' ') + ':') : ''"></span>
    			<a data-bind="text: currentResult().title, attr:{href: currentResult().link}"></a>
    			<small data-bind="text: currentResult().content ? (' - ' + currentResult().content): ''" class="text-muted"></small>
  			</div>
		</div>
		
		<div class="jumbotron" style="font-size: inherit;">
			<div class="row">
				<img class="img-responsive"
					src="${baseUrl}/img/<fmt:message key="request.supplies.image"/>"
					alt="<fmt:message key="request.supplies"/>"></img>
			</div>
			<div class="form-group"></div>
			<div id="playerForm" class="row">
				<div class="col-md-10">
					<form id="infoform" class="form-horizontal" role="form"
						data-bind="submit: sendRequest">
							<div class="form-group"
							data-bind="css: {'has-success' : player().name() !== null,
											 'has-error' : player().name() === null}">
								<div class="input-group">
									<span class="input-group-addon input-large"><fmt:message
										key="request.id" />:</span> 
									<input type="text" class="form-control input-large" name="id"
									id="id"
									placeholder="<fmt:message key="request.supplies.linkPlaceholder"/>"
									style="text-align: center;"
									data-bind="value : selectedName, 
											   valueUpdate: 'keyup'">
								</div>
							</div>
							<div class="form-group text-center"
							data-bind="visible: player().groups() && player().groups().length > 1 && player().dbStatus() === 'ENABLED'">
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
									<div class="form-group"
									data-bind="css:{'has-success': selectedFFs() <= requestAttempt().availableFF() && selectedFFs() > 0,
													'has-error': selectedFFs() > requestAttempt().availableFF() || selectedFFs() < 0}">
										<div class="input-group">
											<span class="input-group-addon"><fmt:message
												key="request.ff" />:</span> 
											<input type="number" class="form-control" id="quant" min="0"
											step="10" style="text-align: center;"
											data-bind="enable: requestAttempt().availableFF() > 0 && selectedType() !== 'ROCKET', 
														value: selectedFFs, 
														attr: {max: requestAttempt().availableFF()}">
										</div>
										<span class="help-block"
										data-bind="visible: requestAttempt().availableFF() != null &&  requestAttempt().availableFF() > 0">
										<fmt:message key="request.available" />: <span
										data-bind="text: requestAttempt().availableFF()"></span> <abbr
										title="<fmt:message key="request.foodFights"/>"><fmt:message
												key="request.ff" /></abbr>
										</span>
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
															&& requestAttempt().availableFF() > 0">
											<fmt:message key="reports.requests.weapons" />
										</option>
											<option value="FOOD"
											data-bind="enable: (requestAttempt().allowedType() == 'WEAPONS_FOOD' 
																|| requestAttempt().allowedType() == 'FOOD') 
																&& requestAttempt().availableFF() > 0">
											<fmt:message key="reports.requests.food" />
										</option>
											<option value="WEAPONS_FOOD"
											data-bind="enable: (requestAttempt().allowedType() == 'WEAPONS_FOOD') 
														&& requestAttempt().availableFF() > 0">
											<fmt:message key="reports.requests.both" />
										</option>
											<option data-divider="true"></option>
											<option value="ROCKET" data-icon="glyphicon-fire"
											data-bind="enable: requestAttempt().rocketAllowed() == true">
											<fmt:message key="reports.requests.rocket" />
										</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12"
								data-bind="if: player().dbStatus() === 'ENABLED'">
									<button id="sendRequestBtn" type="submit"
									class="btn btn-primary btn-lg btn-block" autocomplete="off"
									data-bind="enable: ((requestAttempt().availableFF() > 0 || requestAttempt().rocketAllowed() == true) 
														&& requestStatus() !== 'LOADING'),
											   css: {'btn-success' : requestStatus() == 'OK',
													 'btn-danger' : requestStatus() == 'ERROR'}">
									<fmt:message key="request.doRequest" />
								</button>
								</div>					
							</div>
					</form>
				</div>
				<div class="col-md-2">
					<img id="citizen-avatar"
						class="img-thumbnail img-responsive citizen-avatar"
						src="${baseUrl}/img/profile_avatar.cache.png"
						data-bind="style: {'background': avatarImage}"></img>
					<div class="form-group"></div>
					<div data-bind="if: player().dbStatus() === 'DISABLED'">
						<p>
							<span class="label label-default" data-toggle="popover"
								data-bind="updatePopup:{}"
								data-content="<fmt:message key="request.disabled.help"/>"><fmt:message
									key="shared.disabled" /></span>
						</p>
					</div>
					<div data-bind="if: player().fightStatus() === 'WARNING'">
						<p>
							<span class="label label-warning" data-toggle="popover"
								data-bind="updatePopup:{}"
								data-content="<fmt:message key="request.fights.warning"/>"><fmt:message
									key="shared.fights" />: <fmt:message key="shared.warning" /></span>
						</p>
					</div>
					<div data-bind="if: player().fightStatus() === 'TEMP_BANNED'">
						<p>
							<span class="label label-danger" data-toggle="popover"
								data-bind="updatePopup:{}"
								data-content="<fmt:message key="request.fights.tempBanned"/>"><fmt:message
									key="shared.fights" />: <fmt:message key="shared.tempBanned" /></span>
						</p>
					</div>
					<div data-bind="if: player().workStatus() === 'WARNING'">
						<p>
							<span class="label label-warning" data-toggle="popover"
								data-bind="updatePopup:{}"
								data-content="<fmt:message key="request.work.warning"/>"><fmt:message
									key="shared.work" />: <fmt:message key="shared.warning" /></span>
						</p>
					</div>
					<div data-bind="if: player().workStatus() === 'TEMP_BANNED'">
						<p>
							<span class="label label-danger" data-toggle="popover"
								data-bind="updatePopup:{}"
								data-content="<fmt:message key="request.work.tempBanned"/>"><fmt:message
									key="shared.work" />: <fmt:message key="shared.tempBanned" /></span>
						</p>
					</div>
					<div class="form-group"></div>
					<div class="panel panel-default"
						data-bind="visible: refreshCountdown() && requestAttempt().availableFF() == 0">
						<div class="panel-body text-center">
							<fmt:message key="request.dayChange" /> 
						    <span class="badge" data-bind="text: refreshCountdown"></span>	
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</t:wrapper>

