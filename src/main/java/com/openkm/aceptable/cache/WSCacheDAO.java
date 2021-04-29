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

package com.openkm.aceptable.cache;

import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.exception.AuthenticationException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.WebserviceException;

/**
 * @author agallego
 */
public interface WSCacheDAO {
	String ANONYMOUS_USER = "anonymousUser";
	String ADMIN_USER = "admin";

	/**
	 * It creates the OKMWebservices with a key prefix (username) to distinguish between different users' cache.
	 * The first time method completes caches the OKMWebservices object and from now returns object from cache.
	 * In case object has been evicted from cache, the OKMWebservices object is rebuilt
	 *
	 * @param username as the user's username
	 * @throws WebserviceException 
	 * @throws UnknowException 
	 * @throws AuthenticationException 
	 */
	OKMWebservices getOKMWebservices(String username) throws AuthenticationException, UnknowException, WebserviceException;

	/**
	 * It removes explicitly all items related to the referenced key (username) from cache.
	 *
	 * @param username as the user's username
	 */
	void evictOKMWebservices(String username);

	/**
	 * The same functionality as getOKMWebservices object, but this concrete method is intended to be called only
	 * the first time OKMWebservices object has to be created (for example, at the moment of successful login when
	 * SecurityContextHolder.getContext().getAuthentication() is null, so it does not yet contains credentials from user).
	 * <p>
	 * It also caches OKMWebservices object.
	 * @throws WebserviceException 
	 * @throws UnknowException 
	 * @throws AuthenticationException 
	 */
	OKMWebservices setOKMWebservices(String username, String password) throws AuthenticationException, UnknowException, WebserviceException;

}