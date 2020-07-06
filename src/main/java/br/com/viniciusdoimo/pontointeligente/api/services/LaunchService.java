package br.com.viniciusdoimo.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;

public interface LaunchService {

	/**
	 * Retorna uma lista paginada de lançamentos de um determinado funcionário.
	 * 
	 * @param employee
	 * @param pageRequest
	 * @return Page<Launch>
	 */
	Page<Launch> searchByEmployeeId(Long employeeId, PageRequest pageRequest);
	
	/**
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return Optional<Launch>
	 */
	Optional<Launch> searchById(Long id);
	
	/**
	 * Persiste um lançamento na base de dados.
	 * 
	 * @param launch
	 * @return Launch
	 */
	Launch persist(Launch launch);
	
	/**
	 * Remove um lançamento da base de dados.
	 * 
	 * @param id
	 */
	void remove(Long id);
	
}
