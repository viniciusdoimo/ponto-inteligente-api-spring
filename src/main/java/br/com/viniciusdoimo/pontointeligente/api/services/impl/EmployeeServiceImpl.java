package br.com.viniciusdoimo.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.repositories.EmployeeRepository;
import br.com.viniciusdoimo.pontointeligente.api.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Employee persist(Employee employee) {
		log.info("Persistindo funcionário: {}", employee);
		return this.employeeRepository.save(employee);
	}
	
	public Optional<Employee> searchByCpf(String cpf) {
		log.info("Buscando funcionário pelo CPF {}", cpf);
		return Optional.ofNullable(this.employeeRepository.findByCpf(cpf));
	}
	
	public Optional<Employee> searchByEmail(String email) {
		log.info("Buscando funcionário pelo email {}", email);
		return Optional.ofNullable(this.employeeRepository.findByEmail(email));
	}
	
	public Optional<Employee> searchById(Long id) {
		log.info("Buscando funcionário pelo IDl {}", id);
		return this.employeeRepository.findById(id);
	}

}
