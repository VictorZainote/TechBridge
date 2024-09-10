package com.marcelo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcelo.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
