package com.marcelo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcelo.helpdesk.domain.Cliente;


public interface ClienteRepository extends JpaRepository <Cliente,Integer>{

}
