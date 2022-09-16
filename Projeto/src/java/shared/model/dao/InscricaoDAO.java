/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import shared.model.Inscricao;
import shared.model.ObjetoDB;
import shared.model.StatusAluno;
import shared.model.StatusInscricao;

/**
 *
 * @author superalunocmc
 */
public class InscricaoDAO extends DAOGenerico {

    public InscricaoDAO() {
        super();
    }

    public InscricaoDAO(Connection conManual) {
        super(conManual);
    }

    protected final static String ATIVA = " status not in (" + StatusInscricao.Cancelada.valor + ")";

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Inscricao obj = new Inscricao(resultado.getInt("id"));

        obj.setIdInscricao(resultado.getInt("id_inscricao"));

        obj.setDataCriada(resultado.getDate("dataCriada").toLocalDate());

        Date dataEnviada = resultado.getDate("dataEnviada");
        obj.setDataEnviada(dataEnviada != null ? dataEnviada.toLocalDate() : null);

        Date dataRecebida = resultado.getDate("dataRecebida");
        obj.setDataRecebida(dataRecebida != null ? dataRecebida.toLocalDate() : null);

        EscolaDAO eDAO = new EscolaDAO();
        obj.setEscolaOriginal(eDAO.consultar(resultado.getInt("id_escola_original")));

        if (resultado.getInt("id_escola_alocada") != 0) {
            obj.setEscolaAlocada(eDAO.consultar(resultado.getInt("id_escola_alocada")));
        }

        obj.setStatus(StatusInscricao.fromInteger(resultado.getInt("status")));

        DadosAlunoDAO dDAO = new DadosAlunoDAO();
        obj.setDados(dDAO.consultar(resultado.getInt("id_dados")));

        if (resultado.getInt("statusMatricula") != 0) {
            obj.setStatusMatricula(StatusAluno.fromInteger(resultado.getInt("statusMatricula")));
        }

