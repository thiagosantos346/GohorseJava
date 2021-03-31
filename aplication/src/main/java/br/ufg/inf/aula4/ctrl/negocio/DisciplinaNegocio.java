package br.ufg.inf.aula4.ctrl.negocio;

import java.util.List;

import br.ufg.inf.aula4.ctrl.exception.DisciplinaException;
import br.ufg.inf.aula4.model.dao.DisciplinaDAO;
import br.ufg.inf.aula4.model.entities.Disciplina;

public class DisciplinaNegocio {


		DisciplinaDAO dao = new DisciplinaDAO();
	
		public Disciplina inserir(Disciplina disciplina) throws DisciplinaException {
			this.validarDisciplina(disciplina);
			try{
				dao.inserir(disciplina);
			}catch(Exception e){
				throw new DisciplinaException(e.getMessage());
			}
			return disciplina;
		}
		
		// READ
		public List<Disciplina> buscaTodos() throws DisciplinaException{
			try{
				return dao.buscaTodos();	
			}catch(Exception e){
				throw new DisciplinaException(e.getMessage());
			}
		}
		
		public Disciplina buscaPorId(Integer id) throws DisciplinaException {
			try {
				return dao.buscaPorId(id);	
			} catch (Exception e) {
				throw new DisciplinaException(e.getMessage());
			}
		}
		
		
		// UPDATE
		
		public Disciplina alterar(Disciplina disciplina) throws DisciplinaException {		
			try {
				this.validarDisciplina(disciplina);
				return dao.alterar(disciplina);
			} catch (Exception e) {
				throw new DisciplinaException(e.getMessage());
			}
		}
		
		// DELETE
		
		public void excluir(Integer id) throws DisciplinaException {
			try {
				dao.excluir(id);
			} catch (Exception e) {
				throw new DisciplinaException(e.getMessage());
			} 
		}
		
		private void validarDisciplina(Disciplina disciplina) throws DisciplinaException {
			if (disciplina.getCargaHoraria() <= 0) {
				throw new DisciplinaException("Carga horária deve ter maior que 0.");
			}

			if (disciplina.getNmDisciplina() == null || disciplina.getNmDisciplina().length() == 0) {
				throw new DisciplinaException("Nome da disciplina é obrigatório.");
			}
		}
}
