package br.com.viniciusdoimo.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.viniciusdoimo.pontointeligente.api.entities.Company;
import br.com.viniciusdoimo.pontointeligente.api.entities.Employee;
import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;
import br.com.viniciusdoimo.pontointeligente.api.enums.ProfileEnum;
import br.com.viniciusdoimo.pontointeligente.api.enums.TypeEnum;
import br.com.viniciusdoimo.pontointeligente.api.utils.PasswordUtils;
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LaunchRepositoryTest {
	
	@Autowired
	private LaunchRepository launchRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	private Long employeeId;

	@Before
	public void setUp() throws Exception {
		Company company = this.companyRepository.save(getCompanyData());
		
		Employee employee = this.employeeRepository.save(getEmployeeData(company));
		this.employeeId = employee.getId();
		
		this.launchRepository.save(getLaunchData(employee));
		this.launchRepository.save(getLaunchData(employee));
	}

	@After
	public void tearDown() throws Exception {
		this.companyRepository.deleteAll();
	}

	@Test
	public void testSearchLaunchForEmployeeId() {
		List<Launch> launch = this.launchRepository.findByEmployeeId(employeeId);
		
		assertEquals(2, launch.size());
	}
	
	@Test
	public void testSearchLaunchForEmployeeIdPaged() {
		Pageable page = PageRequest.of(0, 10);
		Page<Launch> launch = this.launchRepository.findByEmployeeId(employeeId, page);
		
		assertEquals(2, launch.getTotalElements());
	}
	
	private Launch getLaunchData(Employee employee) {
		Launch launch = new Launch();
		launch.setDate(new Date());
		launch.setType(TypeEnum.START_LUNCH);
		launch.setEmployee(employee);
		return launch;
	}

	private Employee getEmployeeData(Company company) throws NoSuchAlgorithmException {
		Employee employee = new Employee();
		employee.setName("Fulano de Tal");
		employee.setProfile(ProfileEnum.ROLE_USER);
		employee.setPassword(PasswordUtils.generateBCrypt("123456"));
		employee.setCpf("24291173474");
		employee.setEmail("email@email.com");
		employee.setCompany(company);
		return employee;
	}

	private Company getCompanyData() {
		Company company = new Company();
		company.setCompanyName("Empresa de exemplo");
		company.setCnpj("51463645000100");
		return company;
	}

}
