package br.com.viniciusdoimo.pontointeligente.api.services;

import java.util.Optional;

import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;

public interface EmployeeService {
	
	/**
	 * Persiste um funcionário na base de dados.
	 * 
	 * @param employee
	 * @return Employee
	 */
	Employee persist(Employee employee);
	
	/**
	 * Busca e retorna um funcionário dado um CPF.
	 * 
	 * @param cpf
	 * @return Optional<Employee>
	 */
	Optional<Employee> searchByCpf(String cpf);
	
	/**
	 * Busca e retorna um funcionário dado um email.
	 * 
	 * @param email
	 * @return Optional<Employee>
	 */
	Optional<Employee> searchByEmail(String email);
	
	/**
	 * Busca e retorna um funcionário por ID.
	 * 
	 * @param id
	 * @return Optional<Employee>
	 */
	Optional<Employee> searchById(Long id);

}
