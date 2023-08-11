package com.example.demo.service;

import java.util.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/springBoot/performanceTest/")
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// get all employees
	@GetMapping("/users")
	public List<User> performanceTest() {

		List<User> users = userRepository.findAll();
		List<User> duplicatedUsers = new ArrayList<>(users);
		while (duplicatedUsers.size() < 100) {
			duplicatedUsers.addAll(users);
		}
		return duplicatedUsers;
	}
}