        return obj;
    }
    
    //Invoca consultarSimples em DadosAluno
    protected ObjetoDB gerarObjetoSimples(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Inscricao obj = new Inscricao(resultado.getInt("id"));

        obj.setIdInscricao(resultado.getInt("id_inscricao"));

        obj.setDataCriada(resultado.getDate("dataCriada").toLocalDate());

        Date dataEnviada = resultado.getDate("dataEnviada");
        obj.setDataEnviada(dataEnviada != null ? dataEnviada.toLocalDate() : null);

        Date dataRecebida = resultado.getDate("dataRecebida");
        obj.setDataRecebida(dataRecebida != null ? dataRecebida.toLocalDate() : null);

        EscolaDAO eDAO = new EscolaDAO();
        obj.setEscolaOriginal(eDAO.consultar(resultado.getInt("id_escola_original")));

        if (resultado.getInt("id_escola_alocada") != 0) {
            obj.setEscolaAlocada(eDAO.consultar(resultado.getInt("id_escola_alocada")));
        }

        obj.setStatus(StatusInscricao.fromInteger(resultado.getInt("status")));
        
        DadosAlunoDAO dDAO = new DadosAlunoDAO();
        obj.setDados(dDAO.consultarSimples(resultado.getInt("id_dados")));

        if (resultado.getInt("statusMatricula") != 0) {
            obj.setStatusMatricula(StatusAluno.fromInteger(resultado.getInt("statusMatricula")));
        }

        return obj;
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Inscricao objTipado = (Inscricao) obj;

        comando.setInt(1, objTipado.getEscolaOriginal().getId());

        if (objTipado.getIdInscricao() > 0) {
            comando.setInt(2, objTipado.getIdInscricao());
        } else {
            comando.setNull(2, java.sql.Types.INTEGER);
        }

        comando.setInt(3, objTipado.getDados().getId());
        comando.setInt(4, objTipado.getStatus().valor);

        if (objTipado.getEscolaAlocada() != null) {
            comando.setInt(5, objTipado.getEscolaAlocada().getId());
        } else {
            comando.setNull(5, java.sql.Types.INTEGER);
        }

        comando.setDate(6, java.sql.Date.valueOf(objTipado.getDataCriada()));

        if (objTipado.getDataEnviada() != null) {
            comando.setDate(7, java.sql.Date.valueOf(objTipado.getDataEnviada()));
        } else {
            comando.setNull(7, java.sql.Types.DATE);
        }

        if (objTipado.getDataRecebida() != null) {
            comando.setDate(8, java.sql.Date.valueOf(objTipado.getDataRecebida()));
        } else {
            comando.setNull(8, java.sql.Types.DATE);
        }

        if (objTipado.getStatusMatricula() != null) {
            comando.setInt(9, objTipado.getStatusMatricula().valor);
        } else {
            comando.setNull(9, java.sql.Types.INTEGER);
        }
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Inscricao objTipado = (Inscricao) obj;

        comando.setInt(10, obj.getId());
    }

    protected static String INSERT = "insert into inscricao (id_escola_original, id_inscricao, id_dados, status, id_escola_alocada, dataCriada, dataEnviada, dataRecebida, statusMatricula) values(?,?,?,?,?,?,?,?,?)";
    protected static String UPDATE = "update inscricao set id_escola_original = ?, id_inscricao = ?, id_dados = ?, status = ?, id_escola_alocada = ?, dataCriada = ?, dataEnviada = ?, dataRecebida = ?, statusMatricula = ? where id = ?";
    protected static String DELETE = "delete from inscricao where id = ?";
    protected static String SELECT_ALL = "select * from inscricao where " + ATIVA;
    protected static String SELECT = "select * from inscricao where id = ? and " + ATIVA;

    public int cadastrar(Inscricao obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Inscricao obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public boolean excluirLogico(int id) {
        Inscricao i = (Inscricao) consultarGenerico(SELECT, id);
        i.setStatus(StatusInscricao.Cancelada);
        return atualizar(i);
    }

    public boolean completar(int id) {
        Inscricao i = (Inscricao) consultarGenerico(SELECT, id);
        i.setStatus(StatusInscricao.Completa);
        return atualizar(i);
    }

    public List<Inscricao> listar() {
        return (List<Inscricao>) listarGenerico(SELECT_ALL);
    }

    public Inscricao consultar(int id) {
        return (Inscricao) consultarGenerico(SELECT, id);
    }

    protected static String SELECT_ABERTA = "select * from inscricao where status = " + StatusInscricao.Aberta.valor; //+ " and " + ATIVA;

    //é usado para envio de inscrições
    public List<Inscricao> listarAbertasCompletas() {
        //Não usar consulta simples no envio de inscrições
        return (List<Inscricao>) listarGenerico(SELECT_ABERTA);
    }
    
    //é usado apenas para browser
    public List<Inscricao> listarAbertas() {
        //Usar consulta simples para exibição de página
        return (List<Inscricao>) listarGenerico(SELECT_ABERTA, null, true);
    }

    protected static String SELECT_ENVIADA_UE = "select * from inscricao where status = " + StatusInscricao.EnviadaUE.valor + " and " + ATIVA;

    public List<Inscricao> listarEnviadasUE() {
        return (List<Inscricao>) listarGenerico(SELECT_ENVIADA_UE);
    }

    protected static String SELECT_NAO_ALOCADA = "select * from inscricao where status = " + StatusInscricao.RecebidaSME.valor + " and " + ATIVA;

    public List<Inscricao> listarNaoAlocadas() {
        return (List<Inscricao>) listarGenerico(SELECT_NAO_ALOCADA);
    }

    protected static String SELECT_ALOCADA = "select * from inscricao where status = " + StatusInscricao.Alocada.valor + " and " + ATIVA;

    public List<Inscricao> listarAlocadas() {
        return (List<Inscricao>) listarGenerico(SELECT_ALOCADA);
    }

    protected static String SELECT_ENVIADA_SME = "select * from inscricao where status = " + StatusInscricao.EnviadaSME.valor + " and " + ATIVA;

    public List<Inscricao> listarEnviadasSME() {
        return (List<Inscricao>) listarGenerico(SELECT_ENVIADA_SME);
    }

    protected static String SELECT_RECEBIDA_UE = "select * from inscricao where status = " + StatusInscricao.RecebidaUE.valor + " and " + ATIVA;

    //é usado apenas para browser
    public List<Inscricao> listarRecebidasUE() {
        //Usar consulta simples
        return (List<Inscricao>) listarGenerico(SELECT_RECEBIDA_UE, null, true);
    }

    protected static String SELECT_ARQUIVADA = "select * from inscricao where status = " + StatusInscricao.Arquivada.valor + " and " + ATIVA;

    //é usado apenas para browser
    public List<Inscricao> listarArquivadas() {
        //Usar consulta simples
        return (List<Inscricao>) listarGenerico(SELECT_ARQUIVADA, null, true);
    }

    protected static String SELECT_PENDENTE = "select * from inscricao where status in (" + StatusInscricao.RecebidaSME.valor + "," + StatusInscricao.Alocada.valor + "," + StatusInscricao.EnviadaSME.valor + ") and " + ATIVA;

    //é usado apenas para browser
    public List<Inscricao> listarPendentes() {
        //Usar consulta simples
        return (List<Inscricao>) listarGenerico(SELECT_PENDENTE, null, true);
    }

    protected static String SELECT_IDINSCRICAO = "select * from inscricao where id_inscricao = ? and " + ATIVA;

    public Inscricao consultarIdInscricao(int id_inscricao) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_inscricao);
                } catch (Exception e) {

                }
            }
        };
        return (Inscricao) consultarGenerico(SELECT_IDINSCRICAO, param);

    }

    protected static String SELECT_UNICA = "select * from inscricao where id_escola_original = ? and id_inscricao = ? and " + ATIVA;

    public Inscricao consultarUnica(int idEscolaOriginal, int idInscricao) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, idEscolaOriginal);
                    comando.setInt(2, idInscricao);
                } catch (Exception e) {

                }
            }
        };
        return (Inscricao) consultarGenerico(SELECT_UNICA, param);

    }

}
