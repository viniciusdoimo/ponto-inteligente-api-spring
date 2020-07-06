package br.com.viniciusdoimo.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusdoimo.pontointeligente.api.dtos.RegistrationPJDto;
import br.com.viniciusdoimo.pontointeligente.api.entities.Company;
import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.enums.ProfileEnum;
import br.com.viniciusdoimo.pontointeligente.api.response.Response;
import br.com.viniciusdoimo.pontointeligente.api.services.CompanyService;
import br.com.viniciusdoimo.pontointeligente.api.services.EmployeeService;
import br.com.viniciusdoimo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class RegistrationPJController {

	private static final Logger log = LoggerFactory.getLogger(RegistrationPJController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CompanyService companyService;

	public RegistrationPJController() {
	}

	/**
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param RegistrationPJDto
	 * @param result
	 * @return ResponseEntity<Response<RegistrationPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<RegistrationPJDto>> registration(@Valid @RequestBody RegistrationPJDto registrationPJDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PJ: {}", registrationPJDto.toString());
		Response<RegistrationPJDto> response = new Response<RegistrationPJDto>();

		validateExistingData(registrationPJDto, result);
		Company company = this.convertDtoToCompany(registrationPJDto);
		Employee employee = this.convertDtoToEmployee(registrationPJDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.companyService.persist(company);
		employee.setCompany(company);
		this.employeeService.persist(employee);

		response.setData(this.convertRegisterPJDto(employee));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param registrationPJDto
	 * @param result
	 */
	private void validateExistingData(RegistrationPJDto registrationPJDto, BindingResult result) {
		this.companyService.searchByCnpj(registrationPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

		this.employeeService.searchByCpf(registrationPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.employeeService.searchByEmail(registrationPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para empresa.
	 * 
	 * @param RegistrationPJDto
	 * @return Company
	 */
	private Company convertDtoToCompany(RegistrationPJDto registrationPJDto) {
		Company company = new Company();
		company.setCnpj(registrationPJDto.getCnpj());
		company.setCompanyName(registrationPJDto.getCompanyName());

		return company;
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param RegistrationPJDto
	 * @param result
	 * @return Employee
	 * @throws NoSuchAlgorithmException
	 */
	private Employee convertDtoToEmployee(RegistrationPJDto registrationPJDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Employee employee = new Employee();
		employee.setName(registrationPJDto.getName());
		employee.setEmail(registrationPJDto.getEmail());
		employee.setCpf(registrationPJDto.getCpf());
		employee.setProfile(ProfileEnum.ROLE_ADMIN);
		employee.setPassword(PasswordUtils.generateBCrypt(registrationPJDto.getPassword()));

		return employee;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param Employee
	 * @return RegistrationPJDto
	 */
	private RegistrationPJDto convertRegisterPJDto(Employee employee) {
		RegistrationPJDto registrationPJDto = new RegistrationPJDto();
		registrationPJDto.setId(employee.getId());
		registrationPJDto.setName(employee.getName());
		registrationPJDto.setEmail(employee.getEmail());
		registrationPJDto.setCpf(employee.getCpf());
		registrationPJDto.setCompanyName(employee.getCompany().getCompanyName());
		registrationPJDto.setCnpj(employee.getCompany().getCnpj());

		return registrationPJDto;
	}

}
