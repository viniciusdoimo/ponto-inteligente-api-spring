package br.com.viniciusdoimo.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;
import br.com.viniciusdoimo.pontointeligente.api.repositories.LaunchRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LaunchServiceTest {

	@MockBean
	private LaunchRepository launchRepository;

	@Autowired
	private LaunchService launchService;

	@Before
	public void setUp() throws Exception {
		BDDMockito
				.given(this.launchRepository.findByEmployeeId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<Launch>(new ArrayList<Launch>()));
		BDDMockito.given(this.launchRepository.findById(Mockito.anyLong()));
		BDDMockito.given(this.launchRepository.save(Mockito.any(Launch.class))).willReturn(new Launch());
	}

	@Test
	public void testSearchLaunchForEmployeeId() {
		Page<Launch> launch = this.launchService.searchByEmployeeId(1L, PageRequest.of(0, 10));

		assertNotNull(launch);
	}

	@Test
	public void testSearchLaunchForId() {
		Optional<Launch> launch = this.launchService.searchById(1L);

		assertTrue(launch.isPresent());
	}

	@Test
	public void testPersistLaunch() {
		Launch launch = this.launchService.persist(new Launch());

		assertNotNull(launch);
	}

}
