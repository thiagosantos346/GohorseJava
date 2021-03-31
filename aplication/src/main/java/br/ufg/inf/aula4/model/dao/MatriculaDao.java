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
import br.ufg.inf.aula4.ctrl.exception.MatriculaException;
import br.ufg.inf.aula4.model.entities.Aluno;
import br.ufg.inf.aula4.model.entities.Curso;
import br.ufg.inf.aula4.model.entities.Disciplina;
import br.ufg.inf.aula4.model.entities.Matricula;
import br.ufg.inf.aula4.model.entities.Oferta;
import br.ufg.inf.aula4.model.entities.Pessoa;
import br.ufg.inf.aula4.model.entities.Professor;
import br.ufg.inf.aula4.model.enums.Dia;
import br.ufg.inf.aula4.model.enums.Escolaridade;

public class MatriculaDao {
  	// CREATE
	public Matricula inserir(Matricula matricula) throws MatriculaException {
		
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			st = (PreparedStatement) conn.prepareStatement(
					"INSERT INTO tb_matricula (id_aluno, id_oferta) VALUES(?, ?);",
					Statement.RETURN_GENERATED_KEYS
      );

			st.setInt(1, matricula.getAluno().getIdAluno());
			st.setInt(2, matricula.getOferta().getIdOferta());
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

			if (rowsAffected > 0) {

				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					matricula.setIdMatricula(id);
				}
			}
		} catch (SQLException e) {
			throw new MatriculaException(e.getMessage());
		}

		return matricula;
	}

  
  private Matricula factoryMatricula(ResultSet rs) throws SQLException {
    Matricula matricula = new Matricula();

    matricula.setIdMatricula((Integer)rs.getInt("tmt_id_matricula"));

    matricula.setOferta(
      new Oferta(
        (Integer) rs.getInt("id_oferta"),
        new Professor(
          (Integer)rs.getInt("tpr_id_professor"),
          new Pessoa(
            (Integer)rs.getInt("tpp_id_pessoa"),
            rs.getString("tpp_nm_pessoa"),
            rs.getString("tpp_cpf"),
            rs.getDate("tpp_dt_nascimento")
          ),
          Escolaridade.get(rs.getInt("tpr_escolaridade"))
        ),
        new Disciplina(
          (Integer)rs.getInt("tds_id_disciplina"),
          rs.getString("tds_nm_disciplina"),
          (Integer)rs.getInt("tds_carga_horaria")
        ),
        rs.getDate("tof_dt_inicio"),
        rs.getDate("tof_dt_fim"),
        Dia.get(rs.getInt("tof_dia")),
        rs.getString("tof_hora") 
      )
    ); 

    matricula.setAluno(
      new Aluno( 
        (Integer)rs.getInt("ta_id_aluno"),
        rs.getDate("ta_dt_inicio"),
        rs.getBoolean("ta_ativo"),
        new Pessoa(
          (Integer)rs.getInt("tpa_id_pessoa"),
          rs.getString("tpa_nm_pessoa"),
          rs.getString("tpa_cpf"),
          rs.getDate("tpa_dt_nascimento")              
        ),
        new Curso(
          rs.getInt("tcr_id_curso"),
          rs.getString("tcr_nm_curso ")
        )
      ) 
    );

    return matricula;
  }

	// READ
	public List<Matricula> buscaTodos() throws MatriculaException {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Matricula> matriculas = new ArrayList<Matricula>();
		try {
			Connection conn = DB.getConnection();
			st = conn.prepareStatement( this.getStrQueryFindAll() );
			rs = st.executeQuery();

			while (rs.next()) {
				matriculas.add(
          this.factoryMatricula(rs)
        );
			}

		} catch (SQLException e) {
			throw new MatriculaException(e.getMessage());

		}

		return matriculas;
	}

 
	public Matricula buscaPorId(Integer id) throws MatriculaException {
		Matricula matricula = new Matricula();

		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			st = conn.prepareStatement(this.getStrQueryFindOne());
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
        this.factoryMatricula(rs);
			}

		} catch (SQLException e) {
			throw new MatriculaException(e.getMessage());
		}

		return matricula;
	}

	// UPDATE

	public Matricula alterar(Matricula matricula) throws MatriculaException {

		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			
			String query = "UPDATE tb_matricula SET id_aluno=?, id_oferta=? WHERE id_matricula=?;";

			st = (PreparedStatement) conn.prepareStatement(
        query,
        Statement.RETURN_GENERATED_KEYS
      );
    
			st.setInt(1, matricula.getAluno().getIdAluno() );
			st.setInt(2, matricula.getOferta().getIdOferta() ) ;
			st.setInt(3, matricula.getIdMatricula());
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

		} catch (SQLException e) {
			throw new MatriculaException(e.getMessage());
		}
		
		return matricula;
	}

	// DELETE

	public void excluir(Integer id) throws MatriculaException {
		PreparedStatement st = null;
		try {
			Connection conn = DB.getConnection();
			
			String query = " DELETE FROM tb_matricula WHERE id_matricula = ? ; ";
			st = (PreparedStatement) conn.prepareStatement(query);
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			System.out.println("Linhas alteradas: " + rowsAffected);

		} catch (SQLException e) {
			throw new MatriculaException(e.getMessage());
		}
		
	}

  // querys 

  private String getStrQueryFindAll(){
    return "SELECT "+
    "  tmt.id_matricula     AS tmt_id_matricula, "+
    "  tmt.id_oferta        AS tmt_id_oferta, "+
    "  tmt.id_aluno         AS tmt_id_aluno, "+
    "  tof.id_oferta        AS tof_id_oferta, "+
    "  tof.id_disciplina    AS tof_id_disciplina, "+
    "  tof.id_professor     AS tof_id_professor, "+
    "  tof.dt_inicio        AS tof_dt_inicio, "+
    "  tof.dt_fim           AS tof_dt_fim, "+
    "  tof.dia              AS tof_dia, "+
    "  tof.hora             AS tof_hora, "+
    "  tds.carga_horaria    AS tds_carga_horaria, "+
    "  tds.id_disciplina    AS tds_id_disciplina, "+
    "  tds.nm_disciplina    AS tds_nm_disciplina, "+
    "  tpr.escolaridade     AS tpr_escolaridade, "+
    "  tpr.id_pessoa        AS tpr_id_pessoa, "+
    "  tpr.id_professor     AS tpr_id_professor, "+
    "  tpp.cpf              AS tpp_cpf, "+
    "  tpp.dt_nascimento    AS tpp_dt_nascimento, "+
    "  tpp.id_pessoa        AS tpp_id_pessoa, "+
    "  tpp.nm_pessoa        AS tpp_nm_pessoa, "+
    "  tln.ativo            AS ta_ativo, "+
    "  tln.dt_inicio        AS ta_dt_inicio, "+
    "  tln.id_aluno         AS ta_id_aluno, "+
    "  tln.id_curso         AS ta_id_curso, "+
    "  tln.id_pessoa        AS ta_id_pessoa, "+
    "  tpa.cpf              AS tpa_cpf, "+
    "  tpa.dt_nascimento    AS tpa_dt_nascimento, "+
    "  tpa.id_pessoa        AS tpa_id_pessoa, "+
    "  tpa.nm_pessoa        AS tpa_nm_pessoa "+
    "  tcr.id_curso         AS tcr_id_curso, "+
    "  tcr.nm_curso         AS tcr_nm_curso "+
    "FROM "+
    "tb_matricula tmt "+
    "inner JOIN tb_aluno      tln ON tln.id_aluno      = tmt.id_aluno "+
    "INNER JOIN tb_oferta     tof ON tof.id_oferta     = tmt.id_oferta "+
    "INNER JOIN tb_disciplina tds ON tof.id_disciplina = tds.id_disciplina "+
    "INNER JOIN tb_professor  tpr ON tof.id_professor  = tpr.id_professor"+
    "INNER JOIN tb_pessoa     tpp ON tpp.id_pessoa     = tpr.id_pessoa "+
    "INNER JOIN tb_pessoa     tpa ON tpa.id_pessoa     = tln.id_pessoa "+
    "INNER JOIN tb_curso      tcr ON tcr.id_curso      = tln .id_curso "+
    "ORDER  BY "+
    "  tmt.id_matricula, "+
    "  tmt.id_oferta, "+
    "  tmt.id_aluno; ";
  }

  private String getStrQueryFindOne(){
    return "SELECT "+
    "  tmt.id_matricula     AS tmt_id_matricula, "+
    "  tmt.id_oferta        AS tmt_id_oferta, "+
    "  tmt.id_aluno         AS tmt_id_aluno, "+
    "  tof.id_oferta        AS tof_id_oferta, "+
    "  tof.id_disciplina    AS tof_id_disciplina, "+
    "  tof.id_professor     AS tof_id_professor, "+
    "  tof.dt_inicio        AS tof_dt_inicio, "+
    "  tof.dt_fim           AS tof_dt_fim, "+
    "  tof.dia              AS tof_dia, "+
    "  tof.hora             AS tof_hora, "+
    "  tds.carga_horaria    AS tds_carga_horaria, "+
    "  tds.id_disciplina    AS tds_id_disciplina, "+
    "  tds.nm_disciplina    AS tds_nm_disciplina, "+
    "  tpr.escolaridade     AS tpr_escolaridade, "+
    "  tpr.id_pessoa        AS tpr_id_pessoa, "+
    "  tpr.id_professor     AS tpr_id_professor, "+
    "  tpp.cpf              AS tpp_cpf, "+
    "  tpp.dt_nascimento    AS tpp_dt_nascimento, "+
    "  tpp.id_pessoa        AS tpp_id_pessoa, "+
    "  tpp.nm_pessoa        AS tpp_nm_pessoa, "+
    "  tln.ativo            AS ta_ativo, "+
    "  tln.dt_inicio        AS ta_dt_inicio, "+
    "  tln.id_aluno         AS ta_id_aluno, "+
    "  tln.id_curso         AS ta_id_curso, "+
    "  tln.id_pessoa        AS ta_id_pessoa, "+
    "  tpa.cpf              AS tpa_cpf, "+
    "  tpa.dt_nascimento    AS tpa_dt_nascimento, "+
    "  tpa.id_pessoa        AS tpa_id_pessoa, "+
    "  tpa.nm_pessoa        AS tpa_nm_pessoa "+
    "  tcr.id_curso         AS tcr_id_curso, "+
    "  tcr.nm_curso         AS tcr_nm_curso "+
    "FROM "+
    "tb_matricula tmt "+
    "inner JOIN tb_aluno      tln ON tln.id_aluno      = tmt.id_aluno "+
    "INNER JOIN tb_oferta     tof ON tof.id_oferta     = tmt.id_oferta "+
    "INNER JOIN tb_disciplina tds ON tof.id_disciplina = tds.id_disciplina "+
    "INNER JOIN tb_professor  tpr ON tof.id_professor  = tpr.id_professor"+
    "INNER JOIN tb_pessoa     tpp ON tpp.id_pessoa     = tpr.id_pessoa "+
    "INNER JOIN tb_pessoa     tpa ON tpa.id_pessoa     = tln.id_pessoa "+
    "INNER JOIN tb_curso      tcr ON tcr.id_curso      = tln .id_curso "+
    "WHERE tmt.id_matricula = ?";
  }

}
