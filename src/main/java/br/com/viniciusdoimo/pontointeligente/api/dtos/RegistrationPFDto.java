package br.com.viniciusdoimo.pontointeligente.api.dtos;

import java.util.Optional;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class RegistrationPFDto {
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private Optional<String> hourValue = Optional.empty();
	private Optional<String> hoursWorkedDay = Optional.empty();
	private Optional<String> hoursLunch = Optional.empty();
	private String cnpj;

	public RegistrationPFDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "nome não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
	@Email(message="Email inválido.")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotEmpty(message = "Senha não pode ser vazia.")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotEmpty(message = "CPF não pode ser vazio.")
	@CPF(message="CPF inválido")
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Optional<String> getHourValue() {
		return hourValue;
	}

	public void setHourValue(Optional<String> hourValue) {
		this.hourValue = hourValue;
	}

	public Optional<String> getHoursWorkedDay() {
		return hoursWorkedDay;
	}

	public void setHoursWorkedDay(Optional<String> hoursWorkedDay) {
		this.hoursWorkedDay = hoursWorkedDay;
	}

	public Optional<String> getHoursLunch() {
		return hoursLunch;
	}

	public void setHoursLunch(Optional<String> hoursLunch) {
		this.hoursLunch = hoursLunch;
	}

	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message="CNPJ inválido.")
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "FuncionarioDto [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", cpf=" + cpf
				+ ", hourValue=" + hourValue + ", hoursWorkedDay=" + hoursWorkedDay + ", hoursLunch="
				+ hoursLunch + ", cnpj=" + cnpj + "]";
	}

}
