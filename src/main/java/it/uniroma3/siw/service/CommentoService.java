package it.uniroma3.siw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.CommentoRepository;


@Service
public class CommentoService {
	
	private static final Logger logger = LoggerFactory.getLogger(CommentoService.class);
	
	@Autowired
	private CommentoRepository commentoRepository;

}
