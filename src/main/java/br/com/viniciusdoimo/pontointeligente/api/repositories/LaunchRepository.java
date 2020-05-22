package br.com.viniciusdoimo.pontointeligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "LaunchRepository.findByEmployeeId", 
				query = "SELECT launch FROM Launch launch WHERE launch.employee.id = :employeeId") })
public interface LaunchRepository extends JpaRepository<Launch, Long> {

	List<Launch> findByEmployeeId(@Param("employeeId") Long employeeId);

	Page<Launch> findByEmployeeId(@Param("employeeId") Long employeeId, Pageable pageable);
}
