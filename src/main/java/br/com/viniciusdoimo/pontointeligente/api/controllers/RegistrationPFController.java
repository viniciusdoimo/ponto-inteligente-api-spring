package br.com.viniciusdoimo.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import br.com.viniciusdoimo.pontointeligente.api.dtos.RegistrationPFDto;
import br.com.viniciusdoimo.pontointeligente.api.entities.Company;
import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.enums.ProfileEnum;
import br.com.viniciusdoimo.pontointeligente.api.response.Response;
import br.com.viniciusdoimo.pontointeligente.api.services.CompanyService;
import br.com.viniciusdoimo.pontointeligente.api.services.EmployeeService;
import br.com.viniciusdoimo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class RegistrationPFController {

	private static final Logger log = LoggerFactory.getLogger(RegistrationPFController.class);
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EmployeeService employeeService;

	public RegistrationPFController() {
	}

	/**
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param RegistrationPFDto
	 * @param result
	 * @return ResponseEntity<Response<RegistrationPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<RegistrationPFDto>> registration(@Valid @RequestBody RegistrationPFDto registrationPFDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", registrationPFDto.toString());
		Response<RegistrationPFDto> response = new Response<RegistrationPFDto>();

		validateExistingData(registrationPFDto, result);
		Employee employee = this.convertDtotoEmployee(registrationPFDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Company> company = this.companyService.searchByCnpj(registrationPFDto.getCnpj());
		company.ifPresent(emp -> employee.setCompany(emp));
		this.employeeService.persist(employee);

		response.setData(this.convertRegistrationPFDto(employee));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funcionário não existe na base de dados.
	 * 
	 * @param RegistrationPFDto
	 * @param result
	 */
	private void validateExistingData(RegistrationPFDto registrationPFDto, BindingResult result) {
		Optional<Company> company = this.companyService.searchByCnpj(registrationPFDto.getCnpj());
		if (!company.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.employeeService.searchByCpf(registrationPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.employeeService.searchByEmail(registrationPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param RegistrationPFDto
	 * @param result
	 * @return Employee
	 * @throws NoSuchAlgorithmException
	 */
	private Employee convertDtotoEmployee(RegistrationPFDto registrationPFDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Employee employee = new Employee();
		employee.setName(registrationPFDto.getName());
		employee.setEmail(registrationPFDto.getEmail());
		employee.setCpf(registrationPFDto.getCpf());
		employee.setProfile(ProfileEnum.ROLE_USER);
		employee.setPassword(PasswordUtils.generateBCrypt(registrationPFDto.getPassword()));
		registrationPFDto.getHoursLunch()
				.ifPresent(qtdHorasAlmoco -> employee.setHoursLunch(Float.valueOf(qtdHorasAlmoco)));
		registrationPFDto.getHoursWorkedDay()
				.ifPresent(qtdHorasTrabDia -> employee.setHoursWorkedDay(Float.valueOf(qtdHorasTrabDia)));
		registrationPFDto.getHourValue().ifPresent(valorHora -> employee.setHourValue(new BigDecimal(valorHora)));

		return employee;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param Employee
	 * @return RegistrationPFDto
	 */
	private RegistrationPFDto convertRegistrationPFDto(Employee employee) {
		RegistrationPFDto registrationPFDto = new RegistrationPFDto();
		registrationPFDto.setId(employee.getId());
		registrationPFDto.setName(employee.getName());
		registrationPFDto.setEmail(employee.getEmail());
		registrationPFDto.setCpf(employee.getCpf());
		registrationPFDto.setCnpj(employee.getCompany().getCnpj());
		employee.getHourValueOptinal().ifPresent(hoursLunch -> registrationPFDto
				.setHoursLunch(Optional.of(hoursLunch.toString())));
		employee.getHoursWorkedDayOptinal().ifPresent(
				qtdHorasTrabDia -> registrationPFDto.setHoursWorkedDay(Optional.of(Float.toString(qtdHorasTrabDia))));
		employee.getHoursLunchOptinal()
				.ifPresent(valorHora -> registrationPFDto.setHoursLunch(Optional.of(valorHora.toString())));

		return registrationPFDto;
	}

}
