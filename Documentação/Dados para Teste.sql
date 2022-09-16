insert into escola(nome) values
	('Escola 1'),
	('Escola 2'),
	('Escola 3');
	
insert into credencialEscola(id_escola, login, senha) values
	(1, 'escola1', 'escola1'),
	(2, 'escola2', 'escola2'),
	(3, 'escola3', 'escola3');
	
insert into bairro(nome, id_escola) values
	('Bairro A1', 1),
	('Bairro B1', 1),
	('Bairro C2', 2),
	('Bairro D3', 3),
	('Bairro E3', 3);

insert into tipoContato(id, nome) values
	(1, 'Telefone'),
	(2, 'Email');

insert into tipoDocumento(id, nome, possuiValidade) values
	(1, 'RG', false),
	(2, 'CPF', false),
	(3, 'Certidão de Nascimento', false);
	
insert into cargo(nome) values
	('Administrador'),
	('UE - Gestor'),
	('SME - Setor de Demanda Escolar');
	
insert into tipoPermissao(id, nome) values
	(1, 'VisualizarUsuarios'),
	(2, 'ManterUsuarios'),
	(3, 'VisualizarAlunos'),
	(4, 'ManterAlunos'),
	(5, 'VisualizarInscricoes'),
	(6, 'ManterInscricoes'),
	(7, 'VisualizarFuncionarios'),
	(8, 'ManterFuncionarios'),
	(9, 'VisualizarEscolas');

insert into permissao(id_cargo, id_tipoPermissao) values
	((select id from cargo where nome ilike 'Admin%'),		1),
	((select id from cargo where nome ilike 'Admin%'),		2),
	((select id from cargo where nome ilike 'UE%'),			3),
	((select id from cargo where nome ilike 'UE%'),			4),
	((select id from cargo where nome ilike 'UE%'),			5),
	((select id from cargo where nome ilike 'UE%'),			6),
	((select id from cargo where nome ilike 'SME%'),		3),
	((select id from cargo where nome ilike 'SME%'),		4),
	((select id from cargo where nome ilike 'SME%'),		5),
	((select id from cargo where nome ilike 'SME%'),		6),
	((select id from cargo where nome ilike 'SME%'),		7),
	((select id from cargo where nome ilike 'SME%'),		8),
	((select id from cargo where nome ilike 'UE%'),			7),
	((select id from cargo where nome ilike 'SME%'),		9);

insert into usuario(login, id_cargo, id_escola, salt, senha) values
	('admin',	1, null,	'e8cf948b2a616c4c909f3ec95fc3d11f',	'xWDTcZf18uFBlxyobVufZWCnvR13N/URQ/p8XzOe9AM='),
	('sme',		3, null,	'490e068fc72e1465a86550a7d58d1ce7',	'IylyjCWaFsdBBHUOnZxF+SvE/JmsDpjpwUCsRSKrC+s='),
	('ue',		2, 1,		'ab3b46dd6a9bc9c2c6246895fdc26641',	'qP7TyEjszEmm8GH8Bi3k1vK8gdgFln3xCNU5FqcXKwQ='),
	('ue2',		2, 2,		'c41fc82b5cb8cd2e16d2ae9fc4094fd0',	'xV8nu2tVk+J4MKjpVIvAX+RySoYMJzODia9xufVX2+w='),
	('ue3',		2, 3,		'20c6bc62e0884cb386a778e31f5c0c3e',	'fJW2IykOf2AHgWN5MfFahtaJBdpYiC/W7atwwqlVS94=');


