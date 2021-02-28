package com.ceiba.prueba.service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceiba.prueba.modelo.Persona;
import com.ceiba.prueba.repository.PersonaRepository;

@Service
public class PersonaService {

	private static final Date fechaActual = new Date();

	@Autowired
	private PersonaRepository personaRepository;

	@Transactional
	public Persona create(Persona persona) {
		long daysBetween = ChronoUnit.DAYS.between(persona.getFechaNacimiento().toInstant(), fechaActual.toInstant());		
		if (daysBetween / 365 < 18) {
			throw new IllegalArgumentException("Edad no permitida, debe ser mayor de edad");
		}
		
		Persona personaValidar = new Persona(persona.getNumeroIdentificacion());
		  if (personaRepository.exists(Example.of(personaValidar))) {
		    throw new DuplicateKeyException("Ya existe usuario con numero identificacion: " + persona.getNumeroIdentificacion());
		  }
		  
			return personaRepository.save(persona);
		
	}	
	
	public List<Persona> getAllPersonas() {
		return personaRepository.findAll();
	}

	public void delete(Persona persona) {
		personaRepository.delete(persona);
	}

	public Optional<Persona> findById(Long id) {
		return personaRepository.findById(id);
	}
}
