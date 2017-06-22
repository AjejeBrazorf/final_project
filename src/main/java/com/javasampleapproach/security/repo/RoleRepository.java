package com.javasampleapproach.security.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javasampleapproach.security.model.Role;
import com.javasampleapproach.security.model.User;

public interface RoleRepository extends JpaRepository<Role, String>{

	@Query(value = "SELECT * FROM user_roles r where r.username = ?1", nativeQuery = true)
	List<Role> findRolesByemail(String email);
	
	@Query(value = "insert into user_roles(username, role)"
			+ " VALUES (?1, ?2)"
			+ " RETURNING username", nativeQuery = true)
	String insertUserRole(String email, String role);
}
