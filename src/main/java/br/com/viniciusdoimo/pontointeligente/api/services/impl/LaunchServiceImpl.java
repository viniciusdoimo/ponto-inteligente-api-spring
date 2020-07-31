package br.com.viniciusdoimo.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.viniciusdoimo.pontointeligente.api.entities.Launch;
import br.com.viniciusdoimo.pontointeligente.api.repositories.LaunchRepository;
import br.com.viniciusdoimo.pontointeligente.api.services.LaunchService;

@Service
public class LaunchServiceImpl implements LaunchService {

	private static final Logger log = LoggerFactory.getLogger(LaunchServiceImpl.class);

	@Autowired
	private LaunchRepository launchRepository;

	public Page<Launch> searchByEmployeeId(Long employeeId, PageRequest pageRequest) {
		log.info("Buscando lançamentos para o funcionário ID {}", employeeId);
		return this.launchRepository.findByEmployeeId(employeeId, pageRequest);
	}
	
	@Cacheable("launchById")
	public Optional<Launch> searchById(Long id) {
		log.info("Buscando um lançamento pelo ID {}", id);
		return this.launchRepository.findById(id);
	}
	
	@CachePut("launchById")
	public Launch persist(Launch launch) {
		log.info("Persistindo o lançamento: {}", launch);
		return this.launchRepository.save(launch);
	}
	
	public void remove(Long id) {
		log.info("Removendo o lançamento ID {}", id);
		this.launchRepository.deleteById(id);
	}

}
