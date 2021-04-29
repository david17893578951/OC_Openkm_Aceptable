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

package com.openkm.aceptable.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by pavila on 15/05/17.
 */
@Component
@ConfigurationProperties
public class Config {
	@Value("${openkm.url}")
	public String OPENKM_URL;
	
	@Value("${openkm.url.preview}")
	public String OPENKM_URL_PREVIEW;

	@Value("${anonymous.user}")
	public String ANONYMOUS_USER;

	@Value("${anonymous.password}")
	public String ANONYMOUS_PASSWORD;

	@Value("${admin.user}")
	public String ADMIN_USER;

	@Value("${admin.password}")
	public String ADMIN_PASSWORD;

	@Value("${preview.download.url}")
	public String PREVIEW_DOWNLOAD_URL;

	public String getPreviewKcenterToOpenKMUrl() {
		String baseUrl = OPENKM_URL_PREVIEW;
		if (!OPENKM_URL_PREVIEW.endsWith("/")) {
			baseUrl += "/";
		}
		return baseUrl + "Preview";
	}
}
