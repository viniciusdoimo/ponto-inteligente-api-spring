package br.com.viniciusdoimo.pontointeligente.api.dtos;

import java.util.Optional;

import org.hibernate.validator.constraints.NotEmpty;

public class LaunchDto {
	
	private Optional<Long> id = Optional.empty();
	private String date;
	private String tipo;
	private String description;
	private String location;
	private Long employeeId;

	public LaunchDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}

	@NotEmpty(message = "Data não pode ser vazia.")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "LancamentoDto [id=" + id + ", date=" + date + ", tipo=" + tipo + ", description=" + description
				+ ", location=" + location + ", employeeId=" + employeeId + "]";
	}
	
}
