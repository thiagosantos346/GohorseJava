package br.ufg.inf.aula4;

import br.ufg.inf.aula4.app.TesteApp;
import br.ufg.inf.aula4.ctrl.DisciplinaCtrl;
import br.ufg.inf.aula4.ctrl.OfertaCtrl;
import br.ufg.inf.aula4.ctrl.PessoaCtrl;
import br.ufg.inf.aula4.ctrl.ProfessorCtrl;

public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program. 
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

		TesteApp testeApp = new TesteApp();
		
		//testeApp.testeCrudDisciplina(new DisciplinaCtrl());
		//testeApp.testeCrudPessoa(new PessoaCtrl());
		//testeApp.testeCrudProfessor(new ProfessorCtrl(), new PessoaCtrl());
		testeApp.testeCrudOferta(new OfertaCtrl(), new DisciplinaCtrl(), new ProfessorCtrl());
    }
}
