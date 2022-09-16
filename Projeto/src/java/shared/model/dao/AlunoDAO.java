/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.Aluno;
import shared.model.StatusAluno;

/**
 *
 * @author leona
 */
public class AlunoDAO extends DAOGenerico{
    
    public AlunoDAO() {
        super();
    }

    public AlunoDAO(Connection conManual) {
        super(conManual);
    }
    
    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Aluno objTipado = (Aluno) obj;

        //comando.setInt(1, objTipado.getRm());
        comando.setInt(1, objTipado.getStatus().valor);
        comando.setInt(2, objTipado.getDados().getId());
        
        comando.setInt(3, objTipado.getIdInscricao());
        comando.setInt(4, objTipado.getIdEscolaOriginal());
        
        if (objTipado.getIdTurma() != 0) {
            comando.setInt(5, objTipado.getIdTurma());
        } else {
            comando.setNull(5, java.sql.Types.INTEGER);
        }

    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(6, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Aluno obj = new Aluno(resultado.getInt("id"));

        obj.setRm(resultado.getInt("rm"));
        obj.setStatus(StatusAluno.fromInteger(resultado.getInt("status")));
        
        DadosAlunoDAO dDAO = new DadosAlunoDAO();
        obj.setDados(dDAO.consultar(resultado.getInt("id_dados")));
        
        obj.setIdInscricao(resultado.getInt("id_inscricao"));
        obj.setIdEscolaOriginal(resultado.getInt("id_escola_original"));
        
        obj.setIdTurma(resultado.getInt("id_turma"));
        
        return obj;
    }
    
    protected final static String ATIVO = " status = " + StatusAluno.Matriculado.valor;

    protected static String INSERT = "insert into aluno (status, id_dados, id_inscricao, id_escola_original, id_turma) values(?,?,?,?,?)";
    protected static String UPDATE = "update aluno set status = ?, id_dados = ?, id_inscricao = ?, id_escola_original = ?, id_turma = ? where id = ?";
    protected static String DELETE = "delete from aluno where id = ?";
    protected static String SELECT_ALL = "select * from aluno where " + ATIVO;
    protected static String SELECT = "select * from aluno where id = ? and " + ATIVO;

    public int cadastrar(Aluno obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Aluno obj){
        return atualizarGenerico(obj, UPDATE);
    }
    
    public boolean excluirLogico(int id){
        Aluno a =(Aluno) consultarGenerico(SELECT, id);
        a.setStatus(StatusAluno.Cancelado);
        return atualizar(a);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<Aluno> listar(){
        return (List<Aluno>) listarGenerico(SELECT_ALL);
    }

    public Aluno consultar(int id){
        return (Aluno) consultarGenerico(SELECT, id);
    }
    
    /*
    protected static String SELECT_PENDENTE = "select * from aluno where status = " + StatusAluno.Pendente.valor;

    public List<Aluno> listarPendentes() {
        return (List<Aluno>) listarGenerico(SELECT_PENDENTE);
    }
    */
    protected static String SELECT_NAO_ALOCADO = "select * from aluno where id_turma is null and " + ATIVO;

    public List<Aluno> listarNaoAlocados() {
        return (List<Aluno>) listarGenerico(SELECT_NAO_ALOCADO);
    }
    
    protected static String SELECT_TURMA = "select * from aluno where id_turma = ? and " + ATIVO;
    
    public List<Aluno> listarPorTurma(int id_turma){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_turma);
                } catch (Exception e) {

                }
            }
        };
        return (List<Aluno>) listarGenerico(SELECT_TURMA, param);

    }
    
    protected static String COUNT_TURMA = "select count(id_turma) from aluno where id_turma = ? and " + ATIVO;
    
    public int contarTamanhoTurma(int id_turma){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_turma);
                } catch (Exception e) {

                }
            }
        };
        return contarGenerico(COUNT_TURMA, param);

    }
    
    //SELECT count(id_turma) FROM aluno where id_turma is not null group by id_turma order by id_turma;
    //SELECT count(id_turma) FROM aluno where id_turma = ?;
}
