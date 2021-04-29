package com.openkm.aceptable.controller.admin;

import com.openkm.aceptable.util.PrincipalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pavila on 28/08/16.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	private static Logger log = LoggerFactory.getLogger(AdminController.class);

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("roles", PrincipalUtils.getRoles());
		return "admin/index";
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleNotFound(Exception ex) {
		log.error("Not found", ex);
	}

}
