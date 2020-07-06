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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusdoimo.pontointeligente.api.dtos.EmployeeDto;
import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.response.Response;
import br.com.viniciusdoimo.pontointeligente.api.services.EmployeeService;
import br.com.viniciusdoimo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	@Autowired
	private EmployeeService employeeService;

	public FuncionarioController() {
	}

	/**
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param employeeDto
	 * @param result
	 * @return ResponseEntity<Response<EmployeeDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<EmployeeDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", employeeDto.toString());
		Response<EmployeeDto> response = new Response<EmployeeDto>();

		Optional<Employee> employee = this.employeeService.searchById(id);
		if (!employee.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
		}

		this.updateDataEmployee(employee.get(), employeeDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando funcionário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.employeeService.persist(employee.get());
		response.setData(this.converterEmployeeDto(employee.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * 
	 * @param employee
	 * @param employeeDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void updateDataEmployee(Employee employee, EmployeeDto employeeDto, BindingResult result)
			throws NoSuchAlgorithmException {
		employee.setName(employeeDto.getName());

		if (!employee.getEmail().equals(employeeDto.getEmail())) {
			this.employeeService.searchByEmail(employeeDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			employee.setEmail(employeeDto.getEmail());
		}

		employee.setHoursLunch(null);
		employeeDto.getHoursLunch()
				.ifPresent(hursLunch -> employee.setHoursLunch(Float.valueOf(hursLunch)));

		employee.setHoursWorkedDay(null);
		employeeDto.getHoursWorkedDay()
				.ifPresent(hoursWorkedDay -> employee.setHoursWorkedDay(Float.valueOf(hoursWorkedDay)));

		employee.setHourValue(null);
		employeeDto.getHourValue().ifPresent(hourValue -> employee.setHourValue(new BigDecimal(hourValue)));

		if (employeeDto.getPassword().isPresent()) {
			employee.setPassword(PasswordUtils.generateBCrypt(employeeDto.getPassword().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param employee
	 * @return EmployeeDto
	 */
	private EmployeeDto converterEmployeeDto(Employee employee) {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setId(employee.getId());
		employeeDto.setEmail(employee.getEmail());
		employeeDto.setName(employee.getName());
		employee.getHoursLunchOptinal().ifPresent(
				HoursLunch -> employeeDto.setHoursLunch(Optional.of(Float.toString(HoursLunch))));
		employee.getHoursWorkedDayOptinal().ifPresent(
				qtdHorasTrabDia -> employeeDto.setHoursWorkedDay(Optional.of(Float.toString(qtdHorasTrabDia))));
		employee.getHoursLunchOptinal()
				.ifPresent(hoursLunch -> employeeDto.setHoursLunch(Optional.of(hoursLunch.toString())));

		return employeeDto;
	}

}
