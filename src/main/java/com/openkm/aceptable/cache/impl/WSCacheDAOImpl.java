/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2015 Paco Avila & Josep Llort
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

package com.openkm.aceptable.cache.impl;

import com.openkm.aceptable.cache.WSCacheDAO;
import com.openkm.aceptable.config.Config;
import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.OKMWebservicesFactory;
import com.openkm.sdk4j.exception.AuthenticationException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.WebserviceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * Spring MVC component to manage caching of OKMWebservices object
 */
@Service("wsCache")
public class WSCacheDAOImpl implements WSCacheDAO {
	private static final Logger logger = LoggerFactory.getLogger(WSCacheDAOImpl.class);

	public WSCacheDAOImpl() {
	}

	private String okmUrl;

	@Autowired
	private Config configService;

	@Autowired
	public WSCacheDAOImpl(@Value("${openkm.url}") String okmUrl) {
		this.okmUrl = okmUrl;
	}

	@Override
	@Cacheable(value = "wsCache", key = "#username")
	public OKMWebservices getOKMWebservices(String username) throws AuthenticationException, UnknowException, WebserviceException {
		OKMWebservices ws;

		if (username.equals(ANONYMOUS_USER)) {
			ws = OKMWebservicesFactory.getInstance(okmUrl);
			ws.login(configService.ANONYMOUS_USER, configService.ANONYMOUS_PASSWORD);
		} else if (username.equals(ADMIN_USER)) {
			ws = OKMWebservicesFactory.getInstance(okmUrl);
			ws.login(configService.ADMIN_USER, configService.ADMIN_PASSWORD);
		} else {
			String password = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPassword();
			ws = OKMWebservicesFactory.getInstance(okmUrl);
			ws.login(username, password);
		}

		return ws;
	}


	@Override
	@CacheEvict(value = "wsCache", key = "#username")
	public void evictOKMWebservices(String username) {
		logger.info("evict cache for " + username);
	}

	@Override
	@Cacheable(value = "wsCache", key = "#username")
	public OKMWebservices setOKMWebservices(String username, String password) throws AuthenticationException, UnknowException, WebserviceException {
		logger.info("set OKMWebservices object for the first time in the cache for username " + username);
		OKMWebservices ws = OKMWebservicesFactory.getInstance(okmUrl);
		ws.login(username, password);
		return ws;
	}
}
