package com.demo.producer.repository;

import com.demo.producer.domain.Persona;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends ReactiveCrudRepository<Persona, Integer> {
}
