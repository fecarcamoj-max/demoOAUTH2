package com.example.demoAuth.user;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoUserService {

	private final AppUserRepository appUserRepository;
	private final AppRoleRepository appRoleRepository;
	private final PasswordEncoder passwordEncoder;

	public DemoUserService(AppUserRepository appUserRepository,
			AppRoleRepository appRoleRepository,
			PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<DemoUserView> listUsers() {
		return this.appUserRepository.findAll()
				.stream()
				.map(user -> new DemoUserView(
						user.getId(),
						user.getUsername(),
						user.isEnabled(),
						user.getRoles().stream().map(AppRole::getName).sorted().toList()))
				.toList();
	}

	@Transactional
	public DemoUserView createDemoUser(String username, String rawPassword, Set<String> roles) {
		if (this.appUserRepository.findByUsername(username).isPresent()) {
			throw new IllegalArgumentException("El usuario ya existe: " + username);
		}

		AppUser user = new AppUser(username, this.passwordEncoder.encode(rawPassword), true);
		roles.stream()
				.map(this::getOrCreateRole)
				.forEach(user::addRole);

		AppUser savedUser = this.appUserRepository.save(user);
		return new DemoUserView(
				savedUser.getId(),
				savedUser.getUsername(),
				savedUser.isEnabled(),
				savedUser.getRoles().stream().map(AppRole::getName).sorted().toList());
	}

	private AppRole getOrCreateRole(String roleName) {
		return this.appRoleRepository.findByName(roleName)
				.orElseGet(() -> this.appRoleRepository.save(new AppRole(roleName)));
	}

	public record DemoUserView(Long id, String username, boolean enabled, List<String> roles) {
	}
}
