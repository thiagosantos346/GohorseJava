package br.ufg.inf.aula4.ctrl.negocio;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.aula4.ctrl.exception.DisciplinaException;
import br.ufg.inf.aula4.ctrl.exception.OfertaException;
import br.ufg.inf.aula4.ctrl.exception.ProfessorExection;
import br.ufg.inf.aula4.model.dao.OfertaDAO;
import br.ufg.inf.aula4.model.entities.Oferta;

public class OfertaNegocio {


	OfertaDAO dao = new OfertaDAO();
	ProfessorNegocio professorNegocio = new ProfessorNegocio();
	DisciplinaNegocio disciplinaNegocio = new DisciplinaNegocio();

	public Oferta inserir(Oferta oferta) throws OfertaException {
		this.validarOferta(oferta);
		try{
			dao.inserir(oferta);
		}catch(Exception e){
			throw new OfertaException(e.getMessage());
		}
		return oferta;
	}
	
	// READ
	public List<Oferta> buscaTodos() throws OfertaException, ProfessorExection, DisciplinaException{
		
		List<Oferta> ofertas =  new ArrayList<Oferta>();
		
		try {
			for(Oferta o : dao.buscaTodos()) {
				o.setProfessor(professorNegocio.buscaPorId(o.getProfessor().getIdProfessor()));
				o.setDisciplina(disciplinaNegocio.buscaPorId(o.getDisciplina().getIdDisciplina()));
			}	
		} catch (OfertaException e) {
			throw new OfertaException(e.getMessage());
		} catch (ProfessorExection e){
			throw new ProfessorExection(e.getMessage());
		} catch ( DisciplinaException e ){
			throw new DisciplinaException(e.getMessage());
		}
		
		return ofertas;	
	}
	
	public Oferta buscaPorId(Integer id) throws ProfessorExection, DisciplinaException {
		Oferta oferta = new Oferta();
		try{
			oferta.setProfessor(professorNegocio.buscaPorId(oferta.getProfessor().getIdProfessor()));
			oferta.setDisciplina(disciplinaNegocio.buscaPorId(oferta.getDisciplina().getIdDisciplina()));
		} catch (ProfessorExection e){
			throw new ProfessorExection(e.getMessage());
		} catch ( DisciplinaException e ){
			throw new DisciplinaException(e.getMessage());
		}
		return oferta;
	}
	
	
	// UPDATE
	
	public Oferta alterar(Oferta oferta) throws OfertaException {		
		try{
			this.validarOferta(oferta);
			return dao.alterar(oferta);
		}catch(Exception e){
			throw new OfertaException(e.getMessage());
		}
		
	}
	
	// DELETE
	
	public void excluir(Integer id) throws OfertaException {
		try {
			dao.excluir(id);	
		} catch (Exception e) {
			throw new OfertaException(e.getMessage());
		}
		
	}
	
	private void validarOferta(Oferta oferta) throws OfertaException {

	}
}
