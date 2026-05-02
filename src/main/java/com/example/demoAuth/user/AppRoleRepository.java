package com.example.demoAuth.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

	Optional<AppRole> findByName(String name);
}
