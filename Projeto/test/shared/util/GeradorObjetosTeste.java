/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import shared.model.Aluno;
import shared.model.Bairro;
import shared.model.Cargo;
import shared.model.Contato;
import shared.model.DadosAluno;
import shared.model.Documento;
import shared.model.Endereco;
import shared.model.Escola;
import shared.model.Inscricao;
import shared.model.Permissao;
import shared.model.StatusAluno;
import shared.model.StatusInscricao;
import shared.model.StatusUsuario;
import shared.model.TipoContato;
import shared.model.TipoDocumento;
import shared.model.TipoPermissao;
import shared.model.Usuario;
import shared.model.dao.AlunoDAO;
import shared.model.dao.BairroDAO;
import shared.model.dao.CargoDAO;
import shared.model.dao.ContatoDAO;
import shared.model.dao.DAOGenerico;
import shared.model.dao.DadosAlunoDAO;
import shared.model.dao.DocumentoDAO;
import shared.model.dao.EnderecoDAO;
import shared.model.dao.EscolaDAO;
import shared.model.dao.InscricaoDAO;
import shared.model.dao.InscricaoNovaDAO;
import shared.model.dao.PermissaoDAO;
import shared.model.dao.TipoContatoDAO;
import shared.model.dao.TipoDocumentoDAO;
import shared.model.dao.TipoPermissaoDAO;
import shared.model.dao.UsuarioDAO;

/**
 *
 * @author leona
 */
public class GeradorObjetosTeste {

    //gera uma DAO
    //passar con para usar conexão passada (modo de teste)
    //não passar con para usar padrão (modo produção)
    private static DAOGenerico gerarDAO(Class<? extends DAOGenerico> DAO, Connection con) {
        try {
            if (con == null) {
                return DAO.newInstance();
            } else {
                Constructor construtor = DAO.getDeclaredConstructor(Connection.class);
                return (DAOGenerico) construtor.newInstance(con);
            }
        } catch (Exception e) {
            Auditoria.logErro(e);
            return null;
        }
    }

    //Escola
    public static Escola CadastrarEscolaTeste() {
        return CadastrarEscolaTeste(null);
    }

