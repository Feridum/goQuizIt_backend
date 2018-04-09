package com.goquizit.beta.controller;

import com.goquizit.beta.entity.User;
import com.goquizit.beta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController
{
	@RequestMapping("")
	public String index()
	{
		return "Hej";
	}

	@Autowired
	UserService userService;

	@GetMapping("/listAll")
	public Iterable<User> listAll() {
		return userService.findAll();
	}
}