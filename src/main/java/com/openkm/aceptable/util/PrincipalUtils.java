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

package com.openkm.aceptable.util;

import com.openkm.aceptable.config.auth.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author pavila
 */
public class PrincipalUtils {
	private static Logger log = LoggerFactory.getLogger(PrincipalUtils.class);
	private static final String ROLE_PREFIX = "ROLE_";

	/**
	 * Obtain the logged user.
	 */
	public static CustomUser getUser() {
		Authentication auth = getAuthentication();
		return getUser(auth);
	}

	/**
	 * Get user from Authentication info
	 */
	public static CustomUser getUser(Authentication auth) {
		CustomUser user = null;

		if (auth != null) {
			if (auth.getPrincipal() instanceof CustomUser) {
				user = (CustomUser) auth.getPrincipal();
			}
		}

		return user;
	}

	/**
	 * Obtain the list of user roles.
	 */
	public static Set<String> getRoles() {
		Authentication auth = getAuthentication();
		Set<String> roles = new HashSet<>();

		if (auth != null) {
			for (GrantedAuthority ga : auth.getAuthorities()) {
				roles.add(ga.getAuthority());
			}
		}

		return roles;
	}

	/**
	 * Check for role
	 */
	public static boolean hasRole(String role) {
		Authentication auth = getAuthentication();

		if (auth != null) {
			UserDetails user = (UserDetails) auth.getPrincipal();
			String defaultedRole = getRoleWithDefaultPrefix(ROLE_PREFIX, role);

			for (GrantedAuthority ga : user.getAuthorities()) {
				if (ga.getAuthority().equals(defaultedRole)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check for role
	 */
	public static boolean hasAnyRole(String... roles) {
		Authentication auth = getAuthentication();

		if (auth != null) {
			UserDetails user = (UserDetails) auth.getPrincipal();

			for (String role : roles) {
				String defaultedRole = getRoleWithDefaultPrefix(ROLE_PREFIX, role);

				for (GrantedAuthority ga : user.getAuthorities()) {
					if (ga.getAuthority().equals(defaultedRole)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Obtain authentication token
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * Set authentication token
	 */
	public static void setAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	/**
	 * Create authentication token
	 */
	public static Authentication createAuthentication(String user, Set<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	/**
	 * Prefixes role with defaultRolePrefix if defaultRolePrefix is non-null and if role
	 * does not already start with defaultRolePrefix.
	 */
	private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
		if (role == null) {
			return role;
		}

		if (defaultRolePrefix == null || defaultRolePrefix.length() == 0) {
			return role;
		}
		if (role.startsWith(defaultRolePrefix)) {
			return role;
		}

		return defaultRolePrefix + role;
	}
}
