package br.ufg.inf.aula4.model.entities;

import java.util.Date;

public class Pessoa {

	private Integer idPessoa;
	private String nmPessoa;
	private String cpf;
	private Date dtNascimento;
	

	public Pessoa(Integer idPessoa, String nmPessoa, String cpf, Date dtNascimento) {
		this.idPessoa = idPessoa;
		this.nmPessoa = nmPessoa;
		this.cpf = cpf;
		this.dtNascimento = dtNascimento;
	}

	public Pessoa() {
  }

  public Integer getIdPessoa() {
		return idPessoa;
	}
	
	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}
	
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Date getDtNascimento() {
		return dtNascimento;
	}
	
	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	
	@Override
	public String toString() {
		return "Pessoa [idPessoa=" + idPessoa + ", nmPessoa=" + nmPessoa + ", cpf=" + cpf + ", dtNascimento="
				+ dtNascimento + "]";
	}
	
}