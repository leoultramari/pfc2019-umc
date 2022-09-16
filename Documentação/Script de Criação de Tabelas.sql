create sequence seq_escola;
create table escola(
	id int default(nextval('seq_escola')) primary key,
	nome varchar(50) not null
	);
	
create sequence seq_credencialEscola;
create table credencialEscola(
	id int default(nextval('seq_credencialEscola')) primary key,
	id_escola int not null references escola(id),
	login varchar(50) not null,
	senha varchar(50) not null
	);

create sequence seq_dadosAluno;
create table dadosAluno(
	id int default(nextval('seq_dadosAluno')) primary key,
	nome varchar(100) not null,
	sexo char not null check(upper(sexo) in('M','F')),
	dataNascimento date not null,
	filiacao1 varchar(100) not null,
	filiacao2 varchar(100)
	);

create sequence seq_tipoContato;
create table tipoContato(
	id int default(nextval('seq_tipoContato')) primary key,
	nome varchar(50) not null
	);

create sequence seq_contato;
create table contato(
	id int default(nextval('seq_contato')) primary key,
	id_tipoContato int not null references tipoContato(id),
	dado varchar(100) not null,
	id_dados int not null references dadosAluno(id)
	);

create sequence seq_bairro;
create table bairro(
	id int default(nextval('seq_bairro')) primary key,
	nome varchar(50),
	id_escola int not null references escola(id)
	);

create sequence seq_endereco;
create table endereco(
	id int default(nextval('seq_endereco')) primary key,
	cep varchar(20) not null,
	logradouro varchar(50) not null,
	num varchar(10),
	id_bairro int not null references bairro(id),
	id_dados int not null references dadosAluno(id)
	);

create sequence seq_inscricaoId;
create sequence seq_inscricao;
create table inscricao(
	id int default(nextval('seq_inscricao')) primary key,
	id_inscricao int default(nextval('seq_inscricaoId')) not null,
	id_escola_original int not null references escola(id),
	id_escola_alocada int references escola(id),
	dataCriada date not null default current_timestamp,
	dataEnviada date,
	dataRecebida date,
	status int not null,
	statusMatricula int,
	id_dados int not null references dadosAluno(id)
	);

--create view view_Inscricao as
--select * from inscricao i, escola es, dadosAluno d, endereco en, bairro b where
--	i.id_dados = d.id and
--	i.id_escola_original = es.id and
--	en.id_dados = d.id and
--	b.id = en.id_bairro;

create sequence seq_turma;
create table turma(
	id int default(nextval('seq_turma')) primary key,
	serie int not null,
	classe char not null,
	ano int not null
);

create sequence seq_aluno;
create sequence seq_rm;
create table aluno(
	id int default(nextval('seq_aluno')) primary key,
	rm int default(nextval('seq_rm')) unique not null,
	status int not null,
	id_dados int not null references dadosAluno(id),
	id_escola_original int not null references escola(id),	
	id_inscricao int not null,
	id_turma int references turma(id)
	);

create sequence seq_tipoDocumento;
create table tipoDocumento(
	id int default(nextval('seq_tipoDocumento')) primary key,
	nome varchar(50) not null,
	possuiValidade boolean
	);

create sequence seq_documento;
create table documento(
	id int default(nextval('seq_documento')) primary key,
	id_tipoDocumento int not null references tipoDocumento(id),
	dado varchar(100) not null,
	validade date,
	id_dados int not null references dadosAluno(id)
	);

create sequence seq_cargo;
create table cargo(
	id int default(nextval('seq_cargo')) primary key,
	nome varchar(50) not null
	);

create sequence seq_tipoPermissao;
create table tipoPermissao(
	id int default(nextval('seq_tipoPermissao')) primary key,
	nome varchar(50) not null
);

create sequence seq_permissao;
create table permissao(
	id int default(nextval('seq_permissao')) primary key,
	id_tipoPermissao int not null references tipoPermissao(id),
	id_cargo int not null references cargo(id)
);

create sequence seq_usuario;
create table usuario(
	id int default(nextval('seq_usuario')) primary key,
	login varchar(50) not null,
	salt varchar(100) not null,
	senha varchar(100) not null,
	id_cargo int not null references cargo(id),
	id_escola int references escola(id),
	status int not null default(0)
	);

create sequence seq_filtro;
create table filtro(
	id int default(nextval('seq_filtro')) primary key,
	requerLogin boolean not null,
	contexto int,
	id_tipoPermissao int references tipoPermissao(id),
	URL varchar(50)
);


create sequence seq_funcionario;
create table funcionario(
	id int default(nextval('seq_funcionario')) primary key,
	registro int unique not null,
	nome varchar(100) not null,
	email varchar(100),
	telefone varchar(20),
	dataInicio date not null,
	dataSaida date,
	dataAtualizado date not null default current_timestamp,
	dataEnviado date,
	id_escola int not null references escola(id),
	status int not null default(0)
);