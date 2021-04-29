/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2016  Paco Avila & Josep Llort
 * <p>
 * No bytes were intentionally harmed during the development of this application.
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.aceptable.controller;

import com.openkm.aceptable.config.auth.CustomUser;
import com.openkm.aceptable.util.PrincipalUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

/**
 * Created by pavila on 17/06/16.
 */
@Controller
public class DefaultController {

	@GetMapping("/")
	public String login(Model model) {
		model.addAttribute("fecha", new Date(System.currentTimeMillis()));
		return "index";
	}

	@GetMapping("/header")
	public String header() {
		return "include/header";
	}

	@GetMapping("/menu")
	public String menu(Model model) {
		CustomUser user = PrincipalUtils.getUser();
		model.addAttribute("user", user);
		return "include/menu";
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "User and password not valid.");
		}

		return "login";
	}

}
