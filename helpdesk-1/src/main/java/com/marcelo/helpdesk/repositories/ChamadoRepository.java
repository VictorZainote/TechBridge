package com.marcelo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcelo.helpdesk.domain.Chamado;


public interface ChamadoRepository extends JpaRepository <Chamado,Integer>{

}
