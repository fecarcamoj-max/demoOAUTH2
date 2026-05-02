package com.example.demoAuth.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	private final AppUserRepository appUserRepository;

	public DatabaseUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = this.appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

		String[] authorities = appUser.getRoles()
				.stream()
				.map(AppRole::getName)
				.toArray(String[]::new);

		return User.withUsername(appUser.getUsername())
				.password(appUser.getPassword())
				.disabled(!appUser.isEnabled())
				.authorities(authorities)
				.build();
	}
}
