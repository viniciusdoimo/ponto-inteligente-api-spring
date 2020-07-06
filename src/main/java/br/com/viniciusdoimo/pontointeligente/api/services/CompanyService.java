package br.com.viniciusdoimo.pontointeligente.api.services;

import java.util.Optional;

import br.com.viniciusdoimo.pontointeligente.api.entities.Company;

public interface CompanyService {

	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Company> searchByCnpj(String cnpj);

	/**
	 * Cadastra uma nova empresa na base de dados.
	 * 
	 * @param company
	 * @return Company
	 */
	Company persist(Company company);

}
