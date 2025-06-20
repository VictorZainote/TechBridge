package com.marcelo.helpdesk.secutiry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelo.helpdesk.domain.dtos.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

			return authenticationManager.authenticate(authenticationToken);

		} catch (IOException e) {
			try {
				response.setStatus(400);
				response.setContentType("application/json");
				response.getWriter().append("{ \"error\": \"JSON inválido\" }");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		} catch (AuthenticationException e) {
			throw e; // deixa o Spring tratar e cair no método `unsuccessfulAuthentication`
		} catch (Exception e) {
			e.printStackTrace(); // loga a exception real
			throw new RuntimeException("Erro interno ao tentar autenticar", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String username = ((UserSS) authResult.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		response.setHeader("access-control-expose-headers", "Authorization");
		response.setHeader("Authorization", "Bearer " + token);

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		response.setStatus(401);
		response.setContentType("application/json");
		response.getWriter().append(json());
	}

	private CharSequence json() {
		long date = new Date().getTime();
		return "{"
				+ "\"timestamp\": " + date + ", "
				+ "\"status\": 401, " + "\"error\": \"Não autorizado\", "
				+ "\"message\": \"Email ou senha inválidos\", " 
				+ "\"path\": \"/login\"}";
	}
}
