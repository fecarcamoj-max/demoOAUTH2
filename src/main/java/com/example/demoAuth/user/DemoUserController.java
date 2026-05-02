package com.example.demoAuth.user;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class DemoUserController {

	private final DemoUserService demoUserService;

	public DemoUserController(DemoUserService demoUserService) {
		this.demoUserService = demoUserService;
	}

	@GetMapping
	public java.util.List<DemoUserService.DemoUserView> listUsers() {
		return this.demoUserService.listUsers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DemoUserService.DemoUserView createUser(@RequestBody CreateDemoUserRequest request) {
		return this.demoUserService.createDemoUser(
				request.username(),
				request.password(),
				request.roles() == null || request.roles().isEmpty() ? Set.of("ROLE_USER") : request.roles());
	}

	record CreateDemoUserRequest(String username, String password, Set<String> roles) {
	}
}
