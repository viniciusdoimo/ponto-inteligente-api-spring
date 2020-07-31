package br.com.viniciusdoimo.pontointeligente.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusdoimo.pontointeligente.api.dtos.LaunchDto;
import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;
import br.com.viniciusdoimo.pontointeligente.api.enums.TypeEnum;
import br.com.viniciusdoimo.pontointeligente.api.response.Response;
import br.com.viniciusdoimo.pontointeligente.api.services.EmployeeService;
import br.com.viniciusdoimo.pontointeligente.api.services.LaunchService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LaunchController {

	private static final Logger log = LoggerFactory.getLogger(LaunchController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private LaunchService launchService;

	@Autowired
	private EmployeeService employeeService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPerPagina;

	public LaunchController() {
	}

	/**
	 * Retorna a listagem de lançamentos de um funcionário.
	 * 
	 * @param employeeId
	 * @return ResponseEntity<Response<LaunchDto>>
	 */
	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LaunchDto>>> listByEmployeeId(
			@PathVariable("employeeId") Long employeeId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", employeeId, page);
		Response<Page<LaunchDto>> response = new Response<Page<LaunchDto>>();

		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<Launch> launch = this.launchService.searchByEmployeeId(employeeId, pageRequest);
		Page<LaunchDto> launchDto = launch.map(launchs -> this.converterLaunchDto((Launch) launch));

		response.setData(launchDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<LaunchDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LaunchDto>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		Response<LaunchDto> response = new Response<LaunchDto>();
		Optional<Launch> launch = this.launchService.searchById(id);

		if (!launch.isPresent()) {
			log.info("Lançamento não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterLaunchDto(launch.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Adiciona um novo lançamento.
	 * 
	 * @param launch
	 * @param result
	 * @return ResponseEntity<Response<LaunchDto>>
	 * @throws ParseException 
	 */
	@PostMapping
	public ResponseEntity<Response<LaunchDto>> adicionar(@Valid @RequestBody LaunchDto launchDto,
			BindingResult result) throws ParseException {
		log.info("Adicionando lançamento: {}", launchDto.toString());
		Response<LaunchDto> response = new Response<LaunchDto>();
		validateEmployee(launchDto, result);
		Launch launch = this.converterDtoForLaunch(launchDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		launch = this.launchService.persist(launch);
		response.setData(this.converterLaunchDto(launch));
		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados de um lançamento.
	 * 
	 * @param id
	 * @param launchDto
	 * @return ResponseEntity<Response<Launch>>
	 * @throws ParseException 
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LaunchDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LaunchDto launchDto, BindingResult result) throws ParseException {
		log.info("Atualizando lançamento: {}", launchDto.toString());
		Response<LaunchDto> response = new Response<LaunchDto>();
		validateEmployee(launchDto, result);
		launchDto.setId(Optional.of(id));
		Launch launch = this.converterDtoForLaunch(launchDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		launch = this.launchService.persist(launch);
		response.setData(this.converterLaunchDto(launch));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Launch>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Launch> launch = this.launchService.searchById(id);

		if (!launch.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.launchService.remove(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Valida um funcionário, verificando se ele é existente e válido no
	 * sistema.
	 * 
	 * @param lancamentoDto
	 * @param result
	 */
	private void validateEmployee(LaunchDto launchDto, BindingResult result) {
		if (launchDto.getEmployeeId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado."));
			return;
		}

		log.info("Validando funcionário id {}: ", launchDto.getEmployeeId());
		Optional<Employee> employee = this.employeeService.searchById(launchDto.getEmployeeId());
		if (!employee.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
		}
	}

	/**
	 * Converte uma entidade lançamento para seu respectivo DTO.
	 * 
	 * @param launch
	 * @return LaunchDto
	 */
	private LaunchDto converterLaunchDto(Launch launch) {
		LaunchDto launchDto = new LaunchDto();
		launchDto.setId(Optional.of(launch.getId()));
		launchDto.setDate(this.dateFormat.format(launch.getDate()));
		launchDto.setTipo(launch.getType().toString());
		launchDto.setDescription(launch.getDescription());
		launchDto.setLocation(launch.getLocation());
		launchDto.setEmployeeId(launch.getEmployee().getId());

		return launchDto;
	}

	/**
	 * Converte um LancamentoDto para uma entidade Lancamento.
	 * 
	 * @param launchDto
	 * @param result
	 * @return Launch
	 * @throws ParseException 
	 */
	private Launch converterDtoForLaunch(LaunchDto launchDto, BindingResult result) throws ParseException {
		Launch launch = new Launch();

		if (launchDto.getId().isPresent()) {
			Optional<Launch> lanc = this.launchService.searchById(launchDto.getId().get());
			if (lanc.isPresent()) {
				launch = lanc.get();
			} else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		} else {
			launch.setEmployee(new Employee());
			launch.getEmployee().setId(launchDto.getEmployeeId());
		}

		launch.setDescription(launchDto.getDescription());
		launch.setLocation(launchDto.getLocation());
		launch.setDate(this.dateFormat.parse(launchDto.getDate()));

		if (EnumUtils.isValidEnum(TypeEnum.class, launchDto.getTipo())) {
			launch.setType(TypeEnum.valueOf(launchDto.getTipo()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return launch;
	}

}
