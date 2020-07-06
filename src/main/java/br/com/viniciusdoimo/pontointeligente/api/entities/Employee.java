package br.com.viniciusdoimo.pontointeligente.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.viniciusdoimo.pontointeligente.api.enums.ProfileEnum;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = -6720124689039258703L;
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private BigDecimal hourValue;
	private Float hoursWorkedDay;
	private Float hoursLunch;
	private ProfileEnum profile;
	private Date creationDate;
	private Date updateDate;
	private Company company;
	private List<Launch> launch;
	
	public Employee() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(name = "hour_value", nullable = true)
	public BigDecimal getHourValue() {
		return hourValue;
	}
	
	@Transient
	public Optional<BigDecimal> getHourValueOptinal(){
		return Optional.ofNullable(hourValue);
	}

	public void setHourValue(BigDecimal hourValue) {
		this.hourValue = hourValue;
	}

	@Column(name = "qtd_hours_worked_day", nullable = true)
	public Float getHoursWorkedDay() {
		return hoursWorkedDay;
	}
	
	@Transient
	public Optional<Float> getHoursWorkedDayOptinal(){
		return Optional.ofNullable(hoursWorkedDay);
	}

	public void setHoursWorkedDay(Float hoursWorkedDay) {
		this.hoursWorkedDay = hoursWorkedDay;
	}

	@Column(name = "qtd_hours_lunch", nullable = true)
	public Float getHoursLunch() {
		return hoursLunch;
	}
	
	@Transient
	public Optional<Float> getHoursLunchOptinal(){
		return Optional.ofNullable(hoursWorkedDay);
	}

	public void setHoursLunch(Float hoursLunch) {
		this.hoursLunch = hoursLunch;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "profile", nullable = false)
	public ProfileEnum getProfile() {
		return profile;
	}

	public void setProfile(ProfileEnum profile) {
		this.profile = profile;
	}

	@Column(name = "creation_data", nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "update_data", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Launch> getlaunch() {
		return launch;
	}

	public void setlaunch(List<Launch> launch) {
		this.launch = launch;
	}

	@PreUpdate
	public void preUpdate() {
		updateDate = new Date();
	}
	
	@PrePersist
	public void PrePersist(){
		final Date now = new Date();
		creationDate = now;
		updateDate = now;
	}
	
	@Override
	public String toString() {
		return "Employee ["
				+ "id=" + id 
				+ ", name=" + name
				+ ", email=" + email 
				+ ", password=" + password 
				+ ", cpf=" + cpf 
				+ ", hour_value=" + hourValue  
				+ ", qtd_hours_worked_day=" + hoursWorkedDay
				+ ", qtd_hours_lunch=" + hoursLunch 
				+ ", profile=" + profile 
				+ ", creation_data=" + creationDate 
				+ ", update_data=" + updateDate 
				+ ", company=" + company + "]";
	}
}
