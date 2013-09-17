<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/interface/AdminService.js'></script>
		<script type='text/javascript' src='admin.js'></script>
		<link href="../css/bootstrap-glyphicons.css" rel="stylesheet">
    </jsp:attribute>

	<jsp:body>
			<ul class="nav nav-tabs">
				<li class=""><a href="#moderators" data-toggle="tab"
				id="moderatorsTab">Moderadores</a></li>
				<li><a href="#distributors" data-toggle="tab"
				id="distributorsTab">Distribuidores</a></li>
				<li class="active"><a href="#config" data-toggle="tab"
				id="configTab">Configurações</a></li>
			</ul>
			
			<div class="tab-content">
				<div class="tab-pane" id="moderators">mods</div>
				<div class="tab-pane" id="distributors">dists</div>
				<div class="tab-pane active" id="config">
					<div class="text-center">
						<img id="config-loader" src="../img/loader.cache.gif" />
					</div>
					<form id="config-form" class="form-horizontal" role="form"
					style="display: none;" onsubmit="return false;">
						<div class="form-group"></div>
						<div class="form-group">
							<label class="control-label col-lg-6" for="muName">Nome da Unidade Militar</label>
							<div class="col-lg-5">
								<input type="text" class="form-control" id="muName"
								placeholder="nome da mu" required>
							</div>
						</div> 
						<!-- TODO config default -->
						<div class="form-group">
							<label class="col-lg-6 control-label" for="granularity">Granularidade da Configuração de Distribuição</label>
							<div class="col-lg-5">
								<select class="form-control" data-style="btn-inverse"
								name="granularity" id="granularity">
									<option value="MU">Unidade Militar</option>
									<option value="GROUP">Grupo</option>
									<option value="PLAYER">Jogador</option>
								</select>
							</div>
							<a href="#" class="help" data-toggle="popover" title
							data-content="Define qual o nível máximo de configuração das distribuições - você pode definir até uma configuração diferente para cada jogador! Caso essa configuração não exista, o nível superior será utilizado."
							data-original-title="Granularidade">
								<span class="glyphicon glyphicon-question-sign"></span>
							</a>
						</div> 
						<div class="form-group">
							<label class="col-lg-6 control-label" for="reportsAccess">Acesso aos Relatórios de Distribuição</label>
							<div class="col-lg-5">
								<select class="form-control" data-style="btn-inverse"
								name="reportsAccess" id="reportsAccess">
									<option value="ADMINS">Administradores</option>
									<option value="MODS">Moderadores</option>
									<option value="VIEWER">Visitantes Registrados</option>
									<option value="PUBLIC">Público</option>
								</select>
							</div>
							<a href="#" class="help" data-toggle="popover" title
							data-content="Quem poderá acessar os relatórios de distribuição (FFs, itens enviados, etc)"
							data-original-title="Nível de Acesso">
								<span class="glyphicon glyphicon-question-sign"></span>
							</a>
						</div>
						<div class="form-group">
							<label class="control-label col-lg-6" for="playerReportsAccess">Acesso aos Relatórios de Jogadores</label>
							<div class="col-lg-5">
								<select class="form-control" data-style="btn-inverse"
								name="playerReportsAccess" id="playerReportsAccess">
									<option value="ADMINS">Administradores</option>
									<option value="MODS">Moderadores</option>
									<option value="VIEWER">Visitantes Registrados</option>
									<option value="PUBLIC">Público</option>
								</select>
							</div>
							<a href="#" class="help" data-toggle="popover" title
							data-content="Quem poderá acessar os relatórios de jogadores (lutas, força, etc)"
							data-original-title="Nível de Acesso">
								<span class="glyphicon glyphicon-question-sign"></span>
							</a>
						</div>
						<div class="form-group">
							<label class="control-label col-lg-6" for="email">E-mail do Responsável</label>
							<div class="input-group col-lg-5">
								<input type="email" class="form-control" id="contactEmail"
								placeholder="email@exemplo.com" required>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-envelope"></span>
								</span>
							</div>
						</div>
						<div class="form-group">
							<div class="col-lg-offset-6 col-lg-2">
								<button type="submit" class="btn btn-primary" onclick="saveConfig()">
									<span class="glyphicon glyphicon-floppy-disk"></span>
									 Salvar
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
	</jsp:body>
</t:wrapper>

