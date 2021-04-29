/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2016 Paco Avila & Josep Llort
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.aceptable.config.auth;

import com.openkm.aceptable.cache.WSCacheDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class extends and implements spring-security classes to intercept and handle the logout process
 * in order to execute custom actions, for example, to evict cache of logged out user and to control
 * the template to see after a successful logout
 */
@Service("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	private static final Logger log = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Autowired
	private WSCacheDAO wsCache;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if (authentication != null && authentication.getDetails() != null) {
			wsCache.evictOKMWebservices(authentication.getName());
			log.debug("session before invalidate : " + request.getSession().getCreationTime());
			request.getSession(false).invalidate();
			log.debug("invalidate session");
			log.debug("session after invalidate : " + request.getSession(false));
			log.info(authentication.getName() + " successfully logged out");
		}

		String url = request.getContextPath();
		response.sendRedirect(url);
	}
}
