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

package com.openkm.aceptable.config.auth;

import com.openkm.aceptable.cache.WSCacheDAO;
import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.bean.CommonUser;
import com.openkm.sdk4j.bean.SqlQueryResultColumns;
import com.openkm.sdk4j.bean.SqlQueryResults;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author agallego
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	private static Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private WSCacheDAO wsCache;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		log.debug("loadUserByUsername {}", userName);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		try {
			OKMWebservices ws = wsCache.getOKMWebservices(WSCacheDAO.ADMIN_USER);

			// Get full Name
			CommonUser commonUser = ws.getUser(userName);
			String userFullName = commonUser.getName();

			// Get roles
			List<String> roles = ws.getRolesByUser(userName);

			// set roles for spring-security according to those received from OKM ws response
			Iterator<String> it = roles.iterator();
			String role;
			while (it.hasNext()) {
				role = it.next();
				authorities.add(new SimpleGrantedAuthority(role));
			}

			// Get Password
			String sql = "SELECT USR_PASSWORD FROM OKM_USER WHERE USR_ID='" + userName + "'";
			InputStream is = new ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8));
			SqlQueryResults result = ws.executeSqlQuery(is);

			String password = "";
			for (SqlQueryResultColumns row : result.getResults()) {
				password = row.getColumns().get(0);
			}
			IOUtils.closeQuietly(is);

			return new CustomUser(userName, password, true, true, true, true, authorities, userFullName);
		} catch (Exception e) {
			log.error("Error: {}", e.getMessage(), e);
			return new CustomUser(userName, "", true, true, true, true, authorities, "");
		}
	}
}