--1 = UE
--2 = SME
insert into filtro(requerLogin, contexto, id_tipoPermissao, URL) values
	(false, 	null, 	null, 																	'/login.html'),
	(false, 	null, 	null, 																	'/css/login.css'),
	(false, 	null, 	null, 																	'/js/login.js'),
	(false, 	null, 	null, 																	'/RealizarLogin'),
	(false, 	null, 	null, 																	'/css/bootstrap.min.css'),
	(false, 	null, 	null, 																	'/favicon.ico'),
	(false, 	null, 	null, 																	'/js/jquery-3.3.1.min.js'),
	(false, 	null, 	null, 																	'/js/core-min.js'),
	(false, 	null, 	null, 																	'/js/enc-base64-min.js'),
	(false, 	null, 	null, 																	'/js/sha256-min.js'),
	(false, 	null, 	null, 																	'/js/formToJSON.js'),

	(true, 		null, 	null,																	'/PreencherNavbar'),
	(true, 		null, 	null,																	'/RealizarLogoff'),

	(true, 		null, 	null,																	'/ListarBairro'),
	(true, 		null, 	null,																	'/ListarCargo'),
	(true, 		null, 	null,																	'/ListarEscola'),
	(true, 		null, 	null,																	'/ListarTipoContato'),
	(true, 		null, 	null,																	'/ListarTipoDocumento'),

	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterInscricoes'), 		'/AtualizarInscricao'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterInscricoes'), 		'/CancelarInscricao'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'), 	'/ConsultarInscricao'),

	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarFuncionarios'),'/ListarFuncionario'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarFuncionarios'),'/ConsultarFuncionario'),

	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterUsuarios'), 		'/RemoverUsuario'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterUsuarios'), 		'/CadastrarUsuario'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterUsuarios'), 		'/AtualizarUsuario'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'ManterUsuarios'), 		'/admin/cadastrar-usuario.html'),

	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarUsuarios'), 	'/ConsultarUsuario'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarUsuarios'), 	'/ListarUsuario'),

	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'), 		'/AlocacaoInscricoesManual'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'), 		'/ArquivamentoManual'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'), 		'/EnvioManualInscricoesSME'),

	(true, 		2,	 	(select id from tipoPermissao where nome like 'VisualizarEscolas'),		'/ListarEscolaStatus'),

	(true, 		2,	 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'/ListarInscricaoArquivada'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'/ListarInscricaoPendente'),

	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterFuncionarios'),	'/CadastrarFuncionario'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterFuncionarios'),	'/AtualizarFuncionario'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterFuncionarios'),	'/RemoverFuncionario'),
	(true, 		2,	 	(select id from tipoPermissao where nome like 'ManterFuncionarios'),	'/EnvioManualFuncionarios'),

	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'),		'/EnvioManualInscricoesUE'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'),		'/GerarInscricao'),

	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'/ListarAlunoNaoAlocado'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'/ListarAlunoTurma'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'/ConsultarAluno'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'/ListarTurma'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'/ListarTurmaCompleta'),

	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'/ListarInscricaoEnviada'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'/ListarInscricaoRecebida'),

	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'),		'/RealizarMatricula'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterAlunos'),			'/RealizarMatricula'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterInscricoes'),		'/RealizarMatriculaTodos'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterAlunos'),			'/RealizarMatriculaTodos'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterAlunos'),			'/RemoverAluno'),
	(true, 		1,	 	(select id from tipoPermissao where nome like 'ManterAlunos'), 			'/AlocacaoAlunosManual'),

	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarInscricoes'), 	'%/inscricao/%'),
	(true, 		null, 	(select id from tipoPermissao where nome like 'VisualizarUsuarios'), 	'%/usuario/%'),

	(true, 		2,	 	null,																	'%/SME/%'),
	(true, 		2, 		(select id from tipoPermissao where nome like 'VisualizarFuncionarios'),'%/SME/funcionario/%'),
	(true, 		2, 		(select id from tipoPermissao where nome like 'VisualizarEscolas'),		'%/SME/escola/%'),
	(true, 		2, 		(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'%/SME/inscricao/%'),

	(true, 		1,	 	null,																	'%/UE/%'),
	(true, 		1, 		(select id from tipoPermissao where nome like 'VisualizarFuncionarios'),'%/UE/funcionario/%'),
	(true, 		1, 		(select id from tipoPermissao where nome like 'VisualizarInscricoes'),	'%/UE/inscricao/%'),
	(true, 		1, 		(select id from tipoPermissao where nome like 'VisualizarAlunos'),		'%/UE/aluno/%')
	;




