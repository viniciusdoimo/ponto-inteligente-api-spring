package br.com.viniciusdoimo.pontointeligente.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.viniciusdoimo.pontointeligente.api.enums.TypeEnum;

@Entity
@Table(name = "launch")
public class Launch  implements Serializable{

	private static final long serialVersionUID = 2769717351254615174L;
	
	private Long id;
	private Date date;
	private String description;
	private String location;
	private Date creationDate;
	private Date updateDate;
	private TypeEnum type;
	private Employee employee;
	
	public Launch() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false)
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "location", nullable = true)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "creation_date", nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "update_date", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
		return "launch ["
				+ "id=" + id 
				+ ", date=" + date
				+ ", description=" + description 
				+ ", location=" + location 
				+ ", creation_data=" + creationDate 
				+ ", update_data=" + updateDate 
				+ ", type=" + type 
				+ ", employee=" + employee + "]";
	}
}
