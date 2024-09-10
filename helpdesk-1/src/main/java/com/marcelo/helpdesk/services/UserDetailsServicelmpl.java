package com.marcelo.helpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marcelo.helpdesk.domain.Pessoa;
import com.marcelo.helpdesk.repositories.PessoaRepository;
import com.marcelo.helpdesk.secutiry.UserSS;

@Service
public class UserDetailsServicelmpl implements UserDetailsService {

	@Autowired
	private PessoaRepository repository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Pessoa> user = repository.findByEmail(email);
		if(user.isPresent()) {
			return new UserSS(user.get().getId(),user.get().getEmail(),user.get().getSenha(),user.get().getPerfis());
		}
		throw new UsernameNotFoundException(email);
		
		}

} 
