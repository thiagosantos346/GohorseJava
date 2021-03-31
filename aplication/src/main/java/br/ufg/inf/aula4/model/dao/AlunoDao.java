package br.ufg.inf.aula4.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.aula4.app.DB;
import br.ufg.inf.aula4.ctrl.exception.AlunoException;
import br.ufg.inf.aula4.model.entities.Pessoa;
import br.ufg.inf.aula4.model.entities.Aluno;
import br.ufg.inf.aula4.model.entities.Curso;

public class AlunoDao {

  public Aluno inserir(Aluno aluno) throws AlunoException {
		PreparedStatement st = null;
		try {

			Connection conn = DB.getConnection();

			st = (PreparedStatement) conn.prepareStatement(
					"INSERT INTO tb_aluno " + "(id_aluno, dt_inicio, ativo, id_pessoa, id_curso) " + "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
      );

			st.setInt(1, (Integer) aluno.getIdAluno());
      st.setDate(2, (Date) aluno.getDtInicio());
      st.setBoolean(3, (boolean) aluno.getAtivo());
      st.setInt(4, (Integer) aluno.getPessoa().getIdPessoa());
      st.setInt(5, (Integer) aluno.getCurso().getIdCurso());

			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);
			if (rowsAffected > 0) {

				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					aluno.setIdAluno(id);
				}
			}
		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return aluno;
	}

	public List<Aluno> buscaTodos() throws AlunoException {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Aluno> alunos = new ArrayList<Aluno>();
		try {
			Connection conn = DB.getConnection();
			String query = "SELECT a.id_aluno, a.dt_inicio, "+
                     "a.ativo, a.id_pessoa, a.id_curso, "+
                     " p.cpf, p.dt_nascimento, p.nm_pessoa, "+
                     " c.nm_curso FROM tb_aluno a NATURAL JOIN "+
                     "  tb_pessoa p LEFT JOIN tb_curso c ON "+
                     "a.id_curso = c.id_curso ORDER BY id_aluno"; 

			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				alunos.add(this.vo(rs));
			}
		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return alunos;
	}

	private Aluno vo(ResultSet rs) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setIdAluno(rs.getInt("id_aluno"));
    aluno.setAtivo(rs.getBoolean("ativo"));
    aluno.setDtInicio(rs.getDate("dt_inicio"));
    aluno.setPessoa(
      new Pessoa(
        (Integer) rs.getInt("id_pessoa"),
        (String) rs.getString("nm_pessoa"), 
        (String) rs.getString("cpf"),
        (Date) rs.getDate("dt_nascimento")
      )
    );
    aluno.setCurso(
      new Curso(
        rs.getInt("id_curso"),
        rs.getString("nm_curso")
      )
    );
    		
		return aluno;
	}

	public Aluno buscaPorId(Integer id) throws AlunoException {
		Aluno aluno = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			String query = "SELECT a.id_aluno, a.dt_inicio, "+
      "a.ativo, a.id_pessoa, a.id_curso, "+
      " p.cpf, p.dt_nascimento, p.nm_pessoa, "+
      " c.nm_curso FROM tb_aluno a NATURAL JOIN "+
      "  tb_pessoa p LEFT JOIN tb_curso c ON "+
      "a.id_curso = c.id_curso ORDER BY id_aluno"+
      " WHERE id_aluno = ? ";

			st = conn.prepareStatement(query);
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				aluno = this.vo(rs);
			}
		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return aluno;
	}

	public Aluno alterar(Aluno aluno) throws AlunoException {
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
      String query = "update tb_aluno a NATURAL JOIN tb_pessoa p LEFT JOIN tb_curso c ON   a.id_curso = c.id_curso"+
      "set "+
      " a.id_aluno=:id_aluno,"+
      " a.dt_inicio=:dt_inicio,"+
      " a.ativo=:ativo,"+
      " a.id_pessoa=:id_pessoa,"+
      " a.id_curso=:id_curso,"+
      " p.cpf=:cpf,"+
      " p.dt_nascimento=:dt_nascimento,"+
      " p.nm_pessoa=:nm_pessoa,"+
      " c.id_curso=:id_curso,"+
      " c.nm_curso=:nm_curso"+
      "WHERE id_aluno = :pId_aluno;";

			st = (PreparedStatement) conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      st.setInt(1, aluno.getIdAluno());
      st.setDate(2, (Date) aluno.getDtInicio());
      st.setBoolean(3, aluno.getAtivo());
      st.setInt(4, aluno.getPessoa().getIdPessoa());
      st.setInt(5, aluno.getCurso().getIdCurso());
      st.setString(6, aluno.getPessoa().getCpf());
      st.setDate(7, (Date)aluno.getPessoa().getDtNascimento());
      st.setString(8, aluno.getPessoa().getNmPessoa());
      st.setInt(9, aluno.getCurso().getIdCurso());
      st.setString(10, aluno.getCurso().getNmCurso());
      
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);
		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return aluno;
	}

	public void excluir(Integer id) throws AlunoException {
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			String query = " DELETE FROM tb_aluno WHERE id_aluno = ? ; ";
			st = (PreparedStatement) conn.prepareStatement(query);
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
	}
  
}
