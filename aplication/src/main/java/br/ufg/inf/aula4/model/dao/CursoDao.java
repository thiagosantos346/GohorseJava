package br.ufg.inf.aula4.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.aula4.app.DB;
import br.ufg.inf.aula4.ctrl.exception.AlunoException;
import br.ufg.inf.aula4.model.entities.Curso;

public class CursoDao {
  	// CREATE
	public Curso inserir(Curso curso) throws AlunoException {
		
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			st = (PreparedStatement) conn.prepareStatement(
					"INSERT INTO tb_curso (nm_curso) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS
      );
			st.setString(1, curso.getNmCurso());
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);
			if (rowsAffected > 0) {

				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					curso.setIdCurso(id);
				}
			}
		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return curso;
	}

	// READ
	public List<Curso> buscaTodos() throws AlunoException {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Curso> cursos = new ArrayList<Curso>();
		try {
			Connection conn = DB.getConnection();
			st = conn.prepareStatement("SELECT id_curso, nm_curso FROM tb_curso order by;");
			rs = st.executeQuery();

			while (rs.next()) {
				Curso curso = new Curso();
				curso.setIdCurso(rs.getInt("id_curso"));
				curso.setNmCurso(rs.getString("nm_curso"));
				cursos.add(curso);
			}

		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}

		return cursos;
	}

	public Curso buscaPorId(Integer id) throws AlunoException {
		Curso curso = new Curso();

		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			st = conn.prepareStatement(" SELECT id_curso, nm_curso FROM tb_curso WHERE id_curso = ? ");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				curso.setIdCurso(rs.getInt("id_curso"));
				curso.setNmCurso(rs.getString("nm_curso"));
			}

		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}

		return curso;
	}

	// UPDATE

	public Curso alterar(Curso curso) throws AlunoException {

		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			
			String query = "UPDATE tb_curso "
					+ " SET "
					+ " nm_curso = ? , "
					+ " WHERE id_curso = ? ; ";
			st = (PreparedStatement) conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, curso.getNmCurso());

			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		return curso;
	}

	// DELETE

	public void excluir(Integer id) throws AlunoException {
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			
			String query = " DELETE FROM tb_curso WHERE id_curso = ? ; ";
			st = (PreparedStatement) conn.prepareStatement(query);
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

		} catch (SQLException e) {
			throw new AlunoException(e.getMessage());
		}
		
	}
}
