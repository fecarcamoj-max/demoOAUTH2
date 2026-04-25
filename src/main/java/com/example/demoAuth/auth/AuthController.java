package com.example.demoAuth.auth;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtEncoder jwtEncoder;

	public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request) {
		try {
			Authentication authentication = this.authenticationManager.authenticate(
					UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
			List<String> roles = authentication.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.toList();
			Instant now = Instant.now();
			String token = this.jwtEncoder.encode(
					JwtEncoderParameters.from(
							JwsHeader.with(SignatureAlgorithm.RS256).build(),
							JwtClaimsSet.builder()
									.issuer("http://localhost:8080")
									.subject(authentication.getName())
									.issuedAt(now)
									.expiresAt(now.plusSeconds(3600))
									.claim("roles", roles)
									.build()))
					.getTokenValue();
			return new LoginResponse(authentication.getName(), roles, token, "Login correcto");
		}
		catch (BadCredentialsException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
		}
	}

	@GetMapping("/me")
	public LoginResponse me(Authentication authentication) {
		List<String> roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		return new LoginResponse(authentication.getName(), roles, null, "Usuario autenticado");
	}

	record LoginRequest(String username, String password) {
	}

	record LoginResponse(String username, List<String> roles, String token, String message) {
	}
}
