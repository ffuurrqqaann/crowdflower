package com.comag10.crowdflower.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.comag10.crowdflower.Utils;
import com.comag10.crowdflower.model.Users;
import com.comag10.crowdflower.service.UserService;
import com.comag10.crowdflower.ui.model.Signup;
import com.comag10.crowdflower.validator.PasswordValidator;

@Controller
@SessionAttributes("user")
public class UserController {

	private UserService userService;
	private PasswordValidator passwordValidator = null;

	@Autowired(required=true)
	@Qualifier(value="UserService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public PasswordValidator getPasswordValidator() {
		return passwordValidator;
	}
	
	@Autowired
	public void setPasswordValidator(PasswordValidator passwordValidator) {
		this.passwordValidator = passwordValidator;
	}

	@RequestMapping(value = "/loginUser", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("user", new Users());

		return "login";
	}

	@RequestMapping(value = "/loginUser", method = RequestMethod.POST)
	public String submitLogin(@Valid @ModelAttribute("user") Users u, BindingResult result) {

		if(result.hasErrors()) {
			System.out.println("it has errors");
			return "login";
		}

		Users user = this.userService.loginUser(u);
		System.out.println("in service user is "+ user);

		return "login";
	}

	@RequestMapping(value = "/signupUser", method = RequestMethod.GET)
	public String userSignup(Model model) {
		model.addAttribute("signup", new Signup());

		return "signup";
	}

	@RequestMapping(value = "/signupUser", method = RequestMethod.POST)
	public String submitSignup(@Valid @ModelAttribute("signup") Signup signup, BindingResult result) {

		getPasswordValidator().validate(signup, result);
		
		Boolean isUserAlreadyExist = this.userService.checkUser(signup);
		
		System.out.println("User already exist = "+isUserAlreadyExist);
		
		if(result.hasErrors() || isUserAlreadyExist) {
			System.out.println("it has errors");
			return "signup";
		}
		
		Users user = new Users();
		
		user.setId(0);
		user.setName(signup.getUsername());
		user.setPassword(Utils.MD5(signup.getPassword()));
		user.setBalance(0);
		
		this.userService.addUser(user);
		
		
		
		return "signup";
	}

}