    public static Escola CadastrarEscolaTeste(Connection con) {
        EscolaDAO DAO = (EscolaDAO) gerarDAO(EscolaDAO.class, con);

        Escola obj = new Escola();
        obj.setNome("Teste");

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //CredencialEscola
    //DadosAluno
    private static int numSeries  = Configuracao.getInt("turmas", "numSeries");
    private static int idade1  = Configuracao.getInt("turmas", "idade1");

    private static void PreencherDadosAluno(DadosAluno obj) {
        LocalDateTime horario = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        obj.setNome("Teste " + horario.format(dtf));
        if (Math.random() > 0.5) {
            obj.setSexo('M');
        } else {
            obj.setSexo('F');
        }
        obj.setFiliacao1("Teste1");
        obj.setFiliacao2("Teste2");
        //randomizar datas de nascimento
        //apenas datas válidas para as séries da escola
        int ano = (int) (LocalDate.now().getYear() - idade1 - Math.ceil(Math.random() * numSeries));
        int mes = (int) (Math.ceil(Math.random() * 12));
        int dia = (int) (Math.ceil(Math.random() * 28));
        LocalDate dt = LocalDate.of(ano, mes, dia);
        obj.setDataNascimento(dt);
    }

    //DadosAluno vazio
    public static DadosAluno CadastrarDadosAlunoVazioTeste() {
        return CadastrarDadosAlunoVazioTeste(null);
    }

    public static DadosAluno CadastrarDadosAlunoVazioTeste(Connection con) {
        DadosAlunoDAO DAO = (DadosAlunoDAO) gerarDAO(DadosAlunoDAO.class, con);

        DadosAluno obj = new DadosAluno();
        PreencherDadosAluno(obj);

        int id = DAO.cadastrar(obj);
        obj.setId(id);
        Endereco e = CadastrarEnderecoTeste(con, obj);
        obj.setEndereco(e);
        return obj;
        //return DAO.consultar(id);
    }

    //DadosAluno completo
    public static DadosAluno CadastrarDadosAlunoCompletoTeste() {
        return CadastrarDadosAlunoCompletoTeste(null);
    }

    public static DadosAluno CadastrarDadosAlunoCompletoTeste(Connection con) {
        DadosAlunoDAO DAO = (DadosAlunoDAO) gerarDAO(DadosAlunoDAO.class, con);

        DadosAluno obj = new DadosAluno();
        PreencherDadosAluno(obj);

        int id = DAO.cadastrar(obj);
        obj.setId(id);
        //obj = DAO.consultar(id);
        CadastrarContatoTeste(con, obj);
        CadastrarEnderecoTeste(con, obj);
        CadastrarDocumentoTeste(con, obj);
        return obj;
    }

    //TipoContato
    public static TipoContato CadastrarTipoContatoTeste() {
        return CadastrarTipoContatoTeste(null);
    }

    public static TipoContato CadastrarTipoContatoTeste(Connection con) {
        TipoContatoDAO DAO = (TipoContatoDAO) gerarDAO(TipoContatoDAO.class, con);

        TipoContato obj = new TipoContato();
        obj.setNome("Teste");

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Contato
    public static Contato CadastrarContatoTeste(DadosAluno dados) {
        return CadastrarContatoTeste(null, dados);
    }

    public static Contato CadastrarContatoTeste(Connection con, DadosAluno dados) {
        ContatoDAO DAO = (ContatoDAO) gerarDAO(ContatoDAO.class, con);

        Contato obj = new Contato();
        obj.setDado("Teste");
        obj.setTipoContato(CadastrarTipoContatoTeste());
        obj.setDados(dados);

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Bairro
    public static Bairro CadastrarBairroTeste() {
        return CadastrarBairroTeste(null);
    }

    public static Bairro CadastrarBairroTeste(Connection con) {
        BairroDAO DAO = (BairroDAO) gerarDAO(BairroDAO.class, con);

        Bairro obj = new Bairro();
        obj.setNome("Teste");
        obj.setEscola(CadastrarEscolaTeste());

        int id = DAO.cadastrar(obj);
        obj.setId(id);
        return obj;
        //return DAO.consultar(id);
    }

    //Endereco
    public static Endereco CadastrarEnderecoTeste(DadosAluno dados) {
        return CadastrarEnderecoTeste(null, dados);
    }

    public static Endereco CadastrarEnderecoTeste(Connection con, DadosAluno dados) {
        EnderecoDAO DAO = (EnderecoDAO) gerarDAO(EnderecoDAO.class, con);
        BairroDAO bDAO = (BairroDAO) gerarDAO(BairroDAO.class, con);

        Endereco obj = new Endereco();
        obj.setCep("Teste");
        obj.setLogradouro("Teste");
        //obj.setBairro(CadastrarBairroTeste());
        int bairroRand = (int) Math.ceil(Math.random()*5);
        obj.setBairro(bDAO.consultar(bairroRand));
        obj.setDados(dados);

        int id = DAO.cadastrar(obj);
        obj.setId(id);
        return obj;
        //return DAO.consultar(id);
    }

    //Inscricao
    public static Inscricao CadastrarInscricaoTeste() {
        return CadastrarInscricaoTeste(null);
    }

    public static Inscricao CadastrarInscricaoTeste(Connection con) {
        InscricaoNovaDAO DAO = (InscricaoNovaDAO) gerarDAO(InscricaoNovaDAO.class, con);
        InscricaoDAO DAO2 = (InscricaoDAO) gerarDAO(InscricaoDAO.class, con);
        EscolaDAO eDAO = (EscolaDAO) gerarDAO(EscolaDAO.class, con);

        Inscricao obj = new Inscricao();
        //obj.setDados(CadastrarDadosAlunoCompletoTeste());
        obj.setDados(CadastrarDadosAlunoVazioTeste());
        //obj.setEscolaOriginal(CadastrarEscolaTeste());
        obj.setEscolaOriginal(eDAO.consultar(Configuracao.getInt("autenticacao", "id")));
        obj.setStatus(StatusInscricao.Aberta);
        obj.setDataCriada(LocalDate.now());

        int id = DAO.cadastrar(obj);
        return DAO2.consultar(id);
    }

    //Aluno
    public static Aluno CadastrarAlunoTeste() {
        return CadastrarAlunoTeste(null);
    }

    public static Aluno CadastrarAlunoTeste(Connection con) {
        AlunoDAO DAO = (AlunoDAO) gerarDAO(AlunoDAO.class, con);

        Aluno obj = new Aluno();
        Inscricao ins = CadastrarInscricaoTeste();
        obj.setIdInscricao(ins.getId());
        obj.setDados(ins.getDados());
        obj.setIdEscolaOriginal(ins.getEscolaOriginal().getId());
        obj.setStatus(StatusAluno.Matriculado);

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //TipoDocumento
    public static TipoDocumento CadastrarTipoDocumentoTeste() {
        return CadastrarTipoDocumentoTeste(null);
    }

    public static TipoDocumento CadastrarTipoDocumentoTeste(Connection con) {
        TipoDocumentoDAO DAO = (TipoDocumentoDAO) gerarDAO(TipoDocumentoDAO.class, con);

        TipoDocumento obj = new TipoDocumento();
        obj.setNome("Teste");
        obj.setPossuiValidade(false);

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Documento
    public static Documento CadastrarDocumentoTeste(DadosAluno dados) {
        return CadastrarDocumentoTeste(null, dados);
    }

    public static Documento CadastrarDocumentoTeste(Connection con, DadosAluno dados) {
        DocumentoDAO DAO = (DocumentoDAO) gerarDAO(DocumentoDAO.class, con);

        Documento obj = new Documento();
        obj.setDado("Teste");
        obj.setTipoDocumento(CadastrarTipoDocumentoTeste());
        obj.setDados(dados);

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Cargo
    public static Cargo CadastrarCargoTeste() {
        return CadastrarCargoTeste(null);
    }

    public static Cargo CadastrarCargoTeste(Connection con) {
        CargoDAO DAO = (CargoDAO) gerarDAO(CargoDAO.class, con);

        Cargo c = new Cargo();
        c.setNome("Teste");

        int id = DAO.cadastrar(c);
        return DAO.consultar(id);
    }

    //TipoPermissao
    public static TipoPermissao CadastrarTipoPermissaoTeste() {
        return CadastrarTipoPermissaoTeste(null);
    }

    public static TipoPermissao CadastrarTipoPermissaoTeste(Connection con) {
        TipoPermissaoDAO DAO = (TipoPermissaoDAO) gerarDAO(TipoPermissaoDAO.class, con);

        TipoPermissao obj = new TipoPermissao();
        obj.setNome("Teste");

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Permissao
    public static Permissao CadastrarPermissaoTeste() {
        return CadastrarPermissaoTeste(null);
    }

    public static Permissao CadastrarPermissaoTeste(Connection con) {
        PermissaoDAO DAO = (PermissaoDAO) gerarDAO(PermissaoDAO.class, con);

        Permissao obj = new Permissao();
        obj.setTipoPermissao(CadastrarTipoPermissaoTeste());
        obj.setCargo(CadastrarCargoTeste());

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Usuario
    public static Usuario CadastrarUsuarioTeste() {
        return CadastrarUsuarioTeste(null);
    }

    public static Usuario CadastrarUsuarioTeste(Connection con) {
        UsuarioDAO DAO = (UsuarioDAO) gerarDAO(UsuarioDAO.class, con);

        Usuario obj = new Usuario();
        obj.setLogin("Teste");
        obj.setSenha("Teste");
        obj.setCargo(CadastrarCargoTeste());
        obj.setEscola(CadastrarEscolaTeste());
        //obj.setStatus(StatusUsuario.Ativo);

        int id = DAO.cadastrar(obj);
        return DAO.consultar(id);
    }

    //Filtro
